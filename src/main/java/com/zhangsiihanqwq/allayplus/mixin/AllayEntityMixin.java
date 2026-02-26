package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayItemComparison;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin {
    /**
     * @author zhangsiihanqwq
     * @reason 添加悦灵的深度休眠机制，使其在没有玩家或音符盒刺激时进入休眠状态，减少不必要的 AI 计算和视觉抖动
     */

    @Unique
    private boolean allayPlus$deepSleep = false;

    @Inject
            (method = "tick", at = @At("HEAD"), cancellable = true)

    private void onTick(CallbackInfo ci) {
        AllayEntity self = (AllayEntity) (Object) this;

        if (self.age % 20 == 0) {
            PlayerEntity player = self.getWorld().getClosestPlayer(
                    self.getX(), self.getY(), self.getZ(), 64.0, false);
            boolean heardNoteBlock = self.getBrain().hasMemoryModule(MemoryModuleType.LIKED_NOTEBLOCK);
            allayPlus$deepSleep = (player == null
                    || !self.canSee(player))
                    && !heardNoteBlock;
        }

        if (allayPlus$deepSleep) {
            if (!self.getWorld().isClient) {
                Vibrations.Ticker.tick(self.getWorld(),
                        self.getVibrationListenerData(),
                        self.getVibrationCallback());
            }

            Vec3d velocity = self.getVelocity();
            boolean isMoving = velocity.horizontalLengthSquared() > 1e-5
                    || Math.abs(velocity.y) > 1e-5;

            if (!isMoving && self.getRandom().nextInt(100) == 0) {
                double rx = (self.getRandom().nextDouble() - 0.5) * 0.4;
                double ry = (self.getRandom().nextDouble() - 0.5) * 0.2;
                double rz = (self.getRandom().nextDouble() - 0.5) * 0.4;

                velocity = new Vec3d(rx, ry, rz);
                self.setVelocity(velocity);

                float targetYaw = (float) Math.toDegrees(Math.atan2(-rx, rz));
                self.setYaw(targetYaw);
                self.setHeadYaw(targetYaw);
                isMoving = true;
            }

            if (isMoving) {
                velocity = velocity.multiply(0.92, 0.92, 0.92);
                self.setVelocity(velocity);

                self.move(net.minecraft.entity.MovementType.SELF, velocity);

                self.velocityDirty = true;
                self.velocityModified = true;

                if (!self.getWorld().isClient) {
                    self.updateTrackedPosition(self.getX(), self.getY(), self.getZ());

                    self.setYaw(self.getYaw());
                    self.setHeadYaw(self.getHeadYaw());
                    self.velocityDirty = true;
                }
            } else {
                self.setVelocity(Vec3d.ZERO);
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