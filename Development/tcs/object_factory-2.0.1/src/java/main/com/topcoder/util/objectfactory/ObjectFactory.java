/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;


/**
 * <p>The main class in this component. It is backed by a SpecificationFactory, which it queries for specifications
 * for objects. Gives four main methods to create object where the user can specify two keys, the JAR file or
 * ClassLoader, the params to use in the construction, and the initialization strategy. It also has four convenience
 * methods that let the user only pass the minimum amount of parameters. These can be useful when the construction
 * is to be done off the specification factory.</p>
 *
 * <p>The main four methods allow the user to fully specify what object is to be created. As such, only the key is
 * required. All other parameters can be null, and the factory would use defaults. The purpose of the paramTypes
 * parameter is to let the user pass null parameters to a constructor while letting the factory know the type of
 *  this parameter, as to avoid potential ambiguities in which constructor is to be used. In fact, the paramTypes
 *  is not required or needed unless a null needs to be passed as a parameter. In this case, paramTypes is required
 * and its length must match the params.</p>
 *
 * <p>This class is mutable and is not thread-safe.</p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public class ObjectFactory {
    /**
     * <p>Represents an initialization strategy where there is a check made if the passed key (and identifier,
     * if given) has an entry in the specification factory. If so, then that specification is used in the
     * construction of the object. If not, it will try to use reflection with the key treated as a type.</p>
     */
    public static final String BOTH = "both";

    /**
     * <p>Represents a specification-only initialization strategy. The factory only instantiate the
     * object only if the passed key (and identifier, if given) has an entry in the specification factory.</p>
     */
    public static final String REFLECTION_ONLY = "reflection";

    /**
     * <p>Represents a reflection-only initialization strategy. The factory only instantiate the object
     * only using java reflection api, and only if the passed key is a recognizable class type.</p>
     */
    public static final String SPECIFICATION_ONLY = "specification";

    /**
     * <p>Represents the specification factory instance to be used to obtain object specifications.
     * Set in the constructor and  will never change and will never be null.</p>
     */
    private final SpecificationFactory specificationFactory;

    /**
     * <p>Represents the initialization strategy that this factory will use. The user has three choices: The
     * object can be created from the specification only, it can be created using reflection only, or it can
     * try to use both, first checking if there is a specification for the give key (and identifier, if given),
     * and if not, trying to use reflection with the key treated as a type. These are denoted by
     * SPECIFICATION_ONLY, REFLECTION_ONLY, and BOTH, respectively. BOTH is the default.
     * This value can be set by the two-parameter constructor, or the setter for this member.</p>
     */
    private String initStrategy;

    /**
     * <p>Simply Instantiates the factory with the passed specification factory.</p>
     *
     * @param specificationFactory An instance of SpecificationFactory
     *
     * @throws IllegalArgumentException If param is null
     */
    public ObjectFactory(SpecificationFactory specificationFactory) {
        this(specificationFactory, BOTH);
    }

    /**
     * <p>Instantiates the factory with the passed specification factory and initialization strategy.</p>
     *
     * @param specificationFactory An instance of SpecificationFactory
     * @param initStrategy the initialization strategy
     *
     * @throws IllegalArgumentException If specificationFactory is null,
     *                                   or if the initStategy is not one of the three recognized ones
     */
    public ObjectFactory(SpecificationFactory specificationFactory, String initStrategy) {
        // Check the validation of the parameters.
        ObjectFactoryHelper.checkObjectNotNull(specificationFactory, "specificationFactory");
        checkStrategyString(initStrategy);

        this.specificationFactory = specificationFactory;
        this.initStrategy = initStrategy;
    }

    /**
     * <p>Convenience overload method. Creates an object using this key with the configured initialization strategy.
     * If the key is not recognized, or simply cannot be used to instantiate the desired object, an
     * InvalidClassSpecificationException will be thrown.</p>
     *
     * @return an Object instance
     *
     * @param key key to an object specification or the class type to instantiate
     *
     * @throws IllegalArgumentException If key is null or empty
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(String key) throws InvalidClassSpecificationException {
        return createObject(key, null, (ClassLoader) null, null, null, initStrategy);
    }

    /**
     * <p>Convenience overload method. Creates an object using this key with the configured initialization strategy.
     * If the key is not recognized, or simply cannot be used to instantiate the desired object, an
     * InvalidClassSpecificationException will be thrown. The returned object will be of the passed type.</p>
     *
     * @return an Object instance that is an instance of type
     *
     * @param type key to an object specification or the class type to instantiate
     *
     * @throws IllegalArgumentException If type is null
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(Class type) throws InvalidClassSpecificationException {
        ObjectFactoryHelper.checkObjectNotNull(type, "type");

        return createObject(type, null, (ClassLoader) null, null, null, initStrategy);
    }

    /**
     * <p>Convenience overload method. Creates an object using this key and identifier with the configured
     * initialization strategy. If the key and identifier are not recognized, or simply cannot be used to
     * instantiate the desired object, an InvalidClassSpecificationException will be thrown.</p>
     *
     * @return an Object instance
     *
     * @param key key to an object specification or the class type to instantiate
     * @param identifier additional identifier to an object specification
     *
     * @throws IllegalArgumentException If key or identifier is null or empty
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(String key, String identifier) throws InvalidClassSpecificationException {
        ObjectFactoryHelper.checkStringNotNullOrEmpty(identifier, "identifier");

        return createObject(key, identifier, (ClassLoader) null, null, null, initStrategy);
    }

    /**
     * <p>Convenience overload method. Creates an object using this key and identifier with the configured
     * initialization strategy. If the key and identifier are not recognized, or simply cannot be used to
     * instantiate the desired object, an InvalidClassSpecificationException will be thrown. The returned
     * object will be of the passed type.</p>
     *
     * @return an Object instance that is an instance of type
     *
     * @param type key to an object specification or the class type to instantiate
     * @param identifier additional identifier to an object specification
     *
     * @throws IllegalArgumentException If type is null, or identifier is null or empty
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(Class type, String identifier) throws InvalidClassSpecificationException {
        ObjectFactoryHelper.checkObjectNotNull(type, "type");
        ObjectFactoryHelper.checkStringNotNullOrEmpty(identifier, "identifier");

        return createObject(type, identifier, (ClassLoader) null, null, null, initStrategy);
    }

    /**
     * <p>Creates an object using the passed specifications with the configured initialization strategy.
     * The key needs to be supplied, but the extra indentifier is optional. If a specific jar is required,
     * then a URL to it can be given, otherwise, the default class loader will be used. Parameters to the
     * constructor can be specified, and if some are null, then their type can be specified using the parallel
     * paramTypes parameter. The type and order of the parameters will determine exactly which constructor
     * is used. Finally, the initialization strategy to be used in this object construction call can be also
     * specified, otherwise the default setting will be used. If these pecifications are not recognized,
     * or simply cannot be used to instantiate the desired object, an InvalidClassSpecificationException
     * will be thrown.</p>
     *
     * @return an Object instance
     *
     * @param key key to an object specification or the class type to instantiate
     * @param identifier additional identifier to an object specification
     * @param jar the location of the jar
     * @param params params to pass to the constructor
     * @param paramTypes the Class array specifying the type of Objects in the 'params' parameter
     * @param initStrategy the initialization strategy
     *
     * @throws IllegalArgumentException If key is null or empty, or if length of params and paramTypes does
     *                                   not match up. or if initStrategy is not one of the recognized types.
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(String key, String identifier, URL jar, Object[] params, Class[] paramTypes,
        String initStrategy) throws InvalidClassSpecificationException {
        return createObject(key, identifier, (jar == null) ? null : new URLClassLoader(new URL[] {jar}),
            params, paramTypes, initStrategy);
    }

    /**
     * <p>Creates an object using the passed specifications with the configured initialization strategy.
     * The key needs to be supplied, but the extra indentifier is optional. If a specific jar is required,
     * then a URL to it can be given, otherwise, the default class loader will be used. Parameters to the
     * constructor can be specified, and if some are null, then their type can be specified using the parallel
     * paramTypes parameter. The type and order of the parameters will determine exactly which constructor
     * is used. Finally, the initialization strategy to be used in this object construction call can be also
     * specified, otherwise the default setting will be used. If these specifications are not recognized,
     * or simply cannot be used to instantiate the desired object, an InvalidClassSpecificationException
     * will be thrown. The returned object will be of the passed type.</p>
     *
     * @return an Object instance that is an instance of type
     *
     * @param type key to an object specification or the class type to instantiate
     * @param identifier additional identifier to an object specification
     * @param jar the location of the jar
     * @param params params to pass to the constructor
     * @param paramTypes the Class array specifying the type of Objects in the 'params' parameter
     * @param initStrategy the initialization strategy
     *
     * @throws IllegalArgumentException If type is null, or if length of params and paramTypes does not match up.
     *                                   or if initStrategy is not one of the recognized types.
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(Class type, String identifier, URL jar, Object[] params, Class[] paramTypes,
        String initStrategy) throws InvalidClassSpecificationException {
        ObjectFactoryHelper.checkObjectNotNull(type, "type");

        return createObject(type, identifier, (jar == null) ? null : new URLClassLoader(new URL[] {jar}),
            params, paramTypes, initStrategy);
    }

    /**
     * <p>Creates an object using the passed specifications with the configured initialization strategy.
     * The key needs to be supplied, but the extra indentifier is optional. A specific class loader can
     * be used, otherwise, the default class loader will be used. Parameters to the constructor can be
     * specified, and if some are null, then their type can be specified using the parallel paramTypes
     * parameter. The type and order of the parameters will determine exactly which constructor is used.
     * Finally, the initialization strategy to be used in this object construction call can be also specified,
     * otherwise the default setting will be used. If these specifications are not recognized, or simply
     * cannot be used to instantiate the desired object, an InvalidClassSpecificationException will be thrown.
     * The returned object will be of the passed type.</p>
     *
     * @return an Object instance that is an instance of type
     *
     * @param type key to an object specification or the class type to instantiate
     * @param identifier additional identifier to an object specification
     * @param loader a class loader to obtain class from
     * @param params params to pass to the constructor
     * @param paramTypes the Class array specifying the type of Objects in the 'params' parameter
     * @param initStrategy the initialization strategy
     *
     * @throws IllegalArgumentException If type is null, or if length of params and paramTypes does not match up.
     *                                   or if initStrategy is not one of the recognized types.
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(Class type, String identifier, ClassLoader loader, Object[] params,
        Class[] paramTypes, String initStrategy) throws InvalidClassSpecificationException {
        ObjectFactoryHelper.checkObjectNotNull(type, "type");

        Object result = createObject(type.getName(), identifier, loader, params, paramTypes, initStrategy);

        if (!type.isAssignableFrom(getArrayComponentType(result.getClass()))) {
            throw new InvalidClassSpecificationException("The type returned should be " + type.getName()
                + ", but is " + result.getClass().getName() + ".");
        }

        return result;
    }

    /**
     * <p>Creates an object using the passed specifications with the configured initialization strategy.
     * The key needs to be supplied, but the extra indentifier is optional. A specific class loader can
     * be used, otherwise the default class loader will be used. Parameters to the constructor can be
     * specified, and if some are null, then their type can be specified using the parallel paramTypes
     * parameter. The type and order of the parameters will determine exactly which constructor is used.
     * Finally, the initialization strategy to be used in this object construction call can be also specified,
     * otherwise the default setting will be used. If these specifications are not recognized, or simply
     * cannot be used to instantiate the desired object, an InvalidClassSpecificationException will be thrown.</p>
     *
     * @return an Object instance
     *
     * @param key key to an object specification or the class type to instantiate
     * @param identifier additional identifier to an object specification
     * @param loader a class loader to obtain class from
     * @param params params to pass to the constructor
     * @param paramTypes the Class array specifying the type of Objects in the 'params' parameter
     * @param initStrategy the initialization strategy
     *
     * @throws IllegalArgumentException If key is null or empty, or if length of params and paramTypes does not
     *                                   match up. or if initStrategy is not one of the recognized types.
     * @throws InvalidClassSpecificationException if it cannot instantiate an object with the passed information
     */
    public Object createObject(String key, String identifier, ClassLoader loader, Object[] params,
        Class[] paramTypes, String initStrategy) throws InvalidClassSpecificationException {
        // Check the validation of the parameters.
        ObjectFactoryHelper.checkStringNotNullOrEmpty(key, "key");
        checkStrategyString(initStrategy);

        // params and paramTypes should be both null, or both not null with the same length.
        if ((params == null) && (paramTypes == null)) {
            // if both of them are null, replace them with zero-length array.
            params = new Object[0];
            paramTypes = new Class[0];
        } else if ((params != null)
                && ((paramTypes == null) || ObjectFactoryHelper.checkNullInArray(paramTypes))) {
            // if paramTypes is null, use the class information in params.
            // params should not contains null.
            if (ObjectFactoryHelper.checkNullInArray(params)) {
                throw new IllegalArgumentException("the array contains null.");
            }

            paramTypes = new Class[params.length];

            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }
        } else if (!((params != null) && (paramTypes != null) && (paramTypes.length == params.length))) {
            throw new IllegalArgumentException("The params and paramTypes should be compatible.");
        }

        if (initStrategy.equals(REFLECTION_ONLY)) {
            // Reflection strategy.
            return createObjectByReflection(key, loader, params, paramTypes);
        } else {
            // Specification strategy or both.
            try {
                // Get the ObjectSpecification instance.
                ObjectSpecification objSpec = this.specificationFactory.getObjectSpecification(key, identifier);

                return getObjectBySpecification(objSpec);
            } catch (UnknownReferenceException e) {
                // If the ObjectSpecification doesn't exist. Throw exception if Specification strategy.
                if (initStrategy.equals(SPECIFICATION_ONLY)) {
                    throw new InvalidClassSpecificationException("error occurs when getting specifications.", e);
                } else {
                    // create the object by reference if Both strategy.
                    return createObjectByReflection(key, loader, params, paramTypes);
                }
            }
        }
    }

    /**
     * Get the item type in an array.
     *
     * @param clazz the type for checking.
     *
     * @return the type in the array.
     */
    private Class getArrayComponentType(Class clazz) {
        while (clazz.getComponentType() != null) {
            clazz = clazz.getComponentType();
        }

        return clazz;
    }

    /**
     * Get object by ObjectSpecification instance.
     *
     * @param objSpec the ObjectSpecification instance to create the instance.
     *
     * @throws InvalidClassSpecificationException if any error occurs when creating the object.
     *
     * @return the object created.
     */
    private Object getObjectBySpecification(ObjectSpecification objSpec)
        throws InvalidClassSpecificationException {
        if (objSpec.getSpecType().equals(ObjectSpecification.NULL_SPECIFICATION)) {
            // null specification. directly return null.
            return null;
        } else if (objSpec.getSpecType().equals(ObjectSpecification.SIMPLE_SPECIFICATION)) {
            // simple specification.
            return getObjectBySimpleType(objSpec.getType(), objSpec.getValue());
        } else if (objSpec.getSpecType().equals(ObjectSpecification.COMPLEX_SPECIFICATION)) {
            // complex specification.
            Object[] params = objSpec.getParameters();

            if (params == null) {
                params = new Object[0];
            }

            // create the parameter list of the object.
            Class[] types = createParameterObjects(params);

            // create the object and then returned.
            return createComplexSpecification(objSpec, params, types);
        } else if (objSpec.getSpecType().equals(ObjectSpecification.ARRAY_SPECIFICATION)) {
            // array specification.
            Object[] params = objSpec.getParameters();

            // get the array indicates the dimensions.
            int[] dims = ObjectFactoryHelper.getDimensionArrayFromArray(params);

            try {
                // create the multi-dimension array.
                Object object =
                    Array.newInstance(this.resolveClass(objSpec.getType(), resolveClassLoader(objSpec.getJar())),
                        dims);

                // copy the item of the whole array.
                copyMultiDimArray(objSpec.getType(), dims.length, object, params);

                return object;
            } catch (MalformedURLException e) {
                throw new InvalidClassSpecificationException("The array can not be created.", e);
            } catch (IllegalArgumentException e) {
                throw new InvalidClassSpecificationException("The array can not be created.", e);
            } catch (NegativeArraySizeException e) {
                throw new InvalidClassSpecificationException("The array can not be created.", e);
            } catch (ClassNotFoundException e) {
                throw new InvalidClassSpecificationException("The array can not be created.", e);
            }
        } else {
            // the specType is unexpected.
            throw new InvalidClassSpecificationException("The specType is unexpected.");
        }
    }

    /**
     * Get the object by simple type.
     *
     * @param type the type of the object.
     * @param value the string representing value.
     *
     * @return the object created.
     *
     * @throws InvalidClassSpecificationException if any error occurs when creating object.
     */
    private Object getObjectBySimpleType(String type, String value) throws InvalidClassSpecificationException {
        try {
            // create the object according to the type.
            if (type.equals(ObjectSpecification.INT)) {
                return new Integer(value);
            } else if (type.equals(ObjectSpecification.BYTE)) {
                return new Byte(value);
            } else if (type.equals(ObjectSpecification.FLOAT)) {
                return new Float(value);
            } else if (type.equals(ObjectSpecification.DOUBLE)) {
                return new Double(value);
            } else if (type.equals(ObjectSpecification.CHAR)) {
                return new Character(value.charAt(0));
            } else if (type.equals(ObjectSpecification.LONG)) {
                return new Long(value);
            } else if (type.equals(ObjectSpecification.SHORT)) {
                return new Short(value);
            } else if (type.equals(ObjectSpecification.BOOLEAN)) {
                return new Boolean(value);
            } else if (type.equals(ObjectSpecification.STRING) || type.equals(ObjectSpecification.STRING_FULL)) {
                return new String(value);
            } else {
                throw new InvalidClassSpecificationException("The simple type is unexpected.");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidClassSpecificationException("The value should not be empty.", e);
        } catch (NumberFormatException e) {
            throw new InvalidClassSpecificationException("The number format is wrong.", e);
        }
    }

    /**
     * Copy multi dimension array.
     *
     * @param type the type of the target array.
     * @param dim the dimension of the two array.
     * @param array1 the target array.
     * @param array2 the source array.
     *
     * @throws InvalidClassSpecificationException if copy can not be finished.
     */
    private void copyMultiDimArray(String type, int dim, Object array1, Object[] array2)
        throws InvalidClassSpecificationException {
        try {
            // recursively copy the whole array.
            for (int i = 0; i < array2.length; i++) {
                if (dim > 1) {
                    // if the dimension array of the target is greater than 1, recursively call this method.
                    copyMultiDimArray(type, dim - 1, ((Object[]) array1)[i], (Object[]) array2[i]);
                } else {
                    // otherwise create the object according to the object.
                    if (array2[i] instanceof ObjectSpecification) {
                        // recursively resolve the object.
                        Object tmpObj = getObjectBySpecification((ObjectSpecification) array2[i]);

                        // if the type is one of the simple type, use the corresponding method to assign.
                        if (ObjectFactoryHelper.checkSimpleType(type)) {
                            if (type.equals(ObjectSpecification.INT)) {
                                Array.setInt(array1, i, ((Integer) tmpObj).intValue());
                            } else if (type.equals(ObjectSpecification.BYTE)) {
                                Array.setByte(array1, i, ((Byte) tmpObj).byteValue());
                            } else if (type.equals(ObjectSpecification.BOOLEAN)) {
                                Array.setBoolean(array1, i, ((Boolean) tmpObj).booleanValue());
                            } else if (type.equals(ObjectSpecification.CHAR)) {
                                Array.setChar(array1, i, ((Character) tmpObj).charValue());
                            } else if (type.equals(ObjectSpecification.DOUBLE)) {
                                Array.setDouble(array1, i, ((Double) tmpObj).doubleValue());
                            } else if (type.equals(ObjectSpecification.FLOAT)) {
                                Array.setFloat(array1, i, ((Float) tmpObj).floatValue());
                            } else if (type.equals(ObjectSpecification.LONG)) {
                                Array.setLong(array1, i, ((Long) tmpObj).longValue());
                            } else if (type.equals(ObjectSpecification.STRING)
                                    || type.equals(ObjectSpecification.STRING_FULL)) {
                                Array.set(array1, i, tmpObj);
                            }
                        } else {
                            // if not simple type, directly assign.
                            Array.set(array1, i, tmpObj);
                        }
                    } else {
                        // common object, directly assign.
                        Array.set(array1, i, array2[i]);
                    }
                }
            }
        } catch (ClassCastException e) {
            throw new InvalidClassSpecificationException("The array can not be created.", e);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidClassSpecificationException("The array can not be created.", e);
        } catch (ArrayStoreException e) {
            throw new InvalidClassSpecificationException("The array can not be created.", e);
        }
    }

    /**
     * Resolve the ObjectSpecification in parameter array.
     *
     * @param params the parameter array with some ObjectSpecification in it.
     *
     * @return the types of the parameters.
     *
     * @throws InvalidClassSpecificationException the any error occurs when resolving.
     */
    private Class[] createParameterObjects(Object[] params) throws InvalidClassSpecificationException {
        Class[] classes = new Class[params.length];

        // iterate through the whole array.
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof ObjectSpecification) {
                // resolve the object if type ObjectSpecification.
                ObjectSpecification spec = (ObjectSpecification) params[i];
                params[i] = getObjectBySpecification(spec);

                // get the type in ObjectSpecification.
                try {
                    classes[i] = this.resolveClass(spec.getType(), this.resolveClassLoader(spec.getJar()));

                    // if the ObjectSpecification indicates an array,
                    // should create a temporary array and get the type.
                    if (spec.getSpecType().equals(ObjectSpecification.ARRAY_SPECIFICATION)) {
                        int[] dimes = new int[spec.getDimension()];
                        Arrays.fill(dimes, 1);
                        classes[i] = Array.newInstance(classes[i], dimes).getClass();
                    }
                } catch (MalformedURLException e) {
                    throw new InvalidClassSpecificationException("Error occurs when parsing.", e);
                } catch (ClassNotFoundException e) {
                    throw new InvalidClassSpecificationException("Error occurs when parsing.", e);
                }
            } else {
                // get the type of this object.
                classes[i] = params[i].getClass();
            }
        }

        return classes;
    }

    /**
     * Create the complex object according to the ObjectSpecification and the parameters.
     *
     * @param spec the ObjectSpecification to create object.
     * @param params the parameters.
     * @param paramTypes the types of parameters.
     *
     * @return the object created.
     *
     * @throws InvalidClassSpecificationException if any error occurs when creating the object.
     */
    private Object createComplexSpecification(ObjectSpecification spec, Object[] params, Class[] paramTypes)
        throws InvalidClassSpecificationException {
        try {
            // create the object according to the params and paramTypes.
            return createObjectByReflection(spec.getType(), resolveClassLoader(spec.getJar()), params,
                paramTypes);
        } catch (IllegalArgumentException e) {
            throw new InvalidClassSpecificationException("the params should not contain null", e);
        } catch (MalformedURLException e) {
            throw new InvalidClassSpecificationException("The URL for jar file is invalid.", e);
        }
    }

    /**
     * Create object by reflection mechanism with specified class loader and parameters.
     *
     * @param type the type name of the object.
     * @param loader the class loader, null means default class loader.
     * @param params the parameters for constructor.
     * @param paramTypes the parameter types of the constructor.
     *
     * @return the object created.
     *
     * @throws InvalidClassSpecificationException if any error occurs during the creation.
     */
    private Object createObjectByReflection(String type, ClassLoader loader, Object[] params, Class[] paramTypes)
        throws InvalidClassSpecificationException {
        try {
            // create the class according to the type and parameters.
            return resolveClass(type, loader).getConstructor(paramTypes).newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (SecurityException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (InstantiationException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (IllegalAccessException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (InvocationTargetException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (NoSuchMethodException e) {
            // try to create the object by trying every constructor
            return createObjectByEnumeration(type, loader, params);

        }
    }

    /**
     * Create object by enumerating all the constructors.
     *
     * @param type the type name of the object.
     * @param loader the class loader, null means default class loader.
     * @param params the parameters for constructor.
     * @return the object created.
     * @throws InvalidClassSpecificationException if any error occurs during the creation.
     */
    private Object createObjectByEnumeration(String type, ClassLoader loader, Object[] params)
        throws InvalidClassSpecificationException {
        // First, get all constructors
        Constructor[] ctors = null;
        try {
            ctors = resolveClass(type, loader).getConstructors();
        } catch (ClassNotFoundException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (SecurityException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        }
        if (ctors== null) {
            throw new InvalidClassSpecificationException("The object can not be initialized.");
        }
        
        // Second, try to create the object by trying every constructor
        return createObjectByDFS(ctors, 0, params);
    }

    /**
     * Create the object by trying every constructor.
     *
     * @param ctors the constructors array
     * @param index the current index
     * @param params the parameters for constructor
     * @return the object created
     * @throws InvalidClassSpecificationException if any error occurs during the creation
     */
    private Object createObjectByDFS(Constructor[] ctors, int index, Object[] params)
        throws InvalidClassSpecificationException {
        if (index == ctors.length) {
            throw new InvalidClassSpecificationException("The object can not be initialized.");
        }
        try {
            return ctors[index].newInstance(params);
        } catch (InstantiationException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (IllegalAccessException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (InvocationTargetException e) {
            throw new InvalidClassSpecificationException("The object can not be initialized.", e);
        } catch (IllegalArgumentException e) {
            return createObjectByDFS(ctors, index + 1, params);
        }
    }

    /**
     * Get the class loader from a url.
     *
     * @param jar the string indicates the jar file.
     *
     * @return the class loader from the jar.
     *
     * @throws MalformedURLException if the url is invalid.
     */
    private ClassLoader resolveClassLoader(String jar) throws MalformedURLException {
        return (jar == null) ? null : new URLClassLoader(new URL[] {new URL(jar)});
    }

    /**
     * Resolve the class from the type and the class loader.
     *
     * @param type the type of the class.
     * @param loader the class loader to find the class. null indicates the default loader.
     *
     * @return the class found.
     *
     * @throws ClassNotFoundException if the class doesn't exist.
     */
    private Class resolveClass(String type, ClassLoader loader) throws ClassNotFoundException {
        // if the type is one of the simple types, return the corresponding Class object.
        if (type.equals(ObjectSpecification.INT)) {
            return int.class;
        } else if (type.equals(ObjectSpecification.BYTE)) {
            return byte.class;
        } else if (type.equals(ObjectSpecification.BOOLEAN)) {
            return boolean.class;
        } else if (type.equals(ObjectSpecification.CHAR)) {
            return char.class;
        } else if (type.equals(ObjectSpecification.DOUBLE)) {
            return double.class;
        } else if (type.equals(ObjectSpecification.FLOAT)) {
            return float.class;
        } else if (type.equals(ObjectSpecification.LONG)) {
            return long.class;
        } else if (type.equals(ObjectSpecification.STRING)) {
            return String.class;
        }

        // otherwise, load the jar and return the Class.
        if (loader != null) {
            return loader.loadClass(type);
        } else {
            return Class.forName(type);
        }
    }

    /**
     * <p>Gets the initialization strategy used by this object factory.</p>
     *
     * @return the initialization strategy
     */
    public String getInitStrategy() {
        return initStrategy;
    }

    /**
     * <p>Sets the initialization strategy for this object factory.</p>
     *
     * @param initStrategy the initialization strategy
     * @throws IllegalArgumentException If the initStategy is not one of the three recognized ones
     */
    public void setInitStrategy(String initStrategy) {
        checkStrategyString(initStrategy);

        this.initStrategy = initStrategy;
    }

    /**
     * Check if the strategy string is one of the three expected values.
     *
     * @param strategy the strategy string to check.
     *
     * @throws IllegalArgumentException if the strategy string is invalid.
     *
     * @return the valid or default strategy
     */
    private static String checkStrategyString(String strategy) {
        // strategy should not be null or empty.
        if ((strategy == null) || (strategy.trim().length() == 0)) {
            throw new IllegalArgumentException("the initStrategy should not be null or empty.");
        }

        // strategy should be one of the three strings.
        if (!(strategy.equals(BOTH) || strategy.equals(REFLECTION_ONLY) || strategy.equals(SPECIFICATION_ONLY))) {
            throw new IllegalArgumentException(
                "the initStrategy should be one of the three strings in this class.");
        }

        return strategy;
    }
}
