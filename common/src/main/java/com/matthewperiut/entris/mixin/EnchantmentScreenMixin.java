package com.matthewperiut.entris.mixin;

import com.matthewperiut.entris.Entris;
import com.matthewperiut.entris.client.*;
import com.matthewperiut.entris.game.TetrisGame;
import com.matthewperiut.entris.network.ClientNetworkHelper;
import com.matthewperiut.entris.network.payload.RequestStartEntrisPayload;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

import static com.matthewperiut.entris.BookShelvesUtil.countBookShelves;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

@Mixin(EnchantmentScreen.class)
abstract public class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> implements ClientEntrisInterface {
    @Shadow protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);

    private static final Text GO_BACK = Text.literal("Take Me Back!");

    PressableTextWidget w;
    ShowInventoryButton s;
    StartGameButton p;

    @Unique
    ArrayList<EnchantmentSelectButton> enchantmentSelectButtons = new ArrayList<>();

    boolean showInventory = false;

    int bookshelveMaxLevel = 4;

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        setEntris(true);
        updateSlotDownStatus();
        bookshelveMaxLevel = (int) (4 + (1.74*countBookShelves(MinecraftClient.getInstance().player)));

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
            p.visible = false;
            p.active = false;
            updateSlotDownStatus();
            Entris.disableRegularEnchanting = false;
        }, this.textRenderer));

        s = this.addDrawableChild(new ShowInventoryButton(x + 54, y + 46, (button -> {
            showInventory = !showInventory;
            s.openChest = showInventory;
            p.visible = !showInventory;
            p.active = !showInventory;
            updateSlotDownStatus();
            /*
            */
        })));

        p = this.addDrawableChild(new StartGameButton(x + 15, y + 106, (button -> {
            ClientNetworkHelper.send(new RequestStartEntrisPayload(numberHolder.getNumber()));
        })));

        EnchantmentSelectButton b = this.addDrawableChild(new EnchantmentSelectButton(x + 55, y + 106, Enchantments.SHARPNESS.getValue(), (button -> {
        })));

        enchantmentSelectButtons.add(b);

        p.active = false;
        Entris.disableRegularEnchanting = true;
    }

    public void beginGame() {
        if (!tetrisGame.isStarted && !tetrisGame.gameOver)
            tetrisGame.startGame(numberHolder.getNumber() * 6);
        else if (tetrisGame.gameOver) {
            tetrisGame = new TetrisGame();
            tetrisGame.startGame(numberHolder.getNumber() * 6);
        }
    }
    public void errorHandling() {
        close();
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
            if (tetrisGame.holdTetrominoDisplay != null) {
                tetrisGame.renderTetromino(context, tetrisGame.holdTetrominoDisplay, 0, 0, x + 45, y + 33);
            }

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
        if (enableEntris) {
            if (handle == -1) {
                regenerateHandle();
            }

            blinkCounter++;
            if (blinkCounter > 9) {
                blinkCounter = 0;
            }

            try {
                tetrisGame.tick();
                tetrisGame.continuousInput(handle);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                regenerateHandle();
            }

            ci.cancel();
        }
    }

    int blinkCounter = 0;
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        if (!enableEntris)
            super.drawForeground(context, mouseX, mouseY);
        else {
            context.drawText(this.textRenderer, "Entris", this.titleX + 18, this.titleY, 0x404040, false);
            context.drawText(this.textRenderer, "HOLD:", this.titleX + 5, this.titleY + 17, 0x404040, false);
            if (!showInventory) {
                context.drawText(this.textRenderer, "How much XP?", this.titleX, this.titleY + 70, 0x404040, false);
                if (numberHolder.getNumberStr().isEmpty() && blinkCounter > 5) {
                    context.drawText(this.textRenderer, "_", this.titleX, this.titleY + 80, 0x55FFFF, false);
                } else {
                    context.drawText(this.textRenderer, numberHolder.getNumberStr(), this.titleX, this.titleY + 80, 0x55FFFF, false);
                }
                int seconds = (int)(numberHolder.getNumber() % 10) * 6;
                context.drawText(this.textRenderer, "    LVL = " + (int) Math.floor(numberHolder.getNumber()/ 10.f)  + ":" + String.format("%02d", seconds), this.titleX, this.titleY + 80, 0x404040, false);
                int neededLapis = (int) Math.ceil(numberHolder.getNumber() / 10.f);
                if (tetrisGame.isStarted && !tetrisGame.gameOver) {
                    p.active = false;
                } else {
                    if (!MinecraftClient.getInstance().player.isCreative()) {
                        if (MinecraftClient.getInstance().player.experienceLevel < numberHolder.getNumber() && !MinecraftClient.getInstance().player.isCreative() ) {
                            context.drawText(this.textRenderer, "NOT ENOUGH XP", this.titleX, this.titleY + 91, 0xFF5555, false);
                        }
                        else if (numberHolder.getNumber() > bookshelveMaxLevel){
                            context.drawText(this.textRenderer, "^XP MORE BOOKS", this.titleX - 3, this.titleY + 91, 0xFF5555, false);
                        }
                        else if (handler.getSlot(0).getStack().isEmpty()){
                            context.drawText(this.textRenderer, "NEED ITEM", this.titleX, this.titleY + 91, 0xFF5555, false);
                        }
                        else if (handler.getLapisCount() < neededLapis) {
                            context.drawText(this.textRenderer, "NEEDS " + neededLapis + " LAPIS", this.titleX, this.titleY + 91, 0xFF5555, false);
                        } else {
                            if (numberHolder.getNumber() > 0)
                                p.active = true;
                            else
                                p.active = false;
                        }
                    } else {
                        p.active = true;
                    }
                }

                //handler.onButtonClick()

                context.drawText(this.textRenderer, "TIME: " + tetrisGame.getMinutes() + ":" + String.format("%02d", tetrisGame.getSeconds()), this.titleX, this.titleY + 130, 0x404040, false);
                context.drawText(this.textRenderer, "SCORE: " + tetrisGame.getScore(), this.titleX, this.titleY + 139, 4210752, false);

            }
        }
    }

    @Unique
    LevelInput numberHolder = new LevelInput();

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (enableEntris) {
            if(tetrisGame.input(keyCode, scanCode, modifiers)) {
                return true;
            }

            if (keyCode == GLFW_KEY_SPACE)
                return true;

            numberHolder.input(keyCode);
        }
        return super.keyPressed(keyCode, scanCode, modifiers); // Handle other keys as usual
    }

    private static long handle = -1;

    @Unique
    void regenerateHandle() {
        MinecraftClient gameInstance = (MinecraftClient) (FabricLoader.getInstance().getGameInstance());
        handle = gameInstance.getWindow().getHandle();
    }

}