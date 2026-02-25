package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayItemComparison;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AllayEntity.class)
public class AllayEntityMixin {

    @Unique
    private boolean allayPlus$shouldFreezeAI = false;

    @Unique
    private int allayPlus$graceTicks = 0;

    @Inject(
            method = "mobTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/brain/Brain;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V"),
            cancellable = true
    )
    private void onBeforeBrainTick(CallbackInfo ci) {
        AllayEntity self = (AllayEntity) (Object) this;

        if (self.age % 40 == 0) {
            PlayerEntity closestPlayer = self.getWorld().getClosestPlayer(
                    self.getX(), self.getY(), self.getZ(), 64.0, false);
            if (closestPlayer == null) {
                allayPlus$shouldFreezeAI = true;
            } else {
                allayPlus$shouldFreezeAI = !self.canSee(closestPlayer);
            }
        }

        boolean hasItems = !self.getInventory().isEmpty();
        boolean hasWalkTarget = self.getBrain().hasMemoryModule(MemoryModuleType.WALK_TARGET);
        boolean heardNoteBlock = self.getBrain().hasMemoryModule(MemoryModuleType.LIKED_NOTEBLOCK);

        boolean isBusy = hasItems || hasWalkTarget || heardNoteBlock;

        if (isBusy) {
            allayPlus$graceTicks = 100;
        } else if (allayPlus$graceTicks > 0) {
            allayPlus$graceTicks--;
        }

        if (allayPlus$shouldFreezeAI && allayPlus$graceTicks <= 0) {
            if (!self.getNavigation().isIdle()) {
                self.getNavigation().stop();
            }
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