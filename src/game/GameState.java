package game;

import java.io.Serializable;
import java.util.List;

import animals.Eagle;
import map.GameMap;
import objects.Castle;
import objects.Enemy;
import objects.Hero;

public class GameState implements Serializable {
    private GameMap map;
    private List<Hero> heroes;
    private Hero currentHero;
    private List<Enemy> enemies;
    private Enemy currentEnemy;
    private int day;
    private int week;
    private boolean gameRunning;
    private Castle playerCastle;
    private Castle enemyCastle;
    private Eagle eagle;
    public GameMap getMap() {
        return map;
    }
    public void setMap(GameMap map) { this.map = map; }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public Hero getCurrentHero() {
        return currentHero;
    }

    public void setCurrentHero(Hero activeHero) {
        this.currentHero = activeHero;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public void setCurrentEnemy(Enemy currentEnemy) {
        this.currentEnemy = currentEnemy;
    }

    public Castle getPlayerCastle() {
        return playerCastle;
    }

    public void setPlayerCastle(Castle playerCastle) {
        this.playerCastle = playerCastle;
    }

    public Castle getEnemyCastle() {
        return enemyCastle;
    }

    public void setEnemyCastle(Castle enemyCastle) {
        this.enemyCastle = enemyCastle;
    }

    public Eagle getEagle() {
        return eagle;
    }

    public void setEagle(Eagle eagle) {
        this.eagle = eagle;
    }
}
