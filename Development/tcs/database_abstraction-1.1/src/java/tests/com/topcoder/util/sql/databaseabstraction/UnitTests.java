/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BigDecimalConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BlobConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.BooleanConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteArrayConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ByteConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ClobConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DateConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.DoubleConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.FloatConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.IntConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.LongConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.ShortConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.StringConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimeConverterTestV11;
import com.topcoder.util.sql.databaseabstraction.ondemandconversion.TimestampConverterTestV11;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all unit test cases.
 * </p>
 *
 * @author justforplay
 * @version 1.1
 * @since 1.0
 */
public class UnitTests extends TestCase {

    /**
     * All unit test cases.
     *
     * @return unit test.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // ====================Test cases of V1.0
        suite.addTest(new TestSuite(ColumnTestCase.class));
        suite.addTest(new TestSuite(MapperTestCase.class));
        suite.addTest(new TestSuite(CustomResultSetMetaDataTestCase.class));
        suite.addTest(new TestSuite(CustomResultSetTestCase.class));
        suite.addTest(new TestSuite(DatabaseAbstractorTestCase.class));
        suite.addTest(new TestSuite(ResultSetDataTypeTestCase.class));

        // ====================Test cases of V1.1
        suite.addTest(new TestSuite(ColumnTestCaseV11.class));
        suite.addTest(new TestSuite(BigDecimalConverterTestV11.class));
        suite.addTest(new TestSuite(ByteArrayConverterTestV11.class));
        suite.addTest(new TestSuite(BooleanConverterTestV11.class));
        suite.addTest(new TestSuite(ByteConverterTestV11.class));
        suite.addTest(new TestSuite(DoubleConverterTestV11.class));
        suite.addTest(new TestSuite(FloatConverterTestV11.class));
        suite.addTest(new TestSuite(IntConverterTestV11.class));
        suite.addTest(new TestSuite(LongConverterTestV11.class));
        suite.addTest(new TestSuite(ShortConverterTestV11.class));
        suite.addTest(new TestSuite(DateConverterTestV11.class));
        suite.addTest(new TestSuite(TimeConverterTestV11.class));
        suite.addTest(new TestSuite(TimestampConverterTestV11.class));
        suite.addTest(new TestSuite(StringConverterTestV11.class));
        suite.addTest(new TestSuite(BlobConverterTestV11.class));
        suite.addTest(new TestSuite(ClobConverterTestV11.class));

        suite.addTest(new TestSuite(IllegalMappingExceptionTestV11.class));
        suite.addTest(new TestSuite(InvalidCursorStateExceptionTestV11.class));
        suite.addTest(new TestSuite(MapperTestV11.class));
        suite.addTest(new TestSuite(OnDemandMapperTestV11.class));
        suite.addTest(new TestSuite(AbstractionHelperTestV11.class));
        suite.addTest(new TestSuite(CustomResultSetMetaDataTestV11.class));
        suite.addTest(new TestSuite(RowDataValueTestV11.class));
        suite.addTest(new TestSuite(CustomResultSetTestV11.class));
        suite.addTest(new TestSuite(DatabaseAbstractorTestV11.class));
        suite.addTest(new TestSuite(Demo.class));
        return suite;
    }

};
