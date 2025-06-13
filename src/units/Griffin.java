package units;

public class Griffin extends Unit{
    protected int moveRange = 8;
    protected int attackRange = 1;
    public Griffin(int count) {
        super("Грифон", 120, 30, 5, 1, 8, count);
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
        return "\uD83E\uDD85 ";
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
        return new Griffin(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
