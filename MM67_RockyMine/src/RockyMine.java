/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author edemairy
 */
public class RockyMine {

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
    public String collectGold(int[] dynamite, int[] effect, int W, int[] gold, int[] rocks, int maxMoves) {
        String result = null;
        return result;
    }
}
