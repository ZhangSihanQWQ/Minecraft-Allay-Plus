package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayPlusAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AllayBrain.class)
public class AllayBrainMixin {
    @Inject(method = "getLikedPlayer", at = @At("HEAD"), cancellable = true)
    private static void maskPlayerMemory(LivingEntity allay, CallbackInfoReturnable
            <java.util.Optional<net.minecraft.server.network.ServerPlayerEntity>> cir) {
        if (allay instanceof AllayPlusAccess access && access.allayPlus$isDeepSleeping()) {
            cir.setReturnValue(java.util.Optional.empty());
        }
    }
}