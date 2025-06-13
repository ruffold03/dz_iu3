package test.units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import units.Cavalryman;
import units.Griffin;
import units.Unit;

import static org.junit.jupiter.api.Assertions.*;

class CavalrymanTest {
    private Cavalryman cavalryman;
    private Griffin target;

    @BeforeEach
    void setUp() {
        cavalryman = new Cavalryman(5);
        target = new Griffin(3);
    }

    @Test
    @DisplayName("Тест инициализации")
    void testInitialization() {

        assertEquals("Кавалерист", cavalryman.getName());
        assertEquals(150, cavalryman.getHealth());
        assertEquals(50*5, cavalryman.getAttackPower());
        assertEquals(1, cavalryman.getAttackRange());
        assertEquals(6, cavalryman.getMovementRange());
        assertEquals(5, cavalryman.getCount());
        assertEquals(30*5, cavalryman.getDefense());
    }

    @Test
    @DisplayName("Тест атаки")
    void testAttack() {

        int initialHealth = target.getHealth() * target.getCount();
        cavalryman.attack(target);

        int expectedHealth = initialHealth - (cavalryman.getAttackPower());
        assertTrue(target.getHealth() * target.getCount() <= expectedHealth,
                "Здоровье цели должно уменьшиться после атаки.");
    }

    @Test
    @DisplayName("Тест символа")
    void testSymbol() {

        assertEquals("\uD83D\uDC0E ", cavalryman.getSymbol());
    }

    @Test
    @DisplayName("Тест количества очков передвижения")
    void testMovementRange() {

        assertEquals(6, cavalryman.getMovementRange());
    }

    @Test
    @DisplayName("Тест радиуса атаки")
    void testAttackRange() {

        assertEquals(1, cavalryman.getAttackRange());
    }

    @Test
    void testCloneWithCount() {

        Unit clonedArcher = cavalryman.cloneWithCount(7);
        assertTrue(clonedArcher instanceof Cavalryman);
        assertEquals(7, clonedArcher.getCount());
    }
}

