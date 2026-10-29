package lbarto12.stacks;

public final class CountFormatter {
    public static String format(int count) {
        if (count >= 1_000_000_000)
            return (count / 1_000_000_000) + "b";
        if (count >= 1_000_000)
            return (count / 1_000_000) + "m";
        if (count >= 1_000)
            return (count / 1_000) + "k";
        return Integer.toString(count);
    }

    private CountFormatter() {
    }
}
