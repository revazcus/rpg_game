package io.github.test_game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Map {

    public static final int CELLS_X = 16;
    public static final int CELLS_Y = 19;

    /**
     * Размер клетки
     */
    public static final int CELL_SIZE = 80;

    private byte[][] data;

    private Texture floor;
    private Texture water;

    public Map() {
        this.data = new byte[CELLS_X][CELLS_Y];
        for (int i = 0; i < 15; i++) {
            data[MathUtils.random(0, CELLS_X - 1)][MathUtils.random(0, CELLS_Y - 1)] = 1;
        }
        floor = new Texture("floor.png");
        water = new Texture("water.png");
    }

    /**
     * Определяем проходимость клетки делением текущих координат на размер клетки
     */
    public boolean isCellPassable (Vector2 position) {
        if (position.x < 0.0f || position.x > 1280.0f || position.y < 0.0f || position.y > 720.0f) {
            return false;
        }
        return data[(int) (position.x / CELL_SIZE)][(int) (position.y / CELL_SIZE)] == 0;
    }

    // Сюда добавляются элементы, которые будут отображаться на карте (в том числе динамические)
    public void render(SpriteBatch batch) {
        // Заполнение земли по фиксированному размеру экрана 16:9
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(floor, i * 80, j * 80);
                if (data[i][j] == 1) {
                    batch.draw(water, i * 80, j * 80);
                }
            }
        }
    }
}
