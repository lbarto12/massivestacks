package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lbarto12.stacks.config.StackLimit;
import net.minecraft.world.inventory.Slot;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Inject(at = @At("RETURN"), method = "getMaxStackSize()I", cancellable = true)
    private void massivestacks$raiseSlotMax(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(StackLimit.get());
    }
}
