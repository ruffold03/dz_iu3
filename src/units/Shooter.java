package units;

public class Shooter extends Unit{
    protected int moveRange = 4;
    protected int attackRange = 9;
    public Shooter(int count) {
        super("Стрелок", 90, 25, 3, 9, 4, count);
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
        return "\uD83C\uDFAF ";
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
        return new Shooter(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
