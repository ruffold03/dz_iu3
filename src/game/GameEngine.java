package game;

import animals.Animals;
import animals.AnimalsAdapter;
import animals.Eagle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import map.GameMap;
import objects.*;
import units.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {
    private int width;
    private int height;

    private int day = 0;
    private int week = 1;
    private boolean gameRunning = true;

    private List<Hero> heroes = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private Hero currentHero;
    private Enemy currentEnemy;
    private Castle playerCastle;
    private Castle enemyCastle;
    private GameMap map;
    private Hotel hotel;
    private Eagle eagle;
    GameTime gameTime;
    private int time2;
    private int time;

    private boolean winByCastle = false;
    private boolean loseByCastle = false;
    private int castleCaptureTimer = 1;
    private int castleLossTimer = 1;
    List<Hero> npcList = new ArrayList<>();

    private transient final Scanner scanner = new Scanner(System.in);

    public void initializeGame(GameMap newMap) {
        Random random = new Random();

        width = newMap.getWidth();
        height = newMap.getHeight();

        int heroX = newMap.getCastleX() + 1;
        int heroY = newMap.getCastleY();
        heroes.add(new Hero("Кристиан", heroX, heroY, 10000));
        currentHero = heroes.get(0);
        playerCastle = new Castle(heroX, heroY);

        int enemyX, enemyY;
        enemyX = newMap.getEnemyCastleX() + 1;
        enemyY = newMap.getEnemyCastleY();

        enemies.add(new Enemy(enemyX, enemyY));
        currentEnemy = enemies.get(0);
        enemyCastle = new Castle(enemyX, enemyY);
        int hotelX = random.nextInt(0, width);
        int hotelY = random.nextInt(0, height);
        hotel = new Hotel(hotelX, hotelY);

        map = new GameMap(newMap.getWidth(), newMap.getHeight());
        map.copy(newMap);
        map.setCellType(hotelX, hotelY, "hotel");
        map.setCellFeature(hotelX, hotelY, "no");
    }

    public void initializeGame(int newWidth, int newHeight) {
        Random random = new Random();

        width = newWidth;
        height = newHeight;

        int heroX = random.nextInt(width - 2) + 1;
        int heroY = random.nextInt(height - 2) + 1;
        heroes.add(new Hero("Кристиан", heroX, heroY, 10000));
        currentHero = heroes.get(0);
        playerCastle = new Castle(heroX, heroY);

        int enemyX, enemyY;
        do {
            enemyX = random.nextInt(width - 2) + 1;
            enemyY = random.nextInt(height - 2) + 1;
        } while (Math.abs(heroX - enemyX) + Math.abs(heroY - enemyY) < newWidth);

        enemies.add(new Enemy(enemyX, enemyY));
        currentEnemy = enemies.get(0);
        enemyCastle = new Castle(enemyX, enemyY);


        map = new GameMap(width, height);
        map.createMap(heroX, heroY, enemyX, enemyY);
        boolean flag = true;

        int hotelX = 0;
        int hotelY = 0;

        while (flag) {
            hotelX = random.nextInt(0, width);
            hotelY = random.nextInt(0, height);
            if (!map.getCellType(hotelX, hotelY).equals("castleEnemy") && !map.getCellType(hotelX, hotelY).equals("castleHero")) {
                flag = false;
            }
        }

        hotel = new Hotel(hotelX, hotelY);
        map.setCellType(hotelX, hotelY, "hotel");
        map.setCellFeature(hotelX, hotelY, "no");
    }

    public void startGame() {
        gameTime = new GameTime();
        while (gameRunning) {
            new Thread(() -> {
                Random random = new Random();
                while (true) {
                    try {
                        Thread.sleep(1000*10 + random.nextInt(10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String npcName = "NPC-" + random.nextInt(1000);
                    Hero npc = new Hero(npcName, hotel.getX(), hotel.getY(), 1000);
                    npcList.add(npc);

                    int duration = random.nextBoolean() ? 1440 : 4320;
                    int bonus = duration == 1 ? 20 : 30;

                    hotel.checkIn(npc, duration, bonus, gameTime);
                    if (npcList.size() > 10) npcList.clear();
                }
            }).start();
            gameTime.start();
            updateGameCycle();
            processPlayerTurn();
            if (!gameRunning) {
                double score = (day + week*7)*currentHero.getGold(); // ты можешь придумать свою формулу
                String playerName = currentHero.getName(); // покажу ниже
                RecordsManager.saveRecord(new Records(playerName, score));
                System.out.println("Поздравляем! Ваш счёт: " + score);
                break;
            }
            processEagleTurn();
            processEnemyTurn();
        }
        gameTime.stop();
    }

    private void processEagleTurn() {
        if (currentHero.isEagle()) {
            System.out.println("Ястреб летит");
            while (eagle.getMoveCount() >= 1) {
                eagle.action(width, height);
                map.display(currentHero, currentEnemy, heroes, eagle);
                System.out.println("Введите команду (r – Пропуск хода, n - дальше): ");
                String command = scanner.nextLine();
                if (command.equals("r")) {
                    break;
                }
            }
        }
    }

    private void processEnemyTurn() {
        while (currentEnemy.getMoveCount() >= 1) {
            System.out.println("Ход противника");
            currentEnemy.act(currentHero, map.getMap(), width, height);
            if (currentEnemy.getX() == currentHero.getxCastle() && currentEnemy.getY() == currentHero.getyCastle()) {
                System.out.println("До захвата вашего замка остался 1 ход");
                currentEnemy.setMoveCount(0);
                loseByCastle = true;
            } else {
                time2 = 1;
                loseByCastle = false;
            }
            if (currentEnemy.isCheck()) {
                if (currentHero.getArmy().isEmpty() || currentEnemy.isLose()) {
                    heroes.remove(currentHero);
                    if (!heroes.isEmpty()) {
                        currentHero = heroes.getFirst();
                        System.out.println("Теперь активен герой: " + currentHero.getName());
                    }
                }
                if (currentEnemy.getArmy().isEmpty()) {
                    enemies.remove(currentEnemy);
                }
                if (heroes.isEmpty() && checkPlayerLoseConditions("loseByEnemy")) {
                    gameRunning = false;
                    break;
                } else if (enemies.isEmpty() && checkPlayerWinConditions("winByEnemy")) {
                    gameRunning = false;
                    break;
                } else {
                    currentHero.setWin();
                    currentHero.setGold(currentHero.getGold() + currentEnemy.getGold());
                    System.out.println("Вы получили золото противника");
                }
                map.display(currentHero, currentEnemy, heroes, eagle);
            }
        }
    }

    public void updateGameCycle() {
        if (day >= 7) {
            day = 0;
            week++;
            currentHero.visitStable(false);
            autoSave();
        }
        day++;

        System.out.println("\n" + week + "-я неделя");
        System.out.println(day + "-й День");

        // Начисление дохода
        double rent = playerCastle.getRent();
        System.out.println("Ваша прибыль за ход: " + rent);
        currentHero.setGold(rent + currentHero.getGold());
        currentEnemy.setGold(enemyCastle.getRent() + currentEnemy.getGold());

        System.out.println("Ваше золото: " + currentHero.getGold());
    }

    private void processPlayerTurn() {
        currentHero.setMoveCount(currentHero.hasVisitedStable() ? 8 : 6);

        while (currentHero.getMoveCount() > 0 && gameRunning) {
            map.display(currentHero, currentEnemy, heroes, currentHero.getEagle());
            handlePlayerInput();
            if (currentHero.getX() == currentEnemy.getxCastle() && currentHero.getY() == currentEnemy.getyCastle()) {
                System.out.println("До захвата замка противника остался 1 ход");
                winByCastle = true;
            }
            else {
                time = 1;
                winByCastle = false;
            }
            if (currentHero.isWin()) {
                if (currentHero.getArmy().isEmpty() || currentHero.isLose()) {
                    heroes.remove(currentHero);
                    if (!heroes.isEmpty()) {
                        currentHero = heroes.getFirst();
                        System.out.println("Теперь активен герой: " + currentHero.getName());
                    }
                }
                if (currentEnemy.getArmy().isEmpty()) {
                    enemies.remove(currentEnemy);
                }
                if (heroes.isEmpty() && checkPlayerLoseConditions("loseByEnemy")) {
                    gameRunning = false;
                    break;
                } else if (enemies.isEmpty() && checkPlayerWinConditions("winByEnemy")) {
                    gameRunning = false;
                    break;
                }
                else {
                    currentHero.setWin();
                    currentHero.setGold(currentHero.getGold() + currentEnemy.getGold());
                    System.out.println("Вы получили золото противника");
                }
            }
            if (currentHero.getX() == playerCastle.getX() && currentHero.getY() == playerCastle.getY()) {
                if (!currentHero.hasVisitedStable() && playerCastle.haveStable()) {
                    currentHero.setMoveCount(8);
                    currentHero.visitStable(true);
                    System.out.println("Вы посетили конюшню, теперь дальность хода увеличена на 2");
                }
            }
            System.out.println("Осталось ходов " + currentHero.getMoveCount());
        }
    }

    private void handlePlayerInput() {
        System.out.println("Введите команду: ");
        printAvailableCommands();
        String command = scanner.nextLine().toLowerCase();

        switch (command) {
            case "q" -> quitGame();
            case "r" -> skipTurn();
            case "t" -> showArmy();
            case "c" -> enterCastle();
            case "z" -> developerConsole();
            case "f" -> switchHero();
            case "v" -> {
                System.out.println("Введите название сохранения");
                command = scanner.nextLine();
                try {
                    saveGame(command);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "w", "a", "s", "d" -> moveHero(command);
            default -> System.out.println("Неизвестная команда");
        }
    }

    private void moveHero(String command) {
        currentHero.move(command, map, playerCastle, heroes, currentEnemy, enemyCastle, hotel, gameTime);
    }

    private void switchHero() {
        System.out.println("\nВыберите героя:");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            System.out.println((i + 1) + ". " + h.getName() + " (Золото: " + h.getGold() + ")");
        }

        int choice = scanner.nextInt();

        if (choice < 1 || choice > heroes.size()) {
            System.out.println("Некорректный выбор.");
            return;
        }

        currentHero = heroes.get(choice - 1);
        System.out.println("Теперь активен герой: " + currentHero.getName());
        playerCastle.action(currentHero, heroes, currentHero.getArmy());
    }

    private void printAvailableCommands() {
        System.out.println("w/a/s/d - движение");
        System.out.println("q - Выйти из игры");
        System.out.println("r - Пропуск хода");
        System.out.println("c - Войти в замок");
        System.out.println("g - Сила противника");
        System.out.println("f - Сменить героя");
        System.out.println("t - Показать армию");
        System.out.println("z - Консоль разработчика");
        System.out.println("v - Сохранить игру");
    }

    private void showArmy() {
        for (Unit unit : currentHero.getArmy()) {
            System.out.println(unit.getName() + " " + unit.getCount() + " шт");
        }
    }

    private void enterCastle() {
        System.out.println("Вы вошли в замок");
        playerCastle.action(currentHero, heroes, currentHero.getArmy());
    }

    private void developerConsole() {
        System.out.println("Введите команду: ");
        System.out.println("1. Переместиться к врагу");
        System.out.println("2. Переместиться к замку врага");
        System.out.println("3. Открыть всю карту");
        System.out.println("4. Получить золото");
        System.out.println("5. Выдать себе 30 Чемпионов");
        System.out.println("6. Выдать противнику 30 Чемпионов");
        System.out.println("7. Переместить врага к себе");
        System.out.println("8. Переместить врага к замку");
        System.out.println("9. Назад");
        int cheat = Integer.parseInt(scanner.nextLine());
        if (cheat == 1) {
            if (currentEnemy.getX() > 0) {
                currentHero.setPosition(currentEnemy.getX() - 1, currentEnemy.getY());
            }
            else {
                currentHero.setPosition(currentEnemy.getX() + 1, currentEnemy.getY());
            }
        }
        if (cheat == 2) {
            currentHero.setPosition(currentEnemy.getxCastle(), currentEnemy.getyCastle() + 1);
        }
        if (cheat == 3) {
            map.setRadius(30);
        }
        if (cheat == 4) {
            System.out.println("Сколько золота: ");
            double sum = scanner.nextDouble();
            currentHero.setGold(currentHero.getGold() + sum);
            System.out.println("\nВаше золото: " + currentHero.getGold());
        }
        if (cheat == 5) {
            if (currentHero.getArmy().size() > 10) {
                System.out.println("Ваша армия заполнена!");
                return;
            }
            currentHero.getArmy().add(new Champion(30));
            System.out.println("Вам выдано 30 чемпионов");
        }
        if (cheat == 6) {
            if (currentEnemy.getArmy().size() > 10) {
                System.out.println("Армия врага заполнена!");
                return;
            }
            currentEnemy.getArmy().add(new Champion(30));
            System.out.println("Противнику выдано 30 чемпионов");
        }
        if (cheat == 7) {
            if (currentHero.getX() > 0) {
                currentEnemy.setPosition(currentHero.getX() - 1, currentHero.getY());
            }
            else {
                currentEnemy.setPosition(currentHero.getX() + 1, currentHero.getY());
            }
        }
        if (cheat == 8) {
            currentEnemy.setPosition(currentHero.getxCastle(), currentHero.getyCastle() + 1);
        }
    }

    private void quitGame() {
        System.out.println("Игра завершена");
        gameRunning = false;
    }

    private void skipTurn() {
        System.out.println("Ход пропущен");
        currentHero.setMoveCount(0);
    }



    public boolean checkPlayerWinConditions(String condition) {
        switch (condition) {
            case "winByCastle":
                System.out.println("Вы захватили замок, победа!");
                return true;
            case "winByEnemy":
                System.out.println("Вы победили всех врагов!");
                return true;
            default:
                return false;
        }
    }

    public boolean checkPlayerLoseConditions(String condition) {
        switch (condition) {
            case "loseByCastle":
                System.out.println("Ваш замок захвачен!");
                return true;
            case "loseByEnemy":
                System.out.println("Все ваши герои погибли!");
                return true;
            default:
                return false;
        }
    }

    public GameMap getMap() {
        return map;
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Unit.class, new UnitAdapter())
                .registerTypeAdapter(Animals.class, new AnimalsAdapter())  // Добавлено
                .setPrettyPrinting()
                .create();
    }

    public void saveGame(String filename) throws IOException {

        Path savesDir = Paths.get("saves");
        if (!Files.exists(savesDir)) {
            Files.createDirectories(savesDir);
        }

        GameState state = new GameState();
        state.setMap(this.map);
        state.setHeroes(new ArrayList<>(this.heroes));
        state.setCurrentHero(this.currentHero);
        state.setEnemies(new ArrayList<>(this.enemies));
        state.setCurrentEnemy(this.currentEnemy);
        state.setPlayerCastle(this.playerCastle);
        state.setEnemyCastle(this.enemyCastle);
        state.setDay(this.day);
        state.setWeek(this.week);
        state.setEagle(this.eagle);
        state.setGameRunning(this.gameRunning);

        Gson gson = createGson();
        String json = gson.toJson(state);

        Path savePath = savesDir.resolve(filename + ".json");
        Files.write(savePath, json.getBytes());

        System.out.println("Игра сохранена: " + savePath.toAbsolutePath());
    }

    public void loadGame(String filename) throws IOException {
        Path savePath = Paths.get("saves", filename + ".json");

        if (!Files.exists(savePath)) {
            throw new IOException("Сохранение не найдено");
        }

        String json = Files.readString(savePath);

        Gson gson = createGson();
        GameState state = gson.fromJson(json, GameState.class);
        this.map = state.getMap();
        this.heroes = state.getHeroes();
        this.currentHero = state.getCurrentHero();
        this.enemies = state.getEnemies();
        this.currentEnemy = state.getCurrentEnemy();
        this.playerCastle = state.getPlayerCastle();
        this.enemyCastle = state.getEnemyCastle();
        this.day = state.getDay();
        this.week = state.getWeek();
        this.eagle = state.getEagle();
        this.gameRunning = state.isGameRunning();

        System.out.println("Игра загружена из " + savePath);
    }

    public void autoSave() {
        try {
            saveGame("autosave");
        } catch (IOException e) {
            System.err.println("Автосохранение не удалось");
        }
    }
}