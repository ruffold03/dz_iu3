package battle;

import units.Unit;

import java.util.List;
import java.util.Scanner;

public class Battle {
    private Battlefield battlefield;
    private Scanner scanner;
    private boolean lose;

    public Battle(String type) {
        battlefield = new Battlefield(type);
        scanner = new Scanner(System.in);
        lose = false;
    }

    private Unit getUnitByName(List<Unit> army, String name) {
        for (Unit unit : army) {
            if (unit.getName().equalsIgnoreCase(name.trim())) {
                return unit;
            }
        }
        return null;
    }

    private void setPosition(List<Unit> army, List<Unit> enemyArmy) {
        int y = 0;
        int x = 0;
        for (Unit unit : army) {
            unit.setPosition(x, y);
            y++;
        }
        x = 9;
        y = 0;
        for (Unit unit : enemyArmy) {
            unit.setPosition(x, y);
            y++;
        }
    }

    private Unit findTargetInRange(Unit enemy, List<Unit> army) {
        for (Unit unit : army) {
            int moveRange = enemy.getMovementRange();
            int attackRange = enemy.getAttackRange();

            int distance = Math.abs(enemy.getX() - unit.getX()) + Math.abs(enemy.getY() - unit.getY());

            if (distance <= moveRange + attackRange) {
                return unit;
            }
        }
        return null;
    }

    private void moveTowardsClosestEnemy(Unit enemy, List<Unit> army, List<Unit> enemyArmy) {
        Unit closestEnemy = null;
        int minDistance = Integer.MAX_VALUE;

        for (Unit unit : army) {
            int distance = Math.abs(enemy.getX() - unit.getX()) + Math.abs(enemy.getY() - unit.getY());
            if (distance < minDistance) {
                minDistance = distance;
                closestEnemy = unit;
            }
        }

        if (closestEnemy == null) return;

        int newX = enemy.getX();
        int newY = enemy.getY();

        if (Math.abs(closestEnemy.getX() - newX) > Math.abs(closestEnemy.getY() - newY)) {
            newX += Integer.compare(closestEnemy.getX(), newX);
        } else {
            newY += Integer.compare(closestEnemy.getY(), newY);
        }

        battlefield.moveUnit(enemy, newX, newY,  army,  enemyArmy);
    }

    private void moveAndAttack(Unit enemy, Unit target, List<Unit> army, List<Unit> enemyArmy) {
        int attackRange = enemy.getAttackRange();
        int moveRange = enemy.getMovementRange();

        int newX = enemy.getX();
        int newY = enemy.getY();

        while (Math.abs(target.getX() - newX) + Math.abs(target.getY() - newY) > attackRange && moveRange > 0) {
            if (Math.abs(target.getX() - newX) > Math.abs(target.getY() - newY)) {
                newX += Integer.compare(target.getX(), newX);
            } else {
                newY += Integer.compare(target.getY(), newY);
            }
            moveRange--;
        }

        battlefield.moveUnit(enemy, newX, newY, army, enemyArmy);

        if (Math.abs(target.getX() - newX) + Math.abs(target.getY() - newY) <= attackRange) {
            battlefield.attack(enemy, target.getX(), target.getY(), target, army);
        }
    }

    public void start(List<Unit> army, List<Unit> enemyArmy) {
        setPosition(army, enemyArmy);
        boolean battle = true;
        boolean heroMove = true;
        lose = false;
        while (battle) {
            if (army.isEmpty()) {
                System.out.println("Ваше войско уничтожено! Вы проиграли.");
                return;
            }

            if (enemyArmy.isEmpty()) {
                System.out.println("Вы победили! Все враги повержены.");
                return;
            }
            heroMove = true;
            while (heroMove) {
                System.out.println("Ваш ход ");
                for (Unit unit : army) {
                    unit.setMoveRange();
                }
                for (Unit unit : enemyArmy) {
                    unit.setMoveRange();
                }
                battlefield.display(army, enemyArmy);
                System.out.println("Введите имя юнита или действие (r - закончить ход, q - сбежать с поля боя)");
                String command = scanner.next();
                if (command.equals("r")) {
                    heroMove = false;
                    continue;
                }
                if (command.equals("q")) {
                    System.out.println("Вы бежали с поля боя, но вас поймали. Вы потеряли героя ");
                    lose = true;
                    battle = false;
                    return;
                }
                Unit selectedUnit = getUnitByName(army, command);
                if (selectedUnit == null) {
                    System.out.println("Юнит не найден! Попробуйте снова.");
                    continue;
                }
                System.out.println("Выберете действие: ");
                System.out.println("1. Атаковать ");
                System.out.println("2. Переместиться ");
                int act = scanner.nextInt();
                battlefield.display(army, selectedUnit, enemyArmy, act);
                if (act == 2) {
                    System.out.println("Куда переместиться(x и y): ");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    battlefield.moveUnit(selectedUnit, x, y, army, enemyArmy);
                }
                if (act == 1) {
                    boolean isHereEnemy = false;
                    System.out.println("Кого атаковать (x и y): ");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    String nameEnemyUnit = "";
                    for (Unit enemyUnit : enemyArmy) {
                        if (enemyUnit.getX() == x && enemyUnit.getY() == y) {
                            isHereEnemy = true;
                            nameEnemyUnit = enemyUnit.getName();
                        }
                    }
                    Unit enemyUnit = getUnitByName(enemyArmy, nameEnemyUnit);
                    if (isHereEnemy) {
                        if (!selectedUnit.getIsAttacked()) {
                            battlefield.attack(selectedUnit, x, y, enemyUnit, enemyArmy);
                            heroMove = false;
                        } else {
                            System.out.println("Этот юнит уже атаковал ");
                        }
                    }
                    else {
                        System.out.println("Здесь никого нет ");
                    }
                }
                selectedUnit.setIsAttacked();
            }
            System.out.println("Ход противника ");
            boolean isMoved = false;
            for (Unit enemyUnit : enemyArmy) {
                Unit target = findTargetInRange(enemyUnit, army);
                if (isMoved) {
                    continue;
                }
                if (target != null) {
                    moveAndAttack(enemyUnit, target, army, enemyArmy);
                    if (!target.isAlive()) {
                        army.remove(getUnitByName(army, target.getName()));
                    }
                    isMoved = true;
                } else {
                    System.out.println(enemyUnit.getName() + " передвигается ближе к врагу");
                    moveTowardsClosestEnemy(enemyUnit, army, enemyArmy);
                }
            }
        }
    }

    public boolean isLose() {
        return lose;
    }
}
