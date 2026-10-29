package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import lbarto12.stacks.CountFormatter;
import net.minecraft.world.item.component.ItemContainerContents;

// E.g. when viewing the tooltip for a shulker box
@Mixin(ItemContainerContents.class)
public abstract class ItemContainerContentsMixin {
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"), method = "addToTooltip", index = 1)
    private Object[] massivestacks$shortenTooltipCount(String key, Object[] args) {
        if ("item.container.item_count".equals(key) && args.length > 0 && args[0] instanceof Integer count
                && count >= 1_000) {
            args[0] = CountFormatter.format(count);
        }
        return args;
    }
}
