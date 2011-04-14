/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tco11_mr1_imagescanner;

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
        ImageScanner imageScanner = new ImageScanner();
        Scanner in = new Scanner(System.in);
        int H = Integer.decode( in.nextLine() );
        int W = Integer.decode( in.nextLine() );
        int nb = Integer.decode( in.nextLine() );
        int nLetter = Integer.decode( in.nextLine() );
        String[] ret = imageScanner.restore(H,W,nb,nLetter);
        assert(ret.length == H);
        System.out.println('!');
        for (int i = 0; i < H; i++) {
            assert(ret[i].length() == W );
            System.err.println(ret[i]);
            System.out.println(ret[i]);
        }
        System.out.flush();
    }
}
