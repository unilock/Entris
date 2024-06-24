package com.matthewperiut.entris.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnchantmentSelectButton extends ButtonWidget {
    public EnchantmentSelectButton(int x, int y, Identifier enchantment, PressAction onPress) {
        super(x, y, 81, 12, MinecraftClient.getInstance().world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(enchantment).description(), onPress, textSupplier -> getNarrationMessage(Text.literal("Start Game")));

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean isSelected() {
        return this.isHovered();
    }
}
