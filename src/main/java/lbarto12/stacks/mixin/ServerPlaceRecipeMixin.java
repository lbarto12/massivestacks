package lbarto12.stacks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import lbarto12.stacks.config.StackLimit;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.recipebook.ServerPlaceRecipe;

@Mixin(ServerPlaceRecipe.class)
public abstract class ServerPlaceRecipeMixin {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/component/DataComponentMap;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"), method = "clampToMaxStackSize")
    private static Object massivestacks$raisedMaxStack(DataComponentMap components, DataComponentType<?> type,
            Object fallback, Operation<Object> original) {
        if (components.get(DataComponents.MAX_DAMAGE) == null) {
            return StackLimit.get();
        }
        return original.call(components, type, fallback);
    }
}
