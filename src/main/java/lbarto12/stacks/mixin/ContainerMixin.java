package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lbarto12.stacks.StackConfig;
import net.minecraft.world.Container;

@Mixin(Container.class)
public interface ContainerMixin {
    @Inject(at = @At("RETURN"), method = "getMaxStackSize()I", cancellable = true)
    private void massivestacks$raiseContainerMax(CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() >= 64) {
            cir.setReturnValue(StackConfig.MAX_STACK);
        }
    }
}
