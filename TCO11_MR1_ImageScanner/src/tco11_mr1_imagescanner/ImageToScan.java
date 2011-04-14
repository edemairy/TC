package tco11_mr1_imagescanner;


import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author edemairy
 */
public class ImageToScan {
    static public String scan(int row) {
        System.out.println('?');
        System.out.println(row);
        System.out.flush();
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

}
