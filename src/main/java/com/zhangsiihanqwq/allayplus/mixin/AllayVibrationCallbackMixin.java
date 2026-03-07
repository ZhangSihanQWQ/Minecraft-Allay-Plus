package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayPlusConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.entity.passive.AllayEntity$VibrationCallback")
public abstract class AllayVibrationCallbackMixin {

    @Inject(method = "getRange()I", at = @At("HEAD"), cancellable = true)
    private void allayplus$overrideRange(CallbackInfoReturnable<Integer> cir) {
        if (AllayPlusConfig.maxHearingDistance != -1) {
            cir.setReturnValue(AllayPlusConfig.maxHearingDistance);
        }
    }
}