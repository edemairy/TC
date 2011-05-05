/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tco11_mr1_imagescanner;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author edemairy
 */
public class LettersAnalyzer {

    public LettersAnalyzer() {
    }
    private static char FONT_STYLE[] = {'P', 'B'};

    public void run() throws FileNotFoundException {
        HashMap<Long, Integer> mh;
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            for (char style : FONT_STYLE) {
                for (int size = 8; size < 29; ++size) {

                    String filename = "/user/edemairy/home/Documents/Personal/TC/TCO11_MR1_ImageScanner/src/files/letters/" + letter + style + size + ".txt";
                    InputStream resource = new FileInputStream(filename);
                    Scanner scanner = new Scanner(resource);
                    int H = Integer.decode(scanner.nextLine());
                    int W = Integer.decode(scanner.nextLine());

                    Set<Long> si = new HashSet<Long>();

                    for (int i = 0; i < H; ++i) {
                        long hash = 0;
                        String currentLine = scanner.nextLine();
                        for (int j = 0; j < W; ++j) {
                            if (currentLine.charAt(j) == '1') {
                                hash += Math.pow(2, j);
                            }
                        }
                        si.add(hash);
                        System.out.println(hash + ": " + currentLine);
                    }

                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        LettersAnalyzer analyzer = new LettersAnalyzer();
        analyzer.run();
    }
}
