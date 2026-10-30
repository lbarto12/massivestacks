package lbarto12.stacks.config;

import lbarto12.stacks.StackConfig;
import net.minecraft.util.Mth;

public final class StackLimit {
    private static final int NO_SERVER_VALUE = 0;

    private static volatile int local = StackConfig.DEFAULT_MAX_STACK_SIZE;
    private static volatile int server = NO_SERVER_VALUE;

    private StackLimit() {
    }

    public static int get() {
        int serverValue = server;
        return serverValue != NO_SERVER_VALUE ? serverValue : local;
    }

    public static int local() {
        return local;
    }

    public static void setLocal(int maxStackSize) {
        local = clamp(maxStackSize);
    }

    public static boolean hasServerValue() {
        return server != NO_SERVER_VALUE;
    }

    public static void setServer(int maxStackSize) {
        server = clamp(maxStackSize);
    }

    public static void clearServer() {
        server = NO_SERVER_VALUE;
    }

    public static int clamp(int maxStackSize) {
        return Mth.clamp(maxStackSize, StackConfig.MIN_STACK, StackConfig.MAX_STACK);
    }
}
