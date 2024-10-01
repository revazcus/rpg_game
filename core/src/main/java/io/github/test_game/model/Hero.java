package io.github.test_game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Модель героя
 */
public class Hero {

    private Texture texture;
    private Texture textureHP;
    private float speed;

    private float hp, hpMax;

    /**
     * Координаты
     */
    private float x;
    private float y;

    public Hero(Texture texture) {
        this.texture = texture;
        x = 200.0f;
        y = 200.0f;
        speed = 200.0f;
        hp = 100.0f;
        hpMax = hp;
        textureHP = new Texture("hp_bar.png");
    }

    /**
     * Отрисовка самого себя
     */
    public void render(SpriteBatch batch) { // принимает в параметр ссылку на полотно
        batch.draw(texture, x, y); // отрисовка с текстурой и точкой, где стоит

        batch.setColor(1, 0, 0, 1); // покрасить всех в красный
        batch.draw(textureHP, x + 28, y + 130, 0, 0, hp, 20, 1, 1, 0, 0, 0, 80, 20, false, false); // отрисовка полоски ХП с текстурой и точкой + уменьшение красного цвета, если подошли близко к рыцарю
        batch.setColor(1, 1, 1, 1); // покрасить только белую полоску в красный

    }

    public void takeDamage(float amount) {
        hp -= amount;
    }

    /**
     * Что происходит с рыцарем в цикле
     */
    public void update(float deltaTime) {

        // Блок с использованием клавиатуры и стрелочек
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * deltaTime;
        }
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
