/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author edemairy
 */
public class MinorityVariantsTest {

    String[] input1 = {
        "19	0.000213880331444	109",
        "7	0.000124512493423	53",
        "27	0.243946082074	44",
        "29	0.243946082074	30",
        "25	1.60661281836e-05	141",
        "24	0.243946082074	60",
        "8	0.000124512493423	69",
        "18	0.243946082074	75",
        "25	0.000277140711167	56",
        "22	0.00031328949958	62",
        "34	0.243946082074	45",
        "34	0.243946082074	60",
        "34	0.243946082074	90",
        "34	0.243946082074	30"
    };

    short[] resultReadQualityInput1 = {19, 7, 27, 29, 25, 24, 8, 18, 25, 22, 34, 34, 34, 34};
    double[] resultKmerAbundanceInput1 = {0.000213880331444, 0.000124512493423, 0.243946082074, 0.243946082074, 1.60661281836e-05, 0.243946082074, 0.000124512493423,
        0.243946082074, 0.000277140711167, 0.00031328949958, 0.243946082074, 0.243946082074, 0.243946082074, 0.243946082074};
    short[] resultMappingScoreInput1 = {109, 53, 44, 30, 141, 60, 69, 75, 56, 62, 45, 60, 90, 30};

    public MinorityVariantsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testReadInputs() {
        MinorityVariants mv = new MinorityVariants();
        mv.readInputs(input1);
        assertArrayEquals(resultReadQualityInput1, mv.getReadquality());
        assertArrayEquals(resultKmerAbundanceInput1, mv.getKmerAbundance(), Double.MIN_VALUE);
        assertArrayEquals(resultMappingScoreInput1, mv.getMappingScore());
    }
}
