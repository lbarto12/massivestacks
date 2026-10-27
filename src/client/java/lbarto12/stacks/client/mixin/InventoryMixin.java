package lbarto12.stacks.client.mixin;

import org.spongepowered.asm.mixin.Mixin;

import lbarto12.stacks.StackConfig;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

@Mixin(Inventory.class)
public abstract class InventoryMixin implements Container {
    @Override
    public int getMaxStackSize() {
        return StackConfig.MAX_STACK;
    }
}
