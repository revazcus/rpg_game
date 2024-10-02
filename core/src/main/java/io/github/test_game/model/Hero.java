package io.github.test_game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.test_game.GameScreen;

/**
 * Модель героя
 */
public class Hero extends MainModel {

    public Hero(GameScreen gameScreen, Texture texture) {
        this.texture = texture;
        this.position = new Vector2(200.0f, 200.0f);
        this.speed = 200.0f;
        this.hp = 100.0f;
        this.hpMax = hp;
        this.textureHP = new Texture("hp_bar.png");
        this.weapon = new Weapon("Sword", 50.0f, 0.5f, 10f);
        this.gameScreen = gameScreen;
    }

    /**
     * Что происходит с рыцарем в цикле
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        /**
         * Код ниже отвечает за логику нанесения урона героем
         */
        // Высчитываем дистанцию между героем и рыцарем
        float dst = gameScreen.getDarkKnight().getPosition().dst(position);
        // Если дистанция меньше, чем радиус оружия
        if (dst < weapon.getAttackRadius()) {
            attackTimer += deltaTime;
            // Если промежуток, между атаками героя больше, чем скорость атаки оружия
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                gameScreen.getDarkKnight().takeDamage(weapon.getDamage());
            }
        }

        // Блок с использованием клавиатуры и стрелочек
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += speed * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= speed * deltaTime;
        }
        checkScreenBounds();
    }

    public Vector2 getPosition() {
        return position;
    }
}
