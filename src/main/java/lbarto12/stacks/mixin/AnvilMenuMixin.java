package lbarto12.stacks.mixin;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import lbarto12.stacks.OriginalStackSize;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    protected AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory,
            ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"))
    private int massivestacks$priceAsSingle(ItemStack stack, Operation<Integer> original) {
        if (stack == this.inputSlots.getItem(AnvilMenu.INPUT_SLOT) && OriginalStackSize.isUnstackable(stack)) {
            return 1;
        }
        return original.call(stack);
    }

    @Inject(at = @At("RETURN"), method = "createResult")
    private void massivestacks$singleResult(CallbackInfo ci) {
        ItemStack input = this.inputSlots.getItem(AnvilMenu.INPUT_SLOT);
        ItemStack result = this.resultSlots.getItem(0);
        if (!result.isEmpty() && input.getCount() > 1 && OriginalStackSize.isUnstackable(input)) {
            this.resultSlots.setItem(0, result.copyWithCount(1));
        }
    }

    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void massivestacks$consumeOne(Container container, int slot, ItemStack stack, Operation<Void> original) {
        ItemStack current = container.getItem(slot);
        if (stack.isEmpty() && current.getCount() > 1 && OriginalStackSize.isUnstackable(current)) {
            current.shrink(1);
            original.call(container, slot, current);
            return;
        }
        original.call(container, slot, stack);
    }
}
