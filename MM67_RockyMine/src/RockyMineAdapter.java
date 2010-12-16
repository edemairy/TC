
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author edemairy
 */
public class RockyMineAdapter {

    public static void main(String[] args) {
        RockyMine mine = new RockyMine();
        Scanner in = new Scanner(System.in);
        int D = Integer.decode(in.nextLine());
        int[] dynamite = new int[D];
        int[] effect = new int[25 * D];
        for (int i = 0; i < D; i++) {
            dynamite[i] = Integer.decode(in.nextLine());
        }
        for (int i = 0; i < 25 * D; i++) {
            effect[i] = Integer.decode(in.nextLine());
        }
        int W = Integer.decode(in.nextLine());
        int H = Integer.decode(in.nextLine()) / W;
        int[] gold = new int[W * H];
        for (int i = 0; i < W * H; i++) {
            gold[i] = Integer.decode(in.nextLine());
        }
        int[] rock = new int[W * H];
        for (int i = 0; i < W * H; i++) {
            rock[i] = Integer.decode(in.nextLine());
        }
        int maxMoves = Integer.decode(in.nextLine());
        String result = mine.collectGold(dynamite, effect, W, gold, rock, maxMoves);
        System.out.println(result);
        System.out.flush();
    }
}
