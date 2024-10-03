package io.github.test_game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Управляющий класс для предметов.
 * Можно реализовать аналогичные управляющие классы для иных сущностей.
 */
public class ItemsEmitter {

    private Item[] items = new Item[50];
    private Texture texture;

    /**
     * Разбивка текстуры на части
     */
    private TextureRegion[] textureRegions;

    public ItemsEmitter() {
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item();
        }
        texture = new Texture("coin.png");
        textureRegions = new TextureRegion(texture).split(32, 32)[0];
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].isActive()) {
                batch.draw(textureRegions[items[i].getType().index], items[i].getPosition().x, items[i].getPosition().y);
            }
        }
    }

    /**
     * Генерация множественного лута
     * @param x - координата по ширине
     * @param y - координата по высоте
     * @param count - количество бросков на выпадение предмета
     * @param chance - шанс выпадения
     */
    public void generateRandomItem(float x, float y, int count, float chance) {
        for (int j = 0; j < count; j++) {
            float n = MathUtils.random(0.0f, 1.0f);
            if (n <= chance) {
                int type = MathUtils.random(0, Item.Type.values().length - 1);
                for (Item item : items) {
                    if (!item.isActive()) {
                        item.setup(x, y, Item.Type.values()[type]);
                        break;
                    }
                }
            }
        }

    }

    public void update(float deltaTime) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].isActive()) {
                items[i].update(deltaTime);
            }
        }
    }

    public Item[] getItems() {
        return items;
    }
}
