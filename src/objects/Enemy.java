package objects;

import battle.Battle;
import map.Cell;
import units.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Enemy {
    private int x;
    private int y;
    private double gold;
    private int strength = 100;
    private double moveCount = 6;
    private int radius = 3;
    private List<Unit> army = new ArrayList<>();
    private int xCastle;
    private int yCastle;
    private boolean check = false;
    private boolean lose = false;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        army.add(new Archer(5));
        army.add(new Spearman(10));
        xCastle = x - 1;
        yCastle = y;
        gold = 500;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Unit buyRandomUnits() {
        Random random = new Random();
        int chance = random.nextInt(100);
        int count = 1;
        if (chance < 5) {
            while (gold > 195*count) {
                count++;
            }
            gold -= 195*count;
            return new Champion(count - 1);
        }
        if (chance < 10) {
            while (gold > 130 * count) {
                count++;
            }
            gold -= 130*count;
            return new Cavalryman(count - 1);
        }
        if (chance < 15) {
            while (gold > 180 * count) {
                count++;
            }
            gold -= 180*count;
            return new Fanatic(count - 1);
        }
        if (chance < 21) {
            while (gold > 120 * count) {
                count++;
            }
            gold -= 120*count;
            return new Monk(count - 1);
        }
        if (chance < 29) {
            while (gold > 150 * count) {
                count++;
            }
            gold -= 150*count;
            return new Crusader(count - 1);
        }
        if (chance < 38) {
            while (gold > 100 * count) {
                count++;
            }
            gold -= 100*count;
            return new Swordsman(count - 1);
        }
        if (chance < 46) {
            while (gold > 120 * count) {
                count++;
            }
            gold -= 120*count;
            return new RoyalGriffin(count - 1);
        }
        if (chance < 55) {
            while (gold > 80 * count) {
                count++;
            }
            gold -= 80*count;
            return new Griffin(count - 1);
        }
        if (chance < 65) {
            while (gold > 90 * count) {
                count++;
            }
            gold -= 90*count;
            return new Shooter(count - 1);
        }
        if (chance < 75) {
            while (gold > 60 * count) {
                count++;
            }
            gold -= 60*count;
            return new Archer(count - 1);
        }
        if (chance < 85) {
            while (gold > 60 * count) {
                count++;
            }
            gold -= 60*count;
            return new Halberdier(count - 1);
        }
        if (chance < 100) {
            while (gold > 40 * count) {
                count++;
            }
            gold -= 40*count;
            return new Spearman(count - 1);
        }
        return null;
    }
    public boolean isHeroVisible(int heroX, int heroY, int radius) {
        int dx = Math.abs(heroX - x);
        int dy = Math.abs(heroY - y);
        return dx * dx + dy * dy <= radius * radius;
    }

    public boolean move(int x, int y, Cell[][] cells, int width, int heigth) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        Collections.shuffle(Arrays.asList(directions));
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX < 0 || newY < 0 || newX >= width || newY >= heigth) {
                continue;
            }
            if (cells[newX][newY].getType().equals("sea") || cells[newX][newY].getFeature().equals("mountain")) {
                continue;
            }
            if (moveCount - cells[newX][newY].fine(cells[newX][newY].getType(), cells[newX][newY].getFeature()) >= 0) {
                this.x = newX;
                this.y = newY;
                moveCount -= cells[newX][newY].fine(cells[newX][newY].getType(), cells[newX][newY].getFeature());
                return true;
            }
        }
        return false;
    }

    public void moveAwayFrom(int targetX, int targetY,  Cell[][] cells, int width, int heigth) {
        int bestX = x;
        int bestY = y;
        int bestDistance = -1;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (newX < 0 || newY < 0 || newX >= width || newY >= heigth) {
                continue;
            }

            if (cells[newX][newY].getType().equals("sea") || cells[newX][newY].getFeature().equals("mountain")) {
                continue;
            }

            int distance = Math.abs(newX - targetX) + Math.abs(newY - targetY);
            if (moveCount - cells[newX][newY].fine(cells[newX][newY].getType(), cells[newX][newY].getFeature()) >= 0) {
                if (distance > bestDistance) {
                    bestDistance = distance;
                    bestX = newX;
                    bestY = newY;
                }
            }
        }
        moveCount -= cells[bestX][bestY].fine(cells[bestX][bestY].getType(), cells[bestX][bestY].getFeature());
        x = bestX;
        y = bestY;
    }

    public void moveTowards(int targetX, int targetY, Cell[][] cells, int width, int height) {
        int bestX = x;
        int bestY = y;
        int bestDistance = Integer.MAX_VALUE;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (newX < 0 || newY < 0 || newX >= width || newY >= height) {
                continue;
            }

            if (cells[newX][newY].getType().equals("sea") || cells[newX][newY].getFeature().equals("mountain")) {
                continue;
            }

            int distance = Math.abs(newX - targetX) + Math.abs(newY - targetY);
            double cost = cells[newX][newY].fine(cells[newX][newY].getType(), cells[newX][newY].getFeature());

            if (moveCount - cost >= 0 && distance < bestDistance) {
                bestDistance = distance;
                bestX = newX;
                bestY = newY;
            }
        }

        if (bestX != x || bestY != y) {
            moveCount -= cells[bestX][bestY].fine(cells[bestX][bestY].getType(), cells[bestX][bestY].getFeature());
            x = bestX;
            y = bestY;
        }
    }

    public void act(Hero hero, Cell[][] cells, int width, int heigth) {
        if (x == hero.getxCastle() && y == hero.getyCastle()) {
            return;
        }
        else if (isHeroVisible(hero.getxCastle(), hero.getyCastle(), radius)) {
            moveTowards(hero.getxCastle(), hero.getyCastle(), cells, width, heigth);
        }
        else if (xCastle == hero.getX() && yCastle == hero.getY()) {
            moveTowards(xCastle, yCastle, cells, width, heigth);
        }
        else if (isHeroVisible(hero.getX(), hero.getY(), radius)) {
                if (strength >= hero.getStrength()) {
                    moveTowards(hero.getX(), hero.getY(), cells, width, heigth);
                } else {
                    moveAwayFrom(hero.getX(), hero.getY(), cells, width, heigth);
                }
        } else {
            for (int newX = 0; newX < width; newX++) {
                for (int newY = 0; newY < heigth; newY++) {
                    if (cells[newX][newY].getFeature().equals("gold")) {
                        if (isHeroVisible(newX, newY, radius)) {
                            moveTowards(newX, newY, cells, width, heigth);
                        }
                        else {
                            boolean moved = move(x, y, cells, width, heigth);
                            if (!moved) {
                                setMoveCount(0);
                            }
                        }
                    }
                }
            }
        }
        if (x == hero.getX() && y == hero.getY()) {
            System.out.println("Битва! ");
            Battle battle = new Battle(cells[x][y].getType());
            battle.start(hero.getArmy(), army);
            if (battle.isLose()) {
                lose = true;
            }
            check = true;
        }
    }

    public double getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int newMoveCount) {
        moveCount = newMoveCount;
    }

    public int getStrength() {
        return strength;
    }

    public List<Unit> getArmy() {
        return army;
    }

    public double getGold() {
        return gold;
    }
    public void setGold(double gold) {
        this.gold = gold;
    }

    public int getxCastle() {
        return xCastle;
    }

    public int getyCastle() {
        return yCastle;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
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
}
