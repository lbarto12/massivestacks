package lbarto12.stacks;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public final class OriginalStackSize {
    public static boolean isUnstackable(ItemStack stack) {
        return of(stack) == 1;
    }

    public static int of(ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
    }

    private OriginalStackSize() {
    }
}
