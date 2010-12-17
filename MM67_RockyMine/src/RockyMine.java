
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edemairy
 */
public class RockyMine {

    private class Location {

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
        private int x;
        private int y;

        /**
         * @return the x
         */
        public int getX() {
            return x;
        }

        /**
         * @param x the x to set
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * @return the y
         */
        public int getY() {
            return y;
        }

        /**
         * @param y the y to set
         */
        public void setY(int y) {
            this.y = y;
        }

        public void add(int dx, int dy) {
            x += dx;
            y += dy;
        }
    }

    /**
     *   Represent the state of one turn of the game, i.e.:
     *   - the location of the digger;
     *   - the map of the rocks;
     *   - the map of the gold.
     *   - the score, i.e. how much gold has been collected.
     */
    private class TurnState {

        private int[] rocks;
        private int[] gold;
        private Location location;
        private int score;
        private Location[] countdown;

        public TurnState(int[] rocks, int[] gold, Location location, int score, Location[] countdown) {
            this.rocks = rocks;
            this.gold = gold;
            this.location = location;
            this.score = score;
        }

        /**
         * @return the rocks
         */
        public int[] getRocks() {
            return rocks;
        }

        /**
         * @param rocks the rocks to set
         */
        public void setRocks(int[] rocks) {
            this.rocks = rocks;
        }

        /**
         * @return the gold
         */
        public int[] getGold() {
            return gold;
        }

        /**
         * @param gold the gold to set
         */
        public void setGold(int[] gold) {
            this.gold = gold;
        }

        /**
         * @return the location
         */
        public Location getLocation() {
            return location;
        }

        /**
         * @param location the location to set
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * @return the score
         */
        public int getScore() {
            return score;
        }

        /**
         * @param score the score to set
         */
        public void setScore(int score) {
            this.score = score;
        }

        /**
         * @return the countdown
         */
        public Location[] getCountdown() {
            return countdown;
        }

        /**
         * @param countdown the countdown to set
         */
        public void setCountdown(Location[] countdown) {
            this.countdown = countdown;
        }
    }
    public int width;
    private int height;

    private class Graph {

        private TurnState initState;
        private Map<TurnState, Map<Character, TurnState>> nextMoves;


        public Graph(TurnState initState) {
            this.initState = initState;
            this.nextMoves = new HashMap<TurnState, Map<Character, TurnState>>();
        }

        public void addMove(TurnState start, char move, TurnState end) {
            if ( nextMoves.containsKey(start)) {
                nextMoves.get(start).put(move, end);
            } else {
                nextMoves.put(start, new HashMap<Character, TurnState>() );
            }
        }
        
        public char computeNextMoves(TurnState state) {
            TurnState result = state;
            Location curPos = state.getLocation();

          //  testIfValidMove(state, '-'); // Moving North
            testIfValidMove(state, 'N'); // Moving North
            testIfValidMove(state, 'E'); // Moving North
            testIfValidMove(state, 'W'); // Moving North
            testIfValidMove(state, 'S'); // Moving North

            Map<Character, TurnState> possibleMoves = nextMoves.get(state);
      
//            int choice = (int) (Math.random() * possibleMoves.size());
            return possibleMoves.keySet().iterator().next();
        }

        public void testIfValidMove(TurnState state, char move) {
            // 0. test if the location is valid.
            Location curLoc = state.getLocation();
            Location nextLoc = curLoc;
            if (move == 'N') nextLoc.add(-1,0);
            if (move == 'S') nextLoc.add(1,0);
            if (move == 'W') nextLoc.add(0,-1);
            if (move == 'S') nextLoc.add(0,1);
            if  ( (nextLoc.getX()<0) ||
                    (nextLoc.getY()<0) ||
                    (nextLoc.getX()>=height) ||
                    (nextLoc.getY()>=width)) {
                return;
            }

            // 0bis. check there is enough dynamite.
            // 1. test if there is no explosion harmful
            // 2. if no problem, add the move with the score updated.

            TurnState stateAfter = state;
            stateAfter.setLocation(nextLoc);
            addMove(state, move, stateAfter);
        }
    }

    /**
    @param dynamite describes the set of dynamite cartridges you have when entering the mine: dynamite[k] is the number of dynamite cartridges of type k you have before the first move. You can figure out the number of types of dynamite D as the number of elements in dynamite.
    @param effect describes the effect of exploding one unit of dynamite of each type: effect[i*5+j+25*k] (for i=0..4, j=0..4, k=0..D-1) is the number of layers of rocks that will be destroyed in cell (row-2+i,col-2+j) after using one dynamite cartridge of type k at cell (row,col). If cell (row-2+i, col-2+j) is outside the map, the effect at this cell is ignored. For example, array {3,2,0,2,3,2,4,1,4,2,0,1,0,1,0,2,4,1,4,2,3,2,0,2,3} describes the damage of one type of cartridges which goes as follows:
    3 2 0 2 3
    2 4 1 4 2
    0 1 0 1 0
    2 4 1 4 2
    3 2 0 2 3
    @param W is the width of the mine. You can figure out the height of the mine H as gold.length/W.
    @param gold and rocks describe maps of gold and rocks in the mine: gold[row*W+col] is the value of the piece of gold in cell (row,col), and rock[row*W+col] is the number of layers of rocks in this cell. Cells with row = 0, H-1 or col = 0, W-1 will contain neither gold nor rocks.
    @param maxMoves is the maximum number of moves you are allowed to make.
     */
    public String collectGold(int[] dynamite, int[] effect, int W,
            int[] gold, int[] rocks, int maxMoves) {
        this.width = W;
        this.height = gold.length/W;
        String result = new String();
        TurnState initState = new TurnState(rocks, gold, new Location(0, 0), 0, new Location[0]);
        Graph graph = new Graph(initState);
        TurnState currentState = initState;
        int movesDone = 0;
        while (movesDone < maxMoves) {
            result += graph.computeNextMoves(currentState);
            movesDone++;
        }
        return result;
    }
}
