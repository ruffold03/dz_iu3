package game;

public class GameTime {
    private long gameMinutes = 0;
    private boolean running = true;

    public void start() {
        Thread clockThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(100);
                    gameMinutes++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.setDaemon(true);
        clockThread.start();
    }

    public long getGameMinutes() {
        return gameMinutes;
    }

    public void stop() {
        running = false;
    }
}
