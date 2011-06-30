
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edemairy
 */
public class StringCompression {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringCompression compressor = new StringCompression();
        Scanner scanner = new Scanner(System.in);

        String data = scanner.nextLine();
        System.err.println("data=" + data);
        int N = scanner.nextInt();
        scanner.nextLine();
        System.err.println("N=" + N);
        int limits[] = new int[N];
        for (int i = 0; i < N; i++) {
            limits[i] = scanner.nextInt();
            scanner.nextLine();
            System.err.println("limits[" + i + "]=" + limits[i]);
        }
        String[] ret = compressor.compress(data, limits);
        System.err.println("length=" + ret.length);
        System.out.println(ret.length);
        for (int i = 0; i < ret.length; ++i) {
            System.out.println(ret[i]);
            System.err.println(ret[i]);
        }
        System.out.flush();
    }

    /*
     *   Distance between substring(i,i+l) and substring(j, j+l)
     */
    int delta(int i, int j, int l, String data) {

        String s1 = data.substring(i, i + l - 1);
        String s2 = data.substring(j, j + l - 1);

        return delta(s1, s2);
    }

    /*
     *   Distance between s1 and s2.
     *   assumes that s1.length() == s2.length()
     */
    int delta(String s1, String s2) {
        int result = 0;
        int l = s1.length();
        for (int pos = 0; pos < l; ++pos) {
            result += (s1.charAt(pos) != s2.charAt(pos)) ? 1 : 0;
        }
        return result;
    }

    public String[] compress(String data, int[] limits) {

        System.err.println("data.length = " + data.length());
        if (limits.length == 1) {
            if (data.length() == limits[0]) {
                System.err.println("Bingo!");
                String[] result = new String[1];
                result[0] = data;
                return result;
            } else {
                return null;
            }
        }


        int S = data.length();
        int N = limits.length;

        Integer[] limitsI = new Integer[limits.length];
        for (int i = 0; i < limits.length; ++i) {
            limitsI[i] = limits[i];
        }
        Arrays.sort(limitsI, Collections.reverseOrder());

        for (int cptL = 0; cptL < limitsI.length; cptL++) {
            int l = limitsI[cptL];
            int maxc = 3;
            int ceil = l / 2;
            String fmaxc = null;
            for (int i = 0; i < (S - l); ++i) {
                int found = 0;

                for (int j = (i + 1); j < (S - l); ++j) {
                    int d = delta(i, j, l, data);
                    if (d < ceil) {
                        found++;
                    }
                }
                if (found > maxc - 2) {
                    maxc = Math.max(found, maxc);
                    fmaxc = data.substring(i, i + l);



                    System.err.println("limits.length=" + limits.length + " l=" + l + " string=" + fmaxc + " maxc=" + maxc);
                    if (maxc != 0) {
                        StringBuffer newBuffer = new StringBuffer();
                        for (int is = 0; is < (S - l); ++is) {
                            if (delta(data.substring(is, is + l), fmaxc) < ceil) {
                                newBuffer.append((char) (limits.length+'0'-1));
                                is += (l - 1);
                            } else {
                                newBuffer.append(data.charAt(is));
                            }
                        }
                        System.err.println("newBuffer = " + newBuffer);
                        int[] newLimitsI = new int[limitsI.length - 1];
                        int cptNewLimitsI = 0;
                        for (int j = 0; j < limitsI.length; ++j) {
                            if (cptL != j) {
                                newLimitsI[cptNewLimitsI] = limitsI[j];
                                cptNewLimitsI++;
                            }
                        }
                        String[] tempResult = compress(newBuffer.toString(), newLimitsI);
                        if (tempResult != null) {
                            System.err.println("Bingo" + limits.length);
                            String[] result = new String[limits.length];
//                            Set<String> bagString = new HashSet<String>(Arrays.asList(tempResult));
//                            bagString.add(fmaxc);
                            
                            for (int ri = 0; ri < result.length-1; ri++) {
                                result[ri] = tempResult[ri];                                
                            }
                            result[result.length-1]=fmaxc;
                            for (int ri = 0; ri < result.length; ri++) {
                                System.err.println("ri:"+ri+" "+result[ri]+" length="+result[ri].length());
                            }
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }
}
