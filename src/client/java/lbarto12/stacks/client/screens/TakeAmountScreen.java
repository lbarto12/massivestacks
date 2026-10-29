package lbarto12.stacks.client.screens;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import lbarto12.stacks.controls.TakeAmountPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;

// modal for item quantity selection
public class TakeAmountScreen extends Screen {
    private final Screen parent;
    private final int containerID;
    private final int slotIdx;
    private final int maxStack;
    private EditBox modal;

    public TakeAmountScreen(Screen parent, int containerID, int slotIdx, int maxStack) {
        super(Component.literal("take items"));
        this.parent = parent;
        this.containerID = containerID;
        this.slotIdx = slotIdx;
        this.maxStack = maxStack;
    }

    @Override
    protected void init() {
        int cx = this.width / 2, cy = this.height / 2;
        this.modal = new EditBox(this.font, cx - 50, cy - 20, 100, 200, Component.literal("amount"));
        this.modal.setMaxLength(10);
        this.modal.setHint(Component.literal("max: " + this.maxStack));
        this.modal.setResponder(text -> {
            String digits = text.replaceAll("[^0-9]", "");
            if (!digits.equals(text)) {
                this.modal.setValue(digits);
            }
        });
        this.addRenderableWidget(this.modal);
        this.setInitialFocus(this.modal);
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_OK, c -> this.confirm()).bounds(cx - 50, cy + 5, 48, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, c -> this.onClose())
                .bounds(cx + 2, cy + 5, 48, 20).build());
    }

    private void confirm() {
        int amount;
        try {
            amount = Integer.parseInt(this.modal.getValue());
        } catch (NumberFormatException e) { // u fucked up
            amount = 0;
        }

        amount = Math.min(amount, this.maxStack);
        if (amount > 0) {
            ClientPlayNetworking.send(new TakeAmountPayload(this.containerID, this.slotIdx, amount));
        }
        this.onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
