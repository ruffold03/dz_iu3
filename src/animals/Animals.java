package animals;

public abstract class Animals {
    protected int level;
    protected boolean activated = false;
    protected boolean noFine = false;
    protected double moveCount = 5;
    protected int x;
    protected int y;
    protected int radius = 2;
    protected int speed = 1;
    protected String type = getClass().getSimpleName();
    public Animals(int level) {
        this.level = level;
    }

    public abstract void action(int width, int heigth);

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public double getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int newMoveCount) {
        moveCount = newMoveCount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }
}
