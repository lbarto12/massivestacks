package lbarto12.stacks.client.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import lbarto12.stacks.client.CountFormatter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GUIGraphicsMixin {
    @ModifyVariable(method = "itemCount(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"), argsOnly = true)
    private @Nullable String stacks$shortenCount(@Nullable String countText, Font font, ItemStack itemStack) {
        if (countText == null && itemStack.getCount() >= 1_000) {
            return CountFormatter.format(itemStack.getCount());
        }
        return countText;
    }
}
