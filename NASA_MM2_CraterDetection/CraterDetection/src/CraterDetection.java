
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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

    class Blob {

        public int size;
        public int cn;
        public int cp;
        public int ltn;
        public int ltp;
        public int brn;
        public int brp;

        public void merge(Blob b2) {
            int newSize = size + b2.size;
            cn = (cn * size + b2.size * b2.cn) / newSize;
            cp = (cp * size + b2.size * b2.cp) / newSize;
            ltn = Math.min(ltn, b2.ltn);
            ltp = Math.min(ltp, b2.ltp);
            brn = Math.max(brn, b2.brn);
            brp = Math.max(brp, b2.brp);

            size += b2.size;

        }
    }
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
    Map<Integer, Set<Integer>> blobs;
    Set<Integer> merged;
    int radiusThreshold;

    public int processImage(String name, int W, int H, int[] data) {
        this.merged = new HashSet<Integer>();
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
        this.blobs = new HashMap<Integer, Set<Integer>>();

        // i, x, p : horizontal coordinate.
        // j, y, n : vertical coordinate.
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                int currentPos = pos(i, j);
                if (compos[currentPos] == 0) {
                    bfs(currentPos);
                }
            }
        }

        mergeBlobs();


        BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_BYTE_INDEXED);
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                image.setRGB(i, j, compos[pos(i, j)]);
            }
        }
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        for (int comp = 1; comp < numComp; ++comp) {
            if (merged.contains(comp)) {
                continue;
            }
            int tn = (ltn.get(comp) + brn.get(comp)) / 2;
            int tp = (ltp.get(comp) + brp.get(comp)) / 2;
            int width = brp.get(comp) - ltp.get(comp);
            assert (width >= 0);
            int height = brn.get(comp) - ltn.get(comp);
            assert (height >= 0);
            if ((size.get(comp) > 25)
                    && ((1.0 * width * height) / (W * H) < 0.35) //                  && (Math.sqrt(Math.pow(Math.abs(tn - cn.get(comp)), 2) + Math.pow(Math.abs(tp - cp.get(comp)), 2)) < radiusThreshold)
//                    && ((1.0 * size.get(comp) / (width * height)) > 0.50)
                    ) {

                int xl = ltp.get(comp);
                int yt = ltn.get(comp);
                int xr = brp.get(comp);
                int yb = brn.get(comp);
                assert (xl > 0);
                assert (yt > 0);
                assert (xr > 0);
                assert (yb > 0);
                assert (xl < xr);
                assert (yt < yb);
                assert (xr < W);
                assert (yb < H);

                preresult.add(this.name + " " + xl + " " + yt + " " + xr + " " + yb);
                g2d.drawRect(ltp.get(comp), ltn.get(comp), width, height);
            }

        }


        File oFile = new File("RES_" + name + ".png");
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
        blobs.put(numComp, new HashSet<Integer>());
        int refColor = this.data[currentPos];
        while (!pos.isEmpty()) {
            int cPos = pos.removeLast();
            if (compos[cPos] == 0) {
                int currentColor = this.data[cPos];
                if (Math.abs(refColor - currentColor) < threshold) {
                    blobs.get(numComp).add(cPos);
                    compos[cPos] = numComp;
                    int i = cPos / H;
                    int j = cPos % H;

                    sz++;
                    sn += j;
                    sp += i;

                    ltn = Math.min(ltn, j);
                    ltp = Math.min(ltp, i);
                    brn = Math.max(brn, j);
                    brp = Math.max(brp, i);

                    if (j > 0) {
                        pos.addFirst(cPos - 1);
                    }
                    if (j < (H - 1)) {
                        pos.addFirst(cPos + 1);
                    }
                    if (i > 0) {
                        pos.addFirst(cPos - H);
                    }
                    if (i < (W - 1)) {
                        pos.addFirst(cPos + H);
                    }
                }
            }
        }
        //if ((1.0 * sz / (W * H)) < 0.4) {
            this.size.put(numComp, sz);
            this.ltn.put(numComp, new Integer(ltn));
            this.ltp.put(numComp, new Integer(ltp));
            this.brn.put(numComp, new Integer(brn));
            this.brp.put(numComp, new Integer(brp));
            assert (sz <= ((brn - ltn) * (brp - ltp)));

            cn.put(numComp, new Integer((int) sn / sz));
            cp.put(numComp, new Integer((int) sp / sz));
            numComp++;
        //}
    }

    private int pos(int i, int j) {
        return (i * H + j);
    }

    private void printBlob(int numBlob) {
        System.err.println("numBlob = " + numBlob);
        System.err.println("size = " + size.get(numBlob));
        System.err.println("xl = " + ltp.get(numBlob) + " yt = " + ltn.get(numBlob) + " xr = " + brp.get(numBlob) + " yb = " + brn.get(numBlob));

    }

    private void mergeBlobs() {
        int numCompStart = this.numComp;
        for (int blobSource = 1; blobSource < numCompStart; blobSource++) {
            if (merged.contains(blobSource)) {
                continue;
            }
            boolean mergeDone = false;
            int txl = ltp.get(blobSource);
            int tyt = ltn.get(blobSource);
            int txr = brp.get(blobSource);
            int tyb = brn.get(blobSource);
            int tsize = size.get(blobSource);
            int tcn = cn.get(blobSource);
            int tcp = cp.get(blobSource);
            for (int blobDest = 1; blobDest < numCompStart; blobDest++) {
                if (merged.contains(blobDest)) {
                    continue;
                }
                int xl = Math.min(txl, ltp.get(blobDest));
                int yt = Math.min(tyt, ltn.get(blobDest));
                int xr = Math.max(txr, brp.get(blobDest));
                int yb = Math.max(tyb, brn.get(blobDest));
                int maxSurface = ((xr - xl) * (yb - yt));
                int nSize = Math.min(tsize + size.get(blobDest), maxSurface);
                double r = (1.0 * nSize) / maxSurface;



                if ( (r > 0.95) 
                        && 
                        (1.0*maxSurface/(W*H) < 0.35) 
                        ){
//                    printBlob(blobSource);
//                    printBlob(blobDest);
//                    System.err.println("r=" + r);
                    mergeDone = true;
                    merged.add(blobDest);

                    tyt = yt;
                    txl = xl;
                    tyb = yb;
                    txr = xr;
                    tcn = tsize * tcn + size.get(blobDest) * cn.get(blobDest);
                    tcp = tsize * tcp + size.get(blobDest) * cp.get(blobDest);
                    tsize = nSize;
                    //printBlob(numComp);


                }
            }
            if (mergeDone) {
                merged.add(blobSource);
                this.size.put(this.numComp, tsize);
                this.ltn.put(this.numComp, new Integer(tyt));
                this.ltp.put(this.numComp, new Integer(txl));
                this.brn.put(this.numComp, new Integer(tyb));
                this.brp.put(this.numComp, new Integer(txr));
                cn.put(numComp, tcn);
                cp.put(numComp, tcp);
//                printBlob(numComp);
                this.numComp++;
            }
        }
    }

    public String[] getCraters() {


        String result[] = new String[Math.min(1000000, preresult.size())];
        System.err.println(result.length);
        assert (result.length < 100001);

        for (int i = 0; i < Math.min(1000000, preresult.size()); ++i) {
            result[i] = preresult.get(i);
        }


        return result;
    }
    private ArrayList<String> preresult;

    public int init() {
        threshold = 75;
        radiusThreshold = 10;

        preresult = new ArrayList<String>();
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
