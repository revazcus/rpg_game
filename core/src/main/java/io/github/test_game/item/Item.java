package io.github.test_game.item;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Предметы
 */
public class Item {

    public enum Type {
        COINS(0);

        int index;

        Type(int index) {
            this.index = index;
        }
    }

    private Type type;
    private Vector2 position;
    private float time;
    private float timeMax;
    private boolean active;

    /**
     * Полёт
     */
    private Vector2 velocity;

    public Item() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.type = Type.COINS;
        this.time = 0.0f;
        this.timeMax = 5.0f;
        this.active = false;
    }

    public void setup(float x, float y, Type type) {
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-20, 20), MathUtils.random(-20, 20));
        this.type = type;
        this.time = 0.0f; // Обязательно, чтобы новые созданные item не считались протухшими
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float deltaTime) {
        time += deltaTime;
        // Смещение предмета при обновлении
        position.mulAdd(velocity, deltaTime);
        if (time > timeMax) {
            deactivate();
        }
    }

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }
}
