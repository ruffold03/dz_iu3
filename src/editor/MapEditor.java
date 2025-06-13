package editor;

import map.GameMap;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MapEditor {
    public static int size() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите размер карты");
        System.out.println("1. Маленькая - 10х10");
        System.out.println("2. Средняя - 15х15");
        System.out.println("3. Большая - 20х20");
        System.out.println("4. Гигантская - 25х25");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                return 10;
            }
            case 2 -> {
                return 15;
            }
            case 3 -> {
                return 20;
            }
            case 4 -> {
                return 25;
            }
            default -> System.out.println("Неверный выбор!");
        }
        return 0;
    }

    public static GameMap createMap() {
        Scanner scanner = new Scanner(System.in);
        int size = size();
        GameMap map = new GameMap(size, size);
        boolean editing = true;
        while (editing) {
            System.out.println("\nТекущая карта:");
            map.display();

            System.out.println("1. Добавить объект");
            System.out.println("2. Удалить объект");
            System.out.println("3. Очистить карту");
            System.out.println("4. Сохранить карту");
            System.out.println("5. Сгенерировать случайную карту");
            System.out.println("6. Выйти");

            int choice = getValidChoice(scanner, 1, 6);

            switch (choice) {
                case 1 -> addObjectToMap(scanner, map);
                case 2 -> removeObjectFromMap(scanner, map);
                case 3 -> map.clear(0, 0, map.getWidth(), map.getHeight());
                case 4 -> {
                    saveMapToFile(scanner, map);
                }
                case 5 -> {
                    map.createMap();
                }
                case 6 -> {
                    System.out.println("Выход");
                    editing = false;
                }
            }
        }
        return map;
    }

    private static int getValidChoice(Scanner scanner, int min, int max) {
        while (true) {
            try {
                System.out.print("Выберите действие: ");
                int choice = scanner.nextInt();
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.printf("Введите число от %d до %d!%n", min, max);
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число!");
                scanner.next();
            }
        }
    }

    private static void addObjectToMap(Scanner scanner, GameMap map) {
        System.out.println("Типы объектов:");
        System.out.println("1. Дорога");
        System.out.println("2. Лес");
        System.out.println("3. Гора");
        System.out.println("4. Море");
        System.out.println("5. Снег");
        System.out.println("6. Пустыня");
        System.out.println("7. Золото");
        System.out.println("8. Замок героя");
        System.out.println("9. Замок противника");
        System.out.println("10. Свой объект");

        int type = getValidChoice(scanner, 1, 10);
        System.out.println("Выбрать область или клетку?");
        System.out.println("1. Область");
        System.out.println("2. Клетку");
        int x, y, newX, newY;
        int choice = getValidChoice(scanner, 1, 2);
        if (type == 10) {
            scanner.nextLine();
            System.out.println("Введите название: ");
            String name = scanner.nextLine();

            System.out.println("Введите символ (1 символ): ");
            String symbol = scanner.nextLine().trim();
            if (symbol.isEmpty()) {
                symbol = "?";
            } else {
                symbol = symbol.substring(0, 1);
            }

            System.out.println("Введите штраф: ");
            int fine = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Проходима ли клетка? (y/n): ");
            String choose = scanner.nextLine();
            boolean stop = !choose.equalsIgnoreCase("y");

            x = getCoordinate(scanner, "от X", map.getWidth());
            y = getCoordinate(scanner, "от Y", map.getHeight());
            newX = getCoordinate(scanner, "до X", map.getWidth());
            newY = getCoordinate(scanner, "до Y", map.getHeight());

            map.newCell(name, fine, symbol, stop, x, y, newX, newY);
        } else {
            if (choice == 1) {
                x = getCoordinate(scanner, "от X", map.getWidth());
                y = getCoordinate(scanner, "от Y", map.getHeight());
                newX = getCoordinate(scanner, "до X", map.getWidth());
                newY = getCoordinate(scanner, "до Y", map.getHeight());
            } else {
                x = getCoordinate(scanner, "X", map.getWidth());
                y = getCoordinate(scanner, "Y", map.getHeight());
                newX = x;
                newY = y;
            }
            map.changeMap(type, x, y, newX, newY);
        }
    }

    private static void removeObjectFromMap(Scanner scanner, GameMap map) {
        System.out.println("Удалить область или клетку?");
        System.out.println("1. Область");
        System.out.println("2. Клетку");
        int choice = getValidChoice(scanner, 1, 2);
        int x, y, newX, newY;
        if (choice == 1) {
            x = getCoordinate(scanner, "от X", map.getWidth());
            y = getCoordinate(scanner, "от Y", map.getHeight());
            newX = getCoordinate(scanner, "до X", map.getWidth());
            newY = getCoordinate(scanner, "до Y", map.getHeight());
        }
        else {
            x = getCoordinate(scanner, "X", map.getWidth());
            y = getCoordinate(scanner, "Y", map.getHeight());
            newX = x;
            newY = y;
        }
        map.clear(x, y, newX, newY);
    }

    private static int getCoordinate(Scanner scanner, String name, int max) {
        while (true) {
            try {
                System.out.print("Введите координату " + name);
                int coord = scanner.nextInt();
                if (coord >= 0 && coord < max) {
                    return coord;
                }
                System.out.printf("Координата должна быть от 0 до %d!%n", max-1);
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число!");
                scanner.next();
            }
        }
    }

    private static void saveMapToFile(Scanner scanner, GameMap map) {
        System.out.print("Введите имя файла для сохранения: ");
        String filename = scanner.next();
        try {
            map.saveToFile(filename);
            System.out.println("Карта успешно сохранена!");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }
}
