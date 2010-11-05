/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package digitspattern;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author edemairy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      DigitsPattern digitspattern = new DigitsPattern();
      Scanner cin = new Scanner(System.in);
      Integer H = cin.nextInt();
      System.err.printf("H read = %n", H);

      String[] targetPattern = new String[H];
      for ( int i=0; i<H; i++) {
        String nl = cin.next();
        System.err.printf("i:%d %s\n", i, nl);
        targetPattern[i] = nl;
      }
      int[] res = digitspattern.recreate(targetPattern);
      for (int i=0; i<res.length; i++) {
          System.out.println(res[i]);
          System.err.println(res[i]);
      }
      System.out.flush();
    }

}
