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

        String id1 = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).getPath();
        String id2 = net.minecraft.registry.Registries.ITEM.getId(stack2.getItem()).getPath();
        // 只要两个物品的 ID 都包含 "banner_pattern"，就视为同类
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