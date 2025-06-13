package test.units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import units.Archer;
import units.Unit;

import static org.junit.jupiter.api.Assertions.*;

class ArcherTest {
    private Archer archer;
    private Archer target;

    @BeforeEach
    void setUp() {
        archer = new Archer(3);
        target = new Archer(2);
    }

    @Test
    @DisplayName("Тест инициализации")
    void testInitialization() {
        // Проверяем инициализацию лучника
        assertEquals("Лучник", archer.getName());
        assertEquals(80, archer.getHealth());
        assertEquals(60, archer.getAttackPower());
        assertEquals(9, archer.getAttackRange());
        assertEquals(4, archer.getMovementRange());
        assertEquals(3, archer.getCount());
        assertEquals(2*3, archer.getDefense());
    }

    @Test
    @DisplayName("Тест атаки")
    void testAttack() {

        int initialHealth = target.getHealth() * target.getCount();
        archer.attack(target);


        int expectedHealth = initialHealth - (archer.getAttackPower());
        assertTrue(target.getHealth() * target.getCount() <= expectedHealth,
                "Здоровье цели должно уменьшиться после атаки.");
    }

    @Test
    @DisplayName("Тест символа")
    void testSymbol() {
        assertEquals("\uD83C\uDFF9 ", archer.getSymbol());
    }

    @Test
    @DisplayName("Тест количества очков передвижения")
    void testMovementRange() {
        assertEquals(4, archer.getMovementRange());
        archer.setMovementRange(5);
        assertEquals(5, archer.getMovementRange());
    }

    @Test
    @DisplayName("Тест радиуса атаки")
    void testAttackRange() {

        assertEquals(9, archer.getAttackRange());
    }

    @Test
    @DisplayName("Тест копирования войска")
    void testCloneWithCount() {

        Unit clonedArcher = archer.cloneWithCount(5);
        assertTrue(clonedArcher instanceof Archer);
        assertEquals(5, clonedArcher.getCount());
    }
}
