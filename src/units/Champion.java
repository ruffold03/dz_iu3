package units;

public class Champion extends Unit{
    protected int moveRange = 7;
    protected int attackRange = 1;
    public Champion(int count) {
        super("Чемпион", 160, 55, 35, 1, 7, count);
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
        return "\uD83C\uDFC7\uD83C\uDFFD ";
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
        return new Champion(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
