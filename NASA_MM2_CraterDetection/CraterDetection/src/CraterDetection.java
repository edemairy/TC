
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edemairy
 */
public class CraterDetection {

    private int W, H;
    private int[] data;
    private String name;
    int threshold;
    private int[] compos;
    private String[] result;
    private int numComp;
    Map<Integer, Integer> size;
    Map<Integer, Integer> cn;
    Map<Integer, Integer> cp;
    Map<Integer, Integer> ltn;
    Map<Integer, Integer> ltp;
    Map<Integer, Integer> brn;
    Map<Integer, Integer> brp;
    int radiusThreshold;

    public int processImage(String name, int W, int H, int[] data) {
        this.W = W;
        this.H = H;
        this.data = data;
        this.name = name;
        assert (data.length == (W * H));
        compos = new int[data.length];
        Arrays.fill(compos, 0);
        numComp = 1;
        size = new HashMap<Integer, Integer>();
        cn = new HashMap<Integer, Integer>();
        cp = new HashMap<Integer, Integer>();
        ltn = new HashMap<Integer, Integer>();
        ltp = new HashMap<Integer, Integer>();
        brn = new HashMap<Integer, Integer>();
        brp = new HashMap<Integer, Integer>();

        // i : vertical coordinate.
        // j : horizontal coordinate.
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                int currentPos = pos(i, j);
                if (compos[currentPos] == 0) {
                    bfs(currentPos);
                    numComp++;
                }
            }
        }
        System.err.println("numComp = " + numComp);
        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_BYTE_INDEXED);
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                image.setRGB(i, j, compos[pos(i, j)]);
            }
        }
        File oFile = new File("compos.png");
        try {
            ImageIO.write(image, "png", oFile);
        } catch (IOException ex) {
            Logger.getLogger(CraterDetection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private void bfs(int currentPos) {
        int sz = 0;
        long sn = 0;
        long sp = 0;
        int ltn, ltp, brn, brp;
        ltn = H;
        ltp = W;
        brn = 0;
        brp = 0;
        LinkedList<Integer> pos = new LinkedList<Integer>();
        pos.add(currentPos);
        int refColor = this.data[currentPos];
        while (!pos.isEmpty()) {
            int cPos = pos.removeLast();
            if (compos[cPos] == 0) {
                int currentColor = this.data[cPos];
                if (Math.abs(refColor - currentColor) < threshold) {
                    compos[cPos] = numComp;
                    int i = cPos / H;
                    int j = cPos % H;

                    sz++;
                    sn += i;
                    sp += j;

                    ltn = Math.min(ltn, i);
                    ltp = Math.min(ltp, j);
                    brn = Math.max(brn, i);
                    brp = Math.max(brn, j);

                    if (i > 0) {
                        pos.add(cPos - H);
                    }
                    if (i < (H - 1)) {
                        pos.add(cPos + H);
                    }
                    if (j > 0) {
                        pos.add(cPos - 1);
                    }
                    if (j < (W - 1)) {
                        pos.add(cPos + 1);
                    }
                }
            }
        }
        size.put(numComp, sz);

        this.ltn.put(numComp, new Integer(ltn));
        this.ltp.put(numComp, new Integer(ltp));
        this.brn.put(numComp, new Integer(brn));
        this.brp.put(numComp, new Integer(brp));

        cn.put(numComp, new Integer((int) sn / sz));
        cp.put(numComp, new Integer((int) sp / sz));
    }

    private int pos(int i, int j) {
        return (i * H + j);
    }

    public String[] getCraters() {
        ArrayList<String> preresult = new ArrayList<String>();
        for (int comp = 1; comp < numComp; ++comp) {
            int tn = (ltn.get(comp) + brn.get(comp)) / 2;
            int tp = (ltp.get(comp) + brp.get(comp)) / 2;
            int width = brp.get(comp) - ltp.get(comp);
            int height = brn.get(comp) - ltn.get(comp);
            if ( (Math.max(width,height)>25)&& 
                    (Math.min(width, height)<=200) &&
                    (Math.sqrt(Math.pow(Math.abs(tn - cn.get(comp)), 2) + Math.pow(Math.abs(tp - cp.get(comp)), 2)) < radiusThreshold)) {
                preresult.add(this.name + " " + ltp.get(comp) + " " + ltn.get(comp) + " " + brp.get(comp) + " " + brn.get(comp));
            }
        }
        String result[] = new String[preresult.size()];
        System.err.println(result.length);

        for (int i = 0; i < preresult.size(); ++i) {
            result[i] = preresult.get(i);
            System.err.println(result[i]);
        }
        return result;
    }

    public int init() {
        threshold = 75;
        radiusThreshold = 10;
        return 0;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CraterDetection detector = new CraterDetection();
        System.out.println(detector.init());
        System.out.flush();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            int cmd = scanner.nextInt();
            scanner.nextLine();
            if (cmd == 1) {
                String name = scanner.nextLine();
                int W = scanner.nextInt();
                scanner.nextLine();
                int H = scanner.nextInt();
                scanner.nextLine();
                int len = scanner.nextInt();
                scanner.nextLine();
                int[] data = new int[len];
                for (int i = 0; i < len; i++) {
                    data[i] = scanner.nextInt();
                    scanner.nextLine();
                }

                System.out.println(detector.processImage(name, W, H, data));
                System.out.flush();
            } else {
                String[] ret = detector.getCraters();
                System.out.println(ret.length);
                for (int i = 0; i < ret.length; i++) {
                    System.out.println(ret[i]);
                }
                System.out.flush();
                break;
            }
        }
    }
}
