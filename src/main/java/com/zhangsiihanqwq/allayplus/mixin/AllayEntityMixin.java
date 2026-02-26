package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayItemComparison;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin {

    @Unique private boolean allayPlus$deepSleep = false;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        AllayEntity self = (AllayEntity) (Object) this;

        if (self.age % 20 == 0) {
            PlayerEntity player = self.getWorld().getClosestPlayer(
                    self.getX(), self.getY(), self.getZ(), 64.0, false);
            boolean heardNoteBlock = self.getBrain().hasMemoryModule(MemoryModuleType.LIKED_NOTEBLOCK);
            allayPlus$deepSleep = (player == null || !self.canSee(player)) && !heardNoteBlock;
        }

        if (allayPlus$deepSleep) {
            if (!self.getWorld().isClient) {
                Vibrations.Ticker.tick(self.getWorld(), self.getVibrationListenerData(), self.getVibrationCallback());
            }
            self.setVelocity(0, 0, 0);
            ci.cancel();
        }
    }
    /**
     * @author zhangsiihanqwq
     * @reason 修改悦灵的物品分类规则，使其能模糊匹配特定种类的物品
     */
    @Overwrite
    private boolean areItemsEqual(ItemStack stack, ItemStack stack2) {
        return AllayItemComparison.customAreItemsEqual(stack, stack2);
    }
}