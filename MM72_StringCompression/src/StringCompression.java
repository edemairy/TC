
import java.util.Scanner;

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
        System.err.println("data="+data);
        int N = scanner.nextInt();scanner.nextLine();
        System.err.println("N="+N);
        int limits[] = new int[N];
        for (int i =0; i<N; i++) {
            limits[i] = scanner.nextInt();scanner.nextLine();
            System.err.println("limits["+i+"]="+limits[i]);
        }
        String[] ret = compressor.compress(data,limits);
        System.err.println("length="+ret.length);
        System.out.println(ret.length);
        for (int i=0; i<ret.length; ++i) {
            System.out.println(ret[i]);
            System.err.println(ret[i]);
        }
        System.out.flush();        
    }

    public String[] compress(String data, int[] limits) {
        int S = data.length();
        int N = limits.length;
        String [] result = new String[N]; 
        for (int i=0; i<N; ++i) {
            int s = (i*S)/N;
            int e = ((i+1)*S)/N;
            System.err.println("s="+s);
            System.err.println("e="+e);
            result[i] = data.substring(s,e);
        }
        return result;
    }
}
