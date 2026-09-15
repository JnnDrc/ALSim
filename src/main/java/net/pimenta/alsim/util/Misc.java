package net.pimenta.alsim.util;

import javafx.scene.paint.Color;

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

    public static double parseValue(String text){
        char suffix = text.charAt(text.length()-1);
        String withoutLast = new StringBuilder(text).deleteCharAt(text.length() - 1).toString();

        return switch (suffix) {
            case 'k' -> Double.parseDouble(withoutLast) * 1e3;
            case 'm' -> Double.parseDouble(withoutLast) * 1e-3;
            case 'u' -> Double.parseDouble(withoutLast) * 1e-6;
            case 'n' -> Double.parseDouble(withoutLast) * 1e-9;
            case 'p' -> Double.parseDouble(withoutLast) * 1e-12;
            default -> Double.parseDouble(text);
        };
    }

}
