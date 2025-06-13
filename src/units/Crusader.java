package units;

public class Crusader extends Unit{
    protected int moveRange = 5;
    protected int attackRange = 1;
    public Crusader(int count) {
        super("Крестоносец", 150, 40, 25, 1, 5, count);
    }

    @Override
    public void attack(Unit target) {
        System.out.println(name + " атакует " + target.name);
        System.out.println(name + " наносит " + attackPower*count + " урона!");
        target.takeDamage(attackPower*count);
        isAttacked = true;
    }

    @Override
    public String getSymbol() {
        return "\uD83D\uDEE1\uFE0F ";
    }

    @Override
    public int getMovementRange(){
        return moveRange;
    }

    @Override
    public void setMovementRange(int moveRange){
        this.moveRange = moveRange;
    }

    @Override
    public int getAttackRange(){
        return attackRange;
    }

    @Override
    public Unit cloneWithCount(int newCount) {
        return new Crusader(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
