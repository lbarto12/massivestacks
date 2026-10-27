package lbarto12.stacks.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import lbarto12.stacks.StackConfig;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackCodecMixin {
    @ModifyArg(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"), index = 1)
    private static int stacks$widenCountCodec(int max) {
        return max == 99 ? StackConfig.MAX_STACK : max;
    }
}
