package com.matthewperiut.entris.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class StartGameButton extends ButtonWidget {
    public StartGameButton(int x, int y, PressAction onPress) {
        super(x, y, 50, 12, Text.literal("START"), onPress, textSupplier -> getNarrationMessage(Text.literal("Start Game")));
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
