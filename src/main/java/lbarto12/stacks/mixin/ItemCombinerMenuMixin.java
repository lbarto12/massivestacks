package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin {
    @Shadow
    public abstract int getResultSlot();

    @Inject(at = @At("HEAD"), method = "quickMoveStack", cancellable = true)
    private void massivestacks$stopWhenUnaffordable(Player player, int slotIndex,
            CallbackInfoReturnable<ItemStack> cir) {
        if (slotIndex == this.getResultSlot()
                && !((ItemCombinerMenu) (Object) this).getSlot(slotIndex).mayPickup(player)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
