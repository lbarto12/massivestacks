package lbarto12.stacks.client.screens;

import net.minecraft.util.Mth;

public final class TakeAmountState {
    private final int max;
    private int amount;

    public TakeAmountState(int max) {
        this.max = max;
    }

    public int max() {
        return this.max;
    }

    public int amount() {
        return this.amount;
    }

    public void set(int amount) {
        this.amount = Mth.clamp(amount, 0, this.max);
    }

    // log scale
    public void setFromSliderFraction(double fraction) {
        this.set((int) Math.round(Math.pow(this.max, fraction)));
    }

    public double sliderFraction() {
        if (this.amount <= 0) {
            return 0.0;
        }
        if (this.max <= 1) {
            return 1.0;
        }
        return Math.log(this.amount) / Math.log(this.max);
    }
}
