package test.game;

import game.GameTime;
import map.GameMap;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import objects.*;
import units.*;
import game.GameEngine;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.io.IOException;

class GameTest {
    private static final Logger logger = Logger.getLogger(GameTest.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("game_tests.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Не удалось настроить логгер: " + e.getMessage());
        }
    }

    private GameEngine game;
    private Hero hero;
    private Enemy enemy;
    private Castle castle;
    private Castle castleEnemy;
    private List<Hero> heroes;
    private List<Enemy> enemys;
    private Hotel hotel;
    GameTime gameTime;

    @BeforeEach
    void setUp() {
        logger.info("Начало настройки тестового окружения");
        game = new GameEngine();
        game.initializeGame(15, 15);
        heroes = new ArrayList<>();
        enemys = new ArrayList<>();

        hero = new Hero("Тестовый герой", 5, 5, 1000);
        enemy = new Enemy(15, 10);

        heroes.add(hero);
        enemys.add(enemy);

        castle = new Castle(5, 5);
        castleEnemy = new Castle(15, 10);
        gameTime = new GameTime();
        gameTime.start();
        logger.info("Тестовое окружение успешно настроено");
    }

    @Test
    @DisplayName("Тест победы через уничтожение врагов")
    void testWinByKillEnemy() {
        logger.info("Запуск теста победы через уничтожение врагов");
        enemys.clear();
        assertTrue(game.checkPlayerWinConditions("winByEnemy"), "Игрок должен победить, когда все враги уничтожены");
        logger.info("Тест победы через уничтожение врагов завершен успешно");
    }

    @Test
    @DisplayName("Тест победы  через захват замка")
    void testWinByCapturingCastle() {
        logger.info("Запуск теста победы через захват замка");
        hero.setPosition(castleEnemy.getX(), castleEnemy.getY());

        assertTrue(game.checkPlayerWinConditions("winByCastle"), "Игрок должен победить, захватив замок врага");
        logger.info("Тест победы через захват замка завершен успешно");
    }

    @Test
    @DisplayName("Тест поражения через потерю героев")
    void testLoseByLosingHeroes() {
        logger.info("Запуск теста поражения при потере всех героев");
        heroes.clear();

        assertTrue(game.checkPlayerLoseConditions("loseByEnemy"), "Игрок должен проиграть, когда все герои уничтожены");
        logger.info("Тест поражения при потере героев завершен");
    }

    @Test
    @DisplayName("Тест поражения через потерю замка")
    void testLoseByEnemyCapturingCastle() {
        logger.info("Запуск теста поражения при захвате замка врагом");
        enemy.setPosition(castle.getX(), castle.getY());
        assertTrue(game.checkPlayerLoseConditions("loseByCastle"), "Игрок должен проиграть, если враг захватил его замок");
        logger.info("Тест поражения при захвате замка врагом завершен");
    }

    @Test
    @DisplayName("Тест передачи золота после победы")
    void testGoldTransferAfterVictory() {
        logger.info("Тестирование передачи золота после победы");
        double initialGold = hero.getGold();
        double enemyGold = 500;
        enemy.setGold(enemyGold);

        hero.setWin();
        hero.setGold(hero.getGold() + enemy.getGold());
        enemys.clear();

        assertEquals(initialGold + enemyGold, hero.getGold(), "Золото врага должно перейти к игроку после победы");
        logger.info("Тест передачи золота завершен успешно");
    }

    @Test
    @DisplayName("Тест работы с несколькими героями")
    void testMultipleHeroes() {
        logger.info("Тестирование работы с несколькими героями");
        Hero secondHero = new Hero("Второй герой", 3, 3, 500);
        heroes.add(secondHero);

        hero.setLose(true);
        heroes.remove(hero);

        assertFalse(heroes.isEmpty(), "Игра должна продолжаться, пока есть живые герои");
        assertEquals(secondHero, heroes.get(0), "Активным должен стать следующий герой");
        logger.info("Тест работы с несколькими героями завершен успешно");
    }

    @Test
    @DisplayName("Тест игрового цикла")
    void testGameCycle(){
        logger.info("Тестирование игрового цикла");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));
        try {
            for (int i = 0; i < 8; i++) {
                game.updateGameCycle();
            }

            System.setOut(originalOut);

            String consoleOutput = output.toString();
            assertFalse(consoleOutput.isEmpty(), "Игровой цикл должен что-то выводить");
            System.out.println("Перехваченный вывод:\n" + consoleOutput);
            logger.info("Игровой цикл отработал успешно");
        } catch (Exception e) {
            logger.severe("Ошибка в игровом цикле: " + e.getMessage());
            fail("Игровой цикл завершился с ошибкой");
        }
    }

    @Test
    @DisplayName("Тест перемещения героя")
    void testHeroMove() {
        logger.info("Тестирование перемещения героя");
        try {
            hero.setPosition(1, 1);
            game.getMap().setCellType(1, 0, "sea");
            game.getMap().setCellFeature(1, 0, "no");
            double count = hero.getMoveCount();
            logger.warning("Герой пытается попасть на клетку моря");
            hero.move("w", game.getMap(), castle, heroes, enemy, castleEnemy, hotel, gameTime);
            double newCount = hero.getMoveCount();
            assertEquals(0, newCount-count);
            hero.setPosition(1, 1);
            game.getMap().setCellType(1, 0, "mountain");
            game.getMap().setCellFeature(1, 0, "no");
            count = hero.getMoveCount();
            logger.warning("Герой пытается попасть на клетку горы");
            hero.move("w", game.getMap(), castle, heroes, enemy, castleEnemy, hotel, gameTime);
            newCount = hero.getMoveCount();
            assertEquals(0, newCount-count);
            logger.info("Тест перемещения героя завершен: море и горы непроходимы");
        } catch (Exception e) {
            logger.severe("Ошибка при тестировании перемещения героя: " + e.getMessage());
            fail("Тест перемещения героя завершился с ошибкой");
        }
    }
}