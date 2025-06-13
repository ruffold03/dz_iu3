package units;

public class RoyalGriffin extends Unit{
    protected int moveRange = 8;
    protected int attackRange = 1;
    public RoyalGriffin(int count) {
        super("КоролевскийГрифон", 140, 40, 7, 1, 8, count);
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
        return "\uD83D\uDC26\u200D\uD83D\uDD25 ";
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
        return new RoyalGriffin(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
