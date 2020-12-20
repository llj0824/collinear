import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private static final int THREE_COLLINEAR_SLOPES = 3;
    private List<LineSegment> fourPointStraightLineSegments;

    // for each point - sort and transverse
    // sort - nlogn
    // transverse - n
    // n * (nlogn + n) = n^2logn + n^2
    public FastCollinearPoints(Point[] points) {
        final Stopwatch clock = new Stopwatch();
        this.fourPointStraightLineSegments = new ArrayList<>();
        // Corner cases.
        // Throw an IllegalArgumentException if the argument to the constructor is null,
        // if any point in the array is null,
        // or if the argument to the constructor contains a repeated point.
        if (points == null) {
            throw new IllegalArgumentException();
        }

        final int end = points.length;
        for (int start = 0; start < end; start++) {
            final Point originPoint = points[start];
            final Point[] deepCopyOfPoints = Arrays.copyOfRange(points, start, points.length);
            Arrays.sort(deepCopyOfPoints, originPoint.slopeOrder());
            addCollinearSegments(originPoint, deepCopyOfPoints);
        }
        System.out.println("FastCollinearPoints: Done analyzing points. Time elasped: " + clock.elapsedTime());
    }

    private void addCollinearSegments(final Point originPoint, final Point[] pointsSortedBySlopeToOriginPoint) {
        int consecutiveSameSlopes = 0;
        double prevSlope = 0.0;

        for (Point endpt : pointsSortedBySlopeToOriginPoint) {
            final double currentSlope = originPoint.slopeTo(endpt);

            // collinear as previous point
            if (currentSlope == prevSlope) {
                if (consecutiveSameSlopes >= THREE_COLLINEAR_SLOPES) {
                    fourPointStraightLineSegments.add(new LineSegment(originPoint, endpt));
                }
                consecutiveSameSlopes++;
            } else {
                // Non-collinear point
                consecutiveSameSlopes = 0;
            }
            prevSlope = currentSlope;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return fourPointStraightLineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return fourPointStraightLineSegments.toArray(new LineSegment[fourPointStraightLineSegments.size()]);

    }
}
