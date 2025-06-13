package battle;

import map.Cell;

public class BattlefieldCell extends Cell {
    private boolean isObstacle;

    public BattlefieldCell(String type) {
        super(type);
        this.isVisible = true;
        this.isObstacle = false;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.isObstacle = obstacle;
    }
}
