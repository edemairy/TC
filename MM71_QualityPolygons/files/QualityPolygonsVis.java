import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.security.*;
import javax.swing.*;

// ------------- class Point ------------------------------
class Pnt {
    public int x,y;
    public Pnt() {};
    public Pnt(int x1, int y1) {
        x=x1;
        y=y1;
    }
    public boolean equals(Pnt other) {
        return (x==other.x && y==other.y);
    }
}

// ------------- class G2D --------------------------------
class G2D {
    public static Pnt substr(Pnt p1, Pnt p2) {
        return new Pnt(p1.x-p2.x, p1.y-p2.y);
    }
    public static int norm2(Pnt p) {
        return (p.x*p.x + p.y*p.y);
    }
    public static int dot(Pnt p1, Pnt p2) {
        return p1.x*p2.x+p1.y*p2.y;
    }
    public static int cross(Pnt p1, Pnt p2) {
        return p1.x*p2.y-p1.y*p2.x;
    }
    public static int dist2(Pnt p1, Pnt p2) {
        return norm2(substr(p1, p2));
    }
}


// ------------- class QualityPolygon itself --------------
public class QualityPolygonsVis {
    final int SZ = 700;             //field size
    int NP, Npoly;                  //number of points given, and number of polygons selected
    Pnt[] p;                        //coordinates of points (fixed)
    int[] pointsPar;                //coordinates of points (as an array parameter)
    int sidesDiff, radiiDiff;       //allowed difference of max and min edge/radius, in % of max length
    int[][] polys;                  //indices of points which form polygons
    int[] polysVert;                //number of vertices in each poly
    boolean valid[];
    int[] used;                     //which poly uses this point?
    // ---------------------------------------------------
    void generate(String seed) {
      try {
        SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
        rnd.setSeed(Long.parseLong(seed));
        //generate points by sampling them from one of the distributions
        int i,j,k;
        //number of points
        if (seed.equals("1"))
            NP = 50;
        else {
            if (rnd.nextInt(2)==0)
                NP = rnd.nextInt(450)+50;
            else NP = rnd.nextInt(4501)+500;
        }
        System.out.println("NP = "+NP);
        p = new Pnt[NP];
        //generate distributions
        int nD = rnd.nextInt(16)+5;
        System.out.println("nD = "+nD);
        int[] xD = new int[nD], yD = new int[nD];
        double[] rD = new double[nD], dD = new double[nD];  //radius and stddev
        for (i=0; i<nD; i++) {
            if (i != 0 && rnd.nextInt(3) == 2) {
                //copy the center of the distribution from the previous one
                xD[i] = xD[i-1];
                yD[i] = yD[i-1];
            }
            else {
                //generate a new center
                xD[i] = rnd.nextInt(SZ-100)+50;
                yD[i] = rnd.nextInt(SZ-100)+50;
            }
            //parameters of distribution itself
            rD[i] = rnd.nextDouble()*270+30;
            dD[i] = rnd.nextDouble()*90+10;
        }

        //generate the points themselves
        double r, alpha;
        int distInd;
        boolean ok;
        for (i=0; i<NP; i++)
            do {
                //choose which distribution to sample - equiprobable
                distInd = rnd.nextInt(nD);
                //sample it
                alpha = rnd.nextDouble()*2*Math.PI;
                r = Math.abs(rnd.nextGaussian()*dD[distInd]+rD[distInd]);
                p[i] = new Pnt((int)Math.round(xD[distInd] + r * Math.cos(alpha)), (int)Math.round(yD[distInd]+ r * Math.sin(alpha)));
                if (p[i].x < 0 || p[i].x >= 700 || p[i].y < 0 || p[i].y >= 700) {
                    ok = false;
                    continue;
                }
                ok=true;
                for (j=0; j<i && ok; j++)
                    if (p[i].equals(p[j]))
                        ok=false; 
            }
            while (!ok);

        //convert points to parameter array
        pointsPar = new int[2*NP];
        for (i=0; i<NP; i++) {
            pointsPar[2*i] = p[i].x;
            pointsPar[2*i+1] = p[i].y;
        }

        if (manual) {
            //and to coordToPoint
            coordToPoint = new int[SZ][SZ];
            for (i=0; i<SZ; i++)
            for (j=0; j<SZ; j++)
                coordToPoint[i][j] = -1;
            for (i=0; i<NP; i++)
                for (j=-1; j<2; j++)
                    for (k=-1; k<2; k++)
                        if (p[i].x+j>=0 && p[i].x+j<SZ && p[i].y+k>=0 && p[i].y+k<SZ)
                            coordToPoint[p[i].x+j][p[i].y+k] = i;
        }

        //generate scalar parameters of the test case
        sidesDiff = rnd.nextInt(20)+1;
        radiiDiff = rnd.nextInt(20)+1;
        if (seed.equals("1"))
            sidesDiff = radiiDiff = 20;
        System.out.println("sidesDiff = " + sidesDiff);
        System.out.println("radiiDiff = " + radiiDiff);
      }
      catch (Exception e) { 
        addFatalError("An exception occurred while generating test case.");
        e.printStackTrace(); 
      }
    }
    // ---------------------------------------------------
    long sq(long a) {
        return a*a;
    }
    // ---------------------------------------------------
    String validatePoly(int[] poly, int n) {
        //check that the polygon satisfies all conditions
        int i,j;
        if (n<3)
            return "a polygon must consist of at least 3 vertices.";

        //edges must have approximately equal length
        long maxLen2 = G2D.dist2(p[poly[0]], p[poly[n-1]]), minLen2 = maxLen2, len2;
        double maxLen, minLen;
        for (i=0; i<n-1; i++) {
            len2 = G2D.dist2(p[poly[i]], p[poly[i+1]]);
            maxLen2 = Math.max(maxLen2, len2);
            minLen2 = Math.min(minLen2, len2);
        }
        if (100*100 * minLen2 < maxLen2 * sq(100-sidesDiff)) {
            maxLen = Math.sqrt(maxLen2);
            minLen = Math.sqrt(minLen2);
            return "sides of polygon must be approximately equal; here minLen = "+minLen+", maxLen = "+maxLen+", diff = "+(maxLen-minLen)/maxLen+".";
        }

        //distance from each vertice to polygon center must be the same
        long sumX = 0, sumY = 0;
        for (i=0; i<n; i++) {
            sumX += p[poly[i]].x;
            sumY += p[poly[i]].y;
        }
        long maxR2 = sq(sumX - n*p[poly[0]].x) + sq(sumY - n*p[poly[0]].y), minR2 = maxR2, R2;
        for (i=1; i<n; i++) {
            R2 = sq(sumX - n*p[poly[i]].x) + sq(sumY - n*p[poly[i]].y);
            maxR2 = Math.max(maxR2, R2);
            minR2 = Math.min(minR2, R2);
        }
        if (100*100 * minR2 < maxR2 * sq(100-radiiDiff)) {
            maxLen = Math.sqrt(maxR2)/n;
            minLen = Math.sqrt(minR2)/n;
            return "polygon must be approximately inscribed; here minR = "+minLen+", maxR = "+maxLen+", diff = "+(maxLen-minLen)/maxLen+".";
        }

        //must be convex - for each side all other points lay strictly to one side of it
        Pnt p1, p2;
        long cross;
        for (i=0; i<n; i++) {
            //use p[i] and p[i+1] as reference vector
            p1 = G2D.substr(p[poly[(i+1)%n]], p[poly[i]]);
            //and check that all other points have the same direction with it
            p2 = G2D.substr(p[poly[(i+2)%n]], p[poly[i]]);
            cross = G2D.cross(p1, p2);
            if (cross == 0)
                return "polygon must be convex.";
            else
                cross /= Math.abs(cross);
            for (j=3; j<n; j++) {
                if (cross * G2D.cross(p1, G2D.substr(p[poly[(i+j)%n]], p[poly[i]])) <= 0)
                    return "polygon must be convex.";
            }
        }

        return "";
    }
    // ---------------------------------------------------
    double calcScore() {
        //calculate the score of current set of polygons
        //will be called from interactive editing to show the results of changes
        double score = 0;
        for (int i=0; i<polys.length; i++)
            if (valid[i])
                score += sq(polysVert[i]);
            else if (strict)
                return 0;
        return score;
    }
    // ---------------------------------------------------
    public double runTest(String seed) {
      try {
        int i,j,n;
        generate(seed);
        //init variables
        used = new int[NP];
        for (i=0; i<NP; i++)
            used[i] = -2;

        //allow combining two modes - program output and manual correction
        if (proc!=null) {
            //get the return and parse it into the polygons
            String[] ret;
            try { ret = choosePolygons(pointsPar, sidesDiff, radiiDiff); }
            catch (Exception e) {
                addFatalError("Failed to get result from choosePolygons.");
                return 0;
            }
            //each string represents one polygon: number of vertices, indices of the vertices in original array
            Npoly = ret.length;
            //if there will be manual play, add slots for more polygons for future
            n = Npoly;
            if (manual)
                n = Math.max(n, 1000);
            polys = new int[n][];
            polysVert = new int[n];
            valid = new boolean[n];
            for (i=0; i<Npoly; i++) {
                //parse the string into the polygon
                try {
                    if (debug) System.out.println(ret[i]);
                    String[] st = ret[i].split(" ");
                    n = st.length;        //number of vertices in this poly
                    //if there will be manual play, add slots for more vertices for each polygon
                    if (manual)
                        polys[i] = new int[Math.max(n, 1000)];
                    else 
                        polys[i] = new int[n];
                    polysVert[i] = n;
                    for (j=0; j<n; j++) {
                        polys[i][j] = Integer.parseInt(st[j]);
                        //check whether this point already was used
                        if (used[polys[i][j]]>-2) {
                            addFatalError("Polygon "+i+" reuses point "+polys[i][j]+".");
                            return 0;
                        }
                        else
                            used[polys[i][j]] = i;
                    }
                } catch (Exception e) {
                    addFatalError("Polygon "+i+" parses with errors.");
                    return 0;
                }
                //validate this polygon
                String val = validatePoly(polys[i], polysVert[i]);
                if (val.length()!=0) {
                    addFatalError("Polygon "+i+" is invalid: "+val);
                    valid[i] = false;
                }
                else
                    valid[i] = true;
            }
        }
        else {
            //no polygons
            Npoly = 0;
            n = 1000;
            polys = new int[n][];
            polysVert = new int[n];
            valid = new boolean[n];
        }

        if (vis) {
            //draw the image
            jf.setSize(SZX+17,SZY+37);
            jf.setVisible(true);
            draw();
        }

        if (manual) {
            ready = false;
            Pcur = new int[1400];
            Ncur = 0;
            //wait for the result of manual polygons adjustements - validation will be done there
            while (!ready)
                try { Thread.sleep(1000);}
                catch (Exception e) { e.printStackTrace(); } 
        }

        return calcScore();
      }
      catch (Exception e) { 
        addFatalError("An exception occurred while trying to process your program's results.");
        e.printStackTrace(); 
        return 0.0;
      }
    }
// ------------- visualization part ----------------------
    static String exec;
    static boolean vis, manual, debug, strict;
    static Process proc;
    JFrame jf;
    Vis v;
    InputStream is;
    OutputStream os;
    BufferedReader br;
    // problem-specific drawing params
    final int SZX = SZ+2+100,SZY=SZ+2;
    volatile boolean ready;
    volatile int Ncur;
    volatile int[] Pcur;
    int[][] coordToPoint;
    // ---------------------------------------------------
    String[] choosePolygons(int[] points, int sidesDiff, int radiiDiff) throws IOException
    {   //pass the params to the solution and get the return
        int i;
        StringBuffer sb = new StringBuffer();
        sb.append(points.length).append('\n');
        for (i=0; i<points.length; i++)
            sb.append(points[i]).append('\n');
        sb.append(sidesDiff).append('\n');
        sb.append(radiiDiff).append('\n');
        os.write(sb.toString().getBytes());
        os.flush();
        //get the return - an array of strings
        int nret = Integer.parseInt(br.readLine());
        String[] ret = new String[nret];
        for (i=0; i<nret; i++)
            ret[i] = br.readLine();
        return ret;
    }
    // ---------------------------------------------------
    void draw() {
        if (!vis) return;
        v.repaint();
    }
    // ---------------------------------------------------
    public class Vis extends JPanel implements MouseListener, WindowListener {
        public void paint(Graphics g) {
          try {
            //do painting here
            int i,j,n;
            char[] ch;
            BufferedImage bi = new BufferedImage(SZX+10,SZY+10,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D)bi.getGraphics();
            //background
            g2.setColor(new Color(0xD3D3D3));
            g2.fillRect(0,0,SZX+10,SZY+10);
            g2.setColor(Color.WHITE);
            g2.fillRect(0,0,SZ+1,SZ+1);
            //frame
            g2.setColor(Color.BLACK);
            g2.drawRect(0,0,SZ+1,SZ+1);

            //sides
            for (i=0; i<polys.length; i++) {
                n = polysVert[i];
                if (valid[i])
                    g2.setColor(Color.GREEN);
                else
                    g2.setColor(Color.RED);
                for (j=0; j<n; j++)
                    g2.drawLine(p[polys[i][j]].x, (SZ-1-p[polys[i][j]].y), p[polys[i][(j+1)%n]].x, (SZ-1-p[polys[i][(j+1)%n]].y));
            }
            //draw current poly
            g2.setColor(new Color(0x6495ED));
            for (i=0; i<Ncur; i++) {
                g2.drawLine(p[Pcur[i]].x, (SZ-1-p[Pcur[i]].y), p[Pcur[(i+1)%Ncur]].x, (SZ-1-p[Pcur[(i+1)%Ncur]].y));
            }

            //"buttons"
            if (manual) {
                g2.setColor(Color.BLACK);
                ch = ("SUBMIT").toCharArray();
                g2.setFont(new Font("Arial",Font.BOLD,16));
                g2.drawChars(ch,0,ch.length,SZ+20,30);
                g2.drawRect(SZ+12,8,90,30);

                ch = ("ADD POLY").toCharArray();
                g2.setFont(new Font("Arial",Font.BOLD,14));
                g2.drawChars(ch,0,ch.length,SZ+18,109);
                g2.drawRect(SZ+12,88,90,30);

                ch = ("DEL POLY").toCharArray();
                g2.setFont(new Font("Arial",Font.BOLD,14));
                g2.drawChars(ch,0,ch.length,SZ+19,149);
                g2.drawRect(SZ+12,128,90,30);
            }

            //current score
            ch = (""+calcScore()).toCharArray();
            g2.setFont(new Font("Arial",Font.BOLD,14));
            g2.drawChars(ch,0,ch.length,SZ+10,200);

            //points with small digits near them
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("Arial",Font.PLAIN,10));
            for (i=0; i<NP; i++)
            {   if (used[i]>-1) {
                    if (valid[used[i]])
                        g2.setColor(Color.GREEN);
                    else g2.setColor(Color.RED);
                }
                else {
                    if (used[i]==-1) {
                        //a special highlight for last point in the polygon
                        if (Pcur[Ncur-1]==i)
                            g2.setColor(new Color(0x6495ED));
                        else g2.setColor(new Color(0x000080));
                    }
                    else g2.setColor(Color.BLACK);
                }
                g2.fillOval(p[i].x-2, SZ-1-p[i].y-2,5,5);
                if (debug) {
                    ch = (i+"").toCharArray();
                    g2.drawChars(ch,0,ch.length, p[i].x+2, SZ-1-p[i].y-2);
                }
            }

            g.drawImage(bi,0,0,SZX+10,SZY+10,null);
      }
      catch (Exception e) { e.printStackTrace(); }
        }
        public Vis() {
            if (manual)
                addMouseListener(this);
            jf.addWindowListener(this);
        }
    // ---------------------------------------------------
        //MouseListener
        public void mouseClicked(MouseEvent e) {
            //identify the buttons or the click on the field
            int x = e.getX(), y = e.getY(), i, j;
            if (x>SZ) {
                //can be only mode modifiers
                if (y >= 8 && y <= 38) {
                    //"SUBMIT"
                    ready = true;
                }
                if (y >= 88 && y <= 118) {
                    //"ADD POLY"
                    String val = validatePoly(Pcur, Ncur);
                    if (val.length()!=0) {
                        System.out.println("Current polygon is invalid: "+val);
                    }
                    else {
                        //actually commit the polygon - find first slot with no poly in it and add it there
                        for (i=0; i<polys.length; i++)
                            if (polysVert[i]==0)
                                break;
                        if (i == polys.length) {
                            System.out.println("Can't have more than "+polys.length+" polygons.");
                        } else {
                            //put current poly to the slot
                            if (debug) System.out.println("Adding current polygon to slot "+i);
                            polysVert[i] = Ncur;
                            valid[i] = true;    //already verified
                            if (polys[i] == null || polys[i].length < Ncur)
                                polys[i] = new int[Ncur];
                            for (j=0; j<Ncur; j++) {
                                polys[i][j] = Pcur[j];
                                used[Pcur[j]] = i;
                            }
                            Ncur = 0;
                        }
                    }
                }
                if (y >= 128 && y <= 158) {
                    //"DEL POLY"
                    //delete the current poly (and unmark used points)
                    if (debug) System.out.println("Deleting current polygon");
                    for (i=0; i<Ncur; i++)
                        used[Pcur[i]] = -2;
                    Ncur = 0;
                }
                draw();
                return;
            }
            //now, the clicks weren't buttons - they were points' locations
            y = SZ-y-1;
            int indP = coordToPoint[x][y], indPoly;
            if (indP==-1)
                return;
            //now process the point
            indPoly = used[indP];

            //three scenarios: adding a point to current poly, removing it or choosing a poly to be edited
            if (Ncur == 0 && indPoly > -1) {
                //this polygon is moved to editing: delete it from the list and free its parameters
                if (debug) System.out.println("Editing polygon "+indPoly);
                Ncur = polysVert[indPoly];
                polysVert[indPoly] = 0;
                valid[indPoly] = false;
                for (i=0; i<Ncur; i++) {
                    Pcur[i] = polys[indPoly][i];
                    polys[indPoly][i] = -1;
                    used[Pcur[i]] = -1;
                }
            } else
                if (Ncur > 0 && indPoly == -1 && Pcur[Ncur-1]==indP) {
                    //this point is last in the polygon - remove it
                    if (debug) System.out.println("Removing point "+indP+" from current polygon");
                    Ncur --;
                    used[indP] = -2;
                    Pcur[Ncur] = -2;
                } else
                    if (indPoly == -2) {
                        if (debug) System.out.println("Adding point "+indP+" to current polygon");
                        Pcur[Ncur] = indP;
                        Ncur++;
                        used[indP] = -1;
                    } else {
                        if (debug) System.out.println("Invalid action");
                       return;
                    }
            
            draw();
        }
        public void mousePressed(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
        //WindowListener
        public void windowClosing(WindowEvent e) { 
            if(proc != null)
                try { proc.destroy(); } 
                catch (Exception ex) { ex.printStackTrace(); }
            System.exit(0); 
        }
        public void windowActivated(WindowEvent e) { }
        public void windowDeactivated(WindowEvent e) { }
        public void windowOpened(WindowEvent e) { }
        public void windowClosed(WindowEvent e) { }
        public void windowIconified(WindowEvent e) { }
        public void windowDeiconified(WindowEvent e) { }
    }
    // ---------------------------------------------------
    public QualityPolygonsVis(String seed) {
        //interface for runTest
        if (vis)
        {   jf = new JFrame();
            v = new Vis();
            jf.getContentPane().add(v);
        }
        if (exec != null) {
            try {
                Runtime rt = Runtime.getRuntime();
                proc = rt.exec(exec);
                os = proc.getOutputStream();
                is = proc.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                new ErrorReader(proc.getErrorStream()).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Score = "+runTest(seed));
        if (proc != null)
            try { proc.destroy(); } 
            catch (Exception e) { e.printStackTrace(); }
    }
    // ---------------------------------------------------
    public static void main(String[] args) {
        String seed = "1";
        vis = false;
        manual = false;
        strict = true;
        for (int i = 0; i<args.length; i++)
        {   if (args[i].equals("-seed"))
                seed = args[++i];
            if (args[i].equals("-exec"))
                exec = args[++i];
            if (args[i].equals("-vis"))
                vis = true;
            if (args[i].equals("-manual"))
                manual = true;
            if (args[i].equals("-debug"))
                debug = true;
            if (args[i].equals("-nostrict"))
                strict = false;
        }
        if (manual)
            vis = true;
        QualityPolygonsVis f = new QualityPolygonsVis(seed);
    }
    // ---------------------------------------------------
    void addFatalError(String message) {
        System.out.println(message);
    }
}

class ErrorReader extends Thread{
    InputStream error;
    public ErrorReader(InputStream is) {
        error = is;
    }
    public void run() {
        try {
            byte[] ch = new byte[50000];
            int read;
            while ((read = error.read(ch)) > 0)
            {   String s = new String(ch,0,read);
                System.out.print(s);
                System.out.flush();
            }
        } catch(Exception e) { }
    }
}
