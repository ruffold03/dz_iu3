package units;

public class Archer extends Unit {
    protected int moveRange = 4;
    protected int attackRange = 9;
    public Archer(int count) {
        super("Лучник", 80, 20, 2, 9, 4, count);
    }

    @Override
    public void attack(Unit target) {
        System.out.println(name + " атакует " + target.name + "!");
        target.takeDamage(attackPower*count);
        System.out.println(name + " наносит " + attackPower*count + " урона!");
        isAttacked = true;
    }

    @Override
    public String getSymbol() {
        return "\uD83C\uDFF9 ";
    }

    @Override
    public int getMovementRange(){
        return moveRange;
    }

    @Override
    public int getAttackRange(){
        return attackRange;
    }

    @Override
    public void setMovementRange(int moveRange){
        this.moveRange = moveRange;
    }

    @Override
    public Unit cloneWithCount(int newCount) {
        return new Archer(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}