package lbarto12.stacks.client.mixin;

import lbarto12.stacks.StackConfig;
import net.minecraft.world.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public interface ItemInstanceMixin {
    @Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true)
    private void massivestacks$raiseMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() > 1) {
            cir.setReturnValue(StackConfig.MAX_STACK);
        }
    }
}
