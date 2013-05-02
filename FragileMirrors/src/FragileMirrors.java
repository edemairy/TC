
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

    private ArrayList<StringBuilder> board = new ArrayList<StringBuilder>();
    private ArrayList<StringBuilder> copyBoard = new ArrayList<StringBuilder>();
    
    private int height, width;
    protected static int[] NORTH = {-1, 0};
    protected static int[] SOUTH = {1, 0};
    protected static int[] EAST = {0, 1};
    protected static int[] WEST = {0, -1};

    public int[] destroy(String[] newBoard) {
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for (String b : newBoard) {
            board.add(new StringBuilder(b));
            copyBoard.add(new StringBuilder());            
        }
        height = board.size();
        width = board.get(0).length();
        //======================


        
        
        while (!isBoardEmpty()) {
            bestScore = 0;
            for (int i = 0; i < width; ++i) {
                eval(-1, i, SOUTH);
                eval(height, i, NORTH);
            }
            for (int i = 0; i < height; ++i) {
                eval( i, -1, EAST);
                eval( i, width, WEST);
            }
            play(bestPos[0], bestPos[1], bestDir, board);
            resultList.add(bestPos[0]);
            resultList.add(bestPos[1]);
        }

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

    private boolean isBoardEmpty() {
        for (StringBuilder b : board) {
            for (char c : b.toString().toCharArray()) {
                if (c != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    
    private int bestScore;
    private int[] bestPos = {-100, -100};
    private int[] bestDir = {-1, -1};
    
    private void resetCopyBoard() {
        int pos = 0;
        for (StringBuilder s: board) {
            copyBoard.get(pos).replace(0, width, s.toString());
            pos++;
        }
    }
    
    private void eval(int row, int col, int[] dir) {
        resetCopyBoard();
        int erased = play(row, col, dir, copyBoard);
        if (erased > bestScore) {
            bestScore = erased;
            bestPos[0] = row;
            bestPos[1] = col;
            bestDir = dir;            
        }
    }

    protected void init(ArrayList<StringBuilder> copyBoard) {
        height = copyBoard.size();
        width = copyBoard.get(0).length();
    }
    
    protected int play(int row, int col, int[] dir, ArrayList<StringBuilder> workBoard) {
        int erased = 0;
        do {
            if (!(row < 0 || row >= height || col < 0 || col >= width)) {
                if (workBoard.get(row).charAt(col) == ' ') {
                    // No direction change.
                } else if (workBoard.get(row).charAt(col) == 'L') {
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
                } else if (workBoard.get(row).charAt(col) == 'R') {
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
                workBoard.get(row).setCharAt(col, ' ');
            }
            row += dir[0];
            col += dir[1];
        } while (! (row < 0 || row >= height || col < 0 || col >= width));
        return erased;
    }
}
