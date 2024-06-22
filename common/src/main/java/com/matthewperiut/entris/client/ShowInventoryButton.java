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

    protected static final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.of(MOD_ID, "container/enchanting_table/chest_button"),
            Identifier.of(MOD_ID, "container/enchanting_table/chest_slot"),
            Identifier.of(MOD_ID, "container/enchanting_table/chest_highlight"));

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
        context.drawGuiTexture(TEXTURES.get(this.active, this.isSelected()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
