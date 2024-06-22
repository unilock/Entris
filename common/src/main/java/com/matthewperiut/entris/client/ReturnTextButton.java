package com.matthewperiut.entris.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.math.MathHelper;

public class ReturnTextButton extends PressableTextWidget {
    public boolean enabled = true;
    public ReturnTextButton(int x, int y, int width, int height, Text text, PressAction onPress, TextRenderer textRenderer) {
        super(x, y, width, height, text, onPress, textRenderer);
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (enabled)
            super.renderWidget(context, mouseX, mouseY, delta);
    }

    public void onPress() {
        if (enabled)
            super.onPress();
    }

}
