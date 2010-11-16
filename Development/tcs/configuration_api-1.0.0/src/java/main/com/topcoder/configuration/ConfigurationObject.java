/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * A ConfigurationObject is an object which contains configuration information.
 *
 * <p>
 * ConfigurationObject can have zero or more properties associated with it.
 * All the properties are consisted of a String key and a set of values.
 * Property key must be unique in the same ConfigurationObject.
 * The values set can be empty, and can contain any kind of value (including null).
 * </p>
 *
 * <p>
 * ConfigurationObject can also contain zero or more child ConfigurationObject.
 * The children are uniquely identified by their names. There is no restriction on the child-parent
 * relationships in this interface (API definition). Some implementations may only allow tree structure,
 * or only allow DAG, and so on.
 * </p>
 *
 * <p>
 * Methods in this interface can be categorized in two dimensions.
 * One dimension divides methods into two categories: properties operation, and children operation.
 * The other dimension divides methods by the way to search properites and children.
 * The direct way to search properties and children is to use exactly their names.
 * The second way is to use regex expression to match properties or children names,
 * And the third way is to use wildcard match (like in UNIX file system) to find children.
 * </p>
 *
 * <p>Thread safe: All the implementation should treat the thread safe problem by their own.</p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public interface ConfigurationObject extends Cloneable {
    /**
     * Get the name of the ConfigurationObject.
     *
     * @return the name
     * @throws ConfigurationAccessException if any error occurs while retrieving name.
     */
    public String getName() throws ConfigurationAccessException;

    /**
     * Determine whether property with given key exists in the configuration object.
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key the key of property
     * @throws IllegalArgumentException if key is null or empty
     * @throws ConfigurationAccessException if any error occurs while query the property.
     */
    public boolean containsProperty(String key)
        throws ConfigurationAccessException;

    /**
     * Get the property value for the given key.
     * <p> Note:if the key contains more than one values, the value returned depends on specific implementation.</p>
     *
     * @return the value associated with the key, or null if there is no such key.
     * @param key the key of property.
     * @throws IllegalArgumentException if key is null or empty
     * @throws ConfigurationAccessException if any error occurs while retrieve the property value.
     */
    public Object getPropertyValue(String key)
        throws ConfigurationAccessException;

    /**
     * Get the number of values contained by the specific property.
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     * @param key the key specifying the property
     * @throws IllegalArgumentException if key is null or empty
     * @throws ConfigurationAccessException if any error occurs while count the property values.
     */
    public int getPropertyValuesCount(String key)
        throws ConfigurationAccessException;

    /**
     * Get all the property values associated with given key.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     * @param key the key used to specify property
     * @throws IllegalArgumentException if key is null or empty
     * @throws ConfigurationAccessException if any error occurs while retrieve the property values.
     */
    public Object[] getPropertyValues(String key)
        throws ConfigurationAccessException;

    /**
     * Set a key/value pair to the configuration object.
     * <p> If the key already exists, old values will be overriden. </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key the key of property
     * @param value the value of the property.
     * @throws IllegalArgumentException if key is null or empty
     * @throws InvalidConfigurationException if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException if any error occurs while set the property value
     */
    public Object[] setPropertyValue(String key, Object value)
        throws InvalidConfigurationException, ConfigurationAccessException;

    /**
     * Set a key/values pair to the configuration object.
     * <p> If the key already exists, old values will be overriden. </p>
     * NOTE: the order of properties would be kept.
     *
     * @return the old values of the property, or null if the key is new.
     * @param key the key of property
     * @param values the values array of the property.
     * @throws IllegalArgumentException if key is null or empty
     * @throws InvalidConfigurationException if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException if any error occurs while set the property values
     */
    public Object[] setPropertyValues(String key, Object[] values)
        throws InvalidConfigurationException, ConfigurationAccessException;

    /**
     * Remove property with given key. <p> If the key doens't exist, nothing happened. </p>
     *
     * @return the removed property value, or null if no such property.
     * @param key the key of the property to be removed
     * @throws IllegalArgumentException if key is null or empty.
     * @throws ConfigurationAccessException if any error occurs while removing the property.
     */
    public Object[] removeProperty(String key)
        throws ConfigurationAccessException;

    /**
     * Clear all the properties.
     *
     * @throws ConfigurationAccessException if any error occurs while clearing properties.
     */
    public void clearProperties() throws ConfigurationAccessException;

    /**
     * Get all the property keys matched the pattern.
     *
     * @return an array of matched property keys.
     * @param pattern a regular expression used to match property keys.
     * @throws IllegalArgumentException if pattern is null or an invalid regex.
     * @throws ConfigurationAccessException if any error occurs while retrieving the property keys.
     */
    public String[] getPropertyKeys(String pattern)
        throws ConfigurationAccessException;

    /**
     * Get all the property keys contained by the ConfigurationObject.
     *
     * @return an array of all property keys.
     * @throws ConfigurationAccessException if any error occurs while retrieving the property keys.
     */
    public String[] getAllPropertyKeys() throws ConfigurationAccessException;

    /**
     * Determine whether it contains a child ConfigurationObject with given name.
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name the name of the child
     * @throws IllegalArgumentException if name is null or empty
     * @throws ConfigurationAccessException if any error occurs while querying with given name.
     */
    public boolean containsChild(String name)
        throws ConfigurationAccessException;

    /**
     * Get a child ConfigurationObject with given name.
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name the name of the child
     * @throws IllegalArgumentException if name is null or empty
     * @throws ConfigurationAccessException if any error occurs while retrieving the child.
     */
    public ConfigurationObject getChild(String name)
        throws ConfigurationAccessException;

    /**
     * Add a child. <p> If there is already a child with such name, the old one will be removed. </p>
     *
     * @return the old child with given name, or null if the name is new.
     * @param child the child to be added.
     * @throws IllegalArgumentException if child arg is null.
     * @throws InvalidConfigurationException if given child is not accepted by the specific implementation.
     * @throws ConfigurationAccessException if any error occurs while adding the child.
     */
    public ConfigurationObject addChild(ConfigurationObject child)
        throws InvalidConfigurationException, ConfigurationAccessException;

    /**
     * Remove a child with given name. <p> If no child with such name, return null. </p>
     *
     * @return the removed child, or null if there is no such name.
     * @param name the name of the child to be removed.
     * @throws IllegalArgumentException if name is null or empty.
     * @throws ConfigurationAccessException if any error occurs while adding the child.
     */
    public ConfigurationObject removeChild(String name)
        throws ConfigurationAccessException;

    /**
     * Clear all the children resides in the ConfigurationObject.
     *
     * @throws ConfigurationAccessException if any error occurs while clearing nested children.
     */
    public void clearChildren() throws ConfigurationAccessException;

    /**
     * Get all the children whose names are matched to the given pattern.
     *
     * @return the matched chilren
     * @param pattern a regular expression used to match children names.
     * @throws IllegalArgumentException if pattern is null, or an incorrect regex.
     * @throws ConfigurationAccessException if any error occurs while getting chilren.
     */
    public ConfigurationObject[] getChildren(String pattern)
        throws ConfigurationAccessException;

    /**
     * Get all the child names contained in it.
     *
     * @return all the chilren names.
     * @throws ConfigurationAccessException if any error occurs while getting names.
     */
    public String[] getAllChildrenNames() throws ConfigurationAccessException;

    /**
     * Get all the children contained in it.
     *
     * @return all the chilren.
     * @throws ConfigurationAccessException if any error occurs while getting children.
     */
    public ConfigurationObject[] getAllChildren()
        throws ConfigurationAccessException;

    /**
     * Get all descendants of this ConfigurationObject. (not including itself)
     *
     * @return an array of Descendants.
     * @throws ConfigurationAccessException if any error occurs while retrieving descendants.
     */
    public ConfigurationObject[] getAllDescendants()
        throws ConfigurationAccessException;

    /**
     * Get all descendants, whose name matched to given pattern, of this ConfigurationObject. (not including itself)
     *
     * @return an array of matched Descendants.
     * @param pattern a regex used to match descentants' names.
     * @throws IllegalArgumentException if given pattern is null, or an incorrect pattern.
     * @throws ConfigurationAccessException if any error occurs while retrieving descendants.
     */
    public ConfigurationObject[] getDescendants(String pattern)
        throws ConfigurationAccessException;

    /**
     * Get descendants specified by the given path.
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path the path used to retrieved descendants.
     * @throws IllegalArgumentException if the path is null.
     * @throws ConfigurationAccessException if any error occurs while finding descendants.
     */
    public ConfigurationObject[] findDescendants(String path)
        throws ConfigurationAccessException;

    /**
     * Delete descendants specified by the given path.
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path the path used to retrieved descendants.
     * @throws IllegalArgumentException if the path is null.
     * @throws ConfigurationAccessException if any error occurs while deleting
     */
    public ConfigurationObject[] deleteDescendants(String path)
        throws ConfigurationAccessException;

    /**
     * Set the property values in the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @param value the new value of the property.
     * @throws IllegalArgumentException if any path or key is null, or key is empty.
     * @throws InvalidConfigurationException if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void setPropertyValue(String path, String key, Object value)
        throws InvalidConfigurationException, ConfigurationAccessException;

    /**
     * Set the property values in the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @param values the new values of the property.
     * @throws IllegalArgumentException if path or key is null, or key is empty.
     * @throws InvalidConfigurationException if given key or value is not accepted by the specific implementation.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void setPropertyValues(String path, String key, Object[] values)
        throws InvalidConfigurationException, ConfigurationAccessException;

    /**
     * Remove the property with given key from the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @throws IllegalArgumentException if any arg is null, or key is empty.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void removeProperty(String path, String key)
        throws ConfigurationAccessException;

    /**
     * Clear the properties from all the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void clearProperties(String path)
        throws ConfigurationAccessException;

    /**
     * Add a child to the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @param child the child to be added.
     * @throws IllegalArgumentException if any arg is null
     * @throws InvalidConfigurationException if given child is not accepted by the specific implementation.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void addChild(String path, ConfigurationObject child)
        throws InvalidConfigurationException, ConfigurationAccessException;

    /**
     * Remove a child from the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @param name the name of child to be removed.
     * @throws IllegalArgumentException if any arg is null, or name is empty.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void removeChild(String path, String name)
        throws ConfigurationAccessException;

    /**
     * Clear all chilren from the descendants specified by the path.
     *
     * @param path the path specifying the descendants.
     * @throws IllegalArgumentException if any arg is null.
     * @throws ConfigurationAccessException if any other error occurs.
     */
    public void clearChildren(String path) throws ConfigurationAccessException;

    /**
     * Process descendants by the given processor.
     *
     * @param path path to find descendants
     * @param processor processor used to process all descendants
     * @throws IllegalArgumentException if any arg is null
     * @throws ConfigurationAccessException if any error occurs while acessing the configuration
     * @throws ProcessException propagated from processor
     */
    public void processDescendants(String path, Processor processor)
        throws ConfigurationAccessException, ProcessException;

    /**
     * <p>
     * Clone the current instance.
     * </p>
     * @return the cloned instance
     */
    public Object clone();
}
