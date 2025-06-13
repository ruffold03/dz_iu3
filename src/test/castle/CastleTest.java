package test.castle;

import game.GameEngine;
import objects.*;
import units.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CastleTest {
    private static final Logger logger = Logger.getLogger(CastleTest.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("castle_tests.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Не удалось настроить логгер: " + e.getMessage());
        }
    }
    private Hero hero;
    private Castle castle;
    Map<String, Building> buildings;

    @BeforeEach
    void setUp() {
        hero = new Hero("Тестовый герой", 5, 5, 1000);
        castle = new Castle(5, 5);
        buildings = castle.getBuilding();
    }

    @Test
    @DisplayName("Тест координат замка")
    void testPosition() {
        assertEquals(castle.getX(), hero.getxCastle(), "Координаты должны быть равны");
        assertEquals(castle.getY(), hero.getyCastle(), "Координаты должны быть равны");
        System.out.print("Координаты верные\n");
    }

    @Test
    @DisplayName("Тест первого здания в замке")
    void testFirstBuilding() {
        assertEquals("Сторожевая башня", buildings.get("Сторожевая башня").getName());
        assertEquals(1, castle.getBuilding().size());
        System.out.print("В замке уже есть только Сторожевая башня\n");
    }

    @Test
    @DisplayName("Тест покупки зданий")
    void testBuyBuilding() {
        Building newBuilding = new Building("Башня лучников", 500, "Лучник", 60, "Улучшенная башня лучников", "Стрелок");
        assertTrue(hero.getGold() > newBuilding.getPrice());
        buildings.put(newBuilding.getName(), newBuilding);
        assertEquals(2, buildings.size());
        System.out.print("В замке построена Башня лучников\n");
    }

    @Test
    @DisplayName("Тест покупки зданий, когда нет золота")
    void testCantBuyBuilding(){
        logger.warning("Попытка купить здание без денег");
        hero.setGold(0);
        Building newBuilding = new Building("Башня лучников", 500, "Лучник", 60, "Улучшенная башня лучников", "Стрелок");
        assertFalse(hero.getGold() > newBuilding.getPrice());
        System.out.print("Недостаточно денег, чтобы построить здание\n");
    }
}

