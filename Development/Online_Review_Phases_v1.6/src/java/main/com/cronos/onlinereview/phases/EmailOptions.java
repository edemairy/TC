/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

/**
 * <p>
 * This class represents email options. It is a container for the set of options related to email to be send, such as
 * subject, from address, template source, template name and whether the email is to be sent or not. There is no
 * validation performed in this class.
 * </p>
 *
 * <p>
 * Thread safety: This class is not thread safe as it is mutable.
 * </p>
 *
 * @author TCSDESIGNER, waits
 * @version 1.0
 *
 * @since 1.2
 */
public class EmailOptions {
    /**
     * <p>
     * Represents the subject of the email. This field is initialized to the default value of the data type.  It can be
     * any value. It has getter/setter.
     * </p>
     */
    private String subject;

    /**
     * <p>
     * Represents the from address of the email. This field is initialized to the default value of the data type.  It
     * can be any value. It has getter/setter.
     * </p>
     */
    private String fromAddress;

    /**
     * <p>
     * Represents the template source to load email template for. Template sources can be file or database, etc.  This
     * field is initialized to the default value of the data type.  It can be any value. It has getter/setter.
     * </p>
     */
    private String templateSource;

    /**
     * <p>
     * Represents the template name to load email template. Template name can be a file name for example. This field is
     * initialized to the default value of the data type.  It can be any value. It has getter/setter.
     * </p>
     */
    private String templateName;

    /**
     * <p>
     * Represents whether the email is to be sent or not. This field is initialized to the default value of the data
     * type.  It can be any value. It has getter/setter.
     * </p>
     */
    private Boolean send;

    /**
     * <p>
     * Represents the priority of the email options. Default value is 0.It has getter/setter.
     * </p>
     */
    private int priority = 0;

    /**
     * <p>
     * Default empty Constructor.
     * </p>
     */
    public EmailOptions() {
    }

    /**
     * Returns the value of subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the value to  subject field.
     *
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the value of fromAddress.
     *
     * @return the fromAddress
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Set the value to  fromAddress field.
     *
     * @param fromAddress the fromAddress to set
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * Returns the value of templateSource.
     *
     * @return the templateSource
     */
    public String getTemplateSource() {
        return templateSource;
    }

    /**
     * Set the value to  templateSource field.
     *
     * @param templateSource the templateSource to set
     */
    public void setTemplateSource(String templateSource) {
        this.templateSource = templateSource;
    }

    /**
     * Returns the value of templateName.
     *
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Set the value to  templateName field.
     *
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Returns the value of priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set the value to  priority field.
     *
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the value of send.
     *
     * @return the send
     */
    public Boolean isSend() {
        return send;
    }

    /**
     * Set the value to  send field.
     *
     * @param send the send to set
     */
    public void setSend(Boolean send) {
        this.send = send;
    }
}
