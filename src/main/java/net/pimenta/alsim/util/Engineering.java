package net.pimenta.alsim.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Engineering {
    private static final Pattern VALUE_PATTERN = Pattern.compile(
            "^([+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?)" +
                    "(?:\\s*([pnuµmkKMGT]))?" +
                    "(?:\\s*(Ω|ohm|ohms|V|v|A|a|W|w|F|f|H|h))?$"
    );

    private static double prefixMultiplier(String prefix) {
        return switch (prefix) {
            case "p" -> 1e-12;
            case "n" -> 1e-9;
            case "u", "µ" -> 1e-6;
            case "m" -> 1e-3;
            case "k", "K" -> 1e3;
            case "M" -> 1e6;
            case "G" -> 1e9;
            case "T" -> 1e12;
            default -> throw new IllegalArgumentException(
                    "Unknown prefix: " + prefix
            );
        };
    }

    public static double parse(String text){
        if (text == null)
            throw new IllegalArgumentException("Null value");
        Matcher matcher = VALUE_PATTERN.matcher(text.trim());
        if(!matcher.matches())
            throw new IllegalArgumentException("Invalid enginnering value: " + text);

        double value = Double.parseDouble(matcher.group(1));
        String prefix = matcher.group(2);
        if(prefix != null)
            value *= prefixMultiplier(prefix);

        return value;
    }

    public static String format(double value, String unit){
        if(value == 0.0) return "0" + unit;

        double abs = Math.abs(value);

        String prefix;
        double scaled;

        if(abs >= 1e12){
            prefix = "T";
            scaled = value / 1e12;
        } else if (abs >= 1e9) {
            prefix = "G";
            scaled = value / 1e9;
        } else if (abs >= 1e6) {
            prefix = "M";
            scaled = value / 1e6;
        } else if (abs >= 1e3) {
            prefix = "k";
            scaled = value / 1e3;
        } else if (abs >= 1.0) {
            prefix = "";
            scaled = value;
        } else if (abs >= 1e-3) {
            prefix = "m";
            scaled = value / 1e-3;
        } else if (abs >= 1e-6) {
            prefix = "u";
            scaled = value / 1e-6;
        } else if (abs >= 1e-9) {
            prefix = "n";
            scaled = value / 1e-9;
        } else if (abs >= 1e-12) {
            prefix = "p";
            scaled = value / 1e-12;
        } else {
            return String.format(java.util.Locale.ROOT, "%.3e %s", value, unit);
        }
        String number = BigDecimal.valueOf(scaled).
                setScale(3,RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
        return number + " " + prefix + unit;
    }

}
