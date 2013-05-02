/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author edemairy
 */
public class FragileMirrorsTest {
    
    public FragileMirrorsTest() {
    }

    /**
     * Test of destroy method, of class FragileMirrors.
     */
    @Test
    public void testDestroy() {
        FragileMirrors fm = new FragileMirrors();
        String[] newBoard = {"LLLR", "RLRR", "LLLL", "RRRL"};
        int[] result = fm.destroy(newBoard);
        for (int i=0; i<result.length / 2; i++) {
            System.out.println(result[2*i]+":"+result[2*i+1]);
        }
    }

    
    @Test
    public void testPlay1() {
        FragileMirrors fm = new FragileMirrors();
        String[] newBoard = {"LLLR", "RLRR", "LLLL", "RRRL"};
        ArrayList<StringBuilder> board = new ArrayList<StringBuilder>();
        for (String s: newBoard){
            board.add(new StringBuilder(s));
        }
        fm.init(board);
        int result = fm.play(4,2,FragileMirrors.NORTH,board);
        Assert.assertEquals(6, result);
    }
    /**
     * Test of main method, of class FragileMirrors.
     */
    @Test
    public void testMain() {
    }
}