package objects;

import game.GameTime;
import units.Unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Hotel {
    private String name = "У погибшего альпиниста";
    private int x;
    private int y;
    private static final int maxVisitors = 5;
    private final List<TimedStay> activeStays = new ArrayList<>();
    private final Queue<CheckInRequest> checkInQueue = new LinkedList<>();
    private volatile boolean isBreakTime = false;

    public Hotel(int x, int y) {
        this.x = x;
        this.y = y;
        startBreakScheduler();
    }

    public synchronized boolean checkIn(Hero hero, int duration, int bonus, GameTime time) {
        if (isBreakTime) {
            System.out.println("Перерыв: " + hero.getName() + " встает в очередь на заселение");
            checkInQueue.offer(new CheckInRequest(hero, duration, bonus, time));
            return false;
        }

        if (activeStays.size() >= maxVisitors) {
            System.out.println("Нет свободных мест для " + hero.getName());
            return false;
        }

        TimedStay stay = new TimedStay(hero, duration, bonus, time, this);
        activeStays.add(stay);
        stay.monitor();
        System.out.println(hero.getName() + " заселяется в отель на " + duration/1440 + " дней");
        return true;
    }

    public synchronized void release(TimedStay stay) {
        activeStays.remove(stay);
        System.out.println(stay.getHero().getName() + " покидает отель.");
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private void startBreakScheduler() {
        Thread scheduler = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(800*100);
                    startBreak();

                    Thread.sleep(150 * 100);
                    endBreak();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        scheduler.setDaemon(true);
        scheduler.start();
    }

    private synchronized void startBreak() {
        isBreakTime = true;
        System.out.println("Начался перерыв: заселение временно недоступно.");
    }

    private synchronized void endBreak() {
        isBreakTime = false;
        System.out.println("Перерыв окончен. Заселение продолжается.");

        while (!checkInQueue.isEmpty() && activeStays.size() < maxVisitors) {
            CheckInRequest request = checkInQueue.poll();
            if (request != null) {
                checkIn(request.hero, request.duration, request.bonus, request.time);
            }
        }
    }

    private static class CheckInRequest {
        Hero hero;
        int duration;
        int bonus;
        GameTime time;

        public CheckInRequest(Hero hero, int duration, int bonus, GameTime time) {
            this.hero = hero;
            this.duration = duration;
            this.bonus = bonus;
            this.time = time;
        }
    }

}
