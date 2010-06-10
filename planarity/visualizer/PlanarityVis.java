import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.security.*;
import javax.swing.*;

// ------------- class Point -----------------------------
class P {
    public int x,y;
    public P() {};
    public P(int x1, int y1)
    {   x=x1;    y=y1;    }
}

// ------------- class G2D -------------------------------
class G2D {
    public static P substr(P p1, P p2) {
	return new P(p1.x-p2.x, p1.y-p2.y);
    }
    public static double norm(P p) {
	return Math.sqrt(p.x*p.x + p.y*p.y);
    }
    public static int dot(P p1, P p2) {
	return p1.x*p2.x+p1.y*p2.y;
    }
    public static double dist(P p1, P p2) {
        return norm(substr(p1, p2));
    }
}

// ------------- class Edge ------------------------------
class Edge {
    public P p1,p2,vect;	//vector p1 -> p2
    public double norm;
    public Edge() {};
    public Edge(P p1n, P p2n) {
        p1 = p1n;
        p2 = p2n;
        vect = G2D.substr(p2, p1);
        norm = G2D.norm(vect);
    }
    public Edge(int x1, int y1, int x2, int y2) {
        p1 = new P(x1,y1);
        p2 = new P(x2,y2);
        vect = G2D.substr(p2, p1);
        norm = G2D.norm(vect);
    }
    boolean eq(double a, double b){
      return Math.abs(a-b) < 1e-9;
    }
    // ---------------------------------------------------
    public boolean intersect(Edge other) {
        //do edges "this" and "other" intersect?
        if (Math.min(p1.x,p2.x)>Math.max(other.p1.x,other.p2.x)) return false;
        if (Math.max(p1.x,p2.x)<Math.min(other.p1.x,other.p2.x)) return false;
        if (Math.min(p1.y,p2.y)>Math.max(other.p1.y,other.p2.y)) return false;
        if (Math.max(p1.y,p2.y)<Math.min(other.p1.y,other.p2.y)) return false;

        int den = other.vect.y*vect.x-other.vect.x*vect.y;
        int num1 = other.vect.x*(p1.y-other.p1.y)-other.vect.y*(p1.x-other.p1.x);
        int num2 =       vect.x*(p1.y-other.p1.y)-      vect.y*(p1.x-other.p1.x);

        //parallel edges
        if (den==0)
        {   if (Math.min(other.dist2(this),dist2(other)) > 0)
                return false;
            //on the same line - "not intersect" only if one of the vertices is common,
            //and the other doesn't belong to the line
            if ((this.p1==other.p1 && eq(G2D.dist(this.p2, other.p2) , this.norm + other.norm)) || 
                (this.p1==other.p2 && eq(G2D.dist(this.p2, other.p1) , this.norm + other.norm)) ||
                (this.p2==other.p1 && eq(G2D.dist(this.p1, other.p2) , this.norm + other.norm)) ||
                (this.p2==other.p2 && eq(G2D.dist(this.p1, other.p1) , this.norm + other.norm)))
                return false;
            return true;
        }

        //common vertices
        if (this.p1==other.p1 || this.p1==other.p2 || this.p2==other.p1 || this.p2==other.p2)
            return false;

        double u1 = num1*1./den;
        double u2 = num2*1./den;
        if (u1<0 || u1>1 || u2<0 || u2>1)
            return false;
        return true;
    }
    // ---------------------------------------------------
    public double dist(P p) {
        //distance from p to the edge
        if (G2D.dot(vect, G2D.substr(p, p1))<=0)
            return G2D.dist(p, p1);			//from p to p1
        if (G2D.dot(vect, G2D.substr(p, p2))>=0)
            return G2D.dist(p, p2);			//from p to p2
        //distance to the line itself
        return Math.abs(-vect.y*p.x+vect.x*p.y+p1.x*p2.y-p1.y*p2.x)/norm;
    }
    // ---------------------------------------------------
    public double dist2(Edge other) {
        //distance from the closest of the endpoints of "other" to "this"
        return Math.min(dist(other.p1), dist(other.p2));
    }
}

// ------------- class Planarity itself ------------------
public class PlanarityVis {
    final int SZ = 700;
    int V,E;            //number of vertices and number of edges
    int[] vx,vy;	//current coordinates of vertices (returned or moved manually)
    int[] edges;        //the description of edges
    // ---------------------------------------------------
    void generate(String seed) {
      try {
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
        r.setSeed(Long.parseLong(seed));
        //generate vertices - all at distinct points
        int i,j,k;
        boolean ok;
        V = r.nextInt(91)+10;
        if (seed.equals("1"))
            V=10;
        System.out.println("V = "+V);
        vx = new int[V];
        vy = new int[V];
        for (i=0; i<V; i++)
            do {vx[i] = r.nextInt(SZ);
                vy[i] = r.nextInt(SZ);
                ok=true;
                for (j=0; j<i; j++)
                    if (vx[i]==vx[j] && vy[i]==vy[j])
                    {   ok=false; 
                        break;
                    }
            }
            while (!ok);

        //calculate distances and "values" for each pair of nodes
        P[] vp = new P[V];
        for (i=0; i<V; i++)
            vp[i] = new P(vx[i], vy[i]);
        double alpha = r.nextDouble()*2+1;
        System.out.println("Alpha = "+alpha);
        double[][] val = new double[V][V];
        double sumval=0;
        for (i=0; i<V; i++)
        for (j=i+1; j<V; j++)
        {   val[i][j] = Math.pow(G2D.dist(vp[i], vp[j]),-alpha);
            sumval += val[i][j];
        }

        //choose the vertices
        E = Math.min(V*(V-1)/2,(int)((r.nextDouble() * 3 + 2)*V));
        System.out.println("E = "+E);
        edges = new int[2*E];
        boolean[][] used = new boolean[V][V];
        double rnd;
        for (k=0; k<E; k++)
        {   //choose i-th edge
            rnd = r.nextDouble() * sumval;
            //check which edge is this
            j=1;
            for (i=0; i<V; i++) 
            {   for (j=i+1; j<V; j++)
                    if (!used[i][j])
                    {   rnd -= val[i][j];
                        if (rnd<0) break;
                    }
                if (rnd<0) break;
            }
            used[i][j] = true;
            edges[2*k] = i;
            edges[2*k+1] = j;
            sumval -= val[i][j];
        }
      }
      catch (Exception e) { 
        addFatalError("An exception occurred while generating test case.");
        e.printStackTrace(); 
      }
    }
    // ---------------------------------------------------
    int calcScore() {
        //calculate the score of current layout as the number of intersections between edges
        int i,j,nint=0;
        //we use the number of pairs of edges that intersect (don't care whether intersection points coincide)
        //intersection is having at least one common point, except for common vertice
        P[] vp = new P[V];
        Edge[] es = new Edge[E];
        for (i=0; i<V; i++)
            vp[i] = new P(vx[i], vy[i]);
        for (i=0; i<E; i++)
            es[i] = new Edge(vp[edges[2*i]], vp[edges[2*i+1]]);
        for (i=0; i<E; i++)
        for (j=0; j<i; j++)
            //check whether edges i and j intersect
            if (es[i].intersect(es[j]))
                nint++;
        return nint;
    }
    // ---------------------------------------------------
    public double runTest(String seed) {
      try {
        int i,j;
        generate(seed);

        if (proc!=null)
        {   //get the return and parse it in vx, vy
            int[] ret = untangle(V,edges);
            if (ret.length!=2*V)
            {   addFatalError("Your return contained wrong number of elements.");
                return 0;
            }
            for (i=0; i<2*V; i++)
                if (ret[i]<0 || ret[i]>=SZ)
                {   addFatalError("Each element of your return must be between 0 and "+(SZ-1)+", inclusive.");
                    return 0;
                }
            for (i=0; i<V; i++)
            {   vx[i]=ret[2*i];
                vy[i]=ret[2*i+1];
                for (j=0; j<i; j++)
                    if (vx[i]==vx[j] && vy[i]==vy[j])
                    {   addFatalError("All points of your return must be distinct.");
                        return 0;
                    }
            }
            if (vis)
            {   //draw the image
                imove=-1;
                jf.setSize(SZX+17,SZY+37);
                jf.setVisible(true);
                draw();
            }
        }

        if (manual)
        {   //draw the image
            imove=-1;
            ready=false;
            jf.setSize(SZX+17,SZY+37);
            jf.setVisible(true);
            draw();

            //wait for the result of manual vertices movement
            //don't return it, since it's already stored in vx, vy
            while (!ready)
                try { Thread.sleep(1000);}
                catch (Exception e) { e.printStackTrace(); } 
        }

        return 1+calcScore();
      }
      catch (Exception e) { 
        addFatalError("An exception occurred while trying to get your program's results.");
        e.printStackTrace(); 
        return 0.0;
      }
    }
// ------------- server part -----------------------------
    public String checkData(String test) {
        return "";
    }
    // ---------------------------------------------------
    public String displayTestCase(String test) {
        StringBuffer sb = new StringBuffer();
        sb.append("seed = "+test+"\n");
        generate(test);
        sb.append("V = "+V+"\n");
        sb.append("E = "+E+"\n");
        return sb.toString();
    }
    // ---------------------------------------------------
    public double[] score(double[][] sc) {
        double[] res = new double[sc.length];
        //exotic relative
        int i,j,k;
        for (i=0; i<sc.length; i++)
            res[i]=0;
        if (sc.length<=1)
            return res;
        for (j=0; j<sc[0].length; j++)
        for (i=0; i<sc.length; i++)
            if (sc[i][j]>0)
                for (k=0; k<sc.length; k++)
                {   if (sc[i][j]<sc[k][j])
                        res[i]++;
                    else if (sc[i][j]==sc[k][j] && k!=i)
                        res[i]+=0.5;
                }
        for (i=0; i<sc.length; i++)
            res[i]/=(sc.length-1);
        return res;
    }
// ------------- visualization part ----------------------
    static String exec;
    static boolean vis;
    static boolean manual;
    static Process proc;
    JFrame jf;
    Vis v;
    InputStream is;
    OutputStream os;
    BufferedReader br;
    // problem-specific drawing params
    final int SZX = SZ+2+100,SZY=SZ+3;
    volatile boolean ready;
    volatile int imove, xold, yold;
    // ---------------------------------------------------
    int[] untangle(int V, int[] edges) throws IOException
    {   //pass the params to the solution and get the return
        int i, N;
        StringBuffer sb = new StringBuffer();
        sb.append(V).append('\n');
        sb.append(edges.length).append('\n');
        for (i=0; i<edges.length; i++)
            sb.append(edges[i]).append('\n');
        os.write(sb.toString().getBytes());
        os.flush();
        //get the return
        int[] ret = new int[2*V];
        for (i=0; i<2*V; i++)
            ret[i] = Integer.parseInt(br.readLine());
        return ret;
    }
    // ---------------------------------------------------
    void draw() {
        if (!vis) return;
        v.repaint();
    }
    // ---------------------------------------------------
    public class Vis extends JPanel implements MouseListener, MouseMotionListener, WindowListener {
        public void paint(Graphics g) {
          try {
            //do painting here
            int i;
            //get the size of area occupied by the polymer
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

            //edges
            g2.setColor(new Color(0x808080));
            for (i=0; i<E; i++)
                if (edges[2*i]!=imove && edges[2*i+1]!=imove)
                    g2.drawLine(vx[edges[2*i]]+1, (SZ-1-vy[edges[2*i]])+1, vx[edges[2*i+1]]+1, (SZ-1-vy[edges[2*i+1]])+1);
            
            g2.setColor(Color.RED);
            for (i=0; i<E; i++)
                if (edges[2*i]==imove || edges[2*i+1]==imove)
                    g2.drawLine(vx[edges[2*i]]+1, (SZ-1-vy[edges[2*i]])+1, vx[edges[2*i+1]]+1, (SZ-1-vy[edges[2*i+1]])+1);


            //"ready" "button"
            g2.setColor(Color.BLACK);
            char[] ch = ("READY").toCharArray();
            g2.setFont(new Font("Arial",Font.BOLD,16));
            g2.drawChars(ch,0,ch.length,SZ+25,30);
            g2.drawRect(SZ+12,8,80,30);

            //current number of intersections
            ch = ("Int: "+calcScore()).toCharArray();
            g2.setFont(new Font("Arial",Font.BOLD,14));
            g2.drawChars(ch,0,ch.length,SZ+10,70);

            //vertices
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	    
            for (i=0; i<V; i++)
            {   if (i==imove)
                    g2.setColor(Color.RED);
               else g2.setColor(Color.BLACK);
                g2.fillOval(vx[i]-1, SZ-1-vy[i]-1,5,5);
            }

            g.drawImage(bi,0,0,SZX+10,SZY+10,null);
      }
      catch (Exception e) { e.printStackTrace(); }
        }
        public Vis() {
            if (manual)
            {   addMouseListener(this);
                addMouseMotionListener(this);
            }
            jf.addWindowListener(this);
        }
    // ---------------------------------------------------
        //MouseListener
        public void mouseClicked(MouseEvent e)
        {   //for "ready" state
            int x = e.getX()-SZ-12, y = e.getY()-8;
            if (x>=0 && x<=80 && y>=0 && y<=30)
                ready = true;
        }
        public void mousePressed(MouseEvent e) 
        {   //identify and remember the selected vertice
            int x = e.getX()-1, y = SZ-e.getY();
            if (x>SZ || y>SZ)
                return;
            //find the closest vertice
            imove = -1;
            int mind=65;
            for (int i=0; i<V; i++)
                if ((x-vx[i])*(x-vx[i])+(y-vy[i])*(y-vy[i])<mind)
                {   mind = (x-vx[i])*(x-vx[i])+(y-vy[i])*(y-vy[i]);
                    imove = i;
                }
            if (imove==-1)
                return;
            //vertice identified - remember its coordinates
            xold = vx[imove];
            yold = vy[imove];
        }
        public void mouseReleased(MouseEvent e) 
        {   //finalize the change
            if (imove==-1)	//nothing was moving
                return;
            int x = e.getX()-1, y = SZ-e.getY();
            if (x>SZ-1 || y>SZ-1 || x<0 || y<0)
            {   //cancel the movement
                vx[imove] = xold;
                vy[imove] = yold;
            }
            imove=-1;
            calcScore();
            draw();
        }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
        //MouseMotionListener
        public void mouseDragged(MouseEvent e)
        {   //repaint the moved part of the graph
            if (imove==-1)
                return;
            int x = e.getX()-1, y = SZ-e.getY();
            if (x>SZ-1 || y>SZ-1 || x<0 || y<0)
                //ignore movement
                return;
            vx[imove]=x;
            vy[imove]=y;
            draw();
        }
        public void mouseMoved(MouseEvent e) { }
        //WindowListener
        public void windowClosing(WindowEvent e){ 
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
    public PlanarityVis(String seed) {
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
        for (int i = 0; i<args.length; i++)
        {   if (args[i].equals("-seed"))
                seed = args[++i];
            if (args[i].equals("-exec"))
                exec = args[++i];
            if (args[i].equals("-vis"))
                vis = true;
            if (args[i].equals("-manual"))
                manual = true;
        }
        if (manual)
            vis = true;
        PlanarityVis f = new PlanarityVis(seed);
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
