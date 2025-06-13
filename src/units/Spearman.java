package units;

public class Spearman extends Unit {
    protected int moveRange = 4;
    protected int attackRange = 1;
    public Spearman(int count) {
        super("Копейщик", 100, 15, 5, 1, 4, count);
    }

    @Override
    public void attack(Unit target) {
        System.out.println(name + " атакует " + target.name + " копьем!");
        System.out.println(name + " наносит " + attackPower*count + " урона!");
        target.takeDamage(attackPower*count);
        isAttacked = true;
    }

    @Override
    public String getSymbol() {
        return "\uD83D\uDDE1\uFE0F ";
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
        return new Spearman(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
