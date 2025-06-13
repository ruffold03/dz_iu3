package test.game;

import game.GameEngine;
import map.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameLoadTest {

    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
        gameEngine.initializeGame(15, 15);
    }

    @DisplayName("Тест загрузки игры из файла")
    @Test
    void testLoadGame() {
        String saveFileName = "test_game_save";

        try {
            gameEngine.saveGame(saveFileName);

            gameEngine.loadGame(saveFileName);

            GameMap loadedMap = gameEngine.getMap();
            assertNotNull(loadedMap, "Загруженная карта не должна быть null");
            assertEquals(15, loadedMap.getWidth(), "Ширина карты должна быть 15");
            assertEquals(15, loadedMap.getHeight(), "Высота карты должна быть 15");

            File saveFile = new File("saves/" + saveFileName + ".json");
            boolean isDeleted = saveFile.delete();
            assertTrue(isDeleted, "Файл сохранения должен быть удален после теста");

        } catch (IOException e) {
            fail("Ошибка при загрузке игры: " + e.getMessage());
        }
    }
}
