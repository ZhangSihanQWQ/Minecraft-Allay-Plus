package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayPlusConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.TemptationCooldownTask;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptationCooldownTask.class)
public abstract class TemptationCooldownTaskMixin {
    @Shadow @Final private MemoryModuleType<Integer> moduleType;

    @Inject(method = "shouldKeepRunning", at = @At("HEAD"), cancellable = true)
    private void allayplus$forceFinish(ServerWorld world, LivingEntity entity, long time, CallbackInfoReturnable<Boolean> cir) {
        if (this.moduleType == MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS && AllayPlusConfig.throwCooldownTime != -1) {
            entity.getBrain().getOptionalRegisteredMemory(this.moduleType).ifPresent(ticks -> {
                if (ticks <= 0) {
                    cir.setReturnValue(false);
                }
            });
        }
    }
}