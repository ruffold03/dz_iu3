package objects;

import animals.Animals;
import animals.Eagle;
import animals.Husky;
import animals.Whales;
import battle.Battle;
import game.GameTime;
import map.GameMap;
import units.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hero {
    private int x;
    private int y;
    private double moveCount;
    private int strength = 10;
    private double gold;
    private int xCastle;
    private int yCastle;
    private String name;
    private List<Unit> army = new ArrayList<>();
    private boolean check = false;
    private boolean lose = false;
    private boolean visitStable = false;
    private boolean isMove = false;
    private String education;
    Animals whale = new Whales(0);
    Animals husky = new Husky(0);
    Animals eagle = new Eagle(0);

    public Hero(String name, int x, int y, double gold) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.moveCount = 5;
        this.gold = gold;
        xCastle = x - 1;
        yCastle = y;
        army.add(new Spearman(5));
        army.add(new Archer(5));
        education = "Неуч";
    }
    public void move(String direction, GameMap map, Castle castle, List<Hero> heroes, Enemy enemy, Castle castleEnemy, Hotel hotel, GameTime gameTime) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case "w" -> newY--;
            case "s" -> newY++;
            case "a" -> newX--;
            case "d" -> newX++;
            default -> {
                System.out.println("Стою на месте");
                return;
            }
        }

        if (!map.inBorder(newX, newY)) {
            System.out.println("Дальше живут драконы, вернитесь назад");
            return;
        }

        String cellType = map.getCellType(newX, newY);
        String cellFeature = map.getCellFeature(newX, newY);
        boolean isAllowed = map.isLet(newX, newY);
        boolean stop = map.getCellStop(newX, newY);

        if (newX == enemy.getX() && newY == enemy.getY()) {
            System.out.println("Битва!");
            Battle battle = new Battle(map.getMap()[newX][newY].getType());
            battle.start(army, enemy.getArmy());
            if (battle.isLose()) {
                lose = true;
            }
            check = true;
            return;
        }

        if (cellFeature.equals("mountain")) {
            System.out.println("Я не могу пойти в горы");
            return;
        }

        if (stop) {
            System.out.println("Я не могу пойти туда");
            return;
        }

        if (cellType.equals("sea")) {
            if (whale.getLevel() > 0) {
                double moveCost;
                if (whale.getLevel() == 1) {
                    moveCost = 1.5;
                }
                else {
                    moveCost = 1;
                }
                if (moveCount >= moveCost) {
                    if (whale.getLevel() == 3 || map.getCellType(x, y).equals("sea")) {
                        moveCount -= moveCost;
                    }
                    else {
                        moveCount = 0;
                    }
                    x = newX;
                    y = newY;
                } else {
                    System.out.println("Кит устал, ему не хватит сил");
                }
                return;
            }
            else {
                System.out.println("У меня нет кита");
                return;
            }
        }

        double moveCost = map.getFine(newX, newY);
        if (cellType.equals("snow")) {
            if (husky.getLevel() > 0) {
                if (husky.getLevel() == 1 && !cellFeature.equals("tree")) {
                    moveCost = 1;
                }
                if (husky.getLevel() == 2) {
                    if (cellFeature.equals("tree")) {
                        moveCost = 1.5;
                    }
                    else {
                        moveCost = 1;
                    }
                }
                if (husky.getLevel() == 3) {
                    moveCost = 1;
                }
            }
        }
        if (cellType.equals("custom")) {
            moveCount -= map.getFine(newX, newY);
            x = newX;
            y = newY;
        }
        if (isAllowed) {
            if (moveCount >= moveCost) {
                x = newX;
                y = newY;
                moveCount -= moveCost;
                if (cellFeature.equals("gold")) {
                    gold += 500;
                    System.out.println("Вы получили 500 золота");
                    System.out.println("\nВаше золото: " + gold);
                }
            } else {
                System.out.println("Лошадь устала, ей не хватит сил");
            }
            return;
        }
        if (cellType.equals("castleHero")) {
            x = newX;
            y = newY;
            moveCount--;
            castle.action(this, heroes, army);
        } else if (cellType.equals("castleEnemy")) {
            x = newX;
            y = newY;
            moveCount--;
            castleEnemy.action(this, heroes, army);
        } else if (cellType.equals("hotel")) {
            x = newX;
            y = newY;
            moveCount--;
            action(hotel, gameTime);
        }
    }

    public void action(Hotel hotel, GameTime gameTime) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Сколько дней отдохнуть?");
        int day = Integer.parseInt(scanner.nextLine());
        hotel.checkIn(this, 1440*day, day*10, gameTime);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStrength() {
        return strength;
    }

    public double getMoveCount() {
        return moveCount;
    }
    public void setMoveCount(int newMoveCount) {
        moveCount = newMoveCount;
    }
    public double getGold() {
        return gold;
    }
    public void setGold(double count) {
        gold = count;
    }

    public int getxCastle() {
        return xCastle;
    }

    public int getyCastle() {
        return yCastle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Unit> getArmy() {
        return army;
    }

    public boolean isWin() {
        return check;
    }
    public void setWin() {
        check = false;
    }

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasVisitedStable() {
        return visitStable;
    }

    public void visitStable(boolean is) {
        visitStable = is;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getLevel() {
        if (education.equals("Ученик")) {
            return 1;
        }
        if (education.equals("Бакалавр")) {
            return 2;
        }
        if (education.equals("Магистр")) {
            return 3;
        }
        return 0;
    }

    public void buyAnimals(int family, int level) {
        if (family == 1) {
            whale.setLevel(level);
            whale.setActivated(true);
            System.out.println("Вы приручили кита " + level + "-го уровня!");
        }
        if (family == 2) {
            eagle.setLevel(level);
            eagle.setActivated(true);
            System.out.println("Вы приручили ястреба " + level + "-го уровня!");
            eagle.setPosition(xCastle + 1, yCastle);
        }
        if (family == 3) {
            husky.setLevel(level);
            husky.setActivated(true);
            System.out.println("Вы приручили хаски " + level + "-го уровня!");
        }
    }

    public boolean isEagle() {
        return eagle.getLevel() > 0;
    }

    public Animals getEagle() {
        return eagle;
    }

}
