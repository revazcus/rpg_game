package io.github.test_game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Map {

    public static final int CELLS_X = 16;
    public static final int CELLS_Y = 19;

    /**
     * Размер клетки
     */
    public static final int CELL_SIZE = 80;

    /**
     * Слой карты с указанием местности
     */
    private byte[][] dataLayer = new byte[CELLS_X][CELLS_Y];

    /**
     * Слой карты с указанием типа местности
     */
    private byte[][] typeLayer = new byte[CELLS_X][CELLS_Y];

    private Texture grass;
    private Texture rocks;
    private TextureRegion[] regions;

    public Map() {
        for (int i = 0; i < 15; i++) {
            int cellX = MathUtils.random(0, CELLS_X - 1);
            int cellY = MathUtils.random(0, CELLS_Y - 1);

            dataLayer[cellX][cellY] = 1;
            typeLayer[cellX][cellY] = (byte) MathUtils.random(0, 2);
        }
        grass = new Texture("grass.png");
        rocks = new Texture("rocks.png");

        regions = new TextureRegion(rocks).split(115, 120)[0];
    }

    /**
     * Определяем проходимость клетки делением текущих координат на размер клетки
     */
    public boolean isCellPassable (Vector2 position) {
        if (position.x < 0.0f || position.x > 1280.0f || position.y < 0.0f || position.y > 720.0f) {
            return false;
        }
        return dataLayer[(int) (position.x / CELL_SIZE)][(int) (position.y / CELL_SIZE)] == 0;
    }

    // Сюда добавляются элементы, которые будут отображаться на карте (в том числе динамические)
    public void render(SpriteBatch batch) {
        // Заполнение земли по фиксированному размеру экрана 16:9

        /**
         * Отрисовка земли
         */
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(grass, i * 80, j * 80);
            }
        }

        /**
         * Отрисовка камней
         */
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (dataLayer[i][j] == 1) {
                    // Здесь выбираем какой именно камень отрисовать по значению из typeLayer от 0 до 2
                    batch.draw(regions[typeLayer[i][j]], i * 80 - 10, j * 80 - 10);
                }
            }
        }
    }
}
