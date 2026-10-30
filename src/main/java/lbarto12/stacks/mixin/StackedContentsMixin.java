package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lbarto12.stacks.StackConfig;
import net.minecraft.world.entity.player.StackedContents;

@Mixin(StackedContents.class)
public abstract class StackedContentsMixin<T> {
    @SuppressWarnings("unchecked")
    @WrapOperation(at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Reference2IntOpenHashMap;addTo(Ljava/lang/Object;I)I"), method = "put")
    private int massivstacks$saturatingAdd(Reference2IntOpenHashMap<T> amounts, Object item, int count,
            Operation<Integer> original) {
        int previous = amounts.getInt(item);
        amounts.put((T) item, (int) Math.min((long) previous + count, StackConfig.MAX_STACK));
        return previous;
    }
}
