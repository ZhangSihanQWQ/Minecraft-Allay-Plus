package com.zhangsiihanqwq.allayplus.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {

    /**
     * @author zhangsiihanqwq
     * 在 playNote 执行最开始注入
     * 作用：如果原版逻辑不打算发出 GameEvent ，我们强行发一个。
     */
    @Inject(method = "playNote", at = @At("HEAD"))
    private void onPlayNote(@Nullable Entity entity, BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (!com.zhangsiihanqwq.allayplus.util.AllayPlusConfig.silentResonanceEnabled) {
            return;
        }

        boolean isNotBaseBlock = ((NoteBlockInstrument) state.get(NoteBlock.INSTRUMENT)).isNotBaseBlock();
        boolean isAirAbove = world.getBlockState(pos.up()).isAir();

        if (!isNotBaseBlock && !isAirAbove) {
            world.emitGameEvent(entity, GameEvent.NOTE_BLOCK_PLAY, pos);
        }
    }
}