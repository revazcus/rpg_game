package io.github.test_game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.test_game.GameScreen;

public class DarkKnight extends MainModel {
    /**
     * Используется для выбора действия в заданное время
     */
    private float moveTimer;
    private float activityRadius;

    public DarkKnight(GameScreen gameScreen, Texture texture) {
        this.gameScreen = gameScreen;
        this.texture = texture;
        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while (!gameScreen.getMap().isCellPassable(position)) {
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }
        this.direction = new Vector2(0f, 0f);
        this.temp = new Vector2(0f, 0f);
        this.speed = 100.0f;
        this.activityRadius = 150.0f;
        this.hp = 50;
        this.hpMax = hp;
        this.weapon = new Weapon("Mace", 100.0f, 1.0f, 50.0f);
        this.textureHP = new Texture("hp_bar.png");
    }

    /**
     * Что происходит с рыцарем в цикле
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float dst = gameScreen.getHero().getPosition().dst(position);
        // Если расстояние между рыцарем и героем меньше радиуса рыцаря, то рыцарь будет двигаться в сторону героя
        if (dst < activityRadius) {
            direction.set(gameScreen.getHero().getPosition()).sub(position).nor();// Вычитаем из вектора героя, вектор рыцаря, чтобы рыцарь шёл в сторону героя
        } else {
            moveTimer -= deltaTime;
            if (moveTimer < 0.0f) {
                moveTimer = MathUtils.random(2.0f, 3.0f);
                direction.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f)); // Выбор случайного направления
                direction.nor(); // Нормирование вектора движения
            }
        }

        move(deltaTime);

        // Если рыцарь подошёл на расстояние удара
        if (dst < weapon.getAttackRadius()) {
            attackTimer += deltaTime;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                gameScreen.getHero().takeDamage(weapon.getDamage());
            }
        }
        checkScreenBounds();
    }

    @Override
    public void showHpBar(SpriteBatch batch, BitmapFont font24) {
        batch.setColor(0, 0, 0, 1); // покрасить в чёрный
        batch.draw(textureHP, position.x + 65 - 2, position.y + 225 - 2, 103, 24); // рамка здоровья

        batch.setColor(1, 0, 0, 1); // покрасить всех в красный
        batch.draw(textureHP, position.x + 65, position.y + 225, 0, 0, hp / hpMax * 100, 20, 1, 1, 0, 0, 0, 80, 20, false, false); // отрисовка полоски ХП с текстурой и точкой + уменьшение красного цвета, если подошли близко к рыцарю
        batch.setColor(1, 1, 1, 1); // покрасить только белую полоску в красный

        // Отображение цифр HP
        font24.draw(batch, String.valueOf((int) hp), position.x + 75, position.y + 244, 80, 1, false);
    }

    public Vector2 getPosition() {
        return position;
    }
}
