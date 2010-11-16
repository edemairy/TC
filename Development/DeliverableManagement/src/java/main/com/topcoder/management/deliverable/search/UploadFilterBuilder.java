/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.search;

import com.topcoder.management.deliverable.DeliverableHelper;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * The UploadFilterBuilder class supports building filters for searching for Uploads.
 * </p>
 * <p>
 * This class has only final static fields/methods, so mutability is not an issue.
 * </p>
 *
 * @author aubergineanode, singlewood
 * @version 1.0.2
 */
public class UploadFilterBuilder {

    /**
     * The name of the field under which SearchBuilder Filters can be built in order to filter on
     * the id of the Upload.
     */
    private static final String UPLOAD_ID_FIELD_NAME = "upload_id";

    /**
     * The name of the field under which SearchBuilder Filters can be built in order to filter on
     * the type id of the Upload.
     */
    private static final String UPLOAD_TYPE_ID_FIELD_NAME = "upload_type_id";

    /**
     * The name of the field under which SearchBuilder Filters can be built in order to filter on
     * the status id of the Upload.
     */
    private static final String UPLOAD_STATUS_ID_FIELD_NAME = "upload_status_id";

    /**
     * The name of the field under which SearchBuilder Filters can be built in order to filter on
     * the project the Upload is associated with.
     */
    private static final String PROJECT_ID_FIELD_NAME = "project_id";

    /**
     * The name of the field under which SearchBuilder Filters can be built in order to filter on
     * the resource the Upload is associated with.
     */
    private static final String RESOURCE_ID_FIELD_NAME = "resource_id";

    /**
     * Private constructor to prevent instantiation.
     */
    private UploadFilterBuilder() {
        // no operation
    }

    /**
     * Creates a filter that selects uploads with the given id.
     *
     * @return A filter for selecting uploads with the given id.
     * @param uploadId The upload id to filter on
     * @throws IllegalArgumentException If uploadId is <= 0
     */
    public static Filter createUploadIdFilter(long uploadId) {
        DeliverableHelper.checkGreaterThanZero(uploadId, "uploadId");
        return new EqualToFilter(UPLOAD_ID_FIELD_NAME, new Long(uploadId));
    }

    /**
     * Creates a filter that selects uploads with the given type id.
     *
     * @return A filter for selecting uploads with the given type id.
     * @param uploadTypeId The upload type id to filter on
     * @throws IllegalArgumentException If uploadTypeId is <= 0
     */
    public static Filter createUploadTypeIdFilter(long uploadTypeId) {
        DeliverableHelper.checkGreaterThanZero(uploadTypeId, "uploadTypeId");
        return new EqualToFilter(UPLOAD_TYPE_ID_FIELD_NAME, new Long(uploadTypeId));
    }

    /**
     * Creates a filter that selects uploads with the given status id.
     *
     * @return A filter for selecting uploads with the given status id.
     * @param uploadStatusId The upload id to filter on
     * @throws IllegalArgumentException If uploadStatusId is <= 0
     */
    public static Filter createUploadStatusIdFilter(long uploadStatusId) {
        DeliverableHelper.checkGreaterThanZero(uploadStatusId, "uploadStatusId");
        return new EqualToFilter(UPLOAD_STATUS_ID_FIELD_NAME, new Long(uploadStatusId));
    }

    /**
     * Creates a filter that selects uploads related to the project with the given id.
     *
     * @return A filter for selecting uploads related to the given project
     * @param projectId The project id to filter on
     * @throws IllegalArgumentException If projectId is <= 0
     */
    public static Filter createProjectIdFilter(long projectId) {
        DeliverableHelper.checkGreaterThanZero(projectId, "projectId");
        return new EqualToFilter(PROJECT_ID_FIELD_NAME, new Long(projectId));
    }

    /**
     * Creates a filter that selects uploads related to the given resource id.
     *
     * @return A filter for selecting uploads related to the given resource
     * @param resourceId The resource id to filter on
     * @throws IllegalArgumentException If resourceId is <= 0
     */
    public static Filter createResourceIdFilter(long resourceId) {
        DeliverableHelper.checkGreaterThanZero(resourceId, "resourceId");
        return new EqualToFilter(RESOURCE_ID_FIELD_NAME, new Long(resourceId));
    }
}
