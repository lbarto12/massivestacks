package lbarto12.stacks.mixin;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lbarto12.stacks.CountFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {
    @Shadow
    public abstract int getCount();

    @Inject(at = @At("Head"), method = "addDetailsToTooltip")
    private void massivestacks$addExactCount(Item.TooltipContext context, TooltipDisplay display,
            @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        int count = this.getCount();
        if (count >= 1_000) {
            builder.accept(Component
                    .literal("amount: " + String.format("%,d", count) + " (" + CountFormatter.format(count) + ")")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
