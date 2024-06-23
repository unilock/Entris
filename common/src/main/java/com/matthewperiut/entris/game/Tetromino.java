package com.matthewperiut.entris.game;

import java.util.Random;

import com.matthewperiut.entris.game.TetrisGame.Tile;

public class Tetromino {
    public enum Shape {
        I, O, T, S, Z, J, L
    }

    private static final Tile[][][] SHAPES = {
            { // I
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.LIGHT_BLUE, Tile.LIGHT_BLUE, Tile.LIGHT_BLUE, Tile.LIGHT_BLUE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            { // O
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.YELLOW, Tile.YELLOW, Tile.NONE},
                    {Tile.NONE, Tile.YELLOW, Tile.YELLOW, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            { // T
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.PURPLE, Tile.PURPLE, Tile.PURPLE},
                    {Tile.NONE, Tile.NONE, Tile.PURPLE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            { // S
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.GREEN, Tile.GREEN, Tile.NONE},
                    {Tile.GREEN, Tile.GREEN, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            { // Z
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.RED, Tile.RED, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.RED, Tile.RED, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            { // J
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.BLUE, Tile.BLUE, Tile.BLUE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.BLUE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            { // L
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.ORANGE, Tile.ORANGE, Tile.ORANGE, Tile.NONE},
                    {Tile.ORANGE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            }
    };

    // Super Rotation System (SRS) kick data
    private static final int[][][] SRS_KICKS = {
            { // Kicks for I piece
                    { 0, 0 }, { -2, 0 }, { 1, 0 }, { -2, -1 }, { 1, 2 },
                    { 0, 0 }, { -1, 0 }, { 2, 0 }, { -1, 2 }, { 2, -1 },
                    { 0, 0 }, { 2, 0 }, { -1, 0 }, { 2, 1 }, { -1, -2 },
                    { 0, 0 }, { 1, 0 }, { -2, 0 }, { 1, -2 }, { -2, 1 }
            },
            { // Kicks for all other pieces
                    { 0, 0 }, { -1, 0 }, { -1, 1 }, { 0, -2 }, { -1, -2 },
                    { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, -2 }, { 1, -2 },
                    { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, 2 }, { 1, 2 },
                    { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, 2 }, { -1, 2 }
            }
    };

    private Tile[][] tiles;
    private Shape shape;
    private int rotationState;

    public Tetromino(Shape shape) {
        this.shape = shape;
        this.tiles = SHAPES[shape.ordinal()];
        this.rotationState = 0;
    }

    public Tile getTile() {
        return tiles[1][1]; // Assuming the center tile defines the color
    }

    public int getX(int index) {
        return index % 4;
    }

    public int getY(int index) {
        return index / 4;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tetromino rotate(int direction) {
        Tile[][] rotatedTiles = new Tile[4][4];
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (direction > 0) {
                    rotatedTiles[x][3 - y] = tiles[y][x];
                } else {
                    rotatedTiles[3 - x][y] = tiles[y][x];
                }
            }
        }
        Tetromino rotated = new Tetromino(shape);
        rotated.setTiles(rotatedTiles);
        rotated.rotationState = (rotationState + direction + 4) % 4;
        return rotated;
    }

    public int[] getSrsKick(int direction, int testIndex) {
        int[][] kicks = shape == Shape.I ? SRS_KICKS[0] : SRS_KICKS[1];
        int from = rotationState;
        int to = (rotationState + direction + 4) % 4;
        return new int[]{kicks[from * 5 + testIndex][0] - kicks[to * 5 + testIndex][0],
                kicks[from * 5 + testIndex][1] - kicks[to * 5 + testIndex][1]};
    }

    public static Tetromino getRandom() {
        Random random = new Random();
        Shape shape = Shape.values()[random.nextInt(Shape.values().length)];
        return new Tetromino(shape);
    }

    public Tile getTileAt(int x, int y) {
        return tiles[y][x];
    }
}
