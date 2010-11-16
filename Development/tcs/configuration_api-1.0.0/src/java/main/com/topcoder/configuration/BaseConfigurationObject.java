/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * <p>
 * An abstract adapter implementation of ConfigurationObject interface.
 * All the methods in this class always throw UnsupportedOperationException,
 * except the <code>clone</code> method.
 * This class exists as convenience for creating custom ConfigurationObject.
 * </p>
 *
 * <p>
 * Extend this class to create a custom ConfigurationObject and override
 * the only the methods which can be supported by certain configuration strategy.
 * </p>
 *
 * <p>
 * Thread safe: This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public abstract class BaseConfigurationObject implements ConfigurationObject {
    /**
     * Empty protected constructor.
     *
     */
    protected BaseConfigurationObject() {
    }

    /**<p>
     * Get the name of the ConfigurationObject, always throws UnsupportedOperationException.
     * </p>
     *
     * @return N/A
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public String getName() throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getName method is not supported.");
    }

    /**
     * <p>
     * Determine whether property with given key exists in the configuration object.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key the key of property
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public boolean containsProperty(String key)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The containsProperty method is not supported.");
    }

    /**<p>
     * Get the property value for the given key.
     * Note: if the key contains more than one values,
     * the value returned depends on specific implementation.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the value associated with the key, or null if there is no such key.
     * @param key the key of property.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public Object getPropertyValue(String key)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getPropertyValue method is not supported.");
    }

    /**
     * <p>
     * Get the number of values contained by the specific property.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     * @param key the key specifying the property
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public int getPropertyValuesCount(String key)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getPropertValuesCount method is not supported.");
    }

    /**
     * <p>
     * Get all the property values associated with given key.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array containing all the property values, or null if there is no such property key.
     * @param key the key used to specify property
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public Object[] getPropertyValues(String key)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getPropertyValues method is not supported.");
    }

    /**<p>
     * Set a key/value pair to the configuration object.
     * If the key already exists, old values will be overridden.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key the key of property
     * @param value the value of the property.
     * @throws UnsupportedOperationException Always thrown.
     * @throws InvalidConfigurationException Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public Object[] setPropertyValue(String key, Object value)
        throws InvalidConfigurationException, ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getPropertyValues method is not supported.");
    }

    /**
     * <p>
     * Set a key/values pair to the configuration object.
     * If the key already exists, old values will be overridden.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key the key of property
     * @param values the values to be set
     * @throws UnsupportedOperationException Always thrown.
     * @throws InvalidConfigurationException Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public Object[] setPropertyValues(String key, Object[] values)
        throws InvalidConfigurationException, ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The setPropertyValues method is not supported.");
    }

    /**
     * <p> Remove property with given key. If the key doens't exist, nothing happened.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the removed property value, or null if no such property.
     * @param key the key of the property to be removed
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public Object[] removeProperty(String key)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The removeProperty method is not supported.");
    }

    /**
     * <p>
     * Clear all the properties.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void clearProperties() throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The clearProperties method is not supported.");
    }

    /**
     * <p>
     * Get all the property keys matched the pattern.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array of mathed property keys.
     * @param pattern a regular expression used to match property keys.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public String[] getPropertyKeys(String pattern)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getPropertyKeys method is not supported.");
    }

    /**
     * <p>
     * Get all the property keys contained by the ConfigurationObject.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array of all property keys.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public String[] getAllPropertyKeys() throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getAllPropertyKeys method is not supported.");
    }

    /**
     * <p>
     * Determine whether it contains a child ConfigurationObject with given name.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name the name of the child
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public boolean containsChild(String name)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The containsChild method is not supported.");
    }

    /**
     * <p>
     * Get a child ConfigurationObject with given name.
     * Always throws UnsupportedOperationException
     * </p>
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name the name of the child
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject getChild(String name)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getChild method is not supported.");
    }

    /**
     * <p>
     * Add a child, if there is already a child with such name, the old one will be removed.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the old child with given name, or null if the name is new.
     * @param child the child to be added.
     * @throws UnsupportedOperationException Always thrown.
     * @throws InvalidConfigurationException Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject addChild(ConfigurationObject child)
        throws InvalidConfigurationException, ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The addChild method is not supported.");
    }

    /**
     * <p>
     * Remove a child with given name.
     * Always throws UnsupportedOperationException.
     * </p>
     * @return the removed child, or null if there is no such name.
     * @param name the name of the child to be removed.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject removeChild(String name)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The removeChild method is not supported.");
    }

    /**
     * <p>
     * Clear all the children resides in the ConfigurationObject.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void clearChildren() throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The clearChildren method is not supported.");
    }

    /**
     * <p>
     * Get all the children whose names are matched to the given pattern.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return the matched children
     * @param pattern a regular expression used to match children names.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getChildren(String pattern)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getChildren method is not supported.");
    }

    /**
     * <p>
     * Get all the child names contained in it.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the children names.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public String[] getAllChildrenNames() throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getAllChildrenNames method is not supported.");
    }

    /**
     * <p>
     * Get all the children contained in it.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the children.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getAllChildren()
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getAllChildren method is not supported.");
    }

    /**
     * <p>
     * Get all descendants of this ConfigurationObject, not including itself.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the Descendants of the current ConfigurationObject
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getAllDescendants()
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getAllDescendants method is not supported.");
    }

    /**
     * <p>
     * Get all descendants, whose name matched to given pattern,
     * of this ConfigurationObject, not including itself.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return an array of matched Descendants.
     * @param pattern a regex used to match descentants' names.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] getDescendants(String pattern)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The getDescendants method is not supported.");
    }

    /**
     * <p>
     * Get descendants specified by the given path.
     * The path may contain wildcards, like * and ?.
     * The path should be sepatated by slash or back slash characters.
     * So if the descendant name contains slashes, this method may return invalid results.
     * </p>
     *
     * <p>
     * Note:
     * <li>Heading or trailing slashes will be ignored.</li>
     * <li>Continuous slashes will be considered as one.</li>
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path the path used to retrieved descendants.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] findDescendants(String path)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The findDescendants method is not supported.");
    }

    /**
     * <p>
     * Delete descendants specified by the given path.
     * The path may contain wildcards, like * and ?.
     * The path should be sepatated by slash or back slash characters.
     * So if the descendant name contains slashes, this method may return invalid results.
     * </p>
     *
     * <p>
     * Note:
     * <li>Heading or trailing slashes will be ignored.</li>
     * <li>Continuous slashes will be considered as one.</li>
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path the path used to retrieved descendants.
     * @throws IllegalArgumentException if the path is null.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public ConfigurationObject[] deleteDescendants(String path)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The deleteDescendants method is not supported.");
    }

    /**
     * <p>
     * Set the property values in the descendants specified by the path.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @param value the new value of the property.
     * @throws UnsupportedOperationException Always thrown.
     * @throws InvalidConfigurationException Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void setPropertyValue(String path, String key, Object value)
        throws InvalidConfigurationException, ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The setPropertyValue method is not supported.");
    }

    /**
     * <p>
     * Set the property values in the descendants specified by the path,
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @param values the new values of the property.
     * @throws UnsupportedOperationException Always thrown.
     * @throws InvalidConfigurationException Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void setPropertyValues(String path, String key, Object[] values)
        throws InvalidConfigurationException, ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The setPropertyValues method is not supported.");
    }

    /**
     * <p>
     * Remove the property with given key from the descendants specified by the path,
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param key the key of the property
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void removeProperty(String path, String key)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The removeProperty method is not supported.");
    }

    /**
     * <p>
     * Clear the property with given key from the descendants specified by the path,
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void clearProperties(String path)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The clearProperties method is not supported.");
    }

    /**
     * <p>
     * Add a child to the descendants specified by the path,
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param child the child to be added.
     * @throws UnsupportedOperationException Always thrown.
     * @throws InvalidConfigurationException Never thrown, kept for sub-class to use it.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void addChild(String path, ConfigurationObject child)
        throws InvalidConfigurationException, ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The addChild method is not supported.");
    }

    /**
     * <p>
     * Remove a child from the descendants specified by the path,
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @param name the name of child to be removed.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void removeChild(String path, String name)
        throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The removeChild method is not supported.");
    }

    /**
     * <p>
     * Clear all children from the descendants specified by the path.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path the path specifying the descendants.
     * @throws UnsupportedOperationException Always thrown.
     * @throws ConfigurationAccessException Never thrown, kept for sub-class to use it.
     */
    public void clearChildren(String path) throws ConfigurationAccessException {
        throw new UnsupportedOperationException(
            "The clearChildren method is not supported.");
    }

    /**
     * <p>
     * Process descendants by the given processor.
     * Always throws UnsupportedOperationException.
     * </p>
     *
     * @param path path to find descendants
     * @param processor processor used to process all descendants
     * @throws IllegalArgumentException if any arg is null
     * @throws ConfigurationAccessException if any error occurs while accessing the configuration
     * @throws ProcessException propagated from processor
     * @throws UnsupportedOperationException always throw
     */
    public void processDescendants(String path, Processor processor)
        throws ConfigurationAccessException, ProcessException {
        throw new UnsupportedOperationException(
            "The processDescendants method is not supported.");
    }

    /**
     * <p>
     * Clone the current instance.
     * </p>
     *
     * @return the cloned instance
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            //never occur
            return null;
        }
    }
}
