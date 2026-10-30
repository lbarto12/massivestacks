package lbarto12.stacks.client.screens;

import lbarto12.stacks.StackConfig;
import lbarto12.stacks.config.ConfigFile;
import lbarto12.stacks.config.MassiveStacksConfig;
import lbarto12.stacks.config.StackLimit;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class MassiveStacksConfigScreen extends Screen {
    private static final int FIELD_WIDTH = 200;
    private static final int ROW_HEIGHT = 20;
    private static final int GAP = 4;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SECONDARY_TEXT_COLOR = 0xFFA0A0A0;

    private final Screen parent;
    private int maxStackSize;
    private Button doneButton;
    private int left;
    private int fieldY;

    public MassiveStacksConfigScreen(Screen parent) {
        super(Component.literal("Massive Stacks"));
        this.parent = parent;
        this.maxStackSize = StackLimit.local();
    }

    @Override
    protected void init() {
        this.left = (this.width - FIELD_WIDTH) / 2;
        this.fieldY = this.height / 2 - ROW_HEIGHT;

        EditBox field = new EditBox(this.font, this.left, this.fieldY, FIELD_WIDTH, ROW_HEIGHT,
                Component.literal("Max stack size"));
        field.setValue(Integer.toString(this.maxStackSize));
        DigitField.bind(field, StackConfig.MAX_STACK, value -> {
            this.maxStackSize = value;
            this.doneButton.active = value >= StackConfig.MIN_STACK;
        });
        this.addRenderableWidget(field);
        this.setInitialFocus(field);

        int buttonWidth = (FIELD_WIDTH - GAP) / 2;
        int buttonY = this.fieldY + ROW_HEIGHT + GAP * 2;
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.save())
                .bounds(this.left, buttonY, buttonWidth, ROW_HEIGHT).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, b -> this.onClose())
                .bounds(this.left + buttonWidth + GAP, buttonY, buttonWidth, ROW_HEIGHT).build());
    }

    private void save() {
        StackLimit.setLocal(this.maxStackSize);
        ConfigFile.save(new MassiveStacksConfig(StackLimit.local()));
        this.onClose();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.centeredText(this.font, this.title, this.width / 2, this.fieldY - 36, TEXT_COLOR);
        graphics.text(this.font, String.format("Max stack size (1 - %,d)", StackConfig.MAX_STACK), this.left,
                this.fieldY - 12, SECONDARY_TEXT_COLOR);
        if (StackLimit.hasServerValue()) {
            graphics.centeredText(this.font,
                    Component.literal(String.format("This server uses %,d while you're connected", StackLimit.get())),
                    this.width / 2, this.fieldY + ROW_HEIGHT * 2 + GAP * 4, SECONDARY_TEXT_COLOR);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }
}
