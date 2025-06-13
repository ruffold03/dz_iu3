package animals;

import java.util.Arrays;
import java.util.Collections;

public class Eagle extends Animals{
    public Eagle(int level) {
        super(level);
    }

    @Override
    public void action(int width, int height) {
        switch (level) {
            case 1 -> radius = 2;
            case 2 -> radius = 4;
            case 3 -> speed = 2;
        }
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (moveCount > 0) {
            Collections.shuffle(Arrays.asList(directions));
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX < 0 || newY < 0 || newX >= width || newY >= height) {
                    continue;
                }
                if (moveCount - 1 >= 0) {
                    this.x = newX;
                    this.y = newY;
                    moveCount--;
                    break; // Переместились на одну клетку, выходим из цикла for
                }
            }
        }
    }


}
