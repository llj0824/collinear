import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// examines 4 points at a time and checks whether they all lie on the same line segment,
// returning all such line segments. To check whether the 4 points p, q, r, and s are collinear,
// check whether the three slopes between p and q, between p and r, and between p and s are all equal.
// x and y coordinates are between [0 and 32,767]
// finds all line segments containing 4 points
public class BruteCollinearPoints {
    private final static Point NONEXISTENT_POINT = new Point(-1,-1);
    private List<LineSegment> fourPointStraightLineSegments;

    public BruteCollinearPoints(Point[] points) {
        final Stopwatch clock = new Stopwatch();
        this.fourPointStraightLineSegments = new ArrayList<>();
        // Corner cases.
        // Throw an IllegalArgumentException if the argument to the constructor is null,
        // if any point in the array is null,
        // or if the argument to the constructor contains a repeated point.
        throwExceptionIfInvalidPointExists(points);

        final int size = points.length;
        for (int p1 = 0; p1 < size; p1++) {
            for (int p2 = p1 + 1; p2 < size; p2++) {
                for (int p3 = p2 + 1; p3 < size; p3++) {
                    for (int p4 = p3 + 1; p4 < size; p4++) {
                        final Point pt1 = points[p1];
                        final Point pt2 = points[p2];
                        final Point pt3 = points[p3];
                        final Point pt4 = points[p4];
                        double slopeP1_P2 = pt1.slopeTo(pt2);
                        double slopeP1_P3 = pt1.slopeTo(pt3);
                        double slopeP1_P4 = pt1.slopeTo(pt4);
                        if (slopeP1_P2 != slopeP1_P3) {
                            // not collinear - stop searching path.
                            break;
                        } else if (slopeP1_P2 == slopeP1_P3 && slopeP1_P2 == slopeP1_P4) {
                            // collinear four points
                            final Point[] lineSegmentPoints = {pt1, pt2, pt3, pt4};
                            final LineSegment lineSegment = getLongestSegment(lineSegmentPoints);
                            fourPointStraightLineSegments.add(lineSegment);
                        }
                    }
                }
            }
        }

        /* clean segments to not contain segments */

//        System.out.println("BruteForce: Done analyzing points. Time elasped: " + clock.elapsedTime());
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
                duplicatePointChecker = p;
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
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

    private LineSegment getLongestSegment(final Point[] lineSegmentPoints) {
        Arrays.sort(lineSegmentPoints, Point::compareTo);
        return new LineSegment(lineSegmentPoints[0], lineSegmentPoints[lineSegmentPoints.length - 1]);
    }
}
