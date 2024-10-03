package io.github.test_game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.test_game.GameScreen;

public abstract class MainModel {
    Texture texture;
    Texture textureHP;
    /**
     * Позиция модели по ширине и высоте
     */
    Vector2 position;
    float speed;
    float hp, hpMax;
    float damageEffectTimer;
    float attackTimer;
    GameScreen gameScreen;
    Weapon weapon;
    /**
     * Направление перемещения
     */
    Vector2 direction;
    /**
     * Временной вектор для промежуточных расчётов
     */
    Vector2 temp;

    public Vector2 getPosition() {
        return position;
    }

    public void update(float deltaTime) {
        damageEffectTimer -= deltaTime;
        if (damageEffectTimer < 0.0f) {
            damageEffectTimer = 0.0f;
        }
    };

    /**
     * Отрисовка самого себя
     */
    public void render(SpriteBatch batch, BitmapFont font24) { // принимает в параметр ссылку на полотно
        /**
         * Блок кода с 33 по 37 строчки позволяет создать эффект получения урона у персонажа
         */
        // Если персонаж получил урон, то покрасим его в красный цвет
        if (damageEffectTimer > 0.0f) {
            // Создаёт эффект получения урона у персонажа
            batch.setColor(1, 1 - damageEffectTimer, 1 - damageEffectTimer, 1);
        }
        batch.draw(texture, position.x - 40, position.y - 40); // отрисовка с текстурой и точкой, где стоит
        batch.setColor(1, 1, 1, 1);
        showHpBar(batch, font24);
    }

    /**
     * Отрисовка полоски здоровья
     */
    public void showHpBar(SpriteBatch batch, BitmapFont font24) {
        batch.setColor(0, 0, 0, 1); // покрасить в чёрный
        batch.draw(textureHP, position.x + 28 - 42, position.y + 130 - 42, 103, 24); // рамка здоровья

        batch.setColor(1, 0, 0, 1); // покрасить всех в красный
        batch.draw(textureHP, position.x + 28 - 40, position.y + 130 - 40, 0, 0, hp / hpMax * 100, 20, 1, 1, 0, 0, 0, 80, 20, false, false); // отрисовка полоски ХП с текстурой и точкой + уменьшение красного цвета, если подошли близко к рыцарю
        batch.setColor(1, 1, 1, 1); // покрасить только белую полоску в красный

        // Отображение цифр HP
        font24.draw(batch, String.valueOf((int) hp), position.x, position.y + 109, 80, 1, false);
    }

    /**
     * Не даём рыцарю выйти за пределы экрана
     */
    public void checkScreenBounds() {
        if (position.x > 1280.0f) {
            position.x = 1280.0f;
        }
        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.y > 720.0f) {
            position.y = 720.0f;
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
    }

    public void takeDamage(float amount) {
        hp -= amount;
        damageEffectTimer += 0.5f;
        if (damageEffectTimer > 1.0f) {
            damageEffectTimer = 1.0f;
        }
    }

    public boolean isAlive() {
        return hp > 0.0f;
    }

    public void move(float deltaTime) {
        // В каждом кадре мы к текущей позиции прибавляем вектор направления, умноженный на заданную скорость и промежуток времени между текущим кадром и последним кадром
        temp.set(position).mulAdd(direction, speed * deltaTime);

        // Если клетка проходима, то совершаем движение
        if (gameScreen.getMap().isCellPassable(temp)) {
            position.set(temp);
        }
    }
}
