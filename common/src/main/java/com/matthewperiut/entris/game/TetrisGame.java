package com.matthewperiut.entris.game;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static com.matthewperiut.entris.Entris.MOD_ID;
import static org.lwjgl.glfw.GLFW.*;

public class TetrisGame {
    public enum Tile {
        NONE,
        BLUE,
        GREEN,
        LIGHT_BLUE,
        ORANGE,
        PURPLE,
        RED,
        YELLOW
    }

    public static Identifier[] TILE_ID = {
            Identifier.of(MOD_ID, "container/enchanting_table/tile_blue"),
            Identifier.of(MOD_ID, "container/enchanting_table/tile_green"),
            Identifier.of(MOD_ID, "container/enchanting_table/tile_light_blue"),
            Identifier.of(MOD_ID, "container/enchanting_table/tile_orange"),
            Identifier.of(MOD_ID, "container/enchanting_table/tile_purple"),
            Identifier.of(MOD_ID, "container/enchanting_table/tile_red"),
            Identifier.of(MOD_ID, "container/enchanting_table/tile_yellow")
    };

    public Tile[][] screen = new Tile[10][24];

    private Tetromino currentTetromino;
    private Tetromino holdTetromino;
    private boolean holdUsed;
    private int currentX, currentY;
    private boolean gameOver;
    private boolean isDropping;
    private int dropTimer;
    private Queue<Tetromino.Shape> bag = new LinkedList<>();

    Random random = new Random();

    public TetrisGame() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                screen[i][j] = Tile.NONE;
            }
        }
        fillBag();
        spawnNewTetromino();
    }

    private int calculateDropPosition(Tetromino tetromino, int startX, int startY) {
        int landingY = startY;
        while (isValidPosition(tetromino, startX, landingY - 1)) {
            landingY--;
        }
        return landingY;
    }


    public void render(DrawContext context, int x, int y) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                int id = screen[i][j].ordinal();
                if (id > 0) {
                    context.drawGuiTexture(TILE_ID[id - 1], x + (i * 8), y - (j * 8), 8, 8);
                }
            }
        }

        // Render the current tetromino normally
        renderTetromino(context, currentTetromino, currentX, currentY, x, y);

        // Calculate and render the hard drop position as transparent
        int landingY = calculateDropPosition(currentTetromino, currentX, currentY);
        renderTransparentTetromino(context, currentTetromino, currentX, landingY, x, y);
    }

    int buttonCooldown = 0;
    boolean flipper = false;
    boolean leftRelease = true;
    boolean rightRelease = true;
    public void continuousInput(long handle) {
        flipper = !flipper;

        if (buttonCooldown > 0) {
            buttonCooldown--;
        } else {
            if (flipper) {
                if (glfwGetKey(handle, GLFW.GLFW_KEY_LEFT) == GLFW_PRESS) {
                    leftRelease = false;
                    if (moveTetromino(-1, 0))
                        SoundHelper.moveSound();
                }
                if (glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT) == GLFW_PRESS) {
                    rightRelease = false;
                    if (moveTetromino(1, 0))
                        SoundHelper.moveSound();
                }
            }
        }

        if (glfwGetKey(handle, GLFW.GLFW_KEY_LEFT) == GLFW_RELEASE) {
            leftRelease = true;
        }
        if (glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT) == GLFW_RELEASE) {
            rightRelease = true;
        }

        if (glfwGetKey(handle, GLFW.GLFW_KEY_DOWN) == GLFW_PRESS) {
            isDropping = true;
        } else {
            isDropping = false;
        }
    }

    public boolean input(int keyCode, int scanCode, int modifiers) {
        if (gameOver) return false;

        switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT:
                if (leftRelease) {
                    if (moveTetromino(-1, 0)) {
                        SoundHelper.moveSound();
                    }
                    buttonCooldown = 2;
                }
                break;
            case GLFW.GLFW_KEY_RIGHT:
                if (rightRelease) {
                    if (moveTetromino(1, 0)) {
                        SoundHelper.moveSound();
                    }
                    buttonCooldown = 2;
                }
                break;
            case GLFW.GLFW_KEY_DOWN:
                break;
            case GLFW.GLFW_KEY_SPACE:
                hardDrop();
                break;
            case GLFW.GLFW_KEY_Z:
                rotateTetromino(-1);
                break;
            case GLFW.GLFW_KEY_X:
            case GLFW.GLFW_KEY_UP:
                rotateTetromino(1);
                break;
            case GLFW.GLFW_KEY_C:
                holdTetromino();
                break;
            default:
                return false;
        }

        return true;
    }

    public void tick() {
        if (gameOver) return;

        dropTimer++;
        if (dropTimer >= (isDropping ? 1 : 20)) {
            if (!moveTetromino(0, -1)) {
                placeTetromino(false);
                clearLines();
                spawnNewTetromino();
                holdUsed = false;
            } else {
                SoundHelper.tickSound();
            }
            dropTimer = 0;
        }
    }

    private void fillBag() {
        ArrayList<Tetromino.Shape> shapes = new ArrayList<>();
        for (Tetromino.Shape shape : Tetromino.Shape.values()) {
            shapes.add(shape);
        }
        Collections.shuffle(shapes, random);
        bag.addAll(shapes);
    }

    private void spawnNewTetromino() {
        if (bag.isEmpty()) {
            fillBag();
        }
        currentTetromino = new Tetromino(bag.poll());
        currentX = 4;
        currentY = 18;

        if (!isValidPosition(currentTetromino, currentX, currentY)) {
            gameOver = true;
        }
    }

    private boolean moveTetromino(int dx, int dy) {

        if (isValidPosition(currentTetromino, currentX + dx, currentY + dy)) {
            currentX += dx;
            currentY += dy;
            return true;
        }
        return false;
    }

    public void rotateTetromino(int direction) {
        // Create a new tetromino that represents the rotated state.
        Tetromino rotated = currentTetromino.rotate(direction);

        // Attempt to place the rotated tetromino in the current position.
        if (isValidPosition(rotated, currentX, currentY)) {
            currentTetromino = rotated;
            return;
        }

        // If the rotated piece cannot be placed in the current position, try to "kick" it one block to the left or right.
        int kick = direction > 0 ? 1 : -1; // Positive direction kicks right, negative direction kicks left.
        if (isValidPosition(rotated, currentX + kick, currentY)) {
            currentX += kick;
            currentTetromino = rotated;
            SoundHelper.rotateSound();
        }
    }


    private void hardDrop() {
        while (moveTetromino(0, -1));
        placeTetromino(true);
        SoundHelper.hardDropSound();
        clearLines();
        spawnNewTetromino();
        holdUsed = false;
    }

    private void holdTetromino() {
        if (holdUsed) return;

        if (holdTetromino == null) {
            holdTetromino = currentTetromino;
            spawnNewTetromino();
        } else {
            Tetromino temp = currentTetromino;
            currentTetromino = holdTetromino;
            holdTetromino = temp;
            currentX = 4;
            currentY = 18;
        }

        SoundHelper.swapSound();
        holdUsed = true;
    }

    private void placeTetromino(boolean soundHandled) {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (currentTetromino.getTileAt(x, y) != Tile.NONE) {
                    int worldX = currentX + x;
                    int worldY = currentY + y;
                    if (worldY < 24 && worldY >= 0 && worldX < 10 && worldX >= 0) {
                        screen[worldX][worldY] = currentTetromino.getTileAt(x, y);
                    }
                }
            }
        }
        if (!soundHandled) {
            SoundHelper.finalizeSound();
        }
    }

    private void clearLines() {
        for (int j = 0; j < 24; j++) {
            boolean fullLine = true;
            for (int i = 0; i < 10; i++) {
                if (screen[i][j] == Tile.NONE) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                for (int y = j; y < 23; y++) {
                    for (int x = 0; x < 10; x++) {
                        screen[x][y] = screen[x][y + 1];
                    }
                }
                for (int x = 0; x < 10; x++) {
                    screen[x][23] = Tile.NONE;
                }
                j--;
            }
        }
    }

    private boolean isValidPosition(Tetromino tetromino, int x, int y) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if (tetromino.getTileAt(i, j) != Tile.NONE) {
                    int newX = x + i;
                    int newY = y + j;
                    if (newX < 0 || newX >= 10 || newY < 0 || (newY < 24 && screen[newX][newY] != Tile.NONE)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void renderTetromino(DrawContext context, Tetromino tetromino, int x, int y, int offsetX, int offsetY) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if (tetromino.getTileAt(i, j) != Tile.NONE) {
                    int tileX = x + i;
                    int tileY = y + j;
                    if (tileY >= 0 && tileY < 20 && tileX >= 0 && tileX < 10) {
                        int id = tetromino.getTileAt(i, j).ordinal();
                        context.drawGuiTexture(TILE_ID[id - 1], offsetX + (tileX * 8), offsetY - (tileY * 8), 8, 8);
                    }
                }
            }
        }
    }

    private void renderTransparentTetromino(DrawContext context, Tetromino tetromino, int x, int y, int offsetX, int offsetY) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if (tetromino.getTileAt(i, j) != Tile.NONE) {
                    int tileX = x + i;
                    int tileY = y + j;
                    if (tileY >= 0 && tileY < 20 && tileX >= 0 && tileX < 10) {
                        int id = tetromino.getTileAt(i, j).ordinal();
                        DrawContextUtility.drawTransparentGuiTexture(context, TILE_ID[id - 1], offsetX + (tileX * 8), offsetY - (tileY * 8), 8, 8, 0.3f);
                    }
                }
            }
        }
    }
}
