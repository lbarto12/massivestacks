package lbarto12.stacks.client.mixin;

import org.spongepowered.asm.mixin.Mixin;

import lbarto12.stacks.StackConfig;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemInstance {
    @Override
    public int getMaxStackSize() {
        int vanilla = ItemInstance.super.getMaxStackSize();
        return vanilla > 1 ? StackConfig.MAX_STACK : vanilla;
    }
}
