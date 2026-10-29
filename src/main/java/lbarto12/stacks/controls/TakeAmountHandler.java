package lbarto12.stacks.controls;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class TakeAmountHandler {
    private TakeAmountHandler() {
    }

    public static void handle(ServerPlayer player, TakeAmountPayload payload) {
        AbstractContainerMenu menu = player.containerMenu;

        if (menu.containerId != payload.containerID() || payload.slot() < 0 || payload.slot() >= menu.slots.size()
                || !menu.getCarried().isEmpty())
            return;

        Slot slot = menu.getSlot(payload.slot());
        int amount = Math.min(payload.amount(), slot.getItem().getCount()); // no hack clients?
        if (amount <= 0)
            return;

        ItemStack taken = slot.safeTake(amount, amount, player);
        if (taken.isEmpty())
            return;

        menu.setCarried(taken);
        menu.broadcastChanges();
    }
}
