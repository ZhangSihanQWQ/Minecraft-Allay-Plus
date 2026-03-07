package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayItemComparison;
import com.zhangsiihanqwq.allayplus.util.AllayPlusAccess;
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
public abstract class AllayEntityMixin implements AllayPlusAccess {
    @Unique private boolean allayPlus$deepSleep = false;

    @Override
    public boolean allayPlus$isDeepSleeping() {
        return this.allayPlus$deepSleep;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void onReadNbt(net.minecraft.nbt.NbtCompound nbt, CallbackInfo ci) {
        this.allayPlus$updateLogic();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        AllayEntity self = (AllayEntity) (Object) this;
        if (self.age % 20 == 0) {
            this.allayPlus$updateLogic();
        }
    }

    @Unique
    private void allayPlus$updateLogic() {
        AllayEntity self = (AllayEntity) (Object) this;
        if (self.getWorld() == null || self.getWorld().isClient) return;

        PlayerEntity player = self.getWorld().getClosestPlayer(self.getX(), self.getY(), self.getZ(), 64.0, false);
        boolean hasNoteBlock = self.getBrain().hasMemoryModule(MemoryModuleType.LIKED_NOTEBLOCK);

        if ((player == null || !self.canSee(player)) && !hasNoteBlock) {
            this.allayPlus$deepSleep = true;

            if (self.getBrain().hasMemoryModule(MemoryModuleType.LIKED_PLAYER)) {
                self.getBrain().forget(MemoryModuleType.LIKED_PLAYER);
                self.getNavigation().stop();
            }
        } else {
            this.allayPlus$deepSleep = false;
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