/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mm71_qualitypolygons;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author edemairy
 */
public class QualityPolygons {

    private static void readData(int[] points, int sidesDiff, int radiiDiff) {
        Scanner scanner = new Scanner(System.in);
        int nbPoints = scanner.nextInt();
        points = new int[nbPoints];
        for (int i = 0; i < nbPoints; ++i) {
            points[2 * i] = scanner.nextInt();
            points[2 * i + 1] = scanner.nextInt();
        }
        sidesDiff = scanner.nextInt();
        radiiDiff = scanner.nextInt();
    }

    static protected Point newCartesianPoint(int x, int y) {
        Point p = new Point( x, y);
        return p;
    }
    
    
    static protected class Point {

        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    };

    

    protected class Polygon {

        private ArrayList<Point> points;
        private int nbPoints;

        Polygon(int[] points) {
            this.nbPoints = points.length;
            this.points = new ArrayList<Point>(points.length);
            for (int i = 0; i < nbPoints; ++i) {
                this.points.set(i, newCartesianPoint(points[2 * i], points[2 * i + 1]));
            }
        }

        boolean isQuality() {
            boolean result = testConvexity()
                    && testNoAlignment()
                    && testLength()
                    && testDistance();

            return result;
        }

        public boolean testConvexity() {
            return true;
        }

        private boolean testNoAlignment() {
            return true;
        }

        private boolean testLength() {
            return true;
        }

        private boolean testDistance() {
            return true;
        }
    };
    private int points[];
    public static int sidesDiff;
    public static int radiiDiff;

    String[] choosePolygons(int[] points, int sidesDiff, int radiiDiff) {
        this.points = points;
        sidesDiff = sidesDiff;
        QualityPolygons.radiiDiff = radiiDiff;

        String[] result = new String[0];
        return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        QualityPolygons job = new QualityPolygons();
        int points[] = null;
        int sidesDiff = 0;
        int radiiDiff = 0;

        readData(points, sidesDiff, radiiDiff);
        job.choosePolygons(points, sidesDiff, radiiDiff);
    }
}
