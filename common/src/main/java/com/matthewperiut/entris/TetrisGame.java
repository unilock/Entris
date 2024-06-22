package com.matthewperiut.entris;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.Random;

import static com.matthewperiut.entris.Entris.MOD_ID;
import static com.matthewperiut.entris.TetrisGame.Tile.*;

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

    Random random = new Random();

    public TetrisGame() {


        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 24; j++) {
                screen[i][j] = Tile.values()[random.nextInt(Tile.values().length - 1) + 1];
            }
        }
    }

    public void render(DrawContext context, int x, int y) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                int id = screen[i][j].ordinal();
                if (id > 0) {
                    context.drawGuiTexture(TILE_ID[id-1], x + (i*8), y - (j*8), 8, 8);
                }
            }
        }
    }
}
