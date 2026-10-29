package lbarto12.stacks;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CountFormatter {
    private static final int MAX_DIGITS = 3;

    private static String calc(int count, int div, String suffix) {
        int decimals = Math.max(0, MAX_DIGITS - Integer.toString(count / div).length());
        BigDecimal scaled = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(div)).setScale(decimals,
                RoundingMode.DOWN);
        return scaled.stripTrailingZeros().toPlainString() + suffix;
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
