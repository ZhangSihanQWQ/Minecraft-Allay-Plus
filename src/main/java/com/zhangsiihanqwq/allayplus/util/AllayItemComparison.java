package com.zhangsiihanqwq.allayplus.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import java.util.Objects;

public class AllayItemComparison {

    public static boolean customAreItemsEqual(ItemStack stack, ItemStack stack2) {
        if (stack.isOf(Items.ENCHANTED_BOOK) && stack2.isOf(Items.ENCHANTED_BOOK)) {
            return true;
        }

        if (isPotion(stack) && isPotion(stack2)) {
            return stack.getItem() == stack2.getItem();
        }

        // 在 1.21.1 中，旗帜图案物品没有独立类，我们通过其物品定义来判断
        if (isBannerPattern(stack) && isBannerPattern(stack2)) {
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

        if (stack.isIn(ItemTags.BEDS) && stack2.isIn(ItemTags.BEDS)) {
            return true;
        }

        if (isHorseArmor(stack) && isHorseArmor(stack2)) {
            return true;
        }

        // 默认逻辑回退
        return ItemStack.areItemsEqual(stack, stack2) && !areDifferentPotions(stack, stack2);
    }

    private static boolean isPotion(ItemStack stack) {
        return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION);
    }

    private static boolean isBannerPattern(ItemStack stack) {
        // 如果 BannerPatternItem 无法找到，可以直接判断是否是那几种具体的物品
        return stack.isOf(Items.FLOWER_BANNER_PATTERN) ||
                stack.isOf(Items.CREEPER_BANNER_PATTERN) ||
                stack.isOf(Items.SKULL_BANNER_PATTERN) ||
                stack.isOf(Items.MOJANG_BANNER_PATTERN) ||
                stack.isOf(Items.PIGLIN_BANNER_PATTERN) ||
                stack.isOf(Items.GLOBE_BANNER_PATTERN);
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