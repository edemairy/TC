/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tco11_mr1_imagescanner;

import tco11_mr1_imagescanner.ImageToScan;

/**
 *
 * @author edemairy
 */
class ImageScanner {

    private int val(char input) {
        return input - '0';
    }

    public String[] restore(int H, int W, int nb, int nLetter) {
        String[] result = new String[H];
        for (int i = 0; i < H; i += 2) {
            result[i] = ImageToScan.scan(i);
        }

        for (int i = 1; i < H; i += 2) {
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < W; ++j) {
                int nbNeighbours = 0;

                if ((j - 1) >= 0) {
                    nbNeighbours += val(result[i - 1].charAt(j - 1));
                }
                nbNeighbours += 2*val(result[i - 1].charAt(j));
                if ((j + 1) < W) {
                    nbNeighbours += val(result[i - 1].charAt(j + 1));
                }
                if ((i + 1) < H) {
                    if ((j - 1) >= 0) {
                        nbNeighbours += val(result[i + 1].charAt(j - 1));
                    }
                    nbNeighbours += val(result[i + 1].charAt(j));
                    if ((j + 1) < W) {
                        nbNeighbours += val(result[i + 1].charAt(j + 1));
                    }
                }


                if (nbNeighbours > 4) {
                    buffer.append('1');
                } else {
                    buffer.append('0');
                }
            }
            result[i] = buffer.toString();
        }
        return result;
    }
}
