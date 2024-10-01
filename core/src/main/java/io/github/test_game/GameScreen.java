package io.github.test_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.test_game.model.DarkKnight;
import io.github.test_game.model.Hero;

public class GameScreen {

    private SpriteBatch batch;

    private Hero hero;
    private DarkKnight darkKnight;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    /**
     * Здесь создаётся всё
     */
    public void create() {
        hero = new Hero(new Texture("knight.png")); // ссылка на модуль assets, где должны лежать ресурсы
        darkKnight = new DarkKnight(this, new Texture("dark_knight.png")); // ссылка на модуль assets, где должны лежать ресурсы
    }


    /**
     * Запускается в цикле и выполняется множество раз в секунду
     */
    public void render() {
        update();

        ScreenUtils.clear(0.0f, 0.5f, 0.0f, 1f); // RGBA (READ_GREEN_BLUE_ALFA - прозрачность)
        batch.begin();

        // Отрисовка между begin() и end()
        hero.render(batch);
        darkKnight.render(batch);

        batch.end();
    }

    /**
     * Отдельный метод для расчёта логики отрисовки
     */
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime(); // промежуток времени между текущим кадром и последним кадром в секундах
        hero.update(deltaTime);
        darkKnight.update(deltaTime);

        float dst = (float) Math.sqrt(
            (hero.getX() - darkKnight.getX()) * (hero.getX() - darkKnight.getX()) +
                (hero.getY() - darkKnight.getY()) * (hero.getY() - darkKnight.getY())); // расчёт дистанции между героем и рыцарем

        if (dst <= 80.0f) {
            hero.takeDamage(deltaTime * 10.0f); // если подошли близко, то уменьшение здоровья на 10 в секунду
        }
    }

    public Hero getHero() {
        return hero;
    }

    public DarkKnight getDarkKnight() {
        return darkKnight;
    }
}
