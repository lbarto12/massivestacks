package lbarto12.stacks.client.screens;

import java.util.function.IntConsumer;

import net.minecraft.client.gui.components.EditBox;

public final class DigitField {
    private DigitField() {
    }

    public static void bind(EditBox field, int max, IntConsumer onValue) {
        field.setMaxLength(String.valueOf(max).length());
        field.setResponder(text -> {
            String digits = text.replaceAll("[^0-9]", "");
            int value = digits.isEmpty() ? 0 : (int) Math.min(Long.parseLong(digits), max);
            String normalized = digits.isEmpty() ? "" : Integer.toString(value);
            if (!normalized.equals(text)) {
                field.setValue(normalized);
                return;
            }
            onValue.accept(value);
        });
    }
}
