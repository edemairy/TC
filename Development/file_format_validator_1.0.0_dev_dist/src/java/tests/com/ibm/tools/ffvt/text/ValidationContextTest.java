/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.tools.ffvt.text;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author edemairy
 */
public class ValidationContextTest {

    public ValidationContextTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    // Variable containing the object to be tested.
    private ValidationContext validationContext;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of the construction.
     */
    @Test
    public void testConstructor() {
        this.validationContext = new ValidationContext();
        assertNotNull(this.validationContext);
        assertNotNull(this.validationContext.getErrors());
    }

    /**
     * Test of addError method, of class ValidationContext.
     */
    @Test
    public void testAddError() {
        System.out.println("addError");
        String error = "A new error.";
        ValidationContext instance = new ValidationContext();
        instance.addError(error);
    }

    /**
     * Test of addError method with null method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddErrorNull() {
        System.out.println("addErrorNull");
        String error = null;
        ValidationContext instance = new ValidationContext();
        instance.addError(error);
    }

    /**
     * Test of addError method with empty error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddErrorEmpty() {
        System.out.println("addErrorEmpty");
        String error = "";
        ValidationContext instance = new ValidationContext();
        instance.addError(error);
    }

    /**
     * Test of getErrors method, of class ValidationContext.
     */
    @Test
    public void testGetErrors() {
        System.out.println("getErrors");
        ValidationContext instance = new ValidationContext();
        List expResult = new ArrayList();
        List result = instance.getErrors();
        assertEquals(expResult, result);

        instance.addError("error 1");
        instance.addError("error 2");
        instance.addError("error 3");
        result = instance.getErrors();
        expResult.add("error 1");
        expResult.add("error 2");
        expResult.add("error 3");
        assertEquals(expResult, result);
    }

    /**
     * Test of increaseProcessedLinesNum method, of class ValidationContext.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncreaseProcessedLinesNumNull() {
        System.out.println("increaseProcessedLinesNum");
        LineType type = null;
        ValidationContext instance = new ValidationContext();
        instance.increaseProcessedLinesNum(type);
    }

    /**
     * Test of increaseProcessedLinesNum method, of class ValidationContext.
     */
    @Test
    public void testIncreaseProcessedLinesNum() {
        System.out.println("increaseProcessedLinesNum");
        LineType type = LineType.DATA;
        ValidationContext instance = new ValidationContext();
        instance.increaseProcessedLinesNum(type);
    }

    /**
     * Test of getProcessedLinesNumByType method, of class ValidationContext.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetProcessedLinesNumByTypeNull() {
        System.out.println("getProcessedLinesNumByType");
        LineType type = null;
        ValidationContext instance = new ValidationContext();
        int result = instance.getProcessedLinesNumByType(type);
    }

    /**
     * Test of getProcessedLinesNumByType method, of class ValidationContext.
     */
    @Test
    public void testGetProcessedLinesNumByType() {
        System.out.println("getProcessedLinesNumByType");
        LineType type = null;
        ValidationContext instance = new ValidationContext();
        int expResult = 0;
        int result = instance.getProcessedLinesNumByType(LineType.DATA);
        assertEquals(expResult, result);
        result = instance.getProcessedLinesNumByType(LineType.HEADER);
        assertEquals(expResult, result);
        result = instance.getProcessedLinesNumByType(LineType.FOOTER);
        assertEquals(expResult, result);
        instance.increaseProcessedLinesNum(LineType.DATA);
        instance.increaseProcessedLinesNum(LineType.HEADER);
        instance.increaseProcessedLinesNum(LineType.HEADER);
        instance.increaseProcessedLinesNum(LineType.FOOTER);
        instance.increaseProcessedLinesNum(LineType.FOOTER);
        instance.increaseProcessedLinesNum(LineType.FOOTER);
        result = instance.getProcessedLinesNumByType(LineType.DATA);
        assertEquals(1, result);
        result = instance.getProcessedLinesNumByType(LineType.HEADER);
        assertEquals(2, result);
        result = instance.getProcessedLinesNumByType(LineType.FOOTER);
        assertEquals(3, result);
    }

    /**
     * Test of getLineIndex method, of class ValidationContext.
     */
    @Test
    public void testGetLineIndex() {
        System.out.println("getLineIndex");
        ValidationContext instance = new ValidationContext();
        int expResult = 0;
        int result = instance.getLineIndex();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLineIndex method, of class ValidationContext.
     */
    @Test
    public void testSetLineIndex() {
        System.out.println("setLineIndex");
        int lineIndex = 31416;
        ValidationContext instance = new ValidationContext();
        instance.setLineIndex(lineIndex);
        int result = instance.getLineIndex();
        assertEquals(lineIndex,result);
    }

    /**
     * Test of getPositionFromEnd method, of class ValidationContext.
     */
    @Test
    public void testGetPositionFromEnd() {
        System.out.println("getPositionFromEnd");
        ValidationContext instance = new ValidationContext();
        int expResult = 0;
        int result = instance.getPositionFromEnd();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPositionFromEnd method, of class ValidationContext.
     */
    @Test
    public void testSetPositionFromEnd() {
        System.out.println("setPositionFromEnd");
        int positionFromEnd = 31416;
        ValidationContext instance = new ValidationContext();
        instance.setPositionFromEnd(positionFromEnd);       
        int result = instance.getPositionFromEnd();
        assertEquals(positionFromEnd, result);

    }
}
