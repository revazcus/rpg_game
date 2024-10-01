package io.github.test_game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 * Основной код приложения тут
 * */
public class Main extends ApplicationAdapter {

    /**
     * Открывающееся окно (полотно)
     */
    private SpriteBatch batch;
    private GameScreen gameScreen;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.gameScreen = new GameScreen(batch);
        this.gameScreen.create();
    }


    @Override
    public void render() {
        gameScreen.render();
    }

    /**
     * Освобождение ресурсов (используется для уровней, чтобы быстро не закончилась память)
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

}
