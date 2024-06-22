package com.matthewperiut.entris.mixin;

import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.SlotEnabler;
import com.matthewperiut.entris.TetrisGame;
import com.matthewperiut.entris.client.ShowInventoryButton;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentScreen.class)
abstract public class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    @Shadow protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);

    private static final Text GO_BACK = Text.literal("Take Me Back!");

    PressableTextWidget w;
    ShowInventoryButton s;
    boolean showInventory = false;

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        setEntris(true);
        updateSlotDownStatus();

        int x = (this.width - this.entrisBackgroundWidth) / 2;
        int y = (this.height - this.entrisBackgroundHeight) / 2;
        int i = this.textRenderer.getWidth(GO_BACK);
        int j = this.width - i - 2;
        w = this.addDrawableChild(new PressableTextWidget(x + 1, y - 10, i, 10, GO_BACK, (button) -> {
            setEntris(false);
            w.active = false;
            w.visible = false;
            s.visible = false;
            s.active = false;
            updateSlotDownStatus();
        }, this.textRenderer));

        s = this.addDrawableChild(new ShowInventoryButton(x + 54, y + 46, (button -> {
            showInventory = !showInventory;
            s.openChest = showInventory;
            updateSlotDownStatus();
        })));
    }

    public EnchantmentScreenMixin(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    private static final Identifier ENTRIS = Identifier.of(Entris.MOD_ID, "textures/gui/container/entris.png");
    private static final Identifier INVENTORY = Identifier.of(Entris.MOD_ID, "textures/gui/container/inventory.png");

    @Unique
    protected int entrisBackgroundWidth = 176;
    @Unique
    protected int entrisBackgroundHeight = 166;

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    protected void changeDrawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (enableEntris) {
            int i = (this.width - this.entrisBackgroundWidth) / 2;
            int j = (this.height - this.entrisBackgroundHeight) / 2;
            context.drawTexture(ENTRIS, i, j, 0, 0, this.entrisBackgroundWidth, this.entrisBackgroundHeight);

            int x = (this.width - this.entrisBackgroundWidth) / 2;
            int y = (this.height - this.entrisBackgroundHeight) / 2;
            tetrisGame.render(context, x + 92, y + 155);

            if (showInventory)
                context.drawTexture(INVENTORY, i, j, 0, 0, this.entrisBackgroundWidth, this.entrisBackgroundHeight);
            ci.cancel();
        }
    }

    @Unique
    TetrisGame tetrisGame = new TetrisGame();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;getLapisCount()I"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (enableEntris) {
            ci.cancel();
        }
    }

    @Unique
    private boolean enableEntris = false;
    @Unique
    private boolean slotsDown = false;
    @Unique
    private void setEntris(boolean state) {
        enableEntris = state;
        updateSlotDownStatus();
    }

    @Unique
    private void updateSlotDownStatus() {

        for (int i = 2; i < ((EnchantmentScreenHandler)this.handler).slots.size(); i++)
        {
            Slot slot = ((EnchantmentScreenHandler) this.handler).slots.get(i);
            if (enableEntris) {
                if (showInventory) {
                    ((SlotEnabler) slot).setEnabled(true);
                } else {
                    ((SlotEnabler) slot).setEnabled(false);
                }
            } else {
                ((SlotEnabler) slot).setEnabled(true);
            }
        }
    }

    @Inject(method = "doTick", at = @At("HEAD"), cancellable = true)
    private void doTick(CallbackInfo ci) {
        if (enableEntris)
            ci.cancel();
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        if (!enableEntris)
            super.drawForeground(context, mouseX, mouseY);
        else {
            context.drawText(this.textRenderer, "Entris", this.titleX, this.titleY, 4210752, false);
            context.drawText(this.textRenderer, "Next Piece:", this.titleX, this.titleY + 10, 4210752, false);
            context.drawText(this.textRenderer, "How much XP?", this.titleX, this.titleY + 60, 4210752, false);
            if (!showInventory) {
                context.drawText(this.textRenderer, "10 LVL = 1 min", this.titleX, this.titleY + 70, 4210752, false);
                context.drawText(this.textRenderer, "MAX 30 LVL", this.titleX, this.titleY + 80, 4210752, false);

            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            // Code to execute when the right arrow key is pressed
            System.out.println("Right arrow key was pressed!");
            return true; // Return true to indicate the key was handled
        }
        return super.keyPressed(keyCode, scanCode, modifiers); // Handle other keys as usual
    }
}