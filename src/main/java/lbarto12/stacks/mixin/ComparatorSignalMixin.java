package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import lbarto12.stacks.OriginalStackSize;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

@Mixin(AbstractContainerMenu.class)
public abstract class ComparatorSignalMixin {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I"), method = "getRedstoneSignalFromContainer")
    private static int massivestacks$vanillaCapacity(Container Container, ItemStack stack,
            Operation<Integer> original) {
        return Math.min(original.call(Container, stack), OriginalStackSize.of(stack));
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"), method = "getRedstoneSignalFromContainer")
    private static int massivestacks$cappedCount(ItemStack stack, Operation<Integer> original) {
        return Math.min(original.call(stack), OriginalStackSize.of(stack));
    }
}
