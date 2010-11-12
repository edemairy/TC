/*
 * Copyright (C) 2009 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.ScreeningManagerFactory;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregatorConfigException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * This is the helper class to load manager instances from a default
 * configuration namespace. The namespace is
 * &quot;com.cronos.onlinereview.phases.ManagerHelper&quot;. For configuration
 * properties in this namespace, see the Component Specification, section
 * &quot;3.2 Configuration Parameters&quot;. This class is used by PhaseHandler
 * implementations to retrieve manager instances.
 * </p>
 * <p>
 * Sample configuration file is given below:
 *
 * <pre>
 * &lt;Config name="com.cronos.onlinereview.phases.ManagerHelper"&gt;
 *  &lt;Property name="ProjectManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.ProjectManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.ProjectManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *
 *  &lt;Property name="ProjectLinkManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.persistence.link.ProjectLinkManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.persistence.link.ProjectLinkManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *
 *  &lt;Property name="PhaseManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.phase.DefaultPhaseManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.phase.DefaultPhaseManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ReviewManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.review.DefaultReviewManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.review.DefaultReviewManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ScorecardManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.scorecard.ScorecardManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.scorecard.ScorecardManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ScreeningManager"&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.cronos.onlinereview.autoscreening.management.DefaultDBScreeningManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ConnectionFactoryNS"&gt;
 *      &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ConnectionName"&gt;
 *      &lt;Value&gt;informix_connection&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="SearchBundleManagerNS"&gt;
 *      &lt;Value&gt;com.topcoder.search.builder.Upload_Resource_Search&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ResourceManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.resource.persistence.PersistenceResourceManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceSearchBundleName"&gt;
 *          &lt;Value&gt;ResourceSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceRoleSearchBundleName"&gt;
 *          &lt;Value&gt;ResourceRoleSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="NotificationSearchBundleName"&gt;
 *          &lt;Value&gt;NotificationSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="NotificationTypeSearchBundleName"&gt;
 *          &lt;Value&gt;NotificationTypeSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceIdGeneratorName"&gt;
 *          &lt;Value&gt;resource_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="NotificationTypeIdGeneratorName"&gt;
 *          &lt;Value&gt;notification_type_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceRoleIdGeneratorName"&gt;
 *          &lt;Value&gt;resource_role_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="PersistenceClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.resource.persistence.sql.SqlResourcePersistence&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="UploadManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.deliverable.PersistenceUploadManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadSearchBundleName"&gt;
 *          &lt;Value&gt;UploadSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="SubmissionSearchBundleName"&gt;
 *          &lt;Value&gt;SubmissionSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadIdGeneratorName"&gt;
 *          &lt;Value&gt;upload_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadTypeIdGeneratorName"&gt;
 *          &lt;Value&gt;upload_type_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadStatusIdGeneratorName"&gt;
 *          &lt;Value&gt;upload_status_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="SubmissionIdGeneratorName"&gt;
 *          &lt;Value&gt;submission_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="SubmissionStatusIdGeneratorName"&gt;
 *          &lt;Value&gt;submission_status_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *       &lt;Property name="SubmissionStatusIdGeneratorName"&gt;
 *          &lt;Value&gt;submission_type_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="PersistenceClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="UserProjectDataStore"&gt;
 *      &lt;Property name="UserRetrievalClassName"&gt;
 *          &lt;Value&gt;com.cronos.onlinereview.external.impl.DBUserRetrieval&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UserRetrievalNamespace"&gt;
 *          &lt;Value&gt;com.cronos.onlinereview.external&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ScorecardAggregator"&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 * &lt;/Config&gt;
 * </pre>
 *
 * </p>
 * <p>
 * Change in version 1.4:
 * <ul>
 *     <li>add submission type id generator used to create UploadManager.</li>
 *     <li>use java generic type instead of the raw type.</li>
 * </ul>
 * </p>
 *
 * @author tuenm, bose_java, waits, saarixx, myxgyy
 * @version 1.4
 */
public class ManagerHelper {
    /**
     * The default configuration namespace of this class. It is used in the
     * default constructor.
     */
    private static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.ManagerHelper";

    /** Property name constant for project manager implementation class name. */
    private static final String PROP_PROJECT_MGR_CLASS_NAME = "ProjectManager.ClassName";

     /** Property name constant for project link manager implementation class name. */
    private static final String PROP_PROJECT_LINK_MGR_CLASS_NAME = "ProjectLinkManager.ClassName";

    /**
     * Property name constant for namespace to be passed to project manager
     * implementation constructor.
     */
    private static final String PROP_PROJECT_MGR_NAMESPACE = "ProjectManager.Namespace";


    /** Property name constant for namespace to be passed to project link manager implementation constructor. */
    private static final String PROP_PROJECT_LINK_MGR_NAMESPACE = "ProjectLinkManager.Namespace";


    /** Property name constant for phase manager implementation class name. */
    private static final String PROP_PHASE_MGR_CLASS_NAME = "PhaseManager.ClassName";

    /**
     * Property name constant for namespace to be passed to phase manager
     * implementation constructor.
     */
    private static final String PROP_PHASE_MGR_NAMESPACE = "PhaseManager.Namespace";

    /** Property name constant for review manager implementation class name. */
    private static final String PROP_REVIEW_MGR_CLASS_NAME = "ReviewManager.ClassName";

    /**
     * Property name constant for namespace to be passed to review manager
     * implementation constructor.
     */
    private static final String PROP_REVIEW_MGR_NAMESPACE = "ReviewManager.Namespace";

    /** Property name constant for scorecard manager implementation class name. */
    private static final String PROP_SCORECARD_MGR_CLASS_NAME = "ScorecardManager.ClassName";

    /**
     * Property name constant for namespace to be passed to scorecard manager
     * implementation constructor.
     */
    private static final String PROP_SCORECARD_MGR_NAMESPACE = "ScorecardManager.Namespace";

    /**
     * Property name constant for namespace to be passed to screening manager
     * implementation constructor.
     */
    private static final String PROP_SCREENING_MGR_NAMESPACE = "ScreeningManager.Namespace";

    /** Property name constant for connection factory namespace. */
    private static final String PROP_CONNECTION_FACTORY_NS = "ConnectionFactoryNS";

    /** Property name constant for connection name. */
    private static final String PROP_CONNECTION_NAME = "ConnectionName";

    /**
     * Property name constant for namespace to be passed to SearchBundleManager
     * constructor.
     */
    private static final String PROP_SEARCH_BUNDLE_MGR_NS = "SearchBundleManagerNS";

    /** Property name constant for resource manager implementation class name. */
    private static final String PROP_RESOURCE_MGR_CLASS_NAME = "ResourceManager.ClassName";

    /**
     * Property name constant for resource search bundle name when creating
     * ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_BUNDLE_NAME = "ResourceManager.ResourceSearchBundleName";

    /**
     * Property name constant for resource role search bundle name when creating
     * ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_ROLE_BUNDLE_NAME =
        "ResourceManager.ResourceRoleSearchBundleName";

    /**
     * Property name constant for notification search bundle name when creating
     * ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_NOTIFICATION_BUNDLE_NAME =
        "ResourceManager.NotificationSearchBundleName";

    /**
     * Property name constant for notification type search bundle name when
     * creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_NOTIFICATION_TYPE_BUNDLE_NAME =
        "ResourceManager.NotificationTypeSearchBundleName";

    /**
     * Property name constant for resource IDgenerator name when creating
     * ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_IDGEN_NAME = "ResourceManager.ResourceIdGeneratorName";

    /**
     * Property name constant for notification IDgenerator name when creating
     * ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_NOTIFICATION_TYPE_IDGEN_NAME =
        "ResourceManager.NotificationTypeIdGeneratorName";

    /**
     * Property name constant for resource role IDgenerator name when creating
     * ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_ROLE_IDGEN_NAME =
        "ResourceManager.ResourceRoleIdGeneratorName";

    /**
     * Property name constant for ResourcePersistence implementation class name.
     */
    private static final String PROP_RESOURCE_MGR_PERSISTENCE_CLASS_NAME = "ResourceManager.PersistenceClassName";

    /** Property name constant for upload manager implementation class name. */
    private static final String PROP_UPLOAD_MGR_CLASS_NAME = "UploadManager.ClassName";

    /**
     * Property name constant for upload search bundle name when creating
     * UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_BUNDLE_NAME = "UploadManager.UploadSearchBundleName";

    /**
     * Property name constant for submission search bundle name when creating
     * UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_BUNDLE_NAME = "UploadManager.SubmissionSearchBundleName";

    /**
     * Property name constant for upload IDgenerator name when creating
     * UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_IDGEN_NAME = "UploadManager.UploadIdGeneratorName";

    /**
     * Property name constant for upload type IDgenerator name when creating
     * UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_TYPE_IDGEN_NAME = "UploadManager.UploadTypeIdGeneratorName";

    /**
     * Property name constant for upload status IDgenerator name when creating
     * UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_STATUS_IDGEN_NAME = "UploadManager.UploadStatusIdGeneratorName";

    /**
     * Property name constant for submission IDgenerator name when creating
     * UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_IDGEN_NAME = "UploadManager.SubmissionIdGeneratorName";

    /**
     * This constant stores Online Review's project details page url property name.
     *
     * @since 1.3
     */
    private static final String PROP_PROJECT_DETAILS_URL = "ProjectDetailsURL";

    /**
     * Property name constant for submission status IDgenerator name when
     * creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_STATUS_IDGEN_NAME =
        "UploadManager.SubmissionStatusIdGeneratorName";

    /**
     * Property name constant for submission type IDgenerator name when
     * creating UploadManager instance.
     *
     * @since 1.4
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_TYPE_IDGEN_NAME =
        "UploadManager.SubmissionTypeIdGeneratorName";

    /** Property name constant for UploadPersistence implementation class name. */
    private static final String PROP_UPLOAD_MGR_PERSISTENCE_CLASS_NAME = "UploadManager.PersistenceClassName";

    /** Property name constant for UserRetrieval implementation class name. */
    private static final String PROP_USER_RETRIEVAL_CLASS_NAME = "UserProjectDataStore.UserRetrievalClassName";

    /**
     * Property name constant for namespace to be passed to UserRetrieval
     * implementation constructor.
     */
    private static final String PROP_USER_RETRIEVAL_NAMESPACE = "UserProjectDataStore.UserRetrievalNamespace";

    /**
     * Property name constant for namespace to be passed to
     * ReviewScoreAggregator constructor.
     */
    private static final String PROP_SCORE_AGGREGATOR_NAMESPACE = "ScorecardAggregator.Namespace";

    /**
     * ScorecardManager, ReviewManager, ProjectManager, and UserRetrieval all
     * use same constructor signature which is the one that takes a String
     * parameter. This constant array is used as parameter types array when
     * instantiating using reflection in the initManager() method.
     */
    private static final Class<?>[] MANAGER_PARAM_TYPES = new Class[] {String.class};

    /**
     * ProjectyLinkManager all use same constructor signature which is the one that takes a String and ProjectManager
     * parameter. This constant array is used as parameter types array when instantiating using reflection in the
     * initManager() method.
     *
     * @since 1.3
     */
    private static final Class<?>[] PROJECT_LINK_MANAGER_PARAM_TYPES = new Class[] {String.class,
        ProjectManager.class};

    /**
     * Represents the ProjectManager instance. It is initialized in the constructor and never changed after
     * that. It is never null.
     */
    private final ProjectManager projectManager;

    /**
     * Represents the ProjectLinkManager instance. It is initialized in the constructor and never changed after
     * that. It is never null.
     *
     * @since 1.3
     */
    private final ProjectLinkManager projectLinkManager;

    /**
     * Represents the PhaseManager instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final PhaseManager phaseManager;

    /**
     * Represents the ScorecardManager instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final ScorecardManager scorecardManager;

    /**
     * Represents the ScreeningManager instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final ScreeningManager screeningManager;

    /**
     * Represents the ReviewManager instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final ReviewManager reviewManager;

    /**
     * Represents the ResourceManager instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final ResourceManager resourceManager;

    /**
     * Represents the UploadManager instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final UploadManager uploadManager;

    /**
     * Represents the UserRetrieval instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final UserRetrieval userRetrieval;

    /**
     * Represents the ReviewScoreAggregator instance. It is initialized in the
     * constructor and never changed after that. It is never null.
     */
    private final ReviewScoreAggregator scorecardAggregator;

    /**
     * This constant stores Online Review's project details page URL.
     *
     * @since 1.3
     */
    private final String projectDetailsBaseURL;

    /**
     * Creates a new instance of ManagerHelper using the default configuration
     * namespace of this class. This constructor loads the manager instances
     * using the configuration settings in the default namespace. Please see
     * Configuration Parameters section the Component Specification for
     * properties used. Also see class documentation for sample configuration
     * file.
     *
     * @throws ConfigurationException if required properties are missing or
     *         error occurs when instantiating the managers.
     */
    public ManagerHelper() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Creates a new instance of ManagerHelper using the given namespace. This
     * constructor loads the manager instances using the configuration settings
     * from given namespace. Please see Configuration Parameters section the
     * Component Specification for properties used. Also see class documentation
     * for sample configuration file.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if required properties are missing or
     *         error occurs when instantiating the managers.
     * @throws IllegalArgumentException if the namespace is null or empty
     *         string.
     */
    public ManagerHelper(String namespace) throws ConfigurationException {
        PhasesHelper.checkString(namespace, "namespace");

        this.projectManager = initManager(namespace, PROP_PROJECT_MGR_CLASS_NAME,
            PROP_PROJECT_MGR_NAMESPACE, ProjectManager.class, false);

        this.projectLinkManager = initProjectLinkManager(namespace,
            PROP_PROJECT_LINK_MGR_CLASS_NAME, PROP_PROJECT_LINK_MGR_NAMESPACE);
        this.phaseManager = initManager(namespace,
                        PROP_PHASE_MGR_CLASS_NAME, PROP_PHASE_MGR_NAMESPACE,
                        PhaseManager.class, false);
        this.reviewManager = initManager(namespace,
                        PROP_REVIEW_MGR_CLASS_NAME, PROP_REVIEW_MGR_NAMESPACE,
                        ReviewManager.class, false);
        this.scorecardManager = initManager(namespace,
                        PROP_SCORECARD_MGR_CLASS_NAME,
                        PROP_SCORECARD_MGR_NAMESPACE, ScorecardManager.class,
                        false);
        this.screeningManager = initScreeningManager(namespace);
        this.uploadManager = initUploadManager(namespace);
        this.resourceManager = initResourceManager(namespace);
        this.userRetrieval = initManager(namespace,
                        PROP_USER_RETRIEVAL_CLASS_NAME,
                        PROP_USER_RETRIEVAL_NAMESPACE, UserRetrieval.class,
                        true);
        this.scorecardAggregator = initScorecardAggregator(namespace);
        this.projectDetailsBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_PROJECT_DETAILS_URL, true);
    }

    /**
     * Gets the non-null ProjectManager instance.
     *
     * @return The non-null ProjectManager instance.
     */
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    /**
     * Gets the non-null ProjectLinkManager instance.
     *
     * @return The non-null ProjectLinkManager instance.
     * @since 1.3
     */
    public ProjectLinkManager getProjectLinkManager() {
        return projectLinkManager;
    }

    /**
     * Gets the non-null PhaseManager instance.
     *
     * @return The non-null PhaseManager instance.
     */
    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    /**
     * Gets the non-null ScorecardManager instance.
     *
     * @return The non-null ScorecardManager instance.
     */
    public ScorecardManager getScorecardManager() {
        return scorecardManager;
    }

    /**
     * Gets the non-null ScreeningManager instance.
     *
     * @return The non-null ScreeningManager instance.
     */
    public ScreeningManager getScreeningManager() {
        return screeningManager;
    }

    /**
     * Gets the non-null ReviewManager instance.
     *
     * @return The non-null ReviewManager instance.
     */
    public ReviewManager getReviewManager() {
        return reviewManager;
    }

    /**
     * Gets the non-null ResourceManager instance.
     *
     * @return The non-null ResourceManager instance.
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Gets the non-null UploadManager instance.
     *
     * @return The non-null UploadManager instance.
     */
    public UploadManager getUploadManager() {
        return uploadManager;
    }

    /**
     * Gets the non-null UserRetrieval instance.
     *
     * @return The non-null UserRetrieval instance.
     */
    public UserRetrieval getUserRetrieval() {
        return userRetrieval;
    }

    /**
     * Gets the non-null ReviewScoreAggregator instance.
     *
     * @return The non-null ReviewScoreAggregator instance.
     */
    public ReviewScoreAggregator getScorecardAggregator() {
        return scorecardAggregator;
    }

    /**
     * <p>
     * Gets the project details base url.
     * </p>
     *
     * @return the project details base url.
     * @since 1.3
     */
    public String getProjectDetailsBaseURL() {
        return projectDetailsBaseURL;
    }

    /**
     * This method is called by constructor to create an instance of
     * ScreeningManager. It retrieves the 'ScreeningManager.Namespace' property
     * and uses either to ScreeningManagerFactory.createScreeningManager() or
     * ScreeningManagerFactory.createScreeningManager(namespace) create the
     * ScreeningManager instance.
     *
     * @param namespace the namespace to load configuration settings from.
     *
     * @return a ScreeningManager instance.
     *
     * @throws ConfigurationException if required properties are missing or
     *         error occurs during instantiation.
     */
    private ScreeningManager initScreeningManager(String namespace) throws ConfigurationException {
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace,
                        PROP_SCREENING_MGR_NAMESPACE, false);

        try {
            if (PhasesHelper.isStringNullOrEmpty(mgrNamespace)) {
                return ScreeningManagerFactory.createScreeningManager();
            } else {
                return ScreeningManagerFactory
                                .createScreeningManager(mgrNamespace);
            }
        } catch (com.cronos.onlinereview.autoscreening.management.ConfigurationException ex) {
            throw new ConfigurationException(
                            "Could not instantiate ScreeningManager", ex);
        }
    }

    /**
     * This method is called by constructor to create an instance of
     * UploadManager. It retrieves the required properties from the given
     * namespace, and creates UploadPersistence, search bundle and id generator
     * instances which are required to create the UploadManager instance.
     * <p>
     * Change in version 1.4: add submission type id generator used to create UploadManager.
     * </p>
     * @param namespace the namespace to load configuration settings from.
     *
     * @return a UploadManager instance.
     *
     * @throws ConfigurationException if required properties are missing or
     *         error occurs during various instantiation processes.
     */
    private UploadManager initUploadManager(String namespace) throws ConfigurationException {
        // get all the property values
        String uploadManagerClassName = PhasesHelper.getPropertyValue(
                        namespace, PROP_UPLOAD_MGR_CLASS_NAME, true);
        String uploadSearchBundleName = PhasesHelper.getPropertyValue(
                        namespace, PROP_UPLOAD_MGR_UPLOAD_BUNDLE_NAME, true);
        String submissionSearchBundleName = PhasesHelper
                        .getPropertyValue(namespace,
                                        PROP_UPLOAD_MGR_SUBMISSION_BUNDLE_NAME,
                                        true);
        String uploadIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                        PROP_UPLOAD_MGR_UPLOAD_IDGEN_NAME, true);
        String uploadTypeIdGeneratorName = PhasesHelper
                        .getPropertyValue(namespace,
                                        PROP_UPLOAD_MGR_UPLOAD_TYPE_IDGEN_NAME,
                                        true);
        String uploadStatusIdGeneratorName = PhasesHelper.getPropertyValue(
                        namespace, PROP_UPLOAD_MGR_UPLOAD_STATUS_IDGEN_NAME,
                        true);
        String submissionIdGeneratorName = PhasesHelper.getPropertyValue(
                        namespace, PROP_UPLOAD_MGR_SUBMISSION_IDGEN_NAME, true);
        String submissionStatusIdGeneratorName = PhasesHelper.getPropertyValue(
                        namespace,
                        PROP_UPLOAD_MGR_SUBMISSION_STATUS_IDGEN_NAME, true);
        String submissionTypeIdGeneratorName = PhasesHelper.getPropertyValue(
                namespace,
                PROP_UPLOAD_MGR_SUBMISSION_TYPE_IDGEN_NAME, true);
        String persistenceClassName = PhasesHelper.getPropertyValue(namespace,
                        PROP_UPLOAD_MGR_PERSISTENCE_CLASS_NAME, true);

        // create persistence instance
        UploadPersistence persistence = createPersistence(
                        namespace, persistenceClassName,
                        UploadPersistence.class);

        // create search bundle instances...
        SearchBundleManager searchBundleManager = createSearchBundleManager(namespace);
        SearchBundle uploadSearchBundle = searchBundleManager
                        .getSearchBundle(uploadSearchBundleName);
        SearchBundle submissionSearchBundle = searchBundleManager
                        .getSearchBundle(submissionSearchBundleName);
        setUploadFieldsSearchable(uploadSearchBundle);
        setUploadFieldsSearchable(submissionSearchBundle);

        // get the id generators...
        IDGenerator uploadIdGenerator = null;
        IDGenerator uploadTypeIdGenerator = null;
        IDGenerator uploadStatusIdGenerator = null;
        IDGenerator submissionIdGenerator = null;
        IDGenerator submissionStatusIdGenerator = null;
        // added in version 1.4
        IDGenerator submissionTypeIdGenerator = null;

        try {
            uploadIdGenerator = IDGeneratorFactory
                            .getIDGenerator(uploadIdGeneratorName);
            uploadTypeIdGenerator = IDGeneratorFactory
                            .getIDGenerator(uploadTypeIdGeneratorName);
            uploadStatusIdGenerator = IDGeneratorFactory
                            .getIDGenerator(uploadStatusIdGeneratorName);
            submissionIdGenerator = IDGeneratorFactory
                            .getIDGenerator(submissionIdGeneratorName);
            submissionStatusIdGenerator = IDGeneratorFactory
                            .getIDGenerator(submissionStatusIdGeneratorName);
            submissionTypeIdGenerator = IDGeneratorFactory
                            .getIDGenerator(submissionTypeIdGeneratorName);
        } catch (IDGenerationException e) {
            throw new ConfigurationException(
                            "Could not instantiate IDGenerator", e);
        }

        // create UploadManager instance using reflection...
        Object[] params = new Object[] {persistence, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
                                        uploadTypeIdGenerator, uploadStatusIdGenerator,
                                        submissionIdGenerator, submissionStatusIdGenerator,
                                        submissionTypeIdGenerator};
        Class<?>[] paramTypes = new Class[] {UploadPersistence.class, SearchBundle.class, SearchBundle.class,
                                          IDGenerator.class, IDGenerator.class,
                                          IDGenerator.class, IDGenerator.class,
                                          IDGenerator.class, IDGenerator.class};

        return createObject(uploadManagerClassName,
                        UploadManager.class, params, paramTypes);
    }

    /**
     * This method is called by constructor to create an instance of
     * ResourceManager. It retrieves the required properties from the given
     * namespace, and creates ResourcePersistence, search bundle and id
     * generator instances which are required to create the ResourceManager
     * instance.
     *
     * @param namespace the namespace to load configuration settings from.
     *
     * @return a ResourceManager instance.
     *
     * @throws ConfigurationException if required properties are missing or
     *         error occurs during various instantiation processes.
     */
    private ResourceManager initResourceManager(String namespace) throws ConfigurationException {
        // get all the property values
        String resourceManagerClassName = PhasesHelper.getPropertyValue(
                        namespace, PROP_RESOURCE_MGR_CLASS_NAME, true);
        String resourceSearchBundleName = PhasesHelper
                        .getPropertyValue(namespace,
                                        PROP_RESOURCE_MGR_RESOURCE_BUNDLE_NAME,
                                        true);
        String resourceRoleSearchBundleName = PhasesHelper.getPropertyValue(
                        namespace, PROP_RESOURCE_MGR_RESOURCE_ROLE_BUNDLE_NAME,
                        true);
        String notificationSearchBundleName = PhasesHelper.getPropertyValue(
                        namespace, PROP_RESOURCE_MGR_NOTIFICATION_BUNDLE_NAME,
                        true);
        String notificationTypeSearchBundleName = PhasesHelper
                        .getPropertyValue(
                                        namespace,
                                        PROP_RESOURCE_MGR_NOTIFICATION_TYPE_BUNDLE_NAME,
                                        true);
        String resourceIdGeneratorName = PhasesHelper.getPropertyValue(
                        namespace, PROP_RESOURCE_MGR_RESOURCE_IDGEN_NAME, true);
        String notificationIdGeneratorName = PhasesHelper.getPropertyValue(
                        namespace,
                        PROP_RESOURCE_MGR_NOTIFICATION_TYPE_IDGEN_NAME, true);
        String resourceRoleIdGeneratorName = PhasesHelper.getPropertyValue(
                        namespace, PROP_RESOURCE_MGR_RESOURCE_ROLE_IDGEN_NAME,
                        true);
        String persistenceClassName = PhasesHelper.getPropertyValue(namespace,
                        PROP_RESOURCE_MGR_PERSISTENCE_CLASS_NAME, true);

        // create persistence instance
        ResourcePersistence persistence = (ResourcePersistence) createPersistence(
                        namespace, persistenceClassName,
                        ResourcePersistence.class);

        // create search bundle instances...
        SearchBundleManager searchBundleManager = createSearchBundleManager(namespace);
        SearchBundle resourceSearchBundle = searchBundleManager
                        .getSearchBundle(resourceSearchBundleName);
        SearchBundle resourceRoleSearchBundle = searchBundleManager
                        .getSearchBundle(resourceRoleSearchBundleName);
        SearchBundle notificationSearchBundle = searchBundleManager
                        .getSearchBundle(notificationSearchBundleName);
        SearchBundle notificationTypeSearchBundle = searchBundleManager
                        .getSearchBundle(notificationTypeSearchBundleName);

        // set all fields searchable
        setResourceFieldsSearchable(resourceSearchBundle);
        setResourceFieldsSearchable(resourceRoleSearchBundle);
        setResourceFieldsSearchable(notificationSearchBundle);
        setResourceFieldsSearchable(notificationTypeSearchBundle);

        // get the id generators...
        IDGenerator resourceIdGenerator = null;
        IDGenerator resourceRoleIdGenerator = null;
        IDGenerator notificationTypeIdGenerator = null;

        try {
            resourceIdGenerator = IDGeneratorFactory
                            .getIDGenerator(resourceIdGeneratorName);
            resourceRoleIdGenerator = IDGeneratorFactory
                            .getIDGenerator(resourceRoleIdGeneratorName);
            notificationTypeIdGenerator = IDGeneratorFactory
                            .getIDGenerator(notificationIdGeneratorName);
        } catch (IDGenerationException e) {
            throw new ConfigurationException(
                            "Could not instantiate IDGenerator", e);
        }

        // create ResourceManager instance using reflection...
        Object[] params = new Object[] {persistence, resourceSearchBundle,
                                        resourceRoleSearchBundle, notificationSearchBundle,
                                        notificationTypeSearchBundle, resourceIdGenerator,
                                        resourceRoleIdGenerator, notificationTypeIdGenerator};
        Class<?>[] paramTypes = new Class[] {ResourcePersistence.class,
            SearchBundle.class, SearchBundle.class, SearchBundle.class, SearchBundle.class,
            IDGenerator.class, IDGenerator.class, IDGenerator.class};

        return createObject(resourceManagerClassName,
                        ResourceManager.class, params, paramTypes);
    }

    /**
     * Sets the searchable fields to the search bundle.
     *
     * @param searchBundle the search bundle to set
     */
    private void setResourceFieldsSearchable(SearchBundle searchBundle) {
        Map<String, AbstractObjectValidator> fields = new HashMap<String, AbstractObjectValidator>();

        // set the resource filter fields
        fields.put(ResourceFilterBuilder.RESOURCE_ID_FIELD_NAME, LongValidator
                        .isPositive());
        fields.put(ResourceFilterBuilder.PHASE_ID_FIELD_NAME, LongValidator
                        .isPositive());
        fields.put(ResourceFilterBuilder.PROJECT_ID_FIELD_NAME, LongValidator
                        .isPositive());
        fields.put(ResourceFilterBuilder.SUBMISSION_ID_FIELD_NAME,
                        LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME,
                        LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.EXTENSION_PROPERTY_NAME_FIELD_NAME,
                        StringValidator.startsWith(""));
        fields.put(ResourceFilterBuilder.EXTENSION_PROPERTY_VALUE_FIELD_NAME,
                        StringValidator.startsWith(""));

        // set the resource role filter fields
        fields.put(ResourceRoleFilterBuilder.NAME_FIELD_NAME, StringValidator
                        .startsWith(""));
        fields.put(ResourceRoleFilterBuilder.PHASE_TYPE_ID_FIELD_NAME,
                        LongValidator.isPositive());
        fields.put(ResourceRoleFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME,
                        LongValidator.isPositive());

        // set the notification filter fields
        fields.put(NotificationFilterBuilder.EXTERNAL_REF_ID_FIELD_NAME,
                        LongValidator.isPositive());
        fields.put(NotificationFilterBuilder.NOTIFICATION_TYPE_ID_FIELD_NAME,
                        LongValidator.isPositive());
        fields.put(NotificationFilterBuilder.PROJECT_ID_FIELD_NAME,
                        LongValidator.isPositive());

        // set the notification type filter fields
        fields.put(NotificationTypeFilterBuilder.NOTIFICATION_TYPE_ID_FIELD_NAME,
            LongValidator.isPositive());
        fields.put(NotificationTypeFilterBuilder.NAME_FIELD_NAME,
            StringValidator.startsWith(""));

        searchBundle.setSearchableFields(fields);
    }

    /**
     * Sets the searchable fields to the search bundle.
     *
     * @param searchBundle the search bundle to set
     */
    private void setUploadFieldsSearchable(SearchBundle searchBundle) {
        Map<String, AbstractObjectValidator> fields = new HashMap<String, AbstractObjectValidator>();

        // set the upload filter fields
        fields.put("upload_id", LongValidator.isPositive());
        fields.put("upload_type_id", LongValidator.isPositive());
        fields.put("upload_status_id", LongValidator.isPositive());
        fields.put("project_id", LongValidator.isPositive());
        fields.put("resource_id", LongValidator.isPositive());
        fields.put("submission_id", LongValidator.isPositive());
        fields.put("submission_status_id", LongValidator.isPositive());
        fields.put("submission_type_id", LongValidator.isPositive());
        fields.put("deliverable_id", LongValidator.isPositive());
        fields.put("phase_id", LongValidator.isPositive());
        fields.put("name", StringValidator.startsWith(""));

        searchBundle.setSearchableFields(fields);
    }

    /**
     * This method is called by initResourceManager() and initUploadManager() to
     * create SearchBundleManager instance.
     *
     * @param namespace the namespace to load configuration settings from.
     *
     * @return SearchBundleManager instance.
     *
     * @throws ConfigurationException if 'SearchBundleManagerNS' property is
     *         missing or if error occurs during instantiation.
     */
    private SearchBundleManager createSearchBundleManager(String namespace) throws ConfigurationException {
        String searchBundleManagerNS = PhasesHelper.getPropertyValue(namespace,
                        PROP_SEARCH_BUNDLE_MGR_NS, true);

        try {
            return new SearchBundleManager(searchBundleManagerNS);
        } catch (SearchBuilderConfigurationException e) {
            throw new ConfigurationException(
                            "Could not instantiate SearchBundleManager.", e);
        }
    }

    /**
     * This method is called by initResourceManager() and initUploadManager() to
     * create either ResourcePersistence or UploadPersistence instance. Since
     * constructor signatures for both these classes is same, this common method
     * is used.
     *
     * @param namespace the namespace to load configuration settings from.
     * @param className class name of ResourcePersistence or UploadPersistence
     *        implementation.
     * @param expectedType expected type of the returned instance.
     *
     * @return a ResourcePersistence or UploadPersistence instance.
     *
     * @throws ConfigurationException if some property is missing or an error
     *         occurs during instantiation.
     */
    private <T> T createPersistence(String namespace, String className,
                    Class<T> expectedType) throws ConfigurationException {
        // initialize DBConnectionFactory from given namespace, throw exception
        // if property is missing.
        DBConnectionFactory dbConnFactory = PhasesHelper
                        .createDBConnectionFactory(namespace,
                                        PROP_CONNECTION_FACTORY_NS);
        String connectionName = PhasesHelper.getPropertyValue(namespace,
                        PROP_CONNECTION_NAME, false);

        Object[] params = null;
        Class<?>[] paramTypes = null;

        if (PhasesHelper.isStringNullOrEmpty(connectionName)) {
            // if connection name not specified, call constructor with
            // DBConnectionFactory argument.
            params = new Object[] {dbConnFactory};
            paramTypes = new Class<?>[] {DBConnectionFactory.class};
        } else {
            // else, call constructor with DBConnectionFactory and
            // connectionName arguments.
            params = new Object[] {dbConnFactory, connectionName};
            paramTypes = new Class<?>[] {DBConnectionFactory.class, String.class};
        }

        return createObject(className, expectedType, params, paramTypes);
    }

    /**
     * Helper method to instantiate ReviewScoreAggregator.
     *
     * @param namespace the namespace to load configuration settings from.
     *
     * @return ReviewScoreAggregator instance.
     * @throws ConfigurationException if instantiation fails.
     */
    private ReviewScoreAggregator initScorecardAggregator(String namespace) throws ConfigurationException {
        String scoreAggregatorNS = PhasesHelper.getPropertyValue(namespace,
                        PROP_SCORE_AGGREGATOR_NAMESPACE, false);
        if (PhasesHelper.isStringNullOrEmpty(scoreAggregatorNS)) {
            return new ReviewScoreAggregator();
        } else {
            try {
                return new ReviewScoreAggregator(scoreAggregatorNS);
            } catch (ReviewScoreAggregatorConfigException e) {
                throw new ConfigurationException(
                                "could not instantiate ReviewScoreAggregator",
                                e);
            }
        }
    }

    /**
     * This method is used to instantiate ScorecardManager, ReviewManager,
     * ProjectManager, and UserRetrieval instances since all use the same
     * constructor signature.
     *
     * @param namespace the namespace to load configuration settings from.
     * @param classPropName name of property which holds the class name to
     *        instantiate.
     * @param nsPropName name of property which holds namespace argument for
     *        constructor.
     * @param expectedType type of instance to be returned.
     * @param nsPropertyReqd whether property by name nsPropName is required,
     *        which is true in case of Retrieval classes, false otherwise.
     *
     * @return either a ScorecardManager, ReviewManager, ProjectManager, or
     *         UserRetrieval instance.
     *
     * @throws ConfigurationException if a required property is missing or if a
     *         problem occurs during instantiation.
     */
    private <T> T initManager(String namespace, String classPropName,
                    String nsPropName, Class<T> expectedType,
                    boolean nsPropertyReqd) throws ConfigurationException {
        String mgrClassName = PhasesHelper.getPropertyValue(namespace,
                        classPropName, true);
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace,
                        nsPropName, nsPropertyReqd);

        Object[] params = null;
        Class<?>[] paramTypes = null;

        if (!PhasesHelper.isStringNullOrEmpty(mgrNamespace)) {
            params = new Object[] {mgrNamespace};
            paramTypes = MANAGER_PARAM_TYPES;
        }

        return createObject(mgrClassName, expectedType, params, paramTypes);
    }

    /**
     * This method is used to instantiate ScorecardManager, ReviewManager, ProjectManager, and UserRetrieval
     * instances since all use the same constructor signature.
     *
     * @param namespace the namespace to load configuration settings from.
     * @param classPropName name of property which holds the class name to instantiate.
     * @param nsPropName name of property which holds namespace argument for constructor.
     * @return either a ScorecardManager, ReviewManager, ProjectManager, or UserRetrieval instance.
     * @throws ConfigurationException if a required property is missing or if a problem occurs during instantiation.
     * @since 1.3
     */
    private ProjectLinkManager initProjectLinkManager(String namespace, String classPropName, String nsPropName)
        throws ConfigurationException {
        String mgrClassName = PhasesHelper.getPropertyValue(namespace, classPropName, true);
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace, nsPropName, true);

        Object[] params = new Object[]{mgrNamespace, this.projectManager};

        return createObject(mgrClassName, ProjectLinkManager.class, params,
                                                 PROJECT_LINK_MANAGER_PARAM_TYPES);
    }

    /**
     * Helper method to instantiate the specified className using reflection.
     * The params is passed to constructor during reflection.
     * <p>
     * Change in version 1.4: Use generic type to create object.
     * </p>
     *
     * @param <T> the generic class type.
     * @param className name of class to be instantiated.
     * @param expectedType expected type of the return instance.
     * @param params constructor params.
     * @param paramTypes the Class array specifying the type of Objects in the
     *        'params' parameter
     *
     * @return instance of type className.
     *
     * @throws ConfigurationException if an error occurs during instantiation.
     */
    private <T> T createObject(String className, Class<T> expectedType,
                    Object[] params, Class<?>[] paramTypes) throws ConfigurationException {
        // instantiate using reflection.
        try {
            Class<? extends T> clazz = Class.forName(className).asSubclass(expectedType);
            return clazz.getConstructor(paramTypes).newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Could not find class:"
                            + className, e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException(
                            "The object could not be instantiated.", e);
        } catch (SecurityException e) {
            throw new ConfigurationException(
                            "The object could not be instantiated.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException(
                            "The object could not be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException(
                            "The object could not be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException(
                            "The object could not be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException(
                            "The object could not be instantiated.", e);
        } catch (ClassCastException e) {
            throw new ConfigurationException(className
                + " must be of type " + expectedType.getName());
        } catch (Error e) {
            throw e;
        }
    }
}
