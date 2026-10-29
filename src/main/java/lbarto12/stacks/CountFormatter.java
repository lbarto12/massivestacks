package lbarto12.stacks;

import java.text.DecimalFormat;

public final class CountFormatter {

    private static String calc(int count, int div, String suffix) {
        final float result = (float) count / div;
        DecimalFormat format = new DecimalFormat("0.##");
        String s = format.format(result);
        s = (s.length() - (s.contains(".") ? 1 : 0)) > 3 ? s.substring(0, 3) : s;
        s = s.endsWith(".") ? s.replace(".", "") : s;
        return s + suffix;
    }

    public static String format(int count) {
        if (count >= 1_000_000_000)
            return calc(count, 1_000_000_000, "b");
        if (count >= 1_000_000)
            return calc(count, 1_000_000, "m");
        if (count >= 1_000)
            return calc(count, 1_000, "k");
        return Integer.toString(count);
    }

    private CountFormatter() {
    }
}
