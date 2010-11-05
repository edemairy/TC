/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package digitspattern;

import java.util.Vector;

/**
 *
 * @author edemairy
 */
public class DigitsPattern {

    private Vector<Vector<Integer>> v;
    private int height;
    private int width;

    public int[] recreate(String[] targetPattern) {
        this.v = new Vector<Vector<Integer>>();
        for (int i = 0; i<6; ++i) {
            this.v.add(new Vector<Integer>() );
        }
        this.height = targetPattern.length;
        this.width = targetPattern[0].length();
        int nb = 0;
        for (int row = 0; row < this.height; row++) {
            for (int column = 0; column < this.width; column++) {
                switch(targetPattern[row].charAt(column)) {
                    case '.': break;
                    default:
                        nb++;
                        int num = targetPattern[row].charAt(column) - '0';
                        this.v.elementAt(num).add(row);
                        this.v.elementAt(num).add(column);
                }
            }
        }
        Vector<Integer> result = new Vector<Integer>();
        for (int i = 5; i>0; --i) {
            result.addAll(this.v.get(i));
        }
        System.err.println( "result.size()*2 = " + result.size()*2);
        System.err.println( "2*nb = " + 2*nb);
        assert(result.size() == 2*nb);
        int[] res = new int[2*nb];
        for ( int i = 0; i < 2*nb; ++i) {
            res[i] = result.elementAt(i);
        }
        return res;
    }
}
