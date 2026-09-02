package net.pimenta.alsim.util;

public class Misc {
     public static double distToSegment(
            double px, double py,
            double ax, double ay,
            double bx, double by) {

        double dx = bx - ax;
        double dy = by - ay;

        double lengthSquared = dx * dx + dy * dy;

        if (lengthSquared == 0)
            return Math.hypot(px - ax, py - ay);

        double t = ((px - ax) * dx + (py - ay) * dy)
                / lengthSquared;

        t = Math.clamp(t, 0.0, 1.0);

        double closestX = ax + t * dx;
        double closestY = ay + t * dy;

        return Math.hypot(px - closestX, py - closestY);
    }
}
