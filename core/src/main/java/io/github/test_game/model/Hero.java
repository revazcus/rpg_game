package io.github.test_game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.test_game.GameScreen;
import io.github.test_game.item.Item;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Модель героя
 */
public class Hero extends MainModel {

    private int coins;
    private int level;
    private int exp;

    /**
     * Сетка уровней
     */
    private int[] expTo = {0, 0, 100, 300, 500, 750, 1000, 2000};

    public Hero(GameScreen gameScreen, Texture texture) {
        this.level = 1;
        this.texture = texture;
        this.position = new Vector2(200.0f, 200.0f);
        while (!gameScreen.getMap().isCellPassable(position)) {
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }
        this.speed = 200.0f;
        this.hp = 100.0f;
        this.hpMax = hp;
        this.textureHP = new Texture("hp_bar.png");
        this.weapon = new Weapon("Sword", 50.0f, 0.5f, 10f);
        this.gameScreen = gameScreen;
        this.direction = new Vector2(0, 0);
        this.temp = new Vector2(0, 0);
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

        // Сбрасываем направление в каждой итерации цикла
        direction.set(0, 0);

        // Использование стрелок клавиатуры для перемещения
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.y = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = -1.0f;
        }
        move(deltaTime);
        checkScreenBounds();
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * Отрисовка интерфейса
     */
    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        font24.draw(batch, "Hero: Player\n" + // Имя
            "Level: " + level + "\n" + // Уровень
            "Exp: " + exp + " / " + expTo[level + 1] + "\n" + // Опыта + сколько нужно до следующего левела
            "Coins: " + coins, 20, 700); // Количество монет
    }

    /**
     * Действие при поднятии предмета
     * @param item - выпавший предмет
     */
    public void takeItem(Item item) {
        switch (item.getType()) {
            case COINS:
                coins += MathUtils.random(10, 20);
                break;
        }
        item.deactivate();
    }

    /**
     * Действие при убийстве моба.
     * На текущий момент увеличивается уровень и восстанавливается хп по lvlUp
     */
    public void killDarkKnight(DarkKnight darkKnight) {
        exp += (int) (darkKnight.hpMax * 2);

        /**
         * Здесь прикручиваются награды за повышение уровня
         */
        if (exp >= expTo[level + 1]) {
            level++;
            exp -= expTo[level];
            hpMax += 50;
            hp = hpMax;
        }
    }
}
