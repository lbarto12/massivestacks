package lbarto12.stacks.client.mixin;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lbarto12.stacks.CountFormatter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GUIGraphicsMixin {
    private static final int MAX_WIDTH = 16; // 1 slot

    @Shadow
    @Final
    private Matrix3x2fStack pose;

    @Shadow
    public abstract void text(Font font, String text, int x, int y, int color, boolean shadow);

    @Inject(at = @At("HEAD"), method = "itemCount(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", cancellable = true)
    private void massivestacks$drawScaledCount(Font font, ItemStack itemStack, int x, int y, @Nullable String countText,
            CallbackInfo ci) {
        if (countText != null || itemStack.getCount() < 1_000) {
            return;
        }

        String label = CountFormatter.format(itemStack.count());
        int width = font.width(label);
        float scale = width > MAX_WIDTH ? (float) MAX_WIDTH / width : 1.0F;

        this.pose.pushMatrix();
        this.pose.translate(x + 17, y + 17);
        this.pose.scale(scale, scale);
        this.text(font, label, -width, -8, -1, true);
        this.pose.popMatrix();

        ci.cancel();
    }
}
