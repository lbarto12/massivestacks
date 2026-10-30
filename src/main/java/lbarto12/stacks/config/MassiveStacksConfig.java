package lbarto12.stacks.config;

import lbarto12.stacks.StackConfig;

public class MassiveStacksConfig {
    public int maxStackSize = StackConfig.DEFAULT_MAX_STACK_SIZE;

    public MassiveStacksConfig() {
    }

    public MassiveStacksConfig(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }
}
