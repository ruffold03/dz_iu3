import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import editor.MapEditor;
import game.GameEngine;
import game.Records;
import game.RecordsManager;
import map.GameMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameEngine gameEngine = new GameEngine();

        while (true) {
            System.out.println("\n=== Добро пожаловать в игру! ===");
            System.out.println("1. Случайная генерация мира");
            System.out.println("2. Создать карту в редакторе");
            System.out.println("3. Загрузить сохраненную карту");
            System.out.println("4. Загрузить игру");
            System.out.println("5. Рекорды");
            System.out.println("6. Выход");
            System.out.print("Выберите действие: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        randomGeneration(gameEngine);
                        gameEngine.startGame();
                    }
                    case 2 -> {
                        GameMap editedMap = openMapEditor();
                        System.out.println("Карта отредактирована. Хотите начать игру? (y/n)");
                        String answer = scanner.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            gameEngine.initializeGame(editedMap);
                            gameEngine.startGame();
                        }
                    }
                    case 3 -> {
                        GameMap loadedMap = loadMap();
                        if (loadedMap != null) {
                            System.out.println("Загружена карта размером " + loadedMap.getWidth() + "x" + loadedMap.getHeight());
                            System.out.println("Начать игру? (y/n)");
                            String answer = scanner.nextLine();
                            if (answer.equalsIgnoreCase("y")) {
                                gameEngine.initializeGame(loadedMap);
                                gameEngine.startGame();
                            }
                        }
                    }
                    case 4 -> {
                        showSaveLoadMenu(gameEngine);
                        gameEngine.startGame();
                    }
                    case 5 -> {
                        showHighScores();
                    }
                    case 6 -> {
                        System.out.println("Выход из игры...");
                        System.exit(0);
                    }
                    default -> System.out.println("Неверный выбор!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число от 1 до 5!");
                scanner.nextLine();
            }
        }
    }

    public static void showHighScores() {
        List<Records> records = RecordsManager.loadRecords();
        System.out.println("== ТАБЛИЦА РЕКОРДОВ ==");
        for (int i = 0; i < records.size(); i++) {
            Records r = records.get(i);
            System.out.printf("%d. %s — %.2f\n", i + 1, r.getPlayerName(), r.getScore());
        }
    }

    private static GameMap loadMap(){
        File savesDir = new File("maps");
        if (!savesDir.exists()) {
            savesDir.mkdir();
        }

        List<File> mapFiles = Arrays.stream(savesDir.listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .sorted()
                .collect(Collectors.toList());

        if (mapFiles.isEmpty()) {
            System.out.println("Нет сохраненных карт в папке maps/");
            return null;
        }

        System.out.println("\nДоступные карты:");
        for (int i = 0; i < mapFiles.size(); i++) {
            String name = mapFiles.get(i).getName().replace(".json", "");
            System.out.print(i + 1);
            System.out.print(" " + name + "\n");
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nВыберите карту (1-" + mapFiles.size() + ") или 0 для отмены: ");
            try {
                int choice = scanner.nextInt();
                if (choice == 0) return null;
                if (choice > 0 && choice <= mapFiles.size()) {
                    return loadSavedMap("maps/" + mapFiles.get(choice - 1).getName());
                }
            } catch (InputMismatchException e) {
                scanner.next();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Некорректный ввод!");
        }
    }

    public static GameMap loadSavedMap(String filename) throws IOException, IllegalArgumentException {
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        Path path = Paths.get(filename);

        String jsonContent;
        try {
            jsonContent = Files.readString(path);
        } catch (IOException e) {
            throw new IOException("Ошибка чтения файла: " + e.getMessage());
        }

        Gson gson = new Gson();
        try {
            GameMap map = gson.fromJson(jsonContent, GameMap.class);

            if (map == null) {
                throw new IllegalArgumentException("Файл пуст или содержит некорректные данные");
            }
            if (map.getWidth() <= 0 || map.getHeight() <= 0) {
                throw new IllegalArgumentException("Некорректный размер карты в файле");
            }

            return map;
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Некорректный JSON-формат: " + e.getMessage());
        }
    }

    private static void randomGeneration(GameEngine gameEngine) {
        System.out.println("Выберите размер карты");
        System.out.println("1. Маленькая - 10х10");
        System.out.println("2. Средняя - 15х15");
        System.out.println("3. Большая - 20х20");
        System.out.println("4. Гигантская - 25х25");
        int choice = scanner.nextInt();
        int width;
        int height;
        switch (choice) {
            case 1 -> {
                width = 10;
                height = 10;
                gameEngine.initializeGame(width, height);
            }
            case 2 -> {
                width = 15;
                height = 15;
                gameEngine.initializeGame(width, height);
            }
            case 3 -> {
                width = 20;
                height = 20;
                gameEngine.initializeGame(width, height);
            }
            case 4 -> {
                width = 25;
                height = 25;
                gameEngine.initializeGame(width, height);
            }
            default -> System.out.println("Неверный выбор!");
        }
    }

    private static GameMap openMapEditor() {
        System.out.println("Запуск редактора...");
        return MapEditor.createMap();
    }

    public static void showSaveLoadMenu(GameEngine gameEngine) {
        try {
            File savesDir = new File("saves");
            File[] saves = savesDir.listFiles((dir, name) -> name.endsWith(".json"));

            System.out.println("Доступные сохранения:");
            for (int i = 0; i < saves.length; i++) {
                System.out.println((i+1) + ". " + saves[i].getName().replace(".json", ""));
            }

            System.out.print("Выберите сохранение: ");
            int saveNum = scanner.nextInt();
            scanner.nextLine();

            gameEngine.loadGame(saves[saveNum-1].getName().replace(".json", ""));
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
