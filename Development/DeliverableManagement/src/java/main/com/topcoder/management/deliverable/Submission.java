/*
 * Copyright (C) 2006-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p>
 * The Submission class is the one of the main modeling classes of this component. It represents a submission, which
 * consists of an upload and a status. The Submission class is simply a container for a few basic data fields. All
 * data fields in this class are mutable and have get and set methods.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1: </em>
 * <ul>
 * <li>submissionType attribute was added together with getter and setter.</li>
 * <li>isValidToPersist() was updated to ensure that submission type is specified.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is highly mutable. All fields can be changed.
 * </p>
 *
 * @author aubergineanode, singlewood, saarixx, sparemax
 * @version 1.1
 */
public class Submission extends AuditedDeliverableStructure {
    /**
     * <p>
     * The serial version id.
     * </p>
     */
    private static final long serialVersionUID = -4755373779145413990L;

    /**
     * The upload that is associated with the submission. This field can be null or non-null and is mutable. The
     * default value is null, which indicates that this field has not been set. This field can be set through the
     * setUpload method and retrieved through the getUpload method.
     */
    private Upload upload = null;

    /**
     * The status of the submission. This field can be null or non-null and is mutable. The default value is null,
     * which indicates that this field has not been set.
     */
    private SubmissionStatus submissionStatus = null;

    /**
     * <p>
     * The type of the submission.
     * </p>
     *
     * <p>
     * This field can be null or non-null and is mutable. The default value is null, which indicates that this field
     * has not been set. This field can be set through the setSubmissionType method and retrieved through the
     * getSubmissionType method.
     * </p>
     *
     * @since 1.1
     */
    private SubmissionType submissionType;

    /**
     * The screening score for the submission.
     */
    private Double screeningScore;

    /**
     * The initial score for the submission.
     */
    private Double initialScore;

    /**
     * The final score for the submission.
     */
    private Double finalScore;

    /**
     * The placement for this submission.
     */
    private Long placement;

    /**
     * Creates a new Submission.
     */
    public Submission() {
        super();
    }

    /**
     * Creates a new Submission.
     *
     * @param id
     *            The id of the submission
     * @throws IllegalArgumentException
     *             if id is <= 0
     */
    public Submission(long id) {
        super(id);
    }

    /**
     * Sets the upload associated with the Submission. The parameter may be null or non-null.
     *
     * @param upload
     *            The upload associated with the Submission.
     */
    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    /**
     * Gets the upload associated with the Submission. May return null or non-null.
     *
     * @return The upload associated with the Submission.
     */
    public Upload getUpload() {
        return upload;
    }

    /**
     * Sets the submission status of the Submission. The parameter may be null or non-null.
     *
     * @param submissionStatus
     *            The status of the submission.
     */
    public void setSubmissionStatus(SubmissionStatus submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    /**
     * Gets the submission status of the Submission. May return null or non-null.
     *
     * @return The status of the submission.
     */
    public SubmissionStatus getSubmissionStatus() {
        return submissionStatus;
    }

    /**
     * <p>
     * Sets the submission type of the Submission. The parameter may be null or non-null.
     * </p>
     *
     * @param submissionType
     *            the type of the submission.
     *
     * @since 1.1
     */
    public void setSubmissionType(SubmissionType submissionType) {
        this.submissionType = submissionType;
    }

    /**
     * <p>
     * Gets the submission type of the Submission. May return null or non-null.
     * </p>
     *
     * @return the type of the submission.
     *
     * @since 1.1
     */
    public SubmissionType getSubmissionType() {
        return submissionType;
    }

    /**
     * Returns the finalScore field value.
     *
     * @return the finalScore
     */
    public Double getFinalScore() {
        return finalScore;
    }

    /**
     * Sets the finalScore with the given value.
     *
     * @param finalScore
     *            the finalScore to set
     */
    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    /**
     * Returns the initialScore field value.
     *
     * @return the initialScore
     */
    public Double getInitialScore() {
        return initialScore;
    }

    /**
     * Sets the initialScore with the given value.
     *
     * @param initialScore
     *            the initialScore to set
     */
    public void setInitialScore(Double initialScore) {
        this.initialScore = initialScore;
    }

    /**
     * Returns the placement field value.
     *
     * @return the placement
     */
    public Long getPlacement() {
        return placement;
    }

    /**
     * Sets the placement with the given value.
     *
     * @param placement
     *            the placement to set
     */
    public void setPlacement(Long placement) {
        this.placement = placement;
    }

    /**
     * Returns the screeningScore field value.
     *
     * @return the screeningScore
     */
    public Double getScreeningScore() {
        return screeningScore;
    }

    /**
     * Sets the screeningScore with the given value.
     *
     * @param screeningScore
     *            the screeningScore to set
     */
    public void setScreeningScore(Double screeningScore) {
        this.screeningScore = screeningScore;
    }

    /**
     * <p>
     * Tells whether all the required fields of this Submission have values set.
     * </p>
     *
     * <p>
     * Changes in 1.1:
     * <ul>
     * <li>Added check "submissionType is not null and isValidToPersist"</li>
     * </ul>
     * </p>
     *
     * @return True if all fields required for persistence are present
     */
    public boolean isValidToPersist() {
        // This method returns true if all of the following are true: id is not UNSET_ID, upload is
        // not null and isValidToPersist, submissionStatus is not null and isValidToPersist,
        // submissionType is not null and isValidToPersist, super.isValidTopPersist is true.
        return ((super.getId() != UNSET_ID) && (upload != null) && (upload.isValidToPersist())
                && (submissionStatus != null) && (submissionStatus.isValidToPersist())
                && (submissionType != null) && (submissionType.isValidToPersist()) && super.isValidToPersist());
    }

}
