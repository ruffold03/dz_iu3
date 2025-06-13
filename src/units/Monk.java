package units;

public class Monk extends Unit{
    protected int moveRange = 4;
    protected int attackRange = 9;
    public Monk(int count) {
        super("Монах", 140, 40, 15, 9, 4, count);
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
        return "\uD83E\uDE84 ";
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
        return new Monk(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }

}
