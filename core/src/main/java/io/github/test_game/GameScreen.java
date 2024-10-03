package io.github.test_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.test_game.item.Item;
import io.github.test_game.item.ItemsEmitter;
import io.github.test_game.map.Map;
import io.github.test_game.model.DarkKnight;
import io.github.test_game.model.Hero;
import io.github.test_game.model.MainModel;
import io.github.test_game.text.FlyingTextEmitter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameScreen {

    private SpriteBatch batch;

    /**
     * Используется для интерфейса
     */
    private Stage stage = new Stage();

    /**
     * Позволяем выводить на экран текст и цифры.
     * Для русского текста нужно сгенерировать новый .fnt и .png через hiero, с указанием букв русского алфавита
     */
    private BitmapFont font24;

    private Hero hero;

    private Map map = new Map();

    private List<MainModel> allModels = new ArrayList<>();

    private ItemsEmitter itemsEmitter = new ItemsEmitter();
    private FlyingTextEmitter flyingTextEmitter = new FlyingTextEmitter();

    private boolean paused;

    // Музыкальное сопровождение игры
    private Music music;

    // Звуковые эффекты
    private Sound sound;

    private float spawnTimer;

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
        hero = new Hero(this, new Texture("barbarian_sprite_upd.png")); // ссылка на модуль assets, где должны лежать ресурсы

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

        /**
         * Блок с добавлением кнопки
         */
        // Создаём скин
        Skin skin = new Skin();
        skin.add("button", new Texture("button.png"));

        // Создаём стиль
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button"); // можно поставить down, чтобы картинка зажатой кнопки отличалась
        textButtonStyle.font = font24;

        // Создаём кнопки
        TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);

        // Мапим паузу на нажатие
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });

        // Мапим выход на нажатие
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Объединяем кнопки в меню
        Group menuGroup = new Group();
        menuGroup.addActor(pauseButton);
        menuGroup.addActor(exitButton);

        // Передвижением кнопок в меню
        exitButton.setPosition(150, 0);

        // Передвижение меню
        menuGroup.setPosition(975, 630);

        // Добавить в сцену кнопку
        stage.addActor(menuGroup);

        // Фреймворк отдаёт управление созданному интерфейсе
        Gdx.input.setInputProcessor(stage);

        // Определение музики
        music = Gdx.audio.newMusic(Gdx.files.internal("DRIVE(chosic.com).mp3"));

        // Зацикливание музыки
        music.setLooping(true);

        // Громкость
        music.setVolume(0.5f);

        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("slash1-94367.mp3"));

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

        flyingTextEmitter.render(batch, font24);
        itemsEmitter.render(batch);
        hero.renderHUD(batch, font24);

        // Отрисовка интерфейса
        stage.draw();

        batch.end();
    }

    /**
     * Отдельный метод для расчёта логики отрисовки
     */
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime(); // промежуток времени между текущим кадром и последним кадром в секундах
        if (!paused) { // проверяем, стоит ли на паузе игра

//            // Если прикоснулись к экрану то получаем место касания
//            if (Gdx.input.justTouched()) {
//                Gdx.input.getX();
//            }

            spawnTimer += deltaTime;
            if (spawnTimer > 2.0f) {
                DarkKnight darkKnight = new DarkKnight(this, new Texture("dark_knight.png"));
                allModels.add(darkKnight);
                allDarkKnights.add(darkKnight);
                spawnTimer = 0.0f;
            }
            allModels.forEach(x -> x.update(deltaTime));
            // TODO переписать на стримы
            for (int i = 0; i < allDarkKnights.size(); i++) {
                DarkKnight knight = allDarkKnights.get(i);
                if (!knight.isAlive()) {
                    allDarkKnights.remove(knight);
                    allModels.remove(knight);
                    // Когда рыцарь умирает, то на его месте появляются предметы
                    itemsEmitter.generateRandomItem(knight.getPosition().x + 10, knight.getPosition().y, 5, 0.2f);
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
            flyingTextEmitter.update(deltaTime);
            itemsEmitter.update(deltaTime);

            /**
             * Код ниже можно использовать для дамажещей ауры
             */
//         Если дистанция между позициям героя и рыцаря меньше или равна сравниваемому значению
//        if (hero.getPosition().dst(darkKnight.getPosition()) <= 80.0f) {
//            hero.takeDamage(deltaTime * 10.0f); // если подошли близко, то уменьшение здоровья на 10 в секунду
//        }
        }
        stage.act(deltaTime);
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

    public FlyingTextEmitter getFlyingTextEmitter() {
        return flyingTextEmitter;
    }

    public Sound getSound() {
        return sound;
    }
}
