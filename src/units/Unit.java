package units;

import com.google.gson.annotations.Expose;

public abstract class Unit {
    protected String name;
    protected int health;
    protected int attackPower;
    protected int defense;
    transient protected int attackRange;
    transient protected int moveRange;
    protected int moveRange2;
    protected int x;
    protected int y;
    protected int count;
    protected boolean isAttacked;

    protected String type = getClass().getSimpleName();

    public Unit(String name, int health, int attackPower, int defense, int attackRange, int moveRange, int count) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower*count;
        this.defense = defense*count;
        this.attackRange = attackRange;
        this.moveRange = moveRange;
        this.count = count;
        isAttacked = false;
        moveRange2 = moveRange;
    }

    public void takeDamage(int damage) {
        int reducedDamage = Math.max(0, damage - defense*count);
        int unitHealth = Math.max(0, health*count - reducedDamage);
        if (unitHealth > 0) {
            System.out.println(name + " получил " + reducedDamage + " урона. Осталось здоровья: " + unitHealth);
            count = unitHealth / health + 1;
        }
        else {
            count = 0;
            System.out.println(name + " убит ");
        }
    }

    public void Heal(int amount) {
        health += amount;
    }

    public abstract void attack(Unit target);

    public boolean isAlive() {
        return health*count > 0;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public String getName() { return name; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void setMovementRange(int moveRange);

    public abstract String getSymbol();

    public abstract int getMovementRange();

    public abstract int getAttackRange();

    public boolean getIsAttacked() {
        return isAttacked;
    }
    public void setIsAttacked() {
        isAttacked = false;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }


    public abstract void setMoveRange();

    public abstract Unit cloneWithCount(int newCount);
    public String getType() {
        return type;
    }
}