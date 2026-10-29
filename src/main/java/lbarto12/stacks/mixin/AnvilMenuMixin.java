package lbarto12.stacks.mixin;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lbarto12.stacks.OriginalStackSize;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow
    private int repairItemCountCost;
    @Shadow
    @Final
    private DataSlot cost;
    @Shadow
    private boolean onlyRenaming;

    protected AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory,
            ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    @Inject(at = @At("RETURN"), method = "createResult")
    private void massivestacks$blockStackedInput(CallbackInfo ci) {
        ItemStack input = this.inputSlots.getItem(AnvilMenu.INPUT_SLOT);
        if (input.getCount() > 1 && OriginalStackSize.isUnstackable(input)) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        }
    }

    @Inject(at = @At("HEAD"), method = "onTake")
    private void massivestacks$consumeOneAddition(Player player, ItemStack carried, CallbackInfo ci) {
        ItemStack addition = this.inputSlots.getItem(AnvilMenu.ADDITIONAL_SLOT);
        if (this.repairItemCountCost == 0 && !this.onlyRenaming && addition.getCount() > 1 && OriginalStackSize.isUnstackable(addition)) {
            this.repairItemCountCost = 1;
        }
    }
}
