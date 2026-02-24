package com.zhangsiihanqwq.allayplus.mixin;

import com.zhangsiihanqwq.allayplus.util.AllayItemComparison;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AllayEntity.class)
public class AllayEntityMixin {

    /**
     * @author zhangsiihanqwq
     * @reason 修改悦灵的物品分类规则，使其能模糊匹配特定种类的物品
     */
    @Overwrite
    private boolean areItemsEqual(ItemStack stack, ItemStack stack2) {
        return AllayItemComparison.customAreItemsEqual(stack, stack2);
    }
}