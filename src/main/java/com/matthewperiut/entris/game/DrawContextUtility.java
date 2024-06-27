package com.matthewperiut.entris.game;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class DrawContextUtility {
    private static void drawTexturedQuad(DrawContext context, Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2).color(red, green, blue, alpha);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1).color(red, green, blue, alpha);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public static void drawTransparentTexture(DrawContext context, Identifier texture, int x, int y, int width, int height, float alpha) {
        if (width != 0 && height != 0) {
            drawTexturedQuad(context, texture, x, x + width, y, y + height, 0, 0.0f / width, (0.0f + width) / width, 10.0f / height, (10.0f + height) / height, 1f, 1f, 1f, alpha);
        }
    }
}
