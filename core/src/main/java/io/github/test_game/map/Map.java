package io.github.test_game.map;

// TODO доделать преграды на карте
public class Map {

    public static final int CELLS_X = 16;
    public static final int CELLS_Y = 0;

    private byte[][] data;

    public Map() {
        this.data = new byte[CELLS_X][CELLS_Y];
    }
}
