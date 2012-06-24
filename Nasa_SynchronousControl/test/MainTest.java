

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.URISyntaxException;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author edemairy
 */
public class MainTest {

    public MainTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testInput1() throws FileNotFoundException, IOException, URISyntaxException {
        File tempOutput = File.createTempFile("test", "txt");
        File expectedResult = new File(MainTest.class.getResource("/result.txt").toURI());
        System.setIn(MainTest.class.getResourceAsStream("/input.txt"));
        System.setOut(new PrintStream(tempOutput));
        Main.main(new String[0]);
        String result = getFileContent(tempOutput);
        assertEquals(getFileContent(expectedResult), result);
        System.err.println(result);
    }

    private String getFileContent(final File f) throws FileNotFoundException, IOException {
        FileInputStream fin = new FileInputStream(f);
        BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String thisLine;
        while ((thisLine = myInput.readLine()) != null) {
            sb.append(thisLine).append('\n');
        }
        return sb.toString();
    }
}
