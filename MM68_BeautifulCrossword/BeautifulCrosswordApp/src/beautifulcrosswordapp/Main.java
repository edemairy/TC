/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beautifulcrosswordapp;

import java.lang.String;
import java.util.Scanner;

/**
 *
 * @author edemairy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = Integer.decode(in.nextLine());
        int W = Integer.decode(in.nextLine());
        String[] words = new String[W];
        for (int i = 0; i < W; i++) {
            words[i] = in.nextLine();
        }
        int weights[] = new int[4];
        for (int i = 0; i < 4; i++) {
            weights[i] = Integer.decode(in.nextLine());
        }
        BeautifulCrossword crossword = new BeautifulCrossword();
        String[] ret = crossword.generateCrossword(N, words, weights);
        for (int i = 0; i < N; i++) {
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}
