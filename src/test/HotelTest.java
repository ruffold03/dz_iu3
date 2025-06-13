package test;

import game.GameTime;
import objects.Hero;
import objects.Hotel;
import objects.TimedStay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {

    private Hotel hotel;
    private GameTime time;

    @BeforeEach
    void setUp() {
        hotel = new Hotel(10, 20);
        time = new GameTime();
    }

    @Test
    void testCheckInSuccess() {
        Hero hero = new Hero("TestHero", 0, 0, 100);
        boolean result = hotel.checkIn(hero, 1440, 20, time);
        assertTrue(result);
    }

    @Test
    void testCheckInFailsWhenFull() {
        for (int i = 0; i < 5; i++) {
            Hero hero = new Hero("Hero" + i, 0, 0, 100);
            assertTrue(hotel.checkIn(hero, 1440, 20, time));
        }
        Hero extraHero = new Hero("ExtraHero", 0, 0, 100);
        assertFalse(hotel.checkIn(extraHero, 1440, 20, time));
    }

    @Test
    void testReleaseRemovesHero() {
        Hero hero = new Hero("ReleasableHero", 0, 0, 100);
        hotel.checkIn(hero, 1440, 20, time);

        TimedStay stay = new TimedStay(hero, 1440, 20, time, hotel);
        hotel.release(stay);

        boolean result = hotel.checkIn(hero, 1440, 20, time);
        assertTrue(result);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(10, hotel.getX());
        assertEquals(20, hotel.getY());

        hotel.setX(30);
        hotel.setY(40);

        assertEquals(30, hotel.getX());
        assertEquals(40, hotel.getY());
    }

    @Test
    void testHotelName() {
        assertEquals("У погибшего альпиниста", hotel.getName());
    }
}