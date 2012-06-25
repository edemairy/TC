
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author edemairy
 */
public class SynchronousControl {

    public boolean isExit(Location current) {
        return current.getValue() == EXIT;
    }

    public boolean isWall(Location current) {
        return current.getValue() == IMPASSABLE_WALL;
    }

    private void resetDist(RobotLocation aThis) {
        for (int r = 0; r < caveHeight; ++r) {
            Arrays.fill(bestDist[r], Integer.MAX_VALUE);
        }
        bestDist[aThis.row][aThis.col] = 0;
    }

    private int getDist(Location current) {
        return bestDist[current.row][current.col];
    }

    private void updateDist(Location newLocation, Directions opposite, int newDist) {
        bestDir[newLocation.row][newLocation.col] = opposite;
        setDist(newLocation, newDist);
    }
    private static Character EMPTY_CELL = ' ';
    private static Character IMPASSABLE_WALL = '#';
    private static Character EXIT = 'x';
    private static int nbTC;
    private static StringBuilder result = new StringBuilder();

    private void computeBFS() {

        for (int r = 0; r < caveHeight; ++r) {
            Arrays.fill(bestDist[r], -1);
        }
        LinkedList<Location> queue = new LinkedList<Location>(exitLocation);
        for (Location l : queue) {
            setDist(l, 0);
        }
        Location current = null;
        Location newLocation = null;
        forCompute:
        while (!queue.isEmpty()) {
            current = queue.removeLast();

            ford:
            for (Directions d : Directions.values()) {
                try {
                    newLocation = current.add(d);
                    if (getDist(newLocation) != -1) {
                        continue ford;
                    }
                    if ((!isWall(newLocation))) {
                        int newDist = getDist(current) + 1;
                        updateDist(newLocation, getOpposite(d), newDist);
                        queue.addFirst(newLocation);
                        // imposible due to BFS
                        //else if (getDist(newLocation) > (newDist)) {
                        //    updateDist(newLocation, getOpposite(d), newDist);
                        //}
                    }
                    if (isExit(newLocation)) {
                        break forCompute;
                    }
                } catch (Location.OutsideMapException ex) {
                }
            }
        }
    }

    private void setDist(Location l, int i) {
        bestDist[l.row][l.col] = i;
    }

    private Directions getDir(RobotLocation loc) {
        return bestDir[loc.row][loc.col];
    }

    private int score(String f) {
        int result = 0;
        LinkedList<RobotLocation> afterOrders = new LinkedList<RobotLocation>();
        for (RobotLocation loc : robotsLocation) {
            if (loc.isActive()) {
                afterOrders.add(loc.evalOrders(f));
            } else {
                afterOrders.add(loc);
            }
        }
        for (RobotLocation r : afterOrders) {
            result += getDist(r);
        }
        return result;
    }

    public enum Directions {

        N, E, S, W
    };

    public static Directions getOpposite(final Directions d) {
        switch (d) {
            case E:
                return Directions.W;
            case W:
                return Directions.E;
            case N:
                return Directions.S;
            case S:
                return Directions.N;
        }
        return null;
    }

    public class BFS extends LinkedList<Location> {

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("< ");
            for (Location l : this) {
                sb.append("(").append(l.row).append(",").append(l.col).append(")");
            }
            sb.append(" >/n");
            return sb.toString();
        }
    }

    public class Location {

        @Override
        public boolean equals(Object o) {
            Location l = (Location) o;
            return ((row == l.row) && (col == l.col));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + this.row;
            hash = 17 * hash + this.col;
            return hash;
        }

        public Character getValue() {
            return cave[row].charAt(col);
        }

        public class OutsideMapException extends Exception {
        }

        public Location add(Directions d) throws OutsideMapException {
            Location result = new Location(this);
            switch (d) {
                case E:
                    result.col++;
                    break;
                case W:
                    result.col--;
                    break;
                case N:
                    result.row--;
                    break;
                case S:
                    result.row++;
                    break;
            }
            if ((row < 0) || (col < 0) || (row >= caveHeight) || (col >= caveWidth)) {
                throw new OutsideMapException();
            }
            return result;
        }

        public Location(int r, int c) {
            row = r;
            col = c;
        }

        public Location(final Location loc) {
            row = loc.row;
            col = loc.col;
        }
        public int row;
        public int col;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(row).append(",").append(col).append(":").append(getValue()).append(")");
            return sb.toString();
        }
    }
    private static int[][] bestDist;
    private static Directions[][] bestDir;

    public class RobotLocation extends Location {

        private boolean active;

        public RobotLocation(int r, int c, Directions d) {
            super(r, c);
            active = true;
            dir = d;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(row).append(",").append(col).append(',').append(dir.toString()).append(":").append(getValue()).append(")");
            return sb.toString();
        }
        public Directions dir;
        public BFS bfs;

        // move forward only if it is possible.
        public void moveForward(Directions d) throws OutsideMapException {
            Location result = this.add(d);
            if (!isWall(result)) {
                row = result.row;
                col = result.col;
            }
        }

        private void computeBFS() {
        }

        public BFS getBFS() {
            return bfs;
        }

        private boolean isActive() {
            return active;
        }

        private void setActive(boolean newValue) {
            active = newValue;
        }

        private String computeOrders() throws OutsideMapException {
            StringBuilder sb = new StringBuilder();
            RobotLocation current = new RobotLocation(row, col, dir);
            while (!isExit(current)) {
                String o = orders(current, current.add(getDir(current)));
                sb.append(o);
                current.applyOrders(o);
            }
            return sb.toString();
        }

        private String orders(RobotLocation current, Location get) {
            StringBuilder sb = new StringBuilder();
            if (get.row == (current.row + 1)) {
                sb.append(orientate(current.dir, Directions.S));
            } else if (get.row == (current.row - 1)) {
                sb.append(orientate(current.dir, Directions.N));
            } else if (get.col == (current.col + 1)) {
                sb.append(orientate(current.dir, Directions.E));
            } else if (get.col == (current.col - 1)) {
                sb.append(orientate(current.dir, Directions.W));
            }
            sb.append('F');
            return sb.toString();
        }

        private String orientate(Directions current, Directions target) {
            StringBuilder sb = new StringBuilder();

            int dif = (target.ordinal() - current.ordinal() + 4) % 4;
            int signDiff = 1;
            if (dif == 0) {
                return "";
            }
            if (dif == 3) {
                dif = 1;
                signDiff = -1;
            }
            char move;
            if (signDiff > 0) {
                move = 'R';
            } else {
                move = 'L';
            }
            for (int i = 0; i < dif; ++i) {
                sb.append(move);
            }
            return sb.toString();
        }

        public RobotLocation evalOrders(String orders) {
            RobotLocation result = new RobotLocation(row, col, dir);
            result.applyOrders(orders);
            return result;
        }

        private void applyOrders(String orders) {

            for (int i = 0; i < orders.length(); ++i) {
                switch (orders.charAt(i)) {
                    case 'F':
                        try {
                            moveForward(dir);
                        } catch (OutsideMapException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 'R':
                        this.turnRight();
                        break;
                    case 'L':
                        this.turnLeft();
                }
                if (isExit(this)) {
                    setActive(false);
                    break;
                }
            }
        }

        private void turnRight() {
            dir = Directions.values()[((dir.ordinal() + 1) % 4)];
        }

        private void turnLeft() {
            dir = Directions.values()[((dir.ordinal() + 3) % 4)];
        }
    }
    public double V;
    public String[] cave;
    public int caveWidth;
    public int caveHeight;

    public String evacuateAll(String[] cave, double V) {
        this.cave = cave;
        this.V = V;
        caveHeight = cave.length;
        caveWidth = cave[0].length();
        bestDist = new int[caveHeight][caveWidth];
        bestDir = new Directions[caveHeight][caveWidth];
        analyzeMap();
        computeBFS();
        StringBuilder resultBuilder = new StringBuilder();

        int nbActive;
        ordersLoop:
        do {
            int bscore = Integer.MAX_VALUE;
            RobotLocation bloc = null;
            String border = null;
            nbActive = 0;

            for (RobotLocation loc : robotsLocation) {
                if (loc.isActive()) {
                    ++nbActive;
                }
                String order;
                try {
                    order = loc.computeOrders();

                    int s = score(order);
                    if (s < bscore) {
                        bloc = loc;
                        bscore = s;
                        border = order;
                    }
                } catch (Location.OutsideMapException ex) {
                    Logger.getLogger(SynchronousControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (nbActive==0) {
                break ordersLoop;
            }
            resultBuilder.append(border);
            for (RobotLocation loc : robotsLocation) {
                loc.applyOrders(border);
            }
            //                } catch (Location.OutsideMapException ex) {
            //                    System.err.println(ex);
            //                }
        } while (nbActive > 0);//        System.err.println(resultBuilder);
        return resultBuilder.toString();
    }
    private LinkedList<RobotLocation> robotsLocation = new LinkedList<RobotLocation>();
    private LinkedList<Location> exitLocation = new LinkedList<Location>();

    public void analyzeMap() {
        for (int r = 0; r < caveHeight; ++r) {
            for (int c = 0; c < caveWidth; ++c) {
                Character currentChar = cave[r].charAt(c);
                for (Directions d : Directions.values()) {
                    if (currentChar == d.name().charAt(0)) {
                        robotsLocation.add(new RobotLocation(r, c, d));
                    }
                }
                if (currentChar == EXIT) {
                    exitLocation.add(new Location(r, c));
                }
            }
        }
//        System.err.println("exits = " + exitLocation.size());
//        System.err.println("robots = " + robotsLocation.size());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        System.err.println("hello");
        Scanner scanner = new Scanner(System.in);
        int R = scanner.nextInt();
        String[] cave = new String[R];
        for (int i = 0; i < R; i++) {
            cave[i] = scanner.next();
        }
        double V = Double.valueOf(scanner.next());

        SynchronousControl control = new SynchronousControl();
        String ret = control.evacuateAll(cave, V);

        System.out.println(ret);
        System.out.flush();
    }

    private static StringBuilder oneTestCase(BufferedReader reader) throws IOException {
        Formatter formatter = new Formatter(Locale.ENGLISH);
        StringBuilder output = new StringBuilder();
//        for (int i = 0; i < 5; ++i) {
//            formatter.format("%3d", n[i]);
//        }

        output.append(formatter.out());
        return output;
    }
}
