/*
 * Copyright (C) 2006-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for Submission.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1: </em>
 * <ul>
 * <li>Added test cases for getter and setter of submissionType attribute.</li>
 * <li>Added test case testIsValidToPersist_Accuracy6.</li>
 * <li>Updated test case testIsValidToPersist_Accuracy5.</li>
 * </ul>
 * </p>
 *
 * @author singlewood, sparemax
 * @version 1.1
 */
public class SubmissionTests extends TestCase {
    /**
     * <p>
     * The test Submission instance.
     * </p>
     */
    private Submission submission;

    /**
     * <p>
     * The test Upload instance.
     * </p>
     */
    private Upload upload;

    /**
     * <p>
     * The test SubmissionStatus instance.
     * </p>
     */
    private SubmissionStatus submissionStatus;

    /**
     * <p>
     * The test SubmissionType instance.
     * </p>
     *
     * @since 1.1
     */
    private SubmissionType submissionType;

    /**
     * Create the test instance.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        upload = DeliverableTestHelper.getValidToPersistUpload();
        upload.setId(1);

        submissionStatus = DeliverableTestHelper.getValidToPersistSubmissionStatus();
        submissionStatus.setId(1);
        submissionType = DeliverableTestHelper.getValidToPersistSubmissionType();
        submissionType.setId(1);

        submission = DeliverableTestHelper.getValidToPersistSubmission();
    }

    /**
     * Clean the config.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        upload = null;
        submissionStatus = null;
        submission = null;
    }

    /**
     * The default constructor should set id to UNSET_ID. So check if id is set properly. No
     * exception should be thrown.
     */
    public void testConstructor1_Accuracy1() {
        assertEquals("the constructor doesn't set id properly", Submission.UNSET_ID,
                submission.getId());
    }

    /**
     * Test constructor2 with invalid parameters. The argument will be set to 0.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor2_InvalidLong1() {
        try {
            new Submission(0);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with invalid parameters. The argument will be set to -2.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor2_InvalidLong2() {
        try {
            new Submission(-2);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with valid parameter. Check if the constructor set the id fields properly.
     * No exception should be thrown.
     */
    public void testConstructor2_Accuracy1() {
        submission = new Submission(123);
        assertEquals("constructor doesn't work properly.", 123, submission.getId());
    }

    /**
     * Test the behavior of setUpload. Set the upload field, see if the getUpload method can get the
     * correct value. No exception should be thrown.
     */
    public void testSetUpload_Accuracy1() {
        submission.setUpload(upload);
        assertEquals("upload is not set properly.", upload, submission.getUpload());
    }

    /**
     * Test the behavior of getUpload. Set the upload field, see if the getUpload method can get the
     * correct value. No exception should be thrown.
     */
    public void testGetUpload_Accuracy1() {
        submission.setUpload(upload);
        assertEquals("getUpload doesn't work properly.", upload, submission.getUpload());
    }

    /**
     * Test the behavior of setSubmissionStatus. Set the submissionStatus field, see if the
     * getSubmissionStatus method can get the correct value. No exception should be thrown.
     */
    public void testSetSubmissionStatus_Accuracy1() {
        submission.setSubmissionStatus(submissionStatus);
        assertEquals("submissionStatus is not set properly.", submissionStatus, submission
                .getSubmissionStatus());
    }

    /**
     * Test the behavior of getSubmissionStatus. Set the submissionStatus field, see if the
     * getSubmissionStatus method can get the correct value. No exception should be thrown.
     */
    public void testGetSubmissionStatus_Accuracy1() {
        submission.setSubmissionStatus(submissionStatus);
        assertEquals("getSubmissionStatus doesn't work properly.", submissionStatus, submission
                .getSubmissionStatus());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getSubmissionType()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 1.1
     */
    public void test_getSubmissionType() {
        SubmissionType value = new SubmissionType();
        submission.setSubmissionType(value);

        assertSame("'submissionType' value should be properly retrieved.", value, submission.getSubmissionType());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setSubmissionType(SubmissionType submissionType)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 1.1
     */
    public void test_setSubmissionType() {
        SubmissionType value = new SubmissionType();
        submission.setSubmissionType(value);

        assertSame("'submissionType' value should be properly set.", value, submission.getSubmissionType());
    }

    /**
     * Test the behavior of isValidToPersist. Set the upload field to null, see if the
     * isValidToPersist returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy1() {
        submission.setUpload(null);
        submission.setSubmissionStatus(submissionStatus);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Test the behavior of isValidToPersist. Set the submissionStatus field to null, see if the
     * isValidToPersist returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy2() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(null);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Test the behavior of isValidToPersist. Do not set id field, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy3() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Test the behavior of isValidToPersist. Set super.isValidToPersist() to false, see if the
     * isValidToPersist returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy4() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setId(1);
        submission.setCreationUser(null);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

    /**
     * Test the behavior of isValidToPersist. Set all the field with non-null values, see if the
     * isValidToPersist returns true. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy5() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(submissionType);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", true, submission.isValidToPersist());
    }

    /**
     * <p>
     * Test the behavior of isValidToPersist. Set super.isValidToPersist() to false, see if the isValidToPersist
     * returns false. No exception should be thrown.
     * </p>
     *
     * @since 1.1
     */
    public void testIsValidToPersist_Accuracy6() {
        submission.setUpload(upload);
        submission.setSubmissionStatus(submissionStatus);
        submission.setSubmissionType(null);
        submission.setId(1);
        assertEquals("isValidToPersist doesn't work properly.", false, submission.isValidToPersist());
    }

}
