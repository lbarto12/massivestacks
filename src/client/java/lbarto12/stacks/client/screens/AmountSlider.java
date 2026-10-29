package lbarto12.stacks.client.screens;

import lbarto12.stacks.CountFormatter;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class AmountSlider extends AbstractSliderButton {
    private final TakeAmountState state;
    private final Runnable onChange;

    public AmountSlider(int x, int y, int width, int height, TakeAmountState state, Runnable onChange) {
        super(x, y, width, height, Component.empty(), state.sliderFraction());
        this.state = state;
        this.onChange = onChange;
        this.updateMessage();
    }

    public void sync() {
        this.value = this.state.sliderFraction();
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(CountFormatter.format(this.state.amount())));
    }

    @Override
    protected void applyValue() {
        this.state.setFromSliderFraction(this.value);
        this.onChange.run();
    }
}
