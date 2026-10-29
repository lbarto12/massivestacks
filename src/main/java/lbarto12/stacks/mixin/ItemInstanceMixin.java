package lbarto12.stacks.mixin;

import lbarto12.stacks.StackConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public interface ItemInstanceMixin {
    @Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true)
    private void massivestacks$raiseMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (((ItemInstance) (Object) this).get(DataComponents.MAX_DAMAGE) == null) {
            cir.setReturnValue(StackConfig.MAX_STACK);
        }
    }
}
