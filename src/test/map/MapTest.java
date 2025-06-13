package test.map;
import map.GameMap;
import objects.*;
import units.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private GameMap map;
    private Hero hero;
    private Enemy enemy;
    private List<Hero> heroes;

    @BeforeEach
    void setMap() {
        hero = new Hero("Тестовый герой", 5, 3, 1000);
        enemy = new Enemy(8, 9);
        map = new GameMap(10, 15);
        map.createMap(hero.getX(), hero.getY(), enemy.getX(), enemy.getY());
        heroes = new ArrayList<>();
        heroes.add(hero);
    }

    @Test
    @DisplayName("Тест клеток карты")
    void testCells() {
        map.setCellType(1, 1, "field");
        map.setCellFeature(1, 1, "no");
        assertEquals(map.getFine(1, 1), 1.5);
        System.out.print("Штраф по полю равен 1,5\n");
        map.setCellType(1, 1, "path");
        map.setCellFeature(1, 1, "no");
        assertEquals(map.getFine(1, 1), 1);
        System.out.print("Штраф по дороге равен 1\n");
        map.setCellType(1, 1, "snow");
        map.setCellFeature(1, 1, "tree");
        assertEquals(map.getFine(1, 1), 2);
        System.out.print("Штраф по лесу равен 2\n");
    }

    @Test
    @DisplayName("Тест тумана войны")
    void testWarFog(){
        map.display(hero, enemy, heroes,hero.getEagle());
        assertTrue(map.getMap()[5][4].isVisible());
        assertFalse(map.getMap()[9][11].isVisible());
        System.out.print("Соседняя клетка видима, дальняя клетка закрыта");
    }

    @Test
    @DisplayName("Тест проходимости клеток")
    void testLetCell(){
        map.setCellType(1, 1, "sea");
        map.setCellType(1, 2, "mountain");
        assertFalse(map.isLet(1, 1));
        assertFalse(map.isLet(1, 2));
        System.out.print("Море и горы непроходимы\n");
    }
}
