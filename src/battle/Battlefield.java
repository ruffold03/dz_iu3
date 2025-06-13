package battle;

import units.Unit;

import java.util.List;
import java.util.Random;

public class Battlefield {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private final BattlefieldCell[][] cells;

    public Battlefield(String type) {
        cells = new BattlefieldCell[WIDTH][HEIGHT];
        if (type.equals("castleEnemy") || type.equals("castleHero")) {
            type = "path";
        }
        generateWorld(type);
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    private boolean isHereUnit(List<Unit> army, List<Unit> enemyArmy, int x, int y) {
        for (Unit unit : army) {
            if (unit.getX() == x && unit.getY() == y) {
                return true;
            }
        }
        for (Unit unit : enemyArmy) {
            if (unit.getX() == x && unit.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void moveUnit(Unit unit, int newX, int newY, List<Unit> army, List<Unit> enemyArmy) {
        if (!isValidPosition(newY, newX)) {
            System.out.println("Невозможно переместить юнита за границы карты!");
            return;
        }

        int oldX = unit.getX();
        int oldY = unit.getY();

        if (Math.abs(newX - oldX) + Math.abs(newY - oldY) > unit.getMovementRange()) {
            System.out.println(unit.getName() + " не может переместиться так далеко!");
            return;
        }

        if (!cells[newY][newX].isObstacle() && !isHereUnit(army, enemyArmy, newX, newY)) {
            unit.setPosition(newX, newY);
            unit.setMovementRange(unit.getMovementRange() - Math.abs(newX - oldX) + Math.abs(newY - oldY));
        } else {
            System.out.println("Место занято!");
        }
    }

    private Unit getUnitByName(List<Unit> army, String name) {
        for (Unit unit : army) {
            if (unit.getName().equalsIgnoreCase(name)) {
                return unit;
            }
        }
        return null;
    }

    public void attack(Unit unit, int newX, int newY, Unit enemyUnit, List<Unit> army) {
        if (!isValidPosition(newY, newX)) {
            System.out.println("Невозможно переместить юнита за границы карты!");
            return;
        }

        int oldX = unit.getX();
        int oldY = unit.getY();

        if (Math.abs(newX - oldX) + Math.abs(newY - oldY) > unit.getAttackRange()) {
            System.out.println(unit.getName() + " не может атаковать так далеко!");
            return;
        }

        if (!cells[newY][newX].isObstacle()) {
            unit.attack(enemyUnit);
            if (!unit.isAlive()) {
                army.remove(enemyUnit);
            }
        } else {
            System.out.println("Место занято!");
        }
    }

    private boolean isValidPosition(int y, int x) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public void generateWorld(String type){
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                cells[x][y] = new BattlefieldCell(type);
            }
        }
        if (type.equals("castleEnemy") || type.equals("castleHero")) {
            generateWallsInCastle();
        }
        else {
            generateSymmetricObstacles();
        }

    }

    private void generateWallsInCastle() {
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        for (int y = 0; y < HEIGHT; y++) {
            if (y == centerY || y == centerY - 1) {
                continue;
            }
            cells[centerX][y].setType("wall");
            cells[centerX][y].setObstacle(true);
        }
    }

    private void generateSymmetricObstacles() {
        Random random = new Random();
        int obstacleCount = (WIDTH * HEIGHT) / 40;

        for (int i = 0; i < obstacleCount; i++) {
            int x = random.nextInt((WIDTH) / 2) + 2;
            int y = random.nextInt((HEIGHT) / 2);

            setObstacle(x, y);
            setObstacle(WIDTH - 1 - x, y);
            setObstacle(x, HEIGHT - 1 - y);
            setObstacle(WIDTH - 1 - x, HEIGHT - 1 - y);
        }
    }

    private void setObstacle(int x, int y) {
        if (isValidPosition(y, x)) {
            cells[x][y].setType("stone");
            cells[x][y].setObstacle(true);
        }
    }

    private boolean[][] highlightMovementRange(Unit unit) {
        boolean[][] highlight = new boolean[WIDTH][HEIGHT];
        int startX = unit.getX();
        int startY = unit.getY();
        int range = unit.getMovementRange();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int distance = Math.abs(startX - x) + Math.abs(startY - y);
                if (distance < range && !cells[x][y].isObstacle()) {
                    highlight[x][y] = true;
                }
            }
        }
        return highlight;
    }

    private boolean[][] attackRange(Unit unit) {
        boolean[][] attack = new boolean[WIDTH][HEIGHT];
        int startX = unit.getX();
        int startY = unit.getY();
        int range = unit.getAttackRange();

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int distance = Math.abs(startX - x) + Math.abs(startY - y);
                if (distance <= range && !cells[x][y].isObstacle()) {
                    attack[x][y] = true;
                }
            }
        }
        return attack;
    }

    public void display(List<Unit> army, Unit selected, List<Unit> enemyArmy, int act) {
        boolean[][] highlight = highlightMovementRange(selected);;
        boolean[][] attackRangeLight = attackRange(selected);;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean unitDrawn = false;
                for (Unit unit : army) {
                    if (unit.getX() == x && unit.getY() == y) {
                        System.out.print(unit.getSymbol());
                        unitDrawn = true;
                    }
                }
                for (Unit unit : enemyArmy) {
                    if (unit.getX() == x && unit.getY() == y) {
                        System.out.print(unit.getSymbol());
                        unitDrawn = true;
                    }
                }
                if (unitDrawn) continue;
                if (act == 1) {
                    if (attackRangeLight[x][y]) {
                        System.out.print("\uD83D\uDD34 ");
                    }
                    else {
                        System.out.print(cells[x][y].getSymbol());
                    }
                }
                if (act == 2) {
                    if (highlight[x][y]) {
                        System.out.print("🔵 ");
                    } else {
                        System.out.print(cells[x][y].getSymbol());
                    }
                }
            }
            System.out.println();
        }
    }

    public void display(List<Unit> army, List<Unit> enemyArmy) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean unitDrawn = false;
                for (Unit unit : army) {
                    if (unit.getX() == x && unit.getY() == y) {
                        System.out.print(unit.getSymbol());
                        unitDrawn = true;
                    }
                }
                for (Unit unit : enemyArmy) {
                    if (unit.getX() == x && unit.getY() == y) {
                        System.out.print(unit.getSymbol());
                        unitDrawn = true;
                    }
                }
                if (unitDrawn) {
                    continue;
                }
                System.out.print(cells[x][y].getSymbol());
            }
            System.out.println();
        }
    }
}
