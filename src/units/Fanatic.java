package units;

public class Fanatic extends Unit{
    protected int moveRange = 4;
    protected int attackRange = 9;
    public Fanatic(int count) {
        super("Фанатик", 145, 45, 17, 9, 4, count);
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
        return "\uD83E\uDDD9\u200D♂\uFE0F ";
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
        return new Fanatic(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
