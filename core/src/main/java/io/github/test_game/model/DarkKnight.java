package io.github.test_game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.test_game.GameScreen;

public class DarkKnight {

    private Texture texture;
    private float speed;
    private float radius;

    private float x;
    private float y;

    private GameScreen gameScreen;

    public DarkKnight(GameScreen gameScreen, Texture texture) {
        this.gameScreen = gameScreen;
        this.texture = texture;
        x = 1000.0f;
        y = 100.0f;
        radius = 200.0f;
        speed = 100.0f;
    }

    public void render(SpriteBatch batch) { // принимает в параметр ссылку на полотно
        batch.draw(texture, x, y); // отрисовка с текстурой и точкой, где стоит
    }

    /**
     * Что происходит с рыцарем в цикле
     */
    public void update(float deltaTime) {
        float dst = (float) Math.sqrt(
            (gameScreen.getHero().getX() - this.x) * (gameScreen.getHero().getX() - this.x) +
                (gameScreen.getHero().getY() - this.y) * (gameScreen.getHero().getY() - this.y)); // получаем квадратный корень из квадрата разности расстояний между героем и рыцарем

        if (dst <= radius) { // Если герой подошёл, то рыцарь пойдёт за ним
            if (x < gameScreen.getHero().getX()) { // Если герой правее, то двигаемся вправо
                x += speed * deltaTime;
            }
            if (x > gameScreen.getHero().getX()) { // Если герой левее, то двигаемся влево
                x -= speed * deltaTime;
            }
            if (y < gameScreen.getHero().getY()) { // Если герой выше, то двигаемся выше
                y += speed * deltaTime;
            }
            if (y > gameScreen.getHero().getY()) { // Если герой ниже, то двигаемся ниже
                y -= speed * deltaTime;
            }
        } else {
            x -= speed * deltaTime;
            if (x <= 0.0f) {
                x = 1280.0f;
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
