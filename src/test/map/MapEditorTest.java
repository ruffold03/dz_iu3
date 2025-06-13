package test.map;

import map.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MapEditorTest {
    GameMap map;

    @BeforeEach
    void setUp() {
        map = new GameMap(15, 15);
    }


    @DisplayName("Тест создания карты")
    @Test
    void testCreateMap() {
        assertNotNull(map, "Карта не должна быть null");
        assertEquals(15, map.getWidth(), "Ширина карты должна быть 15");
        assertEquals(15, map.getHeight(), "Высота карты должна быть 15");
    }

    @DisplayName("Тест добавления объекта на карту")
    @Test
    void testAddObjectToMap() {
        map.changeMap(4, 7, 9, 7, 9);
        assertEquals(map.getCellType(7, 9), "sea");
    }

    @DisplayName("Тест удаления объекта с карты")
    @Test
    void testRemoveObjectFromMap() {
        map.changeMap(4, 7, 9, 7, 9);
        map.clear(7, 9, 7, 9);
        assertEquals(map.getCellType(7, 9), "field");
    }

    @DisplayName("Тест сохранения карты в файл")
    @Test
    void testSaveMapToFile() throws IOException {
        String filename = "test_map.json";

        map.saveToFile(filename);

        File file = new File("maps/" + filename);
        assertTrue(file.exists(), "Файл должен быть создан");

        assertTrue(filename.endsWith(".json"), "Файл должен иметь расширение .json");

        boolean isDeleted = file.delete();
        assertTrue(isDeleted, "Файл должен быть удален после теста");
    }

}
