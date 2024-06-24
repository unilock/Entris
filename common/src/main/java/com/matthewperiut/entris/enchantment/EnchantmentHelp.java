package com.matthewperiut.entris.enchantment;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentHelp {

    @Environment(EnvType.SERVER)
    private static World getServerWorldInstance() {
        return ((MinecraftServer)FabricLoader.getInstance().getGameInstance()).getOverworld();
    }
    @Environment(EnvType.CLIENT)
    private static World getClientWorldInstance() {
        return MinecraftClient.getInstance().world;
    }
    private static World getWorldInstance() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return getClientWorldInstance();
        } else {
            return getServerWorldInstance();
        }
    }

    public static Text getEnchantmentText(Identifier enchantment) {
        return getWorldInstance().getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(enchantment).description();
    }
    public static Text getEnchantmentText(Enchantment enchantment) {
        return enchantment.description();
    }
    public static String getEnchantmentIdStr(Enchantment enchantment) {
        return getWorldInstance().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(enchantment).getIdAsString();
    }
    public static Enchantment getEnchantmentIdStr(String enchantmentId) {
        return getWorldInstance().getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(Identifier.of(enchantmentId));
    }

    public static Enchantment[] getPossibleEnchantments(ItemStack itemStack) {
        List<Enchantment> applicableEnchantments = new ArrayList<>();
        for (Enchantment enchantment : getWorldInstance().getRegistryManager().get(RegistryKeys.ENCHANTMENT)) {
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
