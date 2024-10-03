package io.github.test_game.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Обработчик всплывающих подсказок
 */
public class FlyingTextEmitter {

    private FlyingText[] texts = new FlyingText[50];

    public FlyingTextEmitter() {
        for (int i = 0; i < texts.length; i++) {
            texts[i] = new FlyingText();
        }
    }

    public void render(SpriteBatch batch, BitmapFont font24) {
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].isActive()) {
                font24.draw(batch, texts[i].getText(), texts[i].getPosition().x, texts[i].getPosition().y);
            }
        }
    }

    public void setup(float x, float y, StringBuilder text) {
        for (FlyingText flyingText : texts) {
            if (!flyingText.isActive()) {
                flyingText.setup(x, y, text);
                break;
            }
        }
    }


    public void update(float deltaTime) {
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].isActive()) {
                texts[i].update(deltaTime);
            }
        }
    }


    public FlyingText[] getTexts() {
        return texts;
    }
}
