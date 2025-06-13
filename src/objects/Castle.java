package objects;

import units.*;

import java.util.*;

public class Castle {
    private int x;
    private int y;
    private Map<String, Building> buildings = new HashMap<>();
    private Map<String, Building> specialBuildings = new HashMap<>();
    private List<Unit> garrison = new ArrayList<>();
    private double rent;
    private List<String> newHeroes = new ArrayList<>();
    private boolean isLosed = false;
    public Castle(int xHero, int yHero) {
        x = xHero - 1;
        y = yHero;
        rent = 400;
        Building newBuilding = new Building("Сторожевая башня", 400, "Копейщик", 40, "Улучшенная сторожевая башня", "Алебардщик");
        buildings.put(newBuilding.getName(), newBuilding);
        newHeroes.add("Оррин");
        newHeroes.add("Валеска");
        newHeroes.add("Ингэм");
    }

    private Unit getUnitByName(List<Unit> army, String name) {
        for (Unit unit : army) {
            if (unit.getName().equalsIgnoreCase(name.trim())) {
                return unit;
            }
        }
        return null;
    }

    public void action(Hero hero, List<Hero> heroes, List<Unit> army) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Замок ===");
            System.out.println("\nВаше золото: " + hero.getGold());
            System.out.println("\nВаша прибыль за ход: " + rent);
            System.out.println("1. Купить здание");
            System.out.println("2. Оставить гарнизон");
            System.out.println("3. Забрать гарнизон");
            System.out.println("4. Купить юнитов");
            System.out.println("5. Улучшить здание");
            System.out.println("6. Купить героя");
            System.out.println("7. Посмотреть гарнизон");
            System.out.println("8. Обучение зверей");
            System.out.println("9. Выйти");
            System.out.print("Введите команду: ");

            String command = scanner.nextLine();
            switch (command) {
                case "1" -> buyBuilding(hero);
                case "2" -> leaveGarrison(army, hero);
                case "3" -> takeGarrison(army, hero);
                case "4" -> buyUnits(hero);
                case "5" -> upgradeBuilding(hero);
                case "6" -> buyHero(hero, heroes);
                case "7" -> seeGarrison();
                case "8" -> educateAnimals(hero);
                case "9" -> {
                    System.out.println("Вы вышли из замка.");
                    return;
                }
                default -> System.out.println("Некорректная команда.");
            }
        }
    }

    public void education(Hero hero) {
        Scanner scanner = new Scanner(System.in);
        String heroEducation = hero.getEducation();
        System.out.println("\n=== Университет ===");
        System.out.println("\nВаше золото: " + hero.getGold());
        System.out.println("\nВаше уровень обучения: " + heroEducation);
        System.out.println("Обучение: ");
        System.out.println("1 уровень - Ученик (500 золота) ");
        System.out.println("2 уровень - Бакалавр (1000 золота) ");
        System.out.println("3 уровень - Магистр (2000 золота) ");
        System.out.println("Нажмите r - чтобы повысить уровень знаний, q - чтобы выйти ");
        String act = scanner.next();
        if (act.equals("q")) { return; }
        else if (act.equals("r")) {
            if (!(hero.getX() == x && hero.getY() == y)) {
                System.out.println("В замке нет героя");
                return;
            }
            switch (heroEducation) {
                case "Неуч": {
                    if (hero.getGold() < 500) {
                        System.out.println("У вас недостаточно золота ");
                        return;
                    }
                    else {
                        hero.setGold(hero.getGold() - 500);
                        hero.setEducation("Ученик");
                        System.out.println("Уровень знаний повышен до уровня Ученик! ");
                        return;
                    }
                }
                case "Ученик": {
                    if (hero.getGold() < 1000) {
                        System.out.println("У вас недостаточно золота ");
                        return;
                    }
                    else {
                        hero.setGold(hero.getGold() - 1000);
                        hero.setEducation("Бакалавр");
                        System.out.println("Уровень знаний повышен до уровня Бакалавр! ");
                        return;
                    }
                }
                case "Бакалавр": {
                    if (hero.getGold() < 2000) {
                        System.out.println("У вас недостаточно золота ");
                        return;
                    }
                    else {
                        hero.setGold(hero.getGold() - 1000);
                        hero.setEducation("Магистр");
                        System.out.println("Уровень знаний повышен до уровня Магистр! ");
                        return;
                    }
                }
                default: {
                    System.out.println("У вас уже максимальный уровень! ");
                    return;
                }
            }
        }
        else {
            System.out.println("Неверный ввод ");
            return;
        }

    }


    public void educateAnimals(Hero hero) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Храм зверей ===");
        System.out.println("\nВаше золото: " + hero.getGold());
        System.out.println("1. Обучиться ");
        System.out.println("2. Приручить зверя ");
        int act = scanner.nextInt();
        if (act == 2) {
            System.out.println("Выберите зверя для обучения: ");
            System.out.println("1. Семейство китовых");
            System.out.println("    1 уровень - Китенок (необходим уровень Ученик) - позволяет передвигаться по воде с штрафом, как по земле");
            System.out.println("    2 уровень - Кит (необходим уровень Бакалавр) - позволяет передвигаться по воде без штрафа");
            System.out.println("    3 уровень - Вождь китов (необходим уровень Магистр) - позволяет пересесть на кита, не теряя при этом ход");
            System.out.println("2. Семейство ястребов");
            System.out.println("    1 уровень - Птенец ястреба (необходим уровень Ученик) - позволяет открывать неизведанные земли с малым радиусом");
            System.out.println("    2 уровень - Ястреб (необходим уровень Бакалавр) - радиус обзора зверя увеличивается");
            System.out.println("    3 уровень - Королевский ястреб (необходим уровень Магистр) - скорость зверя увеличивается");
            System.out.println("3. Семейство псовых");
            System.out.println("    1 уровень - Щенок Хаски (необходим уровень Ученик) - позволяет передвигаться по снегу без штрафа");
            System.out.println("    2 уровень - Хаски (необходим уровень Бакалавр) - штраф от заснеженных лесов уменьшен");
            System.out.println("    3 уровень - Стая Хаски (необходим уровень Магистр) - передвижение по заснеженным лесам без штрафа");
            System.out.println("Выберете семейство: ");
            int family = scanner.nextInt();
            System.out.println("Выберете уровень: ");
            int level = scanner.nextInt();
            if (hero.getLevel() < level) {
                System.out.println("Вы недостаточно обучены для этого уровня ");
                return;
            }
            if (!(hero.getX() == x && hero.getY() == y)) {
                System.out.println("В замке нет героя");
                return;
            }
            hero.buyAnimals(family, level);
        }
        else if (act == 1){
            education(hero);
        }
        else {
            System.out.println("Неверный ввод ");
            return;
        }
    }

    public void buyHero(Hero hero, List<Hero> heroes) {
        if (hero.getX() == x && hero.getY() == y) {
            System.out.println("В замке уже есть герой!");
            return;
        }
        System.out.println("\nВаше золото: " + hero.getGold());
        System.out.println("Стоимость нового героя 400 золота");
        if (hero.getGold() < 400) {
            System.out.println("У вас недостаточно золота.");
            return;
        }
        System.out.println("Герои в таверне: ");
        for (String name : newHeroes) {
            if (name.equals("Оррин")) {
                System.out.println(name + ", золото - 1200 ");
            }
            if (name.equals("Валеска")) {
                System.out.println(name + ", золото - 900 ");
            }
            if (name.equals("Ингэм")) {
                System.out.println(name + ", золото - 1100 ");
            }
        }
        System.out.print("Введите имя героя: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        hero.setGold(hero.getGold() - 400);
        newHeroes.remove(name);
        switch (name) {
            case "Оррин" -> heroes.add(new Hero("Оррин", x, y, 1200));
            case "Валеска" -> heroes.add(new Hero("Валеска", x, y, 900));
            case "Ингэм" -> heroes.add(new Hero("Ингэм", x, y, 1100));
            default -> {
                System.out.println("Такого героя нет");
                return;
            }
        }
        System.out.println("Вы купили нового героя");
    }

    private void seeGarrison() {
        System.out.println("\nЮниты в гарнизоне:");
        for (Unit unit : garrison) {
            System.out.println(unit.getName() + " " + unit.getCount() + " шт");
        }
    }

    private void upgradeBuilding(Hero hero) {
        List<Building> ownedBuildings = new ArrayList<>(buildings.values());
        if (ownedBuildings.isEmpty()) {
            System.out.println("У вас нет зданий для улучшения!");
            return;
        }
        int count = 0;
        for (int i = 0; i < ownedBuildings.size(); i++) {
            Building b = ownedBuildings.get(i);
            if (!b.isUpgraded()) {
                count++;
            }
        }
        if (count == 0) {
            System.out.println("У вас нет зданий для улучшения!");
            return;
        }

        System.out.println("Выберите здание для улучшения:");
        for (int i = 0; i < ownedBuildings.size(); i++) {
            Building b = ownedBuildings.get(i);
            if (!b.isUpgraded()) {
                System.out.println(i + 1 + ". " + b.getName() + " → " + b.getUpgradeName() + " (Стоимость: " + b.getUpgradePrice() + " золота)");
            }
        }

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice < 1 || choice > ownedBuildings.size()) {
            System.out.println("Некорректный выбор.");
            return;
        }

        Building selected = ownedBuildings.get(choice - 1);

        if (selected.isUpgraded()) {
            System.out.println("Это здание ужу улучшено");
            return;
        }

        if (hero.getGold() < selected.getUpgradePrice()) {
            System.out.println("У вас недостаточно золота.");
            return;
        }

        hero.setGold(hero.getGold() - selected.getUpgradePrice());
        selected.setUpgraded(true);
        Building upgradedBuilding = selected.upgrade();
        upgradedBuilding.setUpgraded(true);
        System.out.println("Вы улучшили " + selected.getName() + " до " + upgradedBuilding.getName() + "!");
        buildings.put(upgradedBuilding.getName(), upgradedBuilding);
    }

    private void buyBuilding(Hero hero) {
        System.out.println("\nВаше золото: " + hero.getGold());
        System.out.println("\nДоступные здания:");
        System.out.println("1. Башня лучников (500 золота) – нанимает лучников (60 золота)");
        System.out.println("2. Башня грифонов (600 золота) – нанимает грифонов (80 золота)");
        System.out.println("3. Казармы (800 золота) – нанимает мечников (100 золота)");
        System.out.println("4. Монастырь (1000 золота) – нанимает монахов (120 золота)");
        System.out.println("5. Ипподром (1200 золота) – нанимает кавалеристов (130 золота)");
        System.out.println("6. Таверна (400 золота) – нанимает героев (100 золота)");
        System.out.println("7. Конюшня (600 золота) – дальность пути увеличивается до конца недели у каждого посетившего");
        System.out.println("8. Рынок (600 золота) – прибыль города увеличивается на 300 золота в ход.");
        System.out.println("9. Храм зверей (600 золота) – позволяет обучать специальных зверей");
        System.out.print("Выберите здание (1-9): ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        Building newBuilding = null;
        Building newSpecialBuilding = null;
        switch (choice) {
            case "1":
                newBuilding = new Building("Башня лучников", 500, "Лучник", 60, "Улучшенная башня лучников", "Стрелок");
                break;
            case "2":
                newBuilding = new Building("Башня грифонов", 600, "Грифон", 80, "Улучшенная башня грифонов", "КоролевскийUрифон");
                break;
            case "3":
                newBuilding = new Building("Казармы", 800, "Мечник", 100, "Улучшенные казармы", "Крестоносец");
                break;
            case "4":
                newBuilding = new Building("Монастырь", 1000, "Монах", 120, "Улучшенный монастырь", "Фанатик");
                break;
            case "5":
                newBuilding = new Building("Ипподром", 1200, "Кавалерист", 130, "Улучшенный ипподром", "Чемпион");
                break;
            case "6":
                newSpecialBuilding = new Building("Таверна", 400, "", 0, "", "");
                newSpecialBuilding.setUpgraded(true);
                break;
            case "7":
                newSpecialBuilding = new Building("Конюшня", 600, "", 0, "", "");
                newSpecialBuilding.setUpgraded(true);
                break;
            case "8":
                newSpecialBuilding = new Building("Рынок", 600, "", 0, "", "");
                if (specialBuildings.containsKey(newSpecialBuilding.getName())) {
                    System.out.print("У вас уже есть это здание!");
                    return;
                }
                newSpecialBuilding.setUpgraded(true);
                rent += 300;
                break;
            case "9":
                newSpecialBuilding = new Building("Храм зверей", 600, "", 0, "", "");
                newSpecialBuilding.setUpgraded(true);
                break;
            default:
                System.out.println("Некорректный выбор.");
                return;
        }
        if (newSpecialBuilding != null) {
            if (specialBuildings.containsKey(newSpecialBuilding.getName())) {
                System.out.print("У вас уже есть это здание!");
                return;
            }
            specialBuildings.put(newSpecialBuilding.getName(), newSpecialBuilding);
            hero.setGold(hero.getGold() - newSpecialBuilding.getPrice());
            System.out.println("Вы построили: " + newSpecialBuilding.getName());
            System.out.println("\nВаше золото: " + hero.getGold());
        }
        else if (hero.getGold() >= newBuilding.getPrice()) {
            if (buildings.containsKey(newBuilding.getName())) {
                System.out.print("У вас уже есть это здание!");
                return;
            }
            buildings.put(newBuilding.getName(), newBuilding);
            hero.setGold(hero.getGold() - newBuilding.getPrice());
            System.out.println("Вы построили: " + newBuilding.getName());
            System.out.println("\nВаше золото: " + hero.getGold());
        } else {
            System.out.println("Недостаточно золота.");
        }
    }

    private void buyUnits(Hero hero) {
        if (buildings.isEmpty()) {
            System.out.println("У вас нет зданий для найма юнитов!");
            return;
        }

        System.out.println("\nВыберите здание для найма юнитов:");
        int index = 1;
        List<Building> buildingList = new ArrayList<>(buildings.values());
        for (Building b : buildingList) {
            System.out.println(index + ". " + b.getName() + " (нанимает " + b.getUnitType() + " за " + b.getUnitPrice() + " золота)");
            index++;
        }
        System.out.print("Введите номер здания: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice < 1 || choice > buildingList.size()) {
            System.out.println("Некорректный выбор.");
            return;
        }

        Building selectedBuilding = buildingList.get(choice - 1);
        System.out.println("Сколько " + selectedBuilding.getUnitType() + " хотите нанять?");
        int num = scanner.nextInt();
        double cost = num * selectedBuilding.getUnitPrice();
        if (hero.getGold() >= cost) {
            Unit unit = null;
            switch (selectedBuilding.getUnitType()) {
                case "Копейщик":
                    unit = new Spearman(num);
                    break;
                case "Алебардщик":
                    unit = new Halberdier(num);
                    break;
                case "Лучник":
                    unit = new Archer(num);
                    break;
                case "Стрелок":
                    unit = new Shooter(num);
                    break;
                case "Грифон":
                    unit = new Griffin(num);
                    break;
                case "КоролевскийГрифон":
                    unit = new RoyalGriffin(num);
                    break;
                case "Мечник":
                    unit = new Swordsman(num);
                    break;
                case "Крестоносец":
                    unit = new Crusader(num);
                    break;
                case "Монах":
                    unit = new Monk(num);
                    break;
                case "Фанатик":
                    unit = new Fanatic(num);
                    break;
                case "Кавалерист":
                    unit = new Cavalryman(num);
                    break;
                case "Чемпион":
                    unit = new Champion(num);
                    break;
                default:
                    System.out.println("Некорректный выбор.");
                    return;
            }
            if (garrison.size() > 10) {
                System.out.println("Гарнизон заполнен!");
                return;
            }
            garrison.add(unit);
            hero.setGold(hero.getGold() - cost);
            System.out.println("Вы наняли " + num + " " + selectedBuilding.getUnitType() + ".");
            System.out.println("\nВаше золото: " + hero.getGold());
        } else {
            System.out.println("Недостаточно золота.");
        }
    }

    private void leaveGarrison(List<Unit> army, Hero hero) {
        if (!(hero.getX() == x && hero.getY() == y)) {
            System.out.println("В замке нет героя");
            return;
        }
        if (garrison.size() > 10) {
            System.out.println("Гарнизон заполнен!");
            return;
        }

        seeGarrison();

        System.out.print("Какой юнит оставить в гарнизоне?\n");
        for (Unit unit : army) {
            System.out.println(unit.getName() + " " + unit.getCount() + " шт");
        }
        Scanner scanner = new Scanner(System.in);
        String unitType = scanner.nextLine();
        Unit unit = getUnitByName(army, unitType);
        if (unit == null) {
            System.out.println("Такого юнита у вас нет.");
            return;
        }
        System.out.print("Сколько оставить? ");
        int num = scanner.nextInt();
        if (num > 0 && num <= unit.getCount()) {
            Unit newUnit = unit.cloneWithCount(num);
            garrison.add(newUnit);
            if (unit.getCount() - num == 0) {
                army.remove(unit);
            }
            else {
                unit.setCount(unit.getCount() - num);
            }
            System.out.println("Вы оставили " + num + " " + unitType + " в гарнизоне.");
        } else {
            System.out.println("Некорректное количество.");
        }
    }

    public boolean haveStable() {
        return specialBuildings.containsKey("Конюшня");
    }

    private void takeGarrison(List<Unit> army, Hero hero) {
        if (!(hero.getX() == x && hero.getY() == y)) {
            System.out.println("В замке нет героя");
            return;
        }
        if (army.size() > 10) {
            System.out.println("Ваша армия заполнена!");
            return;
        }
        if (garrison.isEmpty()) {
            System.out.println("В гарнизоне нет юнитов.");
            return;
        }
        seeGarrison();
        System.out.print("Какой юнит забрать с гарнизона? ");
        Scanner scanner = new Scanner(System.in);
        String unitType = scanner.nextLine();
        Unit unit = getUnitByName(garrison, unitType);
        if (unit == null) {
            System.out.println("Такого юнита в гарнизоне нет.");
            return;
        }
        System.out.print("Сколько забрать? ");
        int num = scanner.nextInt();
        if (num > 0 && num <= unit.getCount()) {
            Unit newUnit = unit.cloneWithCount(num);
            army.add(newUnit);
            if (unit.getCount() - num == 0) {
                garrison.remove(unit);
            }
            else {
                unit.setCount(unit.getCount() - num);
            }
            System.out.println("Вы забрали " + num + " " + unitType + " с гарнизона.");
        } else {
            System.out.println("Некорректное количество.");
        }
    }
    public double getRent() {
        return rent;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public boolean isLosed() {
        return isLosed;
    }

    public void setLosed(boolean losed) {
        isLosed = losed;
    }

    public Map<String, Building> getBuilding() {
        return buildings;
    }

    public Map<String, Building> getSpecialBuilding() {
        return specialBuildings;
    }

    public List<Unit> getGarrison() {
        return garrison;
    }
}
