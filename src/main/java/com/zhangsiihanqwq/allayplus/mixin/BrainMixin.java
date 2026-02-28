package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayPlusConfig;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(Brain.class)
public abstract class BrainMixin {

    @SuppressWarnings("unchecked")
    @ModifyVariable(method = "remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V",
            at = @At("HEAD"), argsOnly = true)
    private <U> U allayplus$modifyCooldownValue(U value, MemoryModuleType<U> type) {
        if (type == MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS && AllayPlusConfig.throwCooldownTime != -1) {
            if (value instanceof Integer) {
                int newValue = (Integer) value;

                if (newValue == 60) {
                    return (U) Integer.valueOf(AllayPlusConfig.throwCooldownTime);
                }
            }
        }
        return value;
    }
}