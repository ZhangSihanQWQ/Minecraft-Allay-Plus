package com.zhangsiihanqwq.allayplus.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.tag.ItemTags;
import java.util.Objects;

public class AllayItemComparison {

    public static boolean customAreItemsEqual(ItemStack stack, ItemStack stack2) {

        //药水逻辑：只比较物品类型，忽略具体效果，允许不同类型的药水互相匹配
        if (isPotion(stack) && isPotion(stack2)) {
            return stack.getItem() == stack2.getItem();
        }

        //旗帜图案：通过物品 ID 模糊匹配，允许所有包含 "banner_pattern" 的物品互相匹配
        String id1 = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).getPath();
        String id2 = net.minecraft.registry.Registries.ITEM.getId(stack2.getItem()).getPath();
        if (id1.contains("banner_pattern") && id2.contains("banner_pattern")) {
            return true;
        }

        // 唱片逻辑：检查是否含有唱片机可播放组件 (JUKEBOX_PLAYABLE)
        if (stack.contains(DataComponentTypes.JUKEBOX_PLAYABLE) && stack2.contains(DataComponentTypes.JUKEBOX_PLAYABLE)) {
            return true;
        }

        // 船逻辑：精准区分运输船和普通船
        boolean isBoat1 = stack.isIn(ItemTags.BOATS);
        boolean isBoat2 = stack2.isIn(ItemTags.BOATS);
        boolean isChestBoat1 = stack.isIn(ItemTags.CHEST_BOATS);
        boolean isChestBoat2 = stack2.isIn(ItemTags.CHEST_BOATS);

        if (isChestBoat1 && isChestBoat2) {
            return true;
        }
        if (isBoat1 && isBoat2 && !isChestBoat1 && !isChestBoat2) {
            return true;
        }
        if (isBoat1 && isBoat2 && (isChestBoat1 != isChestBoat2)) {
            return false;
        }

        //床
        if (stack.isIn(ItemTags.BEDS) && stack2.isIn(ItemTags.BEDS)) {
            return true;
        }

        //马铠
        if (isHorseArmor(stack) && isHorseArmor(stack2)) {
            return true;
        }

        // 陶片逻辑：使用原版 ItemTags.DECORATED_POT_SHERDS 标签判定
        if (stack.isIn(ItemTags.DECORATED_POT_SHERDS) && stack2.isIn(ItemTags.DECORATED_POT_SHERDS)) {
            return true;
        }

        // 锻造模板逻辑：直接使用 SmithingTemplateItem 类判定
        // 这样会同时包含下界合金升级模版和所有的纹饰模版
        if (stack.getItem() instanceof SmithingTemplateItem && stack2.getItem() instanceof SmithingTemplateItem) {
            return true;
        }

        // 默认逻辑回退
        return ItemStack.areItemsEqual(stack, stack2) && !areDifferentPotions(stack, stack2);
    }

    private static boolean isPotion(ItemStack stack) {
        return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION);
    }

    private static boolean isHorseArmor(ItemStack stack) {
        return stack.isOf(Items.LEATHER_HORSE_ARMOR) ||
                stack.isOf(Items.IRON_HORSE_ARMOR) ||
                stack.isOf(Items.GOLDEN_HORSE_ARMOR) ||
                stack.isOf(Items.DIAMOND_HORSE_ARMOR);
    }

    private static boolean areDifferentPotions(ItemStack stack, ItemStack stack2) {
        PotionContentsComponent potion1 = stack.get(DataComponentTypes.POTION_CONTENTS);
        PotionContentsComponent potion2 = stack2.get(DataComponentTypes.POTION_CONTENTS);
        return !Objects.equals(potion1, potion2);
    }
}