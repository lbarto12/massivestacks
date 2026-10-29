package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lbarto12.stacks.crafting.BatchCraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@Mixin({ CraftingMenu.class, InventoryMenu.class })
public abstract class CraftingMenusMixin extends AbstractCraftingMenu {
    protected CraftingMenusMixin(MenuType<?> menuType, int containerId, int width, int height) {
        super(menuType, containerId, width, height);
    }

    @Inject(at = @At("HEAD"), method = "quickMoveStack", cancellable = true)
    private void massivestacks$batchCraft(Player player, int slotIndex, CallbackInfoReturnable<ItemStack> cir) {
        if (slotIndex != this.getResultSlot().index) {
            return;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            BatchCraft.craft(serverPlayer, this.craftSlots, this.resultSlots);
        }
        cir.setReturnValue(ItemStack.EMPTY);
    }
}
