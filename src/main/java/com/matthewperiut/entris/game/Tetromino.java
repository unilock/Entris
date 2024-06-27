package com.matthewperiut.entris.game;

import com.matthewperiut.entris.game.TetrisGame.Tile;

public class Tetromino {
    public enum Shape {
        I, O, T, S, Z, J, L
    }

    private static final Tile[][][] SHAPES = {
            // I
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.LIGHT_BLUE, Tile.LIGHT_BLUE, Tile.LIGHT_BLUE, Tile.LIGHT_BLUE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            // O
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.YELLOW, Tile.YELLOW, Tile.NONE},
                    {Tile.NONE, Tile.YELLOW, Tile.YELLOW, Tile.NONE},
                    {Tile.NONE, Tile.NONE, Tile.NONE, Tile.NONE}
            },
            // T
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.PURPLE, Tile.PURPLE, Tile.PURPLE},
                    {Tile.NONE, Tile.PURPLE, Tile.NONE}
            },
            // S
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.GREEN, Tile.GREEN, Tile.NONE},
                    {Tile.NONE, Tile.GREEN, Tile.GREEN}
            },
            // Z
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.NONE, Tile.RED, Tile.RED},
                    {Tile.RED, Tile.RED, Tile.NONE}
            },
            // J
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.BLUE, Tile.BLUE, Tile.BLUE},
                    {Tile.NONE, Tile.NONE, Tile.BLUE}
            },
            // L
            {
                    {Tile.NONE, Tile.NONE, Tile.NONE},
                    {Tile.ORANGE, Tile.ORANGE, Tile.ORANGE},
                    {Tile.ORANGE, Tile.NONE, Tile.NONE}
            }
    };

    private static final int[][][] KICK_OFFSETS = {
            // Offsets for I tetromino (kicks for 0->1, 1->2, 2->3, 3->0)
            {
                    {0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2},
                    {0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1},
                    {0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2},
                    {0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}
            },
            // Offsets for other tetrominos (J, L, S, Z, T) (kicks for 0->1, 1->2, 2->3, 3->0)
            {
                    {0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2},
                    {0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2},
                    {0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2},
                    {0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}
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
        return tiles[1][1];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tetromino rotate(int direction) {
        int size = tiles.length;
        Tile[][] rotatedTiles = new Tile[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (direction > 0) {
                    rotatedTiles[y][x] = tiles[size - 1 - x][y]; // Clockwise rotation
                } else {
                    rotatedTiles[y][x] = tiles[x][size - 1 - y]; // Counter-clockwise rotation
                }
            }
        }
        return new Tetromino(this.shape, rotatedTiles, (this.rotationState + direction + 4) % 4);
    }

    private Tetromino(Shape shape, Tile[][] tiles, int rotationState) {
        this.shape = shape;
        this.tiles = tiles;
        this.rotationState = rotationState;
    }

    public Tile getTileAt(int x, int y) {
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
            return Tile.NONE;
        }
        return tiles[y][x];
    }

    public int getRotationState() {
        return rotationState;
    }

    public Shape getShape() {
        return shape;
    }

    public boolean isValidPosition(Tile[][] screen, int x, int y) {
        for (int j = 0; j < tiles.length; j++) {
            for (int i = 0; i < tiles[j].length; i++) {
                if (tiles[j][i] != Tile.NONE) {
                    int newX = x + i;
                    int newY = y + j;
                    if (newX < 0 || newX >= screen.length || newY < 0 || newY >= screen[0].length || screen[newX][newY] != Tile.NONE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static int[][] getKickOffsets(Shape shape, int rotationState, int direction) {
        if (shape == Shape.I) {
            return KICK_OFFSETS[0];
        } else if (shape != Shape.O) {
            return KICK_OFFSETS[1];
        } else {
            return new int[][] {{0, 0}}; // No kick offsets for O tetromino
        }
    }
}
