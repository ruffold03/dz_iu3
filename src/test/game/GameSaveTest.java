package test.game;

import game.GameEngine;
import map.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GameSaveTest {

    private GameEngine gameEngine;
    private GameMap gameMap;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine();
        gameMap = new GameMap(15, 15); // Создаем карту размером 15x15
        gameEngine.initializeGame(gameMap);
    }

    @DisplayName("Тест сохранения игры в файл")
    @Test
    void testSaveGame() {
        String saveFileName = "test";
        try {
            gameEngine.saveGame(saveFileName);

            Path saveFilePath = Paths.get("saves", saveFileName + ".json");
            assertTrue(Files.exists(saveFilePath), "Файл сохранения должен быть создан");

            String savedContent = Files.readString(saveFilePath);
            assertNotNull(savedContent, "Содержимое файла сохранения не должно быть пустым");

            File saveFile = new File("saves/" + saveFileName + ".json");
            assertTrue(saveFile.exists(), "Файл сохранения должен быть создан");

            boolean isDeleted = saveFile.delete();
            assertTrue(isDeleted, "Файл сохранения должен быть удален после теста");

        } catch (IOException e) {
            fail("Ошибка при сохранении игры: " + e.getMessage());
        }
    }
}

