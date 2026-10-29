package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import lbarto12.stacks.StackConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Unique
    private int massivestacks$quickMoves;

    @Inject(at = @At("HEAD"), method = "doClick")
    private void massivestacks$resetQuickMoves(int slotIndex, int buttonNum, ContainerInput containerInput,
            Player player, CallbackInfo ci) {
        this.massivestacks$quickMoves = 0;
    }

    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;quickMoveStack(Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack massivestacks$capQuickMoves(AbstractContainerMenu menu, Player player, int slotIndex,
            Operation<ItemStack> original) {
        if (++this.massivestacks$quickMoves > StackConfig.MAX_QUICK_MOVES_PER_CLICK) {
            return ItemStack.EMPTY;
        }
        return original.call(menu, player, slotIndex);
    }
}
