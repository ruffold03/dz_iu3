package map;

import animals.Animals;
import objects.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class GameMap {
    private final Cell[][] cells;
    private int width;
    private int height;
    private int radius;
    private int castleX;
    private int castleY;
    private int enemyCastleX;
    private int enemyCastleY;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clearPath(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getType().equals("path")) {
                    cells[i][j].setType("field");
                }
            }
        }
    }

    public void road(int xHeroCastle, int yHeroCastle, int xEnemyCastle, int yEnemyCastle, Cell[][] cells) {
        int xPath = xHeroCastle;
        int yPath = yHeroCastle + 1;
        Random random = new Random();
        Set<String> visited = new HashSet<>();
        while (!(xPath == xEnemyCastle && yPath == yEnemyCastle + 1)) {
            String posKey = xPath + "," + yPath;
            if (visited.contains(posKey)) {
                break;
            }
            visited.add(posKey);
            if (cells[xPath][yPath].getType().equals("castleHero")) {
                boolean bypassed = false;
                if (isLet(xPath, yPath - 1) && !visited.contains(xPath + "," + (yPath - 1))) {
                    yPath--; bypassed = true;
                } else if (isLet(xPath, yPath + 1) && !visited.contains(xPath + "," + (yPath + 1))) {
                    yPath++; bypassed = true;
                } else if (isLet(xPath - 1, yPath) && !visited.contains((xPath - 1) + "," + yPath)) {
                    xPath--; bypassed = true;
                } else if (isLet(xPath + 1, yPath) && !visited.contains((xPath + 1) + "," + yPath)) {
                    xPath++; bypassed = true;
                }

                if (bypassed) continue;
                break;
            }

            cells[xPath][yPath].setType("path");

            int dx = Integer.compare(xEnemyCastle, xPath);
            int dy = Integer.compare(yEnemyCastle + 1, yPath);

            boolean moved = false;

            if (random.nextBoolean()) {
                if (dx != 0 && isLet(xPath + dx, yPath) && !visited.contains((xPath + dx) + "," + yPath)) {
                    xPath += dx;
                    moved = true;
                } else if (dy != 0 && isLet(xPath, yPath + dy) && !visited.contains(xPath + "," + (yPath + dy))) {
                    yPath += dy;
                    moved = true;
                }
            } else {
                if (dy != 0 && isLet(xPath, yPath + dy) && !visited.contains(xPath + "," + (yPath + dy))) {
                    yPath += dy;
                    moved = true;
                } else if (dx != 0 && isLet(xPath + dx, yPath) && !visited.contains((xPath + dx) + "," + yPath)) {
                    xPath += dx;
                    moved = true;
                }
            }

            if (!moved) {
                clearPath(cells);
                road(xHeroCastle - 1, yHeroCastle, xEnemyCastle - 1, yEnemyCastle, cells);
            }
        }
    }

    public String bioms(int x, int y, Cell cells[][]){
        Random random = new Random();
        int biom = random.nextInt(100);
        if (x == 0 || y == 0){
            if (biom < 15) {
                return "snow";
            }
            if (biom < 30) {
                return "desert";
            }
            if (biom < 45) {
                return "sea";
            }
            return "field";
        }
        else {
            int snowChance = 0;
            int desertChance = 0;
            int seaChance = 0;
            int fieldChance = 0;
            String left = cells[x - 1][y].getType();
            String up = cells[x][y - 1].getType();
            if (left.equals(up)) {
                if (biom < 70) {
                    return left;
                }
            }
            snowChance += left.equals("snow") ? 40 : 0;
            snowChance += up.equals("snow") ? 40 : 0;

            desertChance += left.equals("desert") ? 40 : 0;
            desertChance += up.equals("desert") ? 40 : 0;

            seaChance += left.equals("sea") ? 40 : 0;
            seaChance += up.equals("sea") ? 40 : 0;

            fieldChance += left.equals("field") ? 40 : 0;
            fieldChance += up.equals("field") ? 40 : 0;

            snowChance += 10;
            desertChance += 10;
            seaChance += 10;
            fieldChance += 10;
            int totalChance = snowChance + desertChance + seaChance + fieldChance;
            int pick = random.nextInt(totalChance);

            if (pick < snowChance) return "snow";
            if (pick < snowChance + desertChance) return "desert";
            if (pick < snowChance + desertChance + seaChance) return "sea";
            return "field";
        }
    }

    public void populateFeatures(int width, int height, Cell[][] cells, int xHero, int yHero) {
        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String type = cells[x][y].getType();
                if (type.equals("snow")) {
                    if (random.nextInt(100) < 35) {
                        cells[x][y].setFeature("tree");
                    } else if (random.nextInt(100) < 20 && x != xHero && y != yHero) {
                        cells[x][y].setFeature("mountain");
                    }
                    else if (random.nextInt(100) < 10) {
                        cells[x][y].setFeature("gold");
                    }
                } else if (type.equals("desert")) {
                    if (random.nextInt(100) < 35) {
                        cells[x][y].setFeature("tree");
                    }
                    else if (random.nextInt(100) < 10) {
                        cells[x][y].setFeature("gold");
                    }
                } else if (type.equals("field")) {
                    if (random.nextInt(100) < 55) {
                        cells[x][y].setFeature("tree");
                    } else if (random.nextInt(100) < 40  && x != xHero && y != yHero) {
                        cells[x][y].setFeature("mountain");
                    }
                    else if (random.nextInt(100) < 10) {
                        cells[x][y].setFeature("gold");
                    }
                }
            }
        }
    }

    public void generateWorld(int width, int height, Cell cells[][], int xHero, int yHero){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y].getType().equals("field") && x != xHero && y != yHero) {
                    cells[x][y].setType(bioms(x, y, cells));
                }
            }
        }
    }

    public void randomGeneration(int width, int height, Cell[][] cells, int xHero, int yHero) {
        generateWorld(width, height, cells, xHero, yHero);
        populateFeatures(width, height, cells, xHero, yHero);
    }

    public void createMap(int xHero, int yHero, int xEnemy, int yEnemy) {
        cells[xEnemy - 1][yEnemy].setType("castleEnemy");
        cells[xEnemy - 1][yEnemy + 1].setType("path");
        cells[xHero - 1][yHero].setType("castleHero");
        cells[xHero - 1][yHero + 1].setType("path");
        road(xHero - 1, yHero, xEnemy - 1, yEnemy, cells);
        randomGeneration(width, height, cells, xHero, yHero);
    }

    public void createMap() {
        Random random = new Random();
        int castleX = random.nextInt(width - 3) + 1;
        int castleY = random.nextInt(height - 3) + 1;
        int castleEnemyX, castleEnemyY;
        do {
            castleEnemyX = random.nextInt(width - 3) + 1;
            castleEnemyY = random.nextInt(height - 3) + 1;
        } while (Math.abs(castleX - castleEnemyX) + Math.abs(castleY - castleEnemyY) < width);
        cells[castleEnemyX][castleEnemyY].setType("castleEnemy");
        cells[castleEnemyX][castleEnemyY + 1].setType("path");
        cells[castleX][castleY].setType("castleHero");
        cells[castleX][castleY + 1].setType("path");
        road(castleX, castleY, castleEnemyX, castleEnemyY, cells);
        randomGeneration(width, height, cells, castleX, castleY);
    }

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        radius = 3;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell("field");
            }
        }
    }

    public boolean isLet(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height){
            return (cells[x][y].getType().equals("custom") && cells[x][y].isStop()) || cells[x][y].getType().equals("field") || cells[x][y].getType().equals("path") || cells[x][y].getType().equals("snow") || cells[x][y].getType().equals("desert") ;
        }
        return false;
    }

    public void allVision() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[x][y].setVisible(true);
            }
        }
    }

    public boolean inBorder(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void display(Hero hero, Enemy enemy, List<Hero> heroes, Animals eagle) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[hero.getX()][hero.getY()].getFeature().equals("gold")) {
                    cells[hero.getX()][hero.getY()].setFeature("no");
                }
                boolean visible = false;
                for (Hero h : heroes) {
                    if (Math.abs(h.getX() - x) <= radius && Math.abs(h.getY() - y) <= radius) {
                        visible = true;
                        break;
                    }
                }
                if (hero.isEagle()) {
                    if (Math.abs(eagle.getX() - x) <= eagle.getRadius() && Math.abs(eagle.getY() - y) <= eagle.getRadius()) {
                        visible = true;
                    }
                }
                if (visible) {
                    cells[x][y].setVisible(true);
                }
                boolean heroDrawn = false;
                for (Hero h : heroes) {
                    if (h.getX() == x && h.getY() == y) {
                        if (h.getX() == h.getxCastle() && h.getY() == h.getyCastle()) {
                            System.out.print("\uD83C\uDFF0 ");
                        }
                        else if (h.getX() == enemy.getxCastle() && h.getY() == enemy.getyCastle()){
                            System.out.print("\uD83D\uDD4C ");
                        }
                        else {
                            System.out.print("\uD83C\uDFC7 ");
                        }
                        heroDrawn = true;
                        break;
                    }
                }

                if (heroDrawn) {
                    continue;
                }

                if (hero.isEagle()) {
                    if (eagle.getX() == x && eagle.getY() == y) {
                        System.out.print("\uD83E\uDD85 ");
                        continue;
                    }
                }

                if (enemy != null && !heroes.isEmpty() && enemy.isHeroVisible(heroes.getFirst().getX(), heroes.getFirst().getY(), radius)) {
                    if (enemy.getX() == x && enemy.getY() == y) {
                        if (enemy.getX() == enemy.getxCastle() && enemy.getY() == enemy.getyCastle()) {
                            System.out.print("\uD83D\uDD4C ");
                        } else if (enemy.getX() == hero.getxCastle() && enemy.getY() == hero.getyCastle()) {
                            System.out.print("\uD83C\uDFF0 ");
                        } else {
                            System.out.print("\uD83D\uDC80 ");
                        }
                    } else {
                        System.out.print(cells[x][y].getSymbol());
                    }
                } else {
                    System.out.print(cells[x][y].getSymbol());
                }
            }
            System.out.println();
        }
    }

    public void display() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[x][y].setVisible(true);
                System.out.print(cells[x][y].getSymbol());
                cells[x][y].setVisible(false);
            }
            System.out.println();
        }
    }

    public void copy(GameMap newMap) {
        width = newMap.getWidth();
        height = newMap.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Копируем все поля исходной клетки
                Cell sourceCell = newMap.getMap()[x][y];
                cells[x][y].setType(sourceCell.getType());
                cells[x][y].setFeature(sourceCell.getFeature());

                // Если клетка custom — копируем дополнительные поля
                if ("custom".equals(sourceCell.getType())) {
                    cells[x][y].custom(
                            sourceCell.name,
                            sourceCell.fine,
                            sourceCell.symbol,
                            sourceCell.stop
                    );
                }
            }
        }
    }

    public void clear(int x, int y, int newX, int newY) {
        for (int i = x; i <= newX; i++) {
            for (int j = y; j <= newY; j++) {
                cells[i][j] = new Cell("field");
            }
        }
    }

    public void newCell(String name, int fine, String symbol, boolean stop, int x, int y, int newX, int newY) {
        for (int i = x; i <= newX; i++) {
            for (int j = y; j <= newY; j++) {
                cells[i][j].custom(name, fine, symbol, stop);
            }
        }
    }

    public void changeMap(int type, int x, int y, int newX, int newY) {
        for (int i = x; i <= newX; i++) {
            for (int j = y; j <= newY; j++) {
                switch(type) {
                    case 1 -> cells[i][j].setType("path");
                    case 2 -> cells[i][j].setFeature("tree");
                    case 3 -> cells[i][j].setFeature("mountain");
                    case 4 -> cells[i][j].setType("sea");
                    case 5 -> cells[i][j].setType("snow");
                    case 6 -> cells[i][j].setType("desert");
                    case 7 -> cells[i][j].setFeature("gold");
                    case 8 -> {
                        cells[i][j].setType("heroCastle");
                        setCastleCoord(i, j);
                    }
                    case 9 -> {
                        cells[i][j].setType("enemyCastle");
                        setEnemyCastleCoord(i, j);
                    }
                    case 10 -> cells[i][j].setType("custom");
                    default -> System.out.println("Ошибка");
                }
            }
        }
    }

    public double getFine(int x, int y) {
        return cells[x][y].fine(cells[x][y].getType(), cells[x][y].getFeature());
    }

    public Cell[][] getMap() {
        return cells;
    }

    public String getCellType(int x, int y) {
        return cells[x][y].getType();
    }
    public String getCellFeature(int x, int y) {
        return cells[x][y].getFeature();
    }

    public void setCellType(int x, int y, String type) {
        cells[x][y].setType(type);
    }

    public boolean getCellStop(int x, int y) {
        return cells[x][y].isStop();
    }

    public void setCellFeature(int x, int y, String feature) {
        cells[x][y].setFeature(feature);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCastleCoord(int x, int y) {
        castleX = x;
        castleY = y;
    }

    public void setEnemyCastleCoord(int x, int y) {
        enemyCastleX = x;
        enemyCastleY = y;
    }

    public int getCastleX() {
        return castleX;
    }
    public int getCastleY() {
        return castleY;
    }

    public int getEnemyCastleX() {
        return enemyCastleX;
    }
    public int getEnemyCastleY() {
        return enemyCastleY;
    }

    public void saveToFile(String filename) throws IOException {
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        Path savesDir = Paths.get("maps");
        if (!Files.exists(savesDir)) {
            Files.createDirectories(savesDir);
        }

        Path filePath = savesDir.resolve(filename);

        Gson gson = new Gson();
        String json = gson.toJson(this);
        Files.write(filePath, json.getBytes());

        System.out.println("Карта сохранена в: " + filePath.toAbsolutePath());
    }

}
