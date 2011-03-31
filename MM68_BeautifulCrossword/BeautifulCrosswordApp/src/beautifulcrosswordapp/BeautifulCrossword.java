/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beautifulcrosswordapp;

/**
 *
 * @author edemairy
 */
public class BeautifulCrossword {

    public String[] generateCrossword(int N, String[] words, int[] weights) {
        String result[] = new String[N];
        char oneLine[] = new char[N];
        for (int i = 0; i < N; i++) {
            oneLine[i] = '.';
        }
        for (int i = 0; i < N; i++) {
            result[i] = new String(oneLine);
        }
        assert( result.length == N );
        return result;
    }
}
