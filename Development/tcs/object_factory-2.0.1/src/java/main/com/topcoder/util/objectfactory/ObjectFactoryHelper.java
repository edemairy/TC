/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Arrays;


/**
 * Helper class for the whole component.
 *
 * @author mgmg
 * @version 2.0
 */
public final class ObjectFactoryHelper {
    /**
     * Private empty constructor.
     */
    private ObjectFactoryHelper() {
    }

    /**
     * Check if the object is null.
     *
     * @param obj the object to check.
     * @param name the object name
     *
     * @throws IllegalArgumentException if the object is null.
     */
    public static void checkObjectNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException("the object " + name + " should not be null.");
        }
    }

    /**
     * Check if the string is empty.
     *
     * @param str the string to check.
     * @param name the parameter name.
     *
     * @throws IllegalArgumentException if the object is empty.
     */
    static void checkStringNotEmpty(String str, String name) {
        if ((str != null) && (str.trim().length() == 0)) {
            throw new IllegalArgumentException("the string " + name + " should not be empty.");
        }
    }

    /**
     * Check if the string is null or empty.
     *
     * @param str the string to check.
     * @param name the parameter name.
     *
     * @throws IllegalArgumentException if the string is null or empty.
     */
    public static void checkStringNotNullOrEmpty(String str, String name) {
        checkObjectNotNull(str, name);
        checkStringNotEmpty(str, name);
    }

    /**
     * Check if the class name is valid.
     *
     * @param jar the jar file of the class.
     * @param className the class name to check.
     *
     * @throws IllegalArgumentException if the class name is not valid.
     */
    static void checkClassValid(String jar, String className) {
        try {
            // if jar is null, use the default ClassLoader. Otherwise use the specified jar file.
            if (jar == null) {
                Class.forName(className);
            } else {
                new URLClassLoader(new URL[] {new URL(jar)}).loadClass(className);
            }
        } catch (SecurityException e) {
            throw new IllegalArgumentException("the class type " + className + " is invalid.");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("the class type " + className + " is invalid.");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("the class type " + className + " is invalid.");
        }
    }

    /**
     * Get the dimensions of an array.
     *
     * @param param the array to parse.
     *
     * @return the int array indicates the dimensions.
     *
     * @throws IllegalArgumentException if the array is not valid.
     */
    static int[] getDimensionArrayFromArray(Object param) {
        Class type = param.getClass().getComponentType();

        // if the componentType is null, it means param is not an array.
        if (type == null) {
            return null;
        } else {
            // this is an array.
            Object[] array = (Object[]) param;
            int[] subDims = null;

            // iterate through the whole array.
            for (int i = 0; i < array.length; i++) {
                // for each item, recursively get the dimensions of it.
                // null will be returned if the item is not an array.
                int[] tmpDims = getDimensionArrayFromArray(array[i]);

                if (tmpDims != null) {
                    if (subDims != null) {
                        // if the dimensions of the items are not equals, throw exception.
                        if (!Arrays.equals(subDims, tmpDims)) {
                            throw new IllegalArgumentException("The array is not valid.");
                        }
                    } else {
                        // set the dimensions of the component of current array.
                        subDims = tmpDims;
                    }
                }
            }

            if (subDims != null) {
                // if the dimension of passed-in array is greater than 1.
                int[] dims = new int[subDims.length + 1];
                dims[0] = array.length;

                // build an array indicates the dimensions and then returned.
                for (int i = 0; i < subDims.length; i++) {
                    dims[i + 1] = subDims[i];
                }

                return dims;
            } else {
                // if the dimension of passed-in array is 1.
                return new int[] {array.length};
            }
        }
    }

    /**
     * Check if the array is with the expected dimension.
     *
     * @param param the array object.
     * @param dim the dimension.
     *
     * @throws IllegalArgumentException if the array is not with the expected dimension.
     */
    static void checkArrayDimension(Object[] param, int dim) {
        int[] dims = getDimensionArrayFromArray(param);

        if (dims.length != dim) {
            throw new IllegalArgumentException("dimension should be the same as the array.");
        }
    }

    /**
     * Check if the type is one of the simple types.
     *
     * @param type the type to check.
     *
     * @return if the type is one of the simple type.
     */
    public static boolean checkSimpleType(String type) {
        return type.equals(ObjectSpecification.FLOAT) || type.equals(ObjectSpecification.INT)
            || type.equals(ObjectSpecification.DOUBLE) || type.equals(ObjectSpecification.CHAR)
            || type.equals(ObjectSpecification.BYTE) || type.equals(ObjectSpecification.LONG)
            || type.equals(ObjectSpecification.SHORT) || type.equals(ObjectSpecification.BOOLEAN)
            || type.equals(ObjectSpecification.STRING) || type.equals(ObjectSpecification.STRING_FULL);
    }

    /**
     * Check if the type is a string type.
     *
     * @param type the type to check.
     *
     * @return true if the type is a string.
     */
    public static boolean checkStringSimpleType(String type) {
        return type.equals(ObjectSpecification.STRING) || type.equals(ObjectSpecification.STRING_FULL);
    }

    /**
     * Check if the array contains null.
     *
     * @param objs the array to check.
     *
     * @return true if the array contains null, false otherwise.
     */
    static boolean checkNullInArray(Object[] objs) {
        // iterate through the whole array.
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] == null) {
                return true;
            }
        }

        return false;
    }
}
