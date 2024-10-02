package io.github.test_game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.test_game.GameScreen;

import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<DarkKnight> nearestKnight = new AtomicReference<>();
        AtomicReference<Float> minDist = new AtomicReference<>(Float.MAX_VALUE);
        gameScreen.getAllDarkKnights().forEach(darkKnight -> {
            float dst = darkKnight.getPosition().dst(position);
            if (dst < minDist.get()) {
                minDist.set(dst);
                nearestKnight.set(darkKnight);
            }
        });


        // Если дистанция меньше, чем радиус оружия
        if (minDist.get() < weapon.getAttackRadius()) {
            attackTimer += deltaTime;
            // Если промежуток, между атаками героя больше, чем скорость атаки оружия
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                nearestKnight.get().takeDamage(weapon.getDamage());
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
