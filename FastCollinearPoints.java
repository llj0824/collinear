import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final static Point NONEXISTENT_POINT = new Point(-1, -1);
    private static final int FOUR_COLLINEAR_POINTS = 4; // 3 + originPoint = 4
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
        throwExceptionIfInvalidPointExists(points);

        final int end = points.length;
        for (int start = 0; start < end; start++) {
            final Point originPoint = points[start];
            final Point[] deepCopyOfPoints = Arrays.copyOfRange(points, start, points.length);
            Arrays.sort(deepCopyOfPoints, originPoint.slopeOrder());
            addCollinearSegments(originPoint, deepCopyOfPoints);
        }
//        System.out.println("FastCollinearPoints: Done analyzing points. Time elasped: " + clock.elapsedTime());
    }

    private void throwExceptionIfInvalidPointExists(final Point[] pts) {
        try {
            if (pts == null) {
                throw new IllegalArgumentException();
            }
            // sort array to check for duplicates.
            final Point[] copyOfArray = Arrays.copyOf(pts, pts.length);
            Arrays.sort(copyOfArray, Point::compareTo);

            Point duplicatePointChecker = NONEXISTENT_POINT;
            for (Point p : copyOfArray) {
                if (p == null) {
                    throw new IllegalArgumentException();
                } else if (p.compareTo(duplicatePointChecker) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }  catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
    }


    private void addCollinearSegments(final Point originPoint, final Point[] pointsSortedBySlopeToOriginPoint) {
        Point[] sortedPts = pointsSortedBySlopeToOriginPoint;
        List<Point> collinearConsecutivePoints = new ArrayList<>();

        for (int i = 1; i < pointsSortedBySlopeToOriginPoint.length; i++) {
            Point prevPoint = sortedPts[i -1];
            Point curPoint = sortedPts[i];
            Double prevSlope = originPoint.slopeTo(prevPoint);
            Double nextSlope = originPoint.slopeTo(curPoint);

            if (!prevSlope.equals(nextSlope)) {
                if (collinearConsecutivePoints.size() >= FOUR_COLLINEAR_POINTS) {
                    fourPointStraightLineSegments.add(getLongestSegment(collinearConsecutivePoints));
                }
                // not collinear - reset collinear points
                collinearConsecutivePoints = new ArrayList<>(Arrays.asList(originPoint));
            }
            collinearConsecutivePoints.add(curPoint);
        }

        // check end
        if (collinearConsecutivePoints.size() >= FOUR_COLLINEAR_POINTS) {
            fourPointStraightLineSegments.add(getLongestSegment(collinearConsecutivePoints));
        }

    }

    private LineSegment getLongestSegment(List<Point> collinearConsecutivePoints) {
        collinearConsecutivePoints.sort(Point::compareTo);
        Point startPt = collinearConsecutivePoints.get(0);
        Point endPt = collinearConsecutivePoints.get(collinearConsecutivePoints.size() - 1);
        return new LineSegment(startPt, endPt);

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
