/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mm73_ordinaltraitassociationmapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author edemairy
 */
public class OrdinalTraitAssociationMapping {

    final static int NB_DATA = 281;
    final static int SIZE_DATA = 2913;
    final static int NB_CATEGORIES = 5;
    ArrayList<String> genotypes[];

    /* \param genotype data will be provided to your code as String[] genotype. 
     * It will contain exactly 281 elements and each element will contain 
     * exactly 2913 characters. The j-th character in the i-th element represents 
     * the genotype at SNP marker #j of individual #i. We will use characters '+', 
     * '-' and 'X' to denote 1, -1 and NA, correspondingly. We assume that both 
     * SNP markers and individuals are numbered starting from 0 in the same order 
     * as they appear in genotype data file.     
     * 
     * \param traitData It will contain exactly 281 elements. For each i, 
     * traitData[i] will be an integer 1 to 5, inclusive, equal to the trait 
     * value for individual #i. The individuals are numbered 0 to 280 in the 
     * same order as they appear in the genotype data file. 
     * 
     * \return a int[] containing exactly 2913 elements, each being 0 or 1. If 
     * you think that SNP marker #i is significantly associated with the trait 
     * in traitData, then the i-th element of your return should be 1, otherwise 
     * it should be 0. The SNP markers are assumed to be numbered 0 to 2912, 
     * in the same order as they appear in genotype data file. 
     */
    int[] identifyGenes(String[] genotype, int[] traitData) {
        int[] result = new int[SIZE_DATA];
        genotypes = new ArrayList[NB_CATEGORIES];
        for (int cat = 0; cat < NB_CATEGORIES; cat++) {
            genotypes[cat] = new ArrayList<String>();
        }
        for (int i = 0; i < NB_DATA; ++i) {
            genotypes[traitData[i]-1].add(genotype[i]);
        }

        analyze(genotypes[0], result);

        return result;
    }

    private void analyze(ArrayList<String> data, int[] result) {
        int N = data.size();
        float[] mean = new float[SIZE_DATA];
        float[] stdev = new float[SIZE_DATA];
        float[][] cor = new float[SIZE_DATA][];
        float[][] LD = new float[SIZE_DATA][];
        float[] v = new float[SIZE_DATA];

        for (int i = 0; i < SIZE_DATA; i++) {
            mean[i] = 0;
            for (int j = 0; j < data.size(); j++) {
                mean[i] += getValue(data.get(j).charAt(i));
            }
            mean[i] /= data.size();
        }

        for (int i = 0; i < SIZE_DATA; i++) {
            stdev[i] = 0;
            for (int j = 0; j < data.size(); j++) {
                stdev[i] += Math.pow(getValue(data.get(j).charAt(i)) - mean[i], 2);
            }
            stdev[i] /= data.size();
        }
        for (int i = 0; i < SIZE_DATA; i++) {
            cor[i] = new float[SIZE_DATA];
            LD[i] = new float[SIZE_DATA];
        }

        for (int i = 0; i < SIZE_DATA; i++) {
            cor[i][i] = 1;
            for (int j = 0; j < i; j++) {
                if ((stdev[i] == 0) || (stdev[j] == 0)) {
                    LD[i][j] = LD[j][i] = 0;
                } else {
                    cor[i][j] = cor[j][i] = 0;
                    for (int k = 0; k < data.size(); k++) {
                        cor[i][j] += (getValue(data.get(k).charAt(i)) - mean[i]) * (getValue(data.get(k).charAt(j)) - mean[j]);
                    }
                    cor[i][j] /= (data.size() * stdev[i] * stdev[j]);
                    cor[j][i] = cor[i][j];
                    LD[i][j] = LD[j][i] = cor[j][i] * cor[i][j];
                }
                v[i] += LD[i][j];
                v[j] += LD[i][j];
            }
        }

        float[] vp = (float[]) v.clone();
        Arrays.sort(vp);
        float floor = vp[ Math.round((float) (vp.length * 0.1))];
        for (int i = 0; i < SIZE_DATA; ++i) {
            result[i] = (v[i] > floor) ? 1 : 0;
        }
    }

    int getValue(char c) {
        switch (c) {
            case '+':
                return 1;
            case '-':
                return -1;
            case 'X':
                return 0;
        }
        throw new RuntimeException("Impossible character:" + c);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int gLen = scanner.nextInt();
        scanner.nextLine();
        String[] genotype = new String[gLen];
        for (int i = 0; i < gLen; ++i) {
            genotype[i] = scanner.nextLine();
        }

        while (true) {
            int tLen = scanner.nextInt();
            scanner.nextLine();

            if (tLen == 0) {
                break;
            }
            int[] traitData = new int[tLen];
            for (int i = 0; i < tLen; ++i) {
                traitData[i] = scanner.nextInt();
                scanner.nextLine();
            }
            OrdinalTraitAssociationMapping o = new OrdinalTraitAssociationMapping();
            int[] ret = o.identifyGenes(genotype, traitData);
            
            System.out.println(ret.length); 
            for (int i = 0; i<ret.length; ++i) {
                  System.out.println(ret[i]);
            }
            System.out.flush();
        }
    }
}
