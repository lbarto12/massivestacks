package lbarto12.stacks.client.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.InputConstants;

import lbarto12.stacks.client.screens.TakeAmountScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen {
    @Shadow
    protected @Nullable Slot hoveredSlot;
    @Shadow
    @Final
    protected T menu;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
    private void massivestacks$openTakeAmount(MouseButtonEvent event, boolean doubleClick,
            CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof CreativeModeInventoryScreen) // I see you
            return;

        if (event.button() == InputConstants.MOUSE_BUTTON_RIGHT && event.hasControlDown() && this.hoveredSlot != null
                && this.hoveredSlot.hasItem()
                && this.menu.getCarried().isEmpty()) {
            this.minecraft.setScreenAndShow(new TakeAmountScreen(this, this.menu.containerId, this.hoveredSlot.index,
                    this.hoveredSlot.getItem().getCount()));
            cir.setReturnValue(true);
        }
    }
}
