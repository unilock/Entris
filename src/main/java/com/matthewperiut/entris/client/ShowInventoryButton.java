package com.matthewperiut.entris.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

import static com.matthewperiut.entris.Entris.MOD_ID;

public class ShowInventoryButton extends ButtonWidget {

    Identifier CHEST_BUTTON = Identifier.of(MOD_ID, "textures/gui/container/enchanting_table/chest_button.png");
    Identifier CHEST_SLOT = Identifier.of(MOD_ID, "textures/gui/container/enchanting_table/chest_slot.png");
    Identifier CHEST_HIGHLIGHT = Identifier.of(MOD_ID, "textures/gui/container/enchanting_table/chest_highlight.png");

    Identifier CHEST_OPEN = Identifier.of(MOD_ID, "textures/gui/container/enchanting_table/chest_open.png");
    Identifier CHEST = Identifier.of(MOD_ID, "textures/gui/container/enchanting_table/chest.png");

    public ShowInventoryButton(int x, int y, PressAction onPress) {
        super(x, y, 18, 18, Text.empty(), onPress, new NarrationSupplier() {
            @Override
            public MutableText createNarrationMessage(Supplier<MutableText> textSupplier) {
                return getNarrationMessage(Text.literal("Show Inventory"));
            }
        });
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawTexture(openChest ? CHEST_SLOT : CHEST_BUTTON, this.getX(), this.getY(), 0, 10, this.getWidth(), this.getHeight(), 18, 18);
        if (hovered)
            context.drawTexture(CHEST_HIGHLIGHT, this.getX(), this.getY(), 0, 10, this.getWidth(), this.getHeight(), 18, 18);

        context.drawTexture(openChest ? CHEST_OPEN : CHEST, this.getX() + 1, this.getY() - 1, 0, 10, this.getWidth(), this.getHeight(), 16, 19);

        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean openChest = false;
}
