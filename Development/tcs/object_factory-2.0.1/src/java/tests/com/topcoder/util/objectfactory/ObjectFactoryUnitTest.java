/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.testclasses.TestClass1;
import com.topcoder.util.objectfactory.testclasses.TestClass2;

import junit.framework.TestCase;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


/**
 * Unit test for ObjectFactory.
 *
 * @author mgmg
 * @version 2.0
 */
public class ObjectFactoryUnitTest extends TestCase {
    /**
     * The ConfigManagerSpecificationFactory instance for test.
     */
    private ConfigManagerSpecificationFactory factory = null;

    /**
     * The test instance.
     */
    private ObjectFactory[] instance = new ObjectFactory[3];

    /**
     * Create the test instance.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.loadSingleConfigFile();

        factory = new ConfigManagerSpecificationFactory("valid_config");

        instance[0] = new ObjectFactory(factory, ObjectFactory.SPECIFICATION_ONLY);
        instance[1] = new ObjectFactory(factory, ObjectFactory.REFLECTION_ONLY);
        instance[2] = new ObjectFactory(factory, ObjectFactory.BOTH);
    }

    /**
     * Clean the config.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        TestHelper.clearConfig();
    }

    /**
     * Test constructor1 with null parameter.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor1_NullFactory() {
        try {
            new ObjectFactory(null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor1 with valid parameter.
     * No exception will be thrown.
     */
    public void testConstructor1_Accuracy() {
        ObjectFactory objFactory = new ObjectFactory(factory);

        assertNotNull("The instance should not be null.", objFactory);
    }

    /**
     * Test constructor2 with null parameter.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor2_NullFactory() {
        try {
            new ObjectFactory(null, ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with null parameter.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor2_NullStrategy() {
        try {
            new ObjectFactory(factory, null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with empty parameter.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor2_EmptyStrategy() {
        try {
            new ObjectFactory(factory, " ");
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with null parameter.
     * IllegalArgumentException should be thrown.
     */
    public void testConstructor2_InvalidStrategy() {
        try {
            new ObjectFactory(factory, "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with valid parameter.
     * No exception will be thrown.
     */
    public void testConstructor2_Accuracy() {
        ObjectFactory objFactory = new ObjectFactory(factory, ObjectFactory.REFLECTION_ONLY);

        assertNotNull("The instance should not be null.", objFactory);
    }

    /**
     * Test getInitStrategy.
     * No exception will be thrown.
     */
    public void testGetInitStrategy() {
        String strategy = instance[1].getInitStrategy();

        assertEquals("The result is wrong.", strategy, ObjectFactory.REFLECTION_ONLY);
    }

    /**
     * Test setInitStrategy with null parameter..
     * IllegalArgumentException should be thrown.
     */
    public void testSetInitStrategy_NullStrategy() {
        try {
            instance[2].setInitStrategy(null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test setInitStrategy with empty parameter..
     * IllegalArgumentException should be thrown.
     */
    public void testSetInitStrategy_EmptyStrategy() {
        try {
            instance[1].setInitStrategy(" ");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test setInitStrategy with invalid parameter..
     * IllegalArgumentException should be thrown.
     */
    public void testSetInitStrategy_InvalidStrategy() {
        try {
            instance[1].setInitStrategy("invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test setInitStrategy with valid parameter.
     */
    public void testSetInitStrategy_Accuracy() {
        instance[0].setInitStrategy(ObjectFactory.BOTH);

        assertEquals("The result is wrong.", ObjectFactory.BOTH, instance[0].getInitStrategy());
    }

    /**
     * Test createObject1 with null key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with empty key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with not-existing key.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_NotExistKey() throws Exception {
        try {
            instance[2].createObject("key_not_exist");
            fail("InvalidClassSpecificationException should be thrown because of the not-existing key.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with invalid object.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_ObjectThrowException() throws Exception {
        try {
            instance[0].createObject("frac1");
            fail("InvalidClassSpecificationException should be thrown because of the invalid object.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_Accuracy1() throws Exception {
        Object obj = instance[0].createObject("bar");

        assertTrue("The type is wrong.", obj instanceof TestClass2);
        assertEquals("The result is wrong.", "TestClass2TestClass12Strong2.5", obj.toString());
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_Accuracy2() throws Exception {
        Object obj = instance[1].createObject("java.util.HashSet");

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_Accuracy3() throws Exception {
        Object obj = instance[2].createObject("testcollection");

        assertTrue("The type is wrong.", obj instanceof Collection[]);
        assertEquals("The result is wrong.", 3, ((Collection[]) obj).length);
        assertTrue("The result is wrong.", ((Collection[]) obj)[0] instanceof HashSet);
        assertNull("The result is wrong.", ((Collection[]) obj)[1]);
        assertTrue("The result is wrong.", ((Collection[]) obj)[2] instanceof ArrayList);
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject1_Accuracy4() throws Exception {
        Object obj = instance[2].createObject("java.util.ArrayList");

        assertTrue("The type is wrong.", obj instanceof ArrayList);
    }

    /**
     * Test createObject2 with null type.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject2_NullType() throws Exception {
        try {
            instance[0].createObject((Class) null);
            fail("IllegalArgumentException should be thrown because of the null type.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject2 with not-existiong type.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject2_NotExistType() throws Exception {
        try {
            instance[0].createObject(HashSet.class);
            fail("InvalidClassSpecificationException should be thrown because of the not-existiong type.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject2 with valid type.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject2_Accuracy1() throws Exception {
        Object obj = instance[1].createObject(HashSet.class);

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject2 with valid type.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject2_Accuracy2() throws Exception {
        Object obj = instance[2].createObject(HashSet.class);

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject3 with null key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null, "default");
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with empty key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ", "default");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with null identifier.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_NullIdentifier() throws Exception {
        try {
            instance[0].createObject("frac", null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with empty identifier.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_EmptyIdentifier() throws Exception {
        try {
            instance[0].createObject("frac", "  ");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with not-existing key.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_NotExistObject() throws Exception {
        try {
            instance[0].createObject("not_exist", "not_exist");
            fail("InvalidClassSpecificationException should be thrown because of the not-existing key.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with valid key.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_Accuracy1() throws Exception {
        Object obj = instance[0].createObject("intArray", "arrays");

        assertTrue("The type is wrong.", obj instanceof int[][]);
        assertEquals("The result is wrong.", 17, ((int[][]) obj).length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[5].length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[13].length);
        assertEquals("The result is wrong.", 3, ((int[][]) obj)[4][0]);
        assertEquals("The result is wrong.", 4, ((int[][]) obj)[9][1]);
    }

    /**
     * Test createObject3 with valid key.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject3_Accuracy2() throws Exception {
        Object obj = instance[2].createObject("test", "collection");

        assertTrue("The type is wrong.", obj instanceof Collection[]);
        assertEquals("The result is wrong.", 3, ((Collection[]) obj).length);
        assertTrue("The result is wrong.", ((Collection[]) obj)[0] instanceof HashSet);
        assertNull("The result is wrong.", ((Collection[]) obj)[1]);
        assertTrue("The result is wrong.", ((Collection[]) obj)[2] instanceof ArrayList);
    }

    /**
     * Test createObject4 with null type.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject4_NullType() throws Exception {
        try {
            instance[0].createObject((Class) null, "default");
            fail("IllegalArgumentException should be thrown because of the null type.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with null identifier.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject4_NullIdentifier() throws Exception {
        try {
            instance[0].createObject(HashSet.class, null);
            fail("IllegalArgumentException should be thrown because of the null identifier.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with empty identifier.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject4_EmptyIdentifier() throws Exception {
        try {
            instance[0].createObject(HashSet.class, " ");
            fail("IllegalArgumentException should be thrown because of the empty identifier.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with not-existing key.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject4_KeyNotExist() throws Exception {
        try {
            instance[0].createObject(HashSet.class, "default");
            fail("InvalidClassSpecificationException should be thrown because the object doesn't exist.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject4_Accuracy1() throws Exception {
        Object obj = instance[1].createObject(HashSet.class, "default");

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject4 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject4_Accuracy2() throws Exception {
        Object obj = instance[2].createObject(HashSet.class, "default");

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject5 with null parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null, "default", (URL) null, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with empty parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ", "default", (URL) null, new Object[0], new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[0], new Class[0],
                "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with null strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_NullStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[0], new Class[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam1() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[0],
                new Class[] {String.class}, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam2() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[] {new HashSet()},
                new Class[] {HashSet.class}, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam3() throws Exception {
        try {
            instance[1].createObject("invalid_type", null, (URL) null, new Object[] {new HashSet()},
                new Class[] {HashSet.class}, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam4() throws Exception {
        try {
            instance[1].createObject("invalid_type", "invalid_id", (URL) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam5() throws Exception {
        try {
            instance[2].createObject("frac1", "default", (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam6() throws Exception {
        try {
            instance[2].createObject("int", "Mismatch", (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam7() throws Exception {
        try {
            instance[2].createObject("typeMismatch", null, (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_InvalidParam8() throws Exception {
        try {
            instance[2].createObject("type", null,
                new URL(TestHelper.getURLString("test_files/not_exist.jar")), null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_Accuracy1() throws Exception {
        Object obj =
            instance[0].createObject("frac", "default", (URL) null, null, null, ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_Accuracy2() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class.getName(), null, (URL) null,
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class},
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_Accuracy3() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class.getName(), null, (URL) null,
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_Accuracy4() throws Exception {
        Object obj =
            instance[0].createObject("frac", "default", (URL) null, new Object[] {new Integer(5), "abc"},
                new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject5_Accuracy5() throws Exception {
        Object obj =
            instance[0].createObject("com.test.TestComplex", null,
                new URL(TestHelper.getURLString("test_files/test.jar")), new Object[] {new Integer(5), "abc"},
                new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertEquals("The result is wrong.", obj.getClass().getName(),
            new URLClassLoader(new URL[] {new URL(TestHelper.getURLString("test_files/test.jar"))}).loadClass(
                "com.test.TestComplex").getName());
    }

    /**
     * Test createObject6 with null type.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_NullType() throws Exception {
        try {
            instance[0].createObject((Class) null, "default", (URL) null, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[0], new Class[0],
                "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with null strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_NullStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[0], new Class[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_InvalidParam1() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[0], new Class[] {String.class},
                ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_InvalidParam2() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[] {new HashSet()},
                new Class[] {HashSet.class}, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_InvalidParam3() throws Exception {
        try {
            instance[1].createObject(String.class, "invalid_id", (URL) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_InvalidParam4() throws Exception {
        try {
            instance[2].createObject(int.class, "Mismatch", (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_Accuracy1() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, "default", (URL) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_Accuracy2() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, null, (URL) null, new Object[] {new Integer(5), "abc"},
                new Class[] {int.class, String.class}, ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_Accuracy3() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, null, (URL) null, new Object[] {new Integer(5), "abc"},
                new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject6_Accuracy4() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, "default", (URL) null,
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject7 with null type.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_NullType() throws Exception {
        try {
            instance[0].createObject((Class) null, "default", ClassLoader.getSystemClassLoader(),
                new Object[0], new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(), new Object[0],
                new Class[0], "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with null strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_NullStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(), new Object[0],
                new Class[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_InvalidParam1() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(), new Object[0],
                new Class[] {String.class}, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_InvalidParam2() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(),
                new Object[] {new HashSet()}, new Class[] {HashSet.class}, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_InvalidParam3() throws Exception {
        try {
            instance[1].createObject(String.class, "invalid_id", ClassLoader.getSystemClassLoader(), null,
                null, ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_InvalidParam4() throws Exception {
        try {
            instance[2].createObject(int.class, "Mismatch", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_Accuracy1() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, "default", ClassLoader.getSystemClassLoader(), null,
                null, ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_Accuracy2() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, null, ClassLoader.getSystemClassLoader(),
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class},
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_Accuracy3() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, null, ClassLoader.getSystemClassLoader(),
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject7_Accuracy4() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class, "default", ClassLoader.getSystemClassLoader(),
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject8 with null parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null, "default", ClassLoader.getSystemClassLoader(),
                new Object[0], new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with empty parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ", "default", ClassLoader.getSystemClassLoader(), new Object[0],
                new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(),
                new Object[0], new Class[0], "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with null strategy.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_NullStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(),
                new Object[0], new Class[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam1() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(),
                new Object[0], new Class[] {String.class}, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam2() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(),
                new Object[] {new HashSet()}, new Class[] {HashSet.class}, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam3() throws Exception {
        try {
            instance[1].createObject("invalid_type", null, ClassLoader.getSystemClassLoader(),
                new Object[] {new HashSet()}, new Class[] {HashSet.class}, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam4() throws Exception {
        try {
            instance[1].createObject("invalid_type", "invalid_id", (ClassLoader) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam5() throws Exception {
        try {
            instance[2].createObject("frac1", "default", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam6() throws Exception {
        try {
            instance[2].createObject("int", "Mismatch", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam7() throws Exception {
        try {
            instance[2].createObject("typeMismatch", null, ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam8() throws Exception {
        try {
            instance[0].createObject("java.lang.String", null, (ClassLoader) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam9() throws Exception {
        try {
            instance[0].createObject("testcollection", null, (ClassLoader) null, null, null,
                ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam10() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                ClassLoader.getSystemClassLoader(), new Object[] {new Integer(5), null}, null,
                ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam11() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                ClassLoader.getSystemClassLoader(), new Object[] {new Integer(5), null},
                new Class[] {int.class, null}, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam12() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                ClassLoader.getSystemClassLoader(), null, new Class[] {int.class, null},
                ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_InvalidParam13() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                ClassLoader.getSystemClassLoader(), null, new Class[] {int.class, String.class},
                ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy1() throws Exception {
        Object obj =
            instance[0].createObject("frac", "default", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy2() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class.getName(), null, ClassLoader.getSystemClassLoader(),
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class},
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy3() throws Exception {
        Object obj =
            instance[0].createObject(TestClass1.class.getName(), null, ClassLoader.getSystemClassLoader(),
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy4() throws Exception {
        Object obj =
            instance[0].createObject("frac", "default", ClassLoader.getSystemClassLoader(),
                new Object[] {new Integer(5), "abc"}, new Class[] {int.class, String.class}, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy5() throws Exception {
        Object obj =
            instance[0].createObject("java.lang.String", null, (ClassLoader) null, null, null,
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong", obj instanceof String);
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy6() throws Exception {
        Object obj =
            instance[0].createObject("buffer", "default", (ClassLoader) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong", obj instanceof StringBuffer);
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy7() throws Exception {
        Object obj =
            instance[0].createObject("intArray", "arrays", (ClassLoader) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong", obj instanceof int[][]);
        assertEquals("The result is wrong.", 17, ((int[][]) obj).length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[5].length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[13].length);
        assertEquals("The result is wrong.", 3, ((int[][]) obj)[4][0]);
        assertEquals("The result is wrong.", 4, ((int[][]) obj)[9][1]);
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy8() throws Exception {
        Object obj =
            instance[0].createObject("testcollection", null, (ClassLoader) null, null, null, ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof Collection[]);
        assertEquals("The result is wrong.", 3, ((Collection[]) obj).length);
        assertTrue("The result is wrong.", ((Collection[]) obj)[0] instanceof HashSet);
        assertNull("The result is wrong.", ((Collection[]) obj)[1]);
        assertTrue("The result is wrong.", ((Collection[]) obj)[2] instanceof ArrayList);
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy9() throws Exception {
        Object obj =
            instance[0].createObject("java.lang.String", null, (ClassLoader) null, null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof String);
        assertEquals("The result is wrong.", "", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy10() throws Exception {
        Object obj =
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                ClassLoader.getSystemClassLoader(), new Object[] {new Integer(5), null},
                new Class[] {int.class, String.class}, ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15null", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy11() throws Exception {
        Object obj =
            instance[1].createObject("test", "strings", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof String[][]);
        assertEquals("The result is wrong.", 3, ((String[][]) obj)[0].length);
        assertEquals("The result is wrong.", 3, ((String[][]) obj)[1].length);
        assertEquals("The result is wrong.", "has,,{1,2},{3,4}h,:set", ((String[][]) obj)[0][0]);
        assertEquals("The result is wrong.", "012", ((String[][]) obj)[0][1]);
        assertNull("The result is wrong.", ((String[][]) obj)[0][2]);
        assertNull("The result is wrong.", ((String[][]) obj)[1][0]);
        assertEquals("The result is wrong.", "012", ((String[][]) obj)[1][1]);
        assertEquals("The result is wrong.", "458", ((String[][]) obj)[1][2]);
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception exception to JUnit.
     */
    public void testCreateObject8_Accuracy12() throws Exception {
        Object obj =
            instance[1].createObject("objectArray", null, ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong", obj instanceof Object[][][]);
        assertEquals("The result is wrong.", 1, ((Object[]) obj).length);
        assertEquals("The result is wrong.", 1, ((Object[][]) obj)[0].length);
        assertEquals("The result is wrong.", 3, ((Object[][][]) obj)[0][0].length);
        assertEquals("The result is wrong.", 3, ((Object[][][]) obj)[0][0].length);
        assertTrue("The result is wrong.", ((Object[][][]) obj)[0][0][0] instanceof TestClass1);
        assertTrue("The result is wrong.", ((Object[][][]) obj)[0][0][1] instanceof TestClass2);
        assertNull("The result is wrong.", ((Object[][][]) obj)[0][0][2]);
    }
}
