package io.github.test_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.test_game.item.Item;
import io.github.test_game.item.ItemsEmitter;
import io.github.test_game.map.Map;
import io.github.test_game.model.DarkKnight;
import io.github.test_game.model.Hero;
import io.github.test_game.model.MainModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameScreen {

    private SpriteBatch batch;

    /**
     * Позволяем выводить на экран текст и цифры.
     * Для русского текста нужно сгенерировать новый .fnt и .png через hiero, с указанием букв русского алфавита
     */
    private BitmapFont font24;

    private Hero hero;

    private Map map = new Map();

    private List<MainModel> allModels = new ArrayList<>();

    private ItemsEmitter itemsEmitter = new ItemsEmitter();

    // TODO Оставить единую коллекцию
    private List<DarkKnight> allDarkKnights = new ArrayList<>();

    /**
     * Кастомный компаратор для отображения одной модели поверх другой, в зависимости от высоты текущего положения
     */
    private final Comparator<MainModel> drawOrderComparator = (o1, o2) -> (int) (o2.getPosition().y - o1.getPosition().y);


    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    /**
     * Здесь создаётся всё
     */
    public void create() {
        hero = new Hero(this, new Texture("knight.png")); // ссылка на модуль assets, где должны лежать ресурсы

        allModels.addAll(List.of(
            hero,
            new DarkKnight(this, new Texture("dark_knight.png")),
            new DarkKnight(this, new Texture("dark_knight.png")),
            new DarkKnight(this, new Texture("dark_knight.png")),
            new DarkKnight(this, new Texture("dark_knight.png")),
            new DarkKnight(this, new Texture("dark_knight.png"))

        ));

        for (MainModel model : allModels) {
            if (model instanceof DarkKnight) {
                allDarkKnights.add((DarkKnight) model);
            }
        }

        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));

    }


    /**
     * Запускается в цикле и выполняется множество раз в секунду
     */
    public void render() {
        update();

        // RGBA (READ_GREEN_BLUE_ALFA - прозрачность)
        ScreenUtils.clear(0.0f, 0.5f, 0.0f, 1f);

        // Отрисовка между begin() и end()
        batch.begin();

        // Oтрисовка карты
        map.render(batch);

        allModels.stream().sorted(drawOrderComparator).forEach(x -> x.render(batch, font24));

        itemsEmitter.render(batch);
        hero.renderHUD(batch, font24);
        batch.end();
    }

    /**
     * Отдельный метод для расчёта логики отрисовки
     */
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime(); // промежуток времени между текущим кадром и последним кадром в секундах
        allModels.forEach(x -> x.update(deltaTime));

        // TODO переписать на стримы
        for (int i = 0; i < allDarkKnights.size(); i++) {
            DarkKnight knight = allDarkKnights.get(i);
            if (!knight.isAlive()) {
                allDarkKnights.remove(knight);
                allModels.remove(knight);
                // Когда рыцарь умирает, то на его месте появляются предметы
                itemsEmitter.generateRandomItem(knight.getPosition().x, knight.getPosition().y, 5, 0.8f);
                // Сообщаем герою, что он убил рыцаря и производим действие
                hero.killDarkKnight(knight);
            }
        }

        for (int j = 0; j < itemsEmitter.getItems().length; j++) {
            Item item = itemsEmitter.getItems()[j];
            if (item.isActive()) {
                float dst = hero.getPosition().dst(item.getPosition());
                if (dst < 15.0f) { // Радиус сбора предметов у героя
                    hero.takeItem(item);
                }
            }
        }
        itemsEmitter.update(deltaTime);

        /**
         * Код ниже можно использовать для дамажещей ауры
         */
//         Если дистанция между позициям героя и рыцаря меньше или равна сравниваемому значению
//        if (hero.getPosition().dst(darkKnight.getPosition()) <= 80.0f) {
//            hero.takeDamage(deltaTime * 10.0f); // если подошли близко, то уменьшение здоровья на 10 в секунду
//        }
    }

    public Hero getHero() {
        return hero;
    }

    public List<DarkKnight> getAllDarkKnights() {
        return allDarkKnights;
    }

    public Map getMap() {
        return map;
    }
}
