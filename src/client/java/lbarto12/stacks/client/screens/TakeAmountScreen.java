package lbarto12.stacks.client.screens;

import lbarto12.stacks.client.mixin.AbstractContainerScreenAccessor;
import lbarto12.stacks.controls.TakeAmountPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

// modal for item quantity selection
public class TakeAmountScreen extends Screen {
    private static final Identifier PANEL_SPRITE = Identifier.withDefaultNamespace("popup/background");
    private static final int PANEL_WIDTH = 184;
    private static final int PANEL_HEIGHT = 144;
    private static final int PADDING = 12;
    private static final int ROW_HEIGHT = 20;
    private static final int GAP = 4;
    private static final int FIELD_Y = 36;
    private static final int SLIDER_Y = 60;
    private static final int QUICK_PICK_Y = 84;
    private static final int ACTIONS_Y = 112;
    private static final int ICON_TEXT_OFFSET = 22;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SECONDARY_TEXT_COLOR = 0xFFA0A0A0;
    private static final int STACK_SIZE = 64;

    private final Screen parent;
    private final int containerID;
    private final int slotIdx;
    private final ItemStack stack;
    private final TakeAmountState state;
    private EditBox amountField;
    private AmountSlider slider;
    private Button takeButton;
    private int left;
    private int top;

    public TakeAmountScreen(Screen parent, int containerID, int slotIdx, ItemStack stack) {
        super(Component.literal("take items"));
        this.parent = parent;
        this.containerID = containerID;
        this.slotIdx = slotIdx;
        this.stack = stack;
        this.state = new TakeAmountState(stack.getCount());
    }

    @Override
    protected void init() {
        this.parent.init(this.width, this.height);
        this.left = (this.width - PANEL_WIDTH) / 2;
        this.top = (this.height - PANEL_HEIGHT) / 2;
        int x = this.left + PADDING;
        int innerWidth = PANEL_WIDTH - PADDING * 2;

        this.amountField = new EditBox(this.font, x, this.top + FIELD_Y, innerWidth, ROW_HEIGHT,
                Component.literal("amount"));
        this.amountField.setHint(Component.literal("max: " + this.state.max()));
        if (this.state.amount() > 0) {
            this.amountField.setValue(Integer.toString(this.state.amount()));
        }
        DigitField.bind(this.amountField, this.state.max(), this::onAmountChanged);
        this.addRenderableWidget(this.amountField);
        this.setInitialFocus(this.amountField);

        this.slider = this.addRenderableWidget(
                new AmountSlider(x, this.top + SLIDER_Y, innerWidth, ROW_HEIGHT, this.state, this::syncField));

        int quickPickWidth = (innerWidth - GAP * 3) / 4;
        this.addQuickPick("1", 1, 0, quickPickWidth);
        this.addQuickPick(String.valueOf(STACK_SIZE), STACK_SIZE, 1, quickPickWidth);
        this.addQuickPick("½", Math.max(1, this.state.max() / 2), 2, quickPickWidth);
        this.addQuickPick("All", this.state.max(), 3, quickPickWidth);

        int actionWidth = (innerWidth - GAP) / 2;
        this.takeButton = this.addRenderableWidget(Button.builder(Component.literal("Take"), b -> this.confirm())
                .bounds(x, this.top + ACTIONS_Y, actionWidth, ROW_HEIGHT).build());
        this.takeButton.active = this.state.amount() > 0;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, b -> this.onClose())
                .bounds(x + actionWidth + GAP, this.top + ACTIONS_Y, actionWidth, ROW_HEIGHT).build());
    }

    private void addQuickPick(String label, int amount, int column, int width) {
        Button button = this.addRenderableWidget(Button.builder(Component.literal(label), b -> {
            this.state.set(amount);
            this.syncField();
        }).bounds(this.left + PADDING + column * (width + GAP), this.top + QUICK_PICK_Y, width, ROW_HEIGHT).build());
        button.active = amount <= this.state.max();
    }

    private void onAmountChanged(int amount) {
        this.state.set(amount);
        this.slider.sync();
        this.takeButton.active = this.state.amount() > 0;
    }

    private void syncField() {
        this.amountField.setValue(Integer.toString(this.state.amount()));
    }

    private void confirm() {
        if (this.state.amount() > 0) {
            ClientPlayNetworking.send(new TakeAmountPayload(this.containerID, this.slotIdx, this.state.amount()));
        }
        this.onClose();
    }

    private boolean isInsidePanel(double x, double y) {
        return x >= this.left && x < this.left + PANEL_WIDTH && y >= this.top && y < this.top + PANEL_HEIGHT;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isConfirmation() && this.getFocused() == this.amountField && this.state.amount() > 0) {
            this.confirm();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (super.mouseClicked(event, doubleClick)) {
            return true;
        }
        if (!this.isInsidePanel(event.x(), event.y())) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.parent.extractBackground(graphics, mouseX, mouseY, a);
        graphics.nextStratum();
        this.parent.extractRenderState(graphics, -1, -1, a);
        graphics.nextStratum();
        this.extractTransparentBackground(graphics);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PANEL_SPRITE, this.left, this.top, PANEL_WIDTH,
                PANEL_HEIGHT);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        int x = this.left + PADDING;
        int y = this.top + PADDING;
        graphics.item(this.stack, x, y);
        graphics.text(this.font, this.stack.getHoverName(), x + ICON_TEXT_OFFSET, y, TEXT_COLOR);
        graphics.text(this.font, String.format("%,d in slot", this.state.max()), x + ICON_TEXT_OFFSET, y + 10,
                SECONDARY_TEXT_COLOR);
    }

    @Override
    public void onClose() {
        if (this.parent instanceof AbstractContainerScreenAccessor accessor) {
            accessor.massivestacks$setSkipNextRelease(true);
        }
        this.minecraft.setScreenAndShow(this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
