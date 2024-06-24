package com.matthewperiut.entris.client;

import com.matthewperiut.entris.enchantment.EnchantmentHelp;
import com.matthewperiut.entris.enchantment.RomanNumeralUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.Text;

public class EnchantmentSelectButton extends ButtonWidget {
    public int number = 0;
    public Enchantment enchantment;

    public EnchantmentSelectButton(int x, int y, Enchantment enchantment, PressAction onPress) {
        super(x, y, 81, 12, EnchantmentHelp.getEnchantmentText(enchantment), onPress, textSupplier -> getNarrationMessage(EnchantmentHelp.getEnchantmentText(enchantment)));
        this.enchantment = enchantment;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean isSelected() {
        return this.isHovered();
    }

    @Override
    public void onPress() {
        super.onPress();
    }

    public boolean increment() {
        if (enchantment.getMaxLevel() > number) {
            number++;
            this.setMessage(Text.literal(EnchantmentHelp.getEnchantmentText(enchantment).getString() + " " + RomanNumeralUtil.toRoman(number)));
            return true;
        } else {
            return false;
        }
    }
}
