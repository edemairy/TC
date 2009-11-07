import java.util.*;
import java.io.*;
import java.security.*;
public class Tester{
  String[] missions, availableResources, requiredResources;
  boolean[] consumable;
  double[] mass;
  double[] vol;
  double P, C;
  double[][] best, worst;
  double[] getBest(int mid){
    return best[mid];
  }
  double[] getWorst(int mid){
    return worst[mid];
  }
  void parseResources(String[] res){
    consumable = new boolean[10000];
    mass = new double[10000];
    vol = new double[10000];
    for(int i = 0; i<res.length ;i++){
      String[] sp = res[i].split(" ");
      int rid = Integer.parseInt(sp[0].substring(1));
      consumable[rid] = sp[1].equals("1");
      mass[rid] = Double.parseDouble(sp[2]);
      vol[rid] = Double.parseDouble(sp[3]);
    }
  }
  void parseEvents(String[] req){
    best = new double[10000][];
    worst = new double[10000][];
    double[] tmp = new double[10000];
    int ptr = 0, prev = -1;
    for(int i = 0; i<req.length; i++){
      String[] sp = req[i].split(" ");
      int mid = Integer.parseInt(sp[0].substring(2));
      int rid = Integer.parseInt(sp[1].substring(1));
      if(mid != prev && prev !=-1){
        best[prev] = new double[ptr/3*2];
        worst[prev] = new double[ptr/3*2];
        for(int j = 0; j<ptr; j+=3){
          best[prev][j/3*2] = tmp[j];
          worst[prev][j/3*2] = tmp[j];
          best[prev][j/3*2+1] = tmp[j+1];
          worst[prev][j/3*2+1] = tmp[j+2];
        }
        ptr = 0;
      }
      prev = mid;
      tmp[ptr++] = rid;
      tmp[ptr++] = Double.parseDouble(sp[2]);
      tmp[ptr++] = Double.parseDouble(sp[3]);
    }
    best[prev] = new double[ptr/3*2];
    worst[prev] = new double[ptr/3*2];
    for(int j = 0; j<ptr; j+=3){
      best[prev][j/3*2] = tmp[j];
      worst[prev][j/3*2] = tmp[j];
      best[prev][j/3*2+1] = tmp[j+1];
      worst[prev][j/3*2+1] = tmp[j+2];
    }
  }
  int eval(double[] mk, ArrayList<String[]> mission){
    int evacs = 0;
    for(int i = 0; i<mission.size(); i++){//iterate events
      String[] s = mission.get(i);
      int mid = Integer.parseInt(s[2].substring(2));
      boolean supplied = true;
      double[] req;
      if(s[3].equals("1")){//worst case
        req = getWorst(mid);
      }else{//best case
        req = getBest(mid);
      }
      if(req == null){
        //5170 requires no resources
        req = new double[0];
      }
      for(int j = 0; j<req.length; j+=2){
        int rid = (int)req[j];
        double cnt = req[j+1];
        if(mk[rid] + 1e-12 < cnt){
          supplied = false;
        }
      }
      if(supplied){
        for(int j = 0; j<req.length; j+=2){
          int rid = (int)req[j];
          double cnt = req[j+1];
          if(consumable[rid]){
            mk[rid] -= cnt;
          }
        }
        evacs += Integer.parseInt(s[4]);
      }else{
        evacs += Integer.parseInt(s[5]);
      }
    }
    return evacs;
  }
  String[] readFile(String fn) throws IOException{
    BufferedReader br = new BufferedReader(new FileReader(fn));
    String s;
    ArrayList al = new ArrayList();
    while((s = br.readLine())!=null){
      al.add(s);
    }
    return (String[])al.toArray(new String[0]);
  }
  void readFiles(){
    try{
      missions= readFile("train.txt");
      parseResources(availableResources = readFile("resources.txt"));
      parseEvents(requiredResources = readFile("events.txt"));
    }catch(Exception e){
      System.err.println("Failed to read data files.  Please ensure that you have downloaded them to the correct directory.");
    }
  }
  void append(StringBuffer sb, String[] s){
    sb.append(s.length).append('\n');
    for(int i = 0; i<s.length; i++){
      sb.append(s[i]).append('\n');
    }
  }
  String[] getMedkit(String[] availableResources, String[] requiredResources, String[] missions, double P, double C){
    try {
      Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(exec);
      OutputStream os = proc.getOutputStream();
      InputStream is = proc.getInputStream();
      new ErrorReader(proc.getErrorStream()).start();

      StringBuffer sb = new StringBuffer();
      append(sb,availableResources);
      append(sb,requiredResources);
      append(sb,missions);
      sb.append(P).append('\n');
      sb.append(C).append('\n');
      os.write(sb.toString().getBytes());

      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      int N = Integer.parseInt(br.readLine().trim());
      String[] ret = new String[N];
      for(int i = 0; i<N; i++)
        ret[i] = br.readLine().trim();
      return ret;

    } catch (Exception e) {
      System.err.println("An error occurred while executing your program");
      e.printStackTrace();
      return null;
    }
  }

  String exec;
  public double runTest(String exec, String seed){
    try{
      this.exec = exec;
      readFiles();
      Random r;
      try{
        r = SecureRandom.getInstance("SHA1PRNG");
        r.setSeed(Long.parseLong(seed));
      }catch(Exception e){
        return -1;
      }
      P = r.nextDouble() * 0.04 + 0.01;
      C = r.nextDouble() * 1e-3;
      String[] medkit = getMedkit(availableResources, requiredResources, missions, P, C);
      if(medkit == null){
        System.err.println("Got null");
        return 0;
      }
      double[] mk = new double[10000];
      for(int i = 0; i<medkit.length; i++){
        String[] sp = medkit[i].split(" ");
        if(sp.length != 2){
          System.err.println("Invalid return.  Element not formatted correctly: "+medkit[i]);
          return 0;
        }
        try{
          int rid = Integer.parseInt(sp[0].substring(1));
          double cnt = Double.parseDouble(sp[1]);
          if(cnt < 0 || Double.isNaN(cnt) || Double.isInfinite(cnt)){
            System.err.println("Your return contained an invalid double");
            return 0;
          }
          mk[rid] += cnt;
        }catch(Exception e){
          System.err.println("Invalid return.  Element not formatted correctly: "+medkit[i]);
          return 0;
        }
      }
      String[] sample = missions;
      int[] used = new int[100000];
      ArrayList<String[]> al[] = new ArrayList[10000];
      Arrays.fill(used,-1);
      for(int i = 0; i<10000; i++){
        al[i] = new ArrayList();
        int j = r.nextInt(used.length);
        while(used[j] != -1){
          j = r.nextInt(used.length);
        }
        used[j] = i;
      }
      for(int i = 0; i<sample.length; i++){
        String[] sp = sample[i].split(" ");
        int mid = Integer.parseInt(sp[0]);
        if(used[mid-1] != -1){
          al[used[mid-1]].add(sp);
        }
      }
      int evac = 0;
      for(int i = 0; i<10000; i++){
        double[] m = (double[])mk.clone();
        evac += eval(m,al[i]);
      }
      System.err.println("Total evacuations: "+evac+"\n");
      if(evac <= P * 10000){
        double score = 0;
        double m = 0, v = 0;
        for(int i = 0; i<mk.length; i++){
          m += mass[i] * mk[i];
          v += vol[i] * mk[i];
        }
        score = C * v + m;
        System.out.println("Total mass: "+m+"\n");
        System.out.println("Total volume: "+v+"\n");
        return 1000/score;
      }else {
        System.out.println("Evacutions exceeded allowed rate");
        return 0;
      }

    }catch(Exception e){
      System.err.println(e.toString()+"\n");
      StackTraceElement[] ste = e.getStackTrace();
      for(int i = 0; i<ste.length; i++)
        System.err.println(ste[i]+"\n");
      return -1;
    }
  }
  public static void main(String[] args){
    Tester t = new Tester();
    System.out.println("Score: "+t.runTest(args[0],args[1]));
    System.exit(0);
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

