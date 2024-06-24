package com.matthewperiut.entris.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentHelp {
    public static Text getEnchantmentText(World world, Identifier enchantment) {
        return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(enchantment).description();
    }
    public static Text getEnchantmentText(Enchantment enchantment) {
        return enchantment.description();
    }
    public static String getEnchantmentIdStr(World world, Enchantment enchantment) {
        return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(enchantment).getIdAsString();
    }
    public static Enchantment getEnchantmentIdStr(World world, String enchantmentId) {
        return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(Identifier.of(enchantmentId));
    }

    public static Enchantment[] getPossibleEnchantments(World world, ItemStack itemStack) {
        List<Enchantment> applicableEnchantments = new ArrayList<>();
        for (Enchantment enchantment : world.getRegistryManager().get(RegistryKeys.ENCHANTMENT)) {
            if (enchantment.isAcceptableItem(itemStack)) {
                applicableEnchantments.add(enchantment);
            }
        }
        return applicableEnchantments.toArray(new Enchantment[0]);
    }

    public static boolean disallowedEnchanting(Enchantment enchantment) {
        return enchantment.description().toString().toLowerCase().contains("mending") ||
        enchantment.description().toString().toLowerCase().contains("frost_walker") ||
        enchantment.description().toString().toLowerCase().contains("swift_sneak") ||
        enchantment.description().toString().toLowerCase().contains("soul_speed");
    }
}
