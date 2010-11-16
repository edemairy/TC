/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.defaults;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.Helper;
import com.topcoder.configuration.InvalidConfigurationException;
import com.topcoder.configuration.TemplateConfigurationObject;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * <p>
 * It is the main class of this component, also the default implemetation of ConfigurationObject.
 * It extends from TemplateConfigurationObject to utilize the implemented methods in it.
 * </p>
 *
 *
 * <p>
 * This class uses a Map in memory to hold properties.
 * The key of Map is a String representing the property key.
 * The value of Map is a List instance containing all the property vlaues(null is allowed).
 * </p>
 *
 * <p> And also a Map in memory is used to hold child ConfigurationObjects.
 * The key of this Map is a String representing the name of child object.
 * And the value is the child instance. The relationship graph of this implementation should always be a DAG.
 * </p>
 *
 * <p>
 * Besides ConfigurationObject interface, this class also implements Serializable and Clonable interface.
 * To support Serializable interface, it just ensures that all the coming properties values and child objests
 * are instances of Serializable. To support Clonable interace, because we want to keep DAG structure,
 * a clone overload with a cache parameter is provided. In this case,
 * all the children should be instance of DefaultConfigurationObject.
 * </p>
 *
 * <p>Thread safe: This class is mutable and not thread safe.</p>
 *
 * @author maone, TCSDEVELOPER
 * @version 1.0
 */
public class DefaultConfigurationObject extends TemplateConfigurationObject
    implements Cloneable, Serializable {
    /**
     * The regex pattern used to split a String by slashes.
     */
    private static final String SPLIT_SLASH_PATTERN = "[\\\\/]";
    /**
     * The name of the configurationObject.
     *
     * <p>
     * It is intialized in the constructor, and never changed later.
     * It can be got from the getter.
     * It shouldn't be null or empty.
     * </p>
     *
     */
    private final String name;

    /**
     * Represents all the properites of this configuration object.
     * <p>
     * The key of map is a String representing the property key.
     * The value of map is a List representsing the property values.
     * A single property can contains zero or more values.
     * The elements in the list should be null or Serailizable instances.
     * Because null element is allowed, so used List should allow null value, like ArrayList.
     * </p>
     *
     * <p>
     * The key cann't be null or empty.
     * The value of map cann't be null. But the element in the value can be null.
     * </p>
     *
     * <p>
     * The map itself is intialized in the constructor or changed when do clone,
     * actually it should be final, but since the clone need to be deep copy so just remove
     * the final for the clone, it can not be changed in any other way.
     * And the content can be updated by many methods.
     * </p>
     *
     */
    private Map properties;

    /**
     * Represents all the properites of this configuration object.
     * <p>
     * The key of map is a String representing the child name, which cann't be null or empty.
     * The value of map is a ConfigurationObject instance, which cann't be null.
     * </p>
     *
     * <p>
     * The map itself is intialized in the constructor or changed when do clone,
     * actually it should be final, but since the clone need to be deep copy so just remove
     * the final for the clone, it can not be changed in any other way.
     * And the content can be updated by many methods.
     * </p>
     *
     */
    private Map children;

    /**
     * Constructor with the name of the DefaultConfigurationObject.
     *
     * @param name the name of this ConfigurationObject.
     * @throws IllegalArgumentException if name is null or empty.
     */
    public DefaultConfigurationObject(String name) {
        Helper.checkStringNullOrEmpty(name, "name");
        this.name = name;
        this.properties = new HashMap();
        this.children = new HashMap();
    }

    /**
     * Get the name of the ConfigurationObject.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    /**
     * <p>
     * Set a key/value pair to the configuration object.
     * If the key already exists, old values will be overriden.
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key the key of property
     * @param value the value of the property.
     * @throws IllegalArgumentException if key is null or empty
     * @throws InvalidConfigurationException if given value is not null and not a Serializable instance.
     */
    public Object[] setPropertyValue(String key, Object value)
        throws InvalidConfigurationException  {
        return setPropertyValues(key, new Object[]{value});
    }

    /**
     * Set a key/values pair to the configuration object.
     * <p>
     * If the key already exists, old values will be overriden.
     * </p>
     *
     * @return the old values of the property, or null if the key is new.
     * @param key the key of property
     * @param values the array of values should be set
     * @throws IllegalArgumentException if key is null or empty
     * @throws InvalidConfigurationException if given values contains element
     * which is not null and not a Serailizable instance.
     */
    public Object[] setPropertyValues(String key, Object[] values)
        throws InvalidConfigurationException {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                checkNullOrSerializable(values[i], "values[" + i + "]");
            }
        }
        //get the old values which should be returned
        Object[] result = getPropertyValues(key);

        //override the old value with the new one, if the values is null, just using empty
        List list = values == null ? new ArrayList() : Arrays.asList(values);
        properties.put(key, list);

        return result;
    }
    /**
     * Determine whether property with given key exists in the configuration object.
     *
     * @return true if the specified property is contained, false otherwise.
     * @param key the key of property
     * @throws IllegalArgumentException if key is null or empty
     */
    public boolean containsProperty(String key) {
        Helper.checkStringNullOrEmpty(key, "key");

        return properties.containsKey(key);
    }

    /**
     * Get the property value for the given key.
     * <p>
     * Note: if the key contains more than one values,
     * the value returned if the first value.
     * </p>
     *
     * @return the first value associated with the key, or null if there is no such key.
     * @param key the key of property.
     * @throws IllegalArgumentException if key is null or empty
     */
    public Object getPropertyValue(String key) {
        Helper.checkStringNullOrEmpty(key, "key");

        List values = (List) properties.get(key);

        //if not property value exist, return null instead
        if ((values == null) || (values.size() == 0)) {
            return null;
        }

        return values.get(0);
    }

    /**
     * Get the number of values contained by the specific property.
     *
     * @return the number of values contained in the property, or -1 if there is no such property.
     * @param key the key specifying the property
     * @throws IllegalArgumentException if key is null or empty
     */
    public int getPropertyValuesCount(String key) {
        Helper.checkStringNullOrEmpty(key, "key");

        List values = (List) properties.get(key);

        return (values == null) ? (-1) : values.size();
    }

    /**
     * Get all the property values associated with given key.
     *
     * @return an array containing all the property values, or null if there is no such property key.
     * @param key the key used to specify property
     * @throws IllegalArgumentException if key is null or empty
     */
    public Object[] getPropertyValues(String key) {
        Helper.checkStringNullOrEmpty(key, "key");

        List values = (List) properties.get(key);

        if (values == null) {
            return null;
        } else {
            return values.toArray(new Object[values.size()]);
        }
    }

    /**
     * Remove property with given key. <p> If the key doens't exist, nothing happened. </p>
     *
     * @return the removed property value, or null if no such property.
     * @param key the key of the property to be removed
     * @throws IllegalArgumentException if key is null or empty.
     */
    public Object[] removeProperty(String key) {
        //get the old values which should be returned
        Object[] result = getPropertyValues(key);
        properties.remove(key);

        return result;
    }

    /**
     * Clear all the properties.
     *
     */
    public void clearProperties() {
        properties.clear();
    }

    /**
     * Get all the property keys matched the pattern.
     *
     * @return an array of matched property keys.
     * @param pattern a regular expression used to match property keys.
     * @throws IllegalArgumentException if pattern is null or an invalid regex.
     */
    public String[] getPropertyKeys(String pattern) {
        Helper.checkNull(pattern, "pattern");

        List list = new ArrayList();

        //since we should use the pattern several times, precompile it is a efficient way
        Pattern p = Pattern.compile(pattern);

        //using the pattern to check each key
        for (Iterator it = properties.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();

            if (p.matcher(key).matches()) {
                list.add(key);
            }
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * Get all the property keys contained by the ConfigurationObject.
     *
     * @return an array of all property keys.
     */
    public String[] getAllPropertyKeys() {
        return (String[]) properties.keySet().toArray(new String[properties.size()]);
    }

    /**
     * <p>
     * Add a child, If there is already a child with such name, the old one will be removed.
     * </p>
     *
     * @return the old child with given name, or null if the name is new.
     * @param child the child to be added.
     * @throws IllegalArgumentException if child arg is null.
     * @throws InvalidConfigurationException if cyclic relationship would occur after adding the child,
     * or if the child is not an instance of DefaultConfigurationObject.
     */
    public ConfigurationObject addChild(ConfigurationObject child)
        throws InvalidConfigurationException  {
        Helper.checkNull(child, "child");

        if (child == this) {
            throw new InvalidConfigurationException(
                "The child to be added can not be the same as itself.");
        }

        //the child added should be a DefaultConfigurationObject instance
        if (!(child instanceof DefaultConfigurationObject)) {
            throw new InvalidConfigurationException(
                "The child to be added should be a DefaultConfigurationObject.");
        }

        //cast to DefaultConfigurationObject, since DefaultConfigurationObject.getAllDescendants not throw
        //the ConfigurationAccessException
        ConfigurationObject[] descendants = ((DefaultConfigurationObject) child).getAllDescendants();

        //this can not be Descendant if its child
        for (int i = 0; i < descendants.length; i++) {
            if (descendants[i] == this) {
                throw new InvalidConfigurationException(
                    "cyclic relationship occurs when add the child.");
            }
        }

        try {
            ConfigurationObject oldChild = (ConfigurationObject) children.get(child.getName());
            children.put(child.getName(), child);
            return oldChild;
        } catch (ConfigurationAccessException e) {
            //the ConfigurationAccessException if not thrown by DefaultConfigurationObject
            return null;
        }
    }
    /**
     * Determine whether it contains a child ConfigurationObject with given name.
     *
     * @return a boolean value indicating whether the specified child is contained.
     * @param name the name of the child
     * @throws IllegalArgumentException if name is null or empty
     */
    public boolean containsChild(String name) {
        Helper.checkStringNullOrEmpty(name, "name");

        return children.containsKey(name);
    }

    /**
     * Get a child ConfigurationObject with given name.
     *
     * @return the child ConfigurationObject, or null if there no such child name.
     * @param name the name of the child
     * @throws IllegalArgumentException if name is null or empty
     */
    public ConfigurationObject getChild(String name) {
        Helper.checkStringNullOrEmpty(name, "name");

        return (ConfigurationObject) children.get(name);
    }

    /**
     * Remove a child with given name.
     *
     * @return the removed child, or null if there is no such name.
     * @param name the name of the child to be removed.
     * @throws IllegalArgumentException if name is null or empty.
     */
    public ConfigurationObject removeChild(String name) {
        Helper.checkStringNullOrEmpty(name, "name");

        return (ConfigurationObject) children.remove(name);
    }

    /**
     * Clear all the children resides in the ConfigurationObject.
     *
     */
    public void clearChildren() {
        children.clear();
    }

    /**
     * Get all the children whose names are matched to the given pattern.
     *
     * @return the matched chilren
     * @param pattern a regular expression used to match children names.
     * @throws IllegalArgumentException if pattern is null, or an incorrect regex.
     */
    public ConfigurationObject[] getChildren(String pattern) {
        Helper.checkNull(pattern, "pattern");

        List list = new ArrayList();

        //since we should use the pattern several times, precompile it is a efficient way
        Pattern p = Pattern.compile(pattern);

        //using the pattern to check each key
        for (Iterator it = children.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();

            if (p.matcher((String) entry.getKey()).matches()) {
                list.add(entry.getValue());
            }
        }

        return (ConfigurationObject[]) list.toArray(new ConfigurationObject[list.size()]);
    }

    /**
     * Get all the child names contained in it.
     *
     * @return all the chilren names.
     */
    public String[] getAllChildrenNames() {
        return (String[]) children.keySet().toArray(new String[children.size()]);
    }

    /**
     * Get all the children contained in it.
     *
     * @return all the chilren.
     */
    public ConfigurationObject[] getAllChildren() {
        return (ConfigurationObject[]) children.values().toArray(new ConfigurationObject[children.size()]);
    }

    /**
     * Get all descendants of this ConfigurationObject.
     *
     * @return an array of Descendants.
     */
    public ConfigurationObject[] getAllDescendants() {
        return getTheDescendants(null);
    }

    /**
     * Get all descendants, whose name matched to given pattern, of this ConfigurationObject.
     *
     * @return an array of matched Descendants.
     * @param pattern a regex used to match descentants' names.
     * @throws IllegalArgumentException if given pattern is null, or an incorrect pattern.
     */
    public ConfigurationObject[] getDescendants(String pattern) {
        Helper.checkNull(pattern, "pattern");
        return getTheDescendants(pattern);
    }

    /**
     * Get the Descendants of the current DefaultConfigurationObject,
     * the pattern may be used if it is not null.
     *
     * @param pattern a regex used to match descentants' names, but also may be null, which means not use pattern.
     * @return an array of matched Descendants.
     */
    private ConfigurationObject[] getTheDescendants(String pattern) {
        Pattern p = null;
        if (pattern != null) {
            p = Pattern.compile(pattern);
        }
        Set visited = new HashSet();
        Set result = new HashSet();

        //using linked list is more efficient, since it is implemented like a queue
        LinkedList queue = new LinkedList();
        visited.add(this);
        queue.addLast(this);

        while (queue.size() > 0) {
            //get the first head item of the queue to broadcast
            ConfigurationObject nextObject = (ConfigurationObject) queue.removeFirst();
            ConfigurationObject[] currentChildren = null;

            try {
                currentChildren = nextObject.getAllChildren();
            } catch (ConfigurationAccessException e) {
                //ConfigurationAccessException will never occur in DefaultConfigurationObject
                //since it is do the operation in memory
            }

            for (int i = 0; i < currentChildren.length; i++) {
                //if already visited, then just ignore it, only treat those not contains
                if (!visited.contains(currentChildren[i])) {
                    visited.add(currentChildren[i]);
                    queue.addLast(currentChildren[i]);
                    if (p == null || p.matcher(((DefaultConfigurationObject)currentChildren[i]).getName()).matches()) {
                        result.add(currentChildren[i]);
                    }
                }
            }
        }

        return (ConfigurationObject[]) result.toArray(new ConfigurationObject[result.size()]);
    }
    /**
     * <p>
     * Slash trimmed the String, not only the white spaces are ignore at the both end,
     * also ignore the slashes at the both end.
     * </p>
     *
     * @param str the String to be slash trimmed
     * @return the String after slash trimmed.
     */
    private String trimSlashes(String str) {
        int len = str.length();
        int st = 0;

        while ((st < len) && ((str.charAt(st) <= ' ') || (str.charAt(st) == '\\')
                        || (str.charAt(st) == '/'))) {
            st++;
        }

        while ((st < len) && ((str.charAt(len - 1) <= ' ') || (str.charAt(len - 1) == '\\')
                        || (str.charAt(len - 1) == '/'))) {
            len--;
        }

        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
    }

    /**
     * <p>
     * Remove the Continuous slashes in the given String,
     * Continuous slashes will be considered as one.
     * </p>
     *
     * @param str the given String
     * @return the String after remove the Continuous slashes
     */
    private String removeContinuousSlashes(String str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (i > 0 && (str.charAt(i - 1) == '/' || str.charAt(i - 1) == '\\')
                    && (str.charAt(i) == '/' || str.charAt(i) == '\\')) {
                continue;
            }
            buffer.append(str.charAt(i));
        }
        return buffer.toString();
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
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path the path used to retrieved descendants.
     * @throws IllegalArgumentException if the path is null.
     */
    public ConfigurationObject[] findDescendants(String path) {
        return findOrDeleteDescendants(path, false);
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
     * </p>
     *
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     * @param path the path used to retrieved descendants.
     * @throws IllegalArgumentException if the path is null.
     */
    public ConfigurationObject[] deleteDescendants(String path) {
        return findOrDeleteDescendants(path, true);
    }

    /**
     * <p>
     * Find the Descendants of this ConfigurationObject with the path,
     * at the same time delete them if they are required to be deleted.
     * </p>
     *
     * @param path the path used to retrieved descendants.
     * @param shouldDelete whether the descendants with the path should be deleted
     * @return all the descendants specified by the path, or an empty array if nothing is matched.
     */
    private ConfigurationObject[] findOrDeleteDescendants(String path,
        boolean shouldDelete) {
        Helper.checkNull(path, "path");
        //trim the path by remove the whitespace, slashes, and
        //remove the Continuous slashes, Continuous slashes will be considered as one
        path = removeContinuousSlashes(trimSlashes(path));

        String[] paths = path.split(SPLIT_SLASH_PATTERN);

        //here should use set, since the graph it not a tree,
        //so the same node may be reached twice at one layer
        Set current = new HashSet();

        //hode the node to process for the next path
        Set next = new HashSet();
        current.add(this);

        for (int i = 0; i < paths.length; i++) {
            for (Iterator currentOnes = current.iterator();
                    currentOnes.hasNext();) {
                ConfigurationObject currentChild = (ConfigurationObject) currentOnes.next();

                try {
                    ConfigurationObject[] currentChildren = currentChild.getAllChildren();

                    for (int j = 0; j < currentChildren.length; j++) {
                        //search the node alone the path
                        if (wildMatch(paths[i], currentChildren[j].getName())) {
                            next.add(currentChildren[j]);

                            //if this node is the target one and should be deleted, just delete it
                            if ((i == (paths.length - 1)) && shouldDelete) {
                                currentChild.removeChild(currentChildren[j].getName());
                            }
                        }
                    }
                } catch (ConfigurationAccessException e) {
                    //the ConfigurationAccessException will never occur in DefaultConfigurationObject
                }
            }

            //avoid to create many sets
            current.clear();
            current.addAll(next);
            next.clear();
        }

        return (ConfigurationObject[]) current.toArray(new ConfigurationObject[current.size()]);
    }

    /**
     * <p>
     * Wild regular expression match with the pattern and target String,
     * it is most like the <code>XPath</code> rule:
     * <li>that * can represent any segment of letter</li>
     * <li>? can represent can single letter</li>.
     *
     * Note: the pattern and String are ensured to be not null, and the String is not empty.
     * since the name of the child can not be empty.
     * </p>
     *
     * <p>
     * Here we use dynamic programming strategy, which will reduce the complication of the
     * match algorithm, the worst case length(pattern) * length(str).
     * </p>
     *
     * @param pattern the pattern used to comparison
     * @param str the String that to be matched with the given pattern
     * @return true if the String is wild matched with the pattern, otherwise false
     */
    private boolean wildMatch(String pattern, String str) {
        boolean[][] status = new boolean[pattern.length() + 1][str.length() + 1];

        for (int i = 0; i <= pattern.length(); i++) {
            for (int j = 0; j <= str.length(); j++) {
                status[i][j] = false;
            }
        }

        status[pattern.length()][str.length()] = true;

        //the follow steps are using dynamic programming,
        //status[i][j] == true means pattern.subString(i) matches str.subString(j)
        for (int i = pattern.length() - 1; i >= 0; i--) {
            for (int j = str.length() - 1; j >= 0; j--) {
                char p = pattern.charAt(i);
                char s = str.charAt(j);

                //'*' can be used to match any letters
                if (p == '*') {
                    status[i][j] |= status[i + 1][j + 1];
                    status[i][j] |= status[i + 1][j];
                    status[i][j] |= status[i][j + 1];
                } else if ((p == '?') || (p == s)) {
                    status[i][j] |= status[i + 1][j + 1];
                }
            }
        }

        //return whether the whole String can match
        return status[0][0];
    }

    /**
     * Implementation of clone methods, all the children are deep copied and the properties
     * are only shadow copied.
     *
     * @return the clone of the DefaultConfigurationObject
     */
    public Object clone() {
        return clone(new HashMap());
    }

    /**
     * A Helper method that used to deep copy the whole DAG of the DefaultConfigurationObject.
     *
     *
     * @return the clone
     * @param cache the cache contained all the cloned objects.
     */
    private DefaultConfigurationObject clone(Map cache) {
        DefaultConfigurationObject clone = null;

        clone = (DefaultConfigurationObject) super.clone();

        // clone the properties
        clone.properties = new HashMap(this.properties);

        clone.children = new HashMap();

        // iterate every child to clone it.
        for (Iterator itr = children.values().iterator(); itr.hasNext();) {
            DefaultConfigurationObject child = (DefaultConfigurationObject) itr.next();

            // if the child is not cloned yet, clone it, and put it into the cache.
            if (!cache.containsKey(child)) {
                cache.put(child, child.clone(cache));
            }

            // retrieve the child's clone from cache
            clone.children.put(child.getName(), cache.get(child));
        }

        return clone;
    }
    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg the argument to check
     * @param name the name of the argument to check
     * @return the original object to check
     *
     * @throws InvalidConfigurationException if the given Object is null
     */
    private void checkNullOrSerializable(Object arg, String name) throws InvalidConfigurationException {
        if (arg != null && !(arg instanceof Serializable)) {
            throw new InvalidConfigurationException(name + "should be null or a Serializable instance.");
        }
    }
}
