package objects;

import game.GameTime;
import units.Unit;

public class TimedStay {
    private final Hero hero;
    private final long endTime;
    private final int healthBonus;
    private final GameTime gameTime;
    private final Hotel hotel;

    public TimedStay(Hero hero, int durationMinutes, int healthBonus, GameTime gameTime, Hotel hotel) {
        this.hero = hero;
        this.healthBonus = healthBonus;
        this.gameTime = gameTime;
        this.hotel = hotel;
        this.endTime = gameTime.getGameMinutes() + durationMinutes;
    }

    public void monitor() {
        new Thread(() -> {
            while (gameTime.getGameMinutes() < endTime) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Unit unit : hero.getArmy()) {
                unit.Heal(healthBonus);
            }

            hotel.release(this);
        }).start();
    }

    public Hero getHero() {
        return hero;
    }
}
