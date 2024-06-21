package com.matthewperiut.entris.mixin;

import com.matthewperiut.entris.SlotEnabler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin implements SlotEnabler {
    @Unique
    public boolean overridden = false;
    @Unique
    public boolean enabled;

    @Override
    public void setEnabled(boolean enabled) {
        overridden = true;
        this.enabled = enabled;
    }

    @Inject(method = "isEnabled",at = @At("HEAD"), cancellable = true)
    public void isEnabledModifier(CallbackInfoReturnable<Boolean> cir) {
        if (overridden)
            cir.setReturnValue(enabled);
    }
}
