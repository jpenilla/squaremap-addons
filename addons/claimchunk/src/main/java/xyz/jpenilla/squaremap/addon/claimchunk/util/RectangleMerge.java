package xyz.jpenilla.squaremap.addon.claimchunk.util;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import xyz.jpenilla.squaremap.addon.claimchunk.data.Claim;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.Polygon;

// https://stackoverflow.com/a/56326866

public final class RectangleMerge {

    public static Polygon getPoly(List<Claim> claims) {
        List<Point> combined = new ArrayList<>();
        for (Claim claim : claims) {
            int x = claim.x() << 4;
            int z = claim.z() << 4;
            List<Point> points = Arrays.asList(
                Point.of(x, z),
                Point.of(x, z + 16),
                Point.of(x + 16, z + 16),
                Point.of(x + 16, z)
            );
            if (combined.isEmpty()) {
                combined = points;
            } else {
                combined = merge(combined, points);
            }
        }
        return Marker.polygon(combined);
    }

    private static List<Point> merge(List<Point> p1, List<Point> p2) {
        Area area = new Area(toShape(p1));
        area.add(new Area(toShape(p2)));
        return toPoints(area);
    }

    private static Shape toShape(List<Point> points) {
        Path2D path = new Path2D.Double();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (i == 0) {
                path.moveTo(p.x(), p.z());
            } else {
                path.lineTo(p.x(), p.z());
            }
        }
        path.closePath();
        return path;
    }

    private static List<Point> toPoints(Shape shape) {
        List<Point> result = new ArrayList<>();
        PathIterator iter = shape.getPathIterator(null, 0.0);
        double[] coords = new double[6];
        while (!iter.isDone()) {
            int segment = iter.currentSegment(coords);
            switch (segment) {
                case PathIterator.SEG_MOVETO, PathIterator.SEG_LINETO -> result.add(Point.of(coords[0], coords[1]));
            }
            iter.next();
        }
        return result;
    }

}

