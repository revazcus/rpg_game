package io.github.test_game.text;

import com.badlogic.gdx.math.Vector2;
import io.github.test_game.item.Item;

public class FlyingText {
    private Vector2 position;
    private float time;
    private float timeMax;
    private boolean active;
    private StringBuilder text;

    /**
     * Полёт
     */
    private Vector2 velocity;

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.text = new StringBuilder();
        this.time = 0.0f;
        this.timeMax = 5.0f;
        this.active = false;
    }

    public void setup(float x, float y, StringBuilder text) {
        this.position.set(x, y);
        this.velocity.set(20, 40);
        this.text.setLength(0);
        this.text.append(text);
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

    public Vector2 getPosition() {
        return position;
    }

    public StringBuilder getText() {
        return text;
    }
}
