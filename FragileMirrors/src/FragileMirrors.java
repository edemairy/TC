
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edemairy
 */
public class FragileMirrors {

    public static class Board {

        private char[] board;
        private int N;

        public Board(String[] sboard) {
            N = sboard.length;
            board = new char[N * N];
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; j++) {
                    board[pos(i, j)] = sboard[i].charAt(j);
                }
            }
        }

        public String toString() {
            String result = new String();
            for (int row = 0; row < N; ++row) {
                for (int col = 0; col < N; ++col) {
                    result += board[pos(row, col)];
                }
                result += '\n';
            }
            return result;
        }

        final public int pos(int i, int j) {
            return i * N + j;
        }

        public void resetFrom(Board b) {
            System.arraycopy(b.board, 0, board, 0, N*N);
        }

        public int getWidth() {
            return N;
        }

        public int getHeight() {
            return N;
        }
        private int empty = 0;

        public boolean isEmpty() {
            return empty == N * N;
        }

        public void set(int row, int col, char c) {
            if (board[pos(row, col)] != ' ' && c==' ') empty++;
            board[pos(row, col)] = c;
            
        }

        private char get(int row, int col) {
            return board[pos(row, col)];
        }
    }
    private Board board;
    private Board copyBoard;
    private int height, width;
    protected static int[] NORTH = {-1, 0};
    protected static int[] SOUTH = {1, 0};
    protected static int[] EAST = {0, 1};
    protected static int[] WEST = {0, -1};

    public static class Move {

        private int row, col;
        private int[] direction;

        public Move(int _row, int _col, int[] _direction) {
            row = _row;
            col = _col;
            direction = _direction;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public int[] getDir() {
            return direction;
        }
    }

    public int[] destroy(String[] newBoard) {
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        board = new Board(newBoard);
        height = board.getHeight();
        width = board.getWidth();
        //======================
        copyBoard = new Board(newBoard);


        System.err.println("board initialisation done");
        ArrayList<Move> elementaryMoves = new ArrayList<Move>();
        for (int i = 0; i < width; ++i) {
            elementaryMoves.add(new Move(-1, i, SOUTH));
            elementaryMoves.add(new Move(height, i, NORTH));
        }
        for (int i = 0; i < height; ++i) {
            elementaryMoves.add(new Move(i, -1, EAST));
            elementaryMoves.add(new Move(i, width, WEST));
        }

        System.err.println("elementaryMoves initialisation done");

        ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
        for (Move m : elementaryMoves) {
            ArrayList<Move> newList = new ArrayList<Move>();
            newList.add(m);
            moves.add(newList);
        }
        ArrayList<ArrayList<Move>> newMoves = new ArrayList<ArrayList<Move>>();
        for (ArrayList<Move> listMoves : moves) {
            for (Move m : elementaryMoves) {
                ArrayList<Move> newList = (ArrayList<Move>) listMoves.clone();
                newList.add(m);
                newMoves.add(newList);
            }
        }
        moves = newMoves;
        System.err.println("moves initialisation done");

        while (!board.isEmpty()) {
            bestScore = 0;
            for (ArrayList<Move> listMoves : moves) {
                eval(listMoves);
            }
            for (Move m : bestMove) {
                play(m, board);
                resultList.add(m.getRow());
                resultList.add(m.getCol());
                System.err.println("adding " + m.getRow() + ":" + m.getCol());
                if (board.isEmpty()) {
                    break;
                }
            }
        }
        System.err.println("finished");

        //=======================
        return convert(resultList);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FragileMirrors fm = new FragileMirrors();
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.nextLine();
        String[] board = new String[N];
        for (int i = 0; i < N; i++) {
            board[i] = sc.nextLine();
        }
        int[] ret = fm.destroy(board);

        System.out.println(ret.length);
        for (int i : ret) {
            System.out.println(i);
        }
    }

    private int[] convert(ArrayList<Integer> resultList) {
        int[] result = new int[resultList.size()];
        int pos = 0;
        for (Integer value : resultList) {
            result[pos++] = value;
        }
        return result;
    }
    private int bestScore;
    private ArrayList<Move> bestMove;

    private void resetCopyBoard() {
        int pos = 0;
        copyBoard.resetFrom(board);
    }

    private void eval(ArrayList<Move> listMoves) {
        int erased = 0;
        resetCopyBoard();
        for (Move m : listMoves) {
            erased += play(m, copyBoard);
        }
        if (erased > bestScore) {
            bestScore = erased;
            bestMove = listMoves;
        }
    }

    protected void init(ArrayList<StringBuilder> copyBoard) {
        height = copyBoard.size();
        width = copyBoard.get(0).length();
    }

    protected int play(Move move, Board workBoard) {
        int erased = 0;
        int row = move.getRow();
        int col = move.getCol();
        int width = workBoard.getWidth();
        int height = workBoard.getHeight();
        int[] dir = move.getDir();
        do {
            if (!(row < 0 || row >= height || col < 0 || col >= width)) {
                if (workBoard.get(row, col) == ' ') {
                    // No direction change.
                } else if (workBoard.get(row, col) == 'L') {
                    erased++;
                    if (dir == NORTH) {
                        dir = EAST;
                    } else if (dir == EAST) {
                        dir = NORTH;
                    } else if (dir == SOUTH) {
                        dir = WEST;
                    } else if (dir == WEST) {
                        dir = SOUTH;
                    }
                } else if (workBoard.get(row, col) == 'R') {
                    erased++;
                    if (dir == NORTH) {
                        dir = WEST;
                    } else if (dir == WEST) {
                        dir = NORTH;
                    } else if (dir == SOUTH) {
                        dir = EAST;
                    } else if (dir == EAST) {
                        dir = SOUTH;
                    }
                };
                workBoard.set(row, col, ' ');
            }
            row += dir[0];
            col += dir[1];
        } while (!(row < 0 || row >= height || col < 0 || col >= width));
        return erased;
    }
}
