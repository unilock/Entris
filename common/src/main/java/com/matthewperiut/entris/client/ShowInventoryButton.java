package com.matthewperiut.entris.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

import static com.matthewperiut.entris.Entris.MOD_ID;

public class ShowInventoryButton extends ButtonWidget {

    Identifier CHEST_BUTTON = Identifier.of(MOD_ID, "container/enchanting_table/chest_button");
    Identifier CHEST_SLOT = Identifier.of(MOD_ID, "container/enchanting_table/chest_slot");
    Identifier CHEST_HIGHLIGHT = Identifier.of(MOD_ID, "container/enchanting_table/chest_highlight");

    Identifier CHEST_OPEN = Identifier.of(MOD_ID, "container/enchanting_table/chest_open");
    Identifier CHEST = Identifier.of(MOD_ID, "container/enchanting_table/chest");

    public ShowInventoryButton(int x, int y, PressAction onPress) {
        super(x, y, 18, 18, Text.empty(), onPress, new NarrationSupplier() {
            @Override
            public MutableText createNarrationMessage(Supplier<MutableText> textSupplier) {
                return getNarrationMessage(Text.literal("Show Inventory"));
            }
        });
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(openChest ? CHEST_SLOT : CHEST_BUTTON, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (hovered)
            context.drawGuiTexture(CHEST_HIGHLIGHT, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        context.drawGuiTexture(openChest ? CHEST_OPEN : CHEST, this.getX() + 1, this.getY() - 1, this.getWidth(), this.getHeight());

        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean openChest = false;
}
