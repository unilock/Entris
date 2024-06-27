package com.matthewperiut.entris.mixin;

import com.matthewperiut.entris.client.SlotEnabler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin implements SlotEnabler {
    @Unique
    public boolean overriddenEnabled = false;
    @Unique
    public boolean enabled;
    @Unique
    boolean overriddenCanTake;
    @Unique boolean canTake;

    @Override
    public void setEnabled(boolean enabled) {
        overriddenEnabled = true;
        this.enabled = enabled;
    }

    @Override
    public void setCanTake(boolean canTake) {
        overriddenCanTake = true;
        this.canTake = canTake;
    }

    @Inject(method = "isEnabled",at = @At("HEAD"), cancellable = true)
    public void isEnabledModifier(CallbackInfoReturnable<Boolean> cir) {
        if (overriddenEnabled)
            cir.setReturnValue(enabled);
    }

    @Inject(method = "canTakeItems",at = @At("HEAD"), cancellable = true)
    public void canTakeItemsModifier(CallbackInfoReturnable<Boolean> cir) {
        if (overriddenCanTake)
            cir.setReturnValue(canTake);
    }
}
