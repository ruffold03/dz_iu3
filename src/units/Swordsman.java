package units;

public class Swordsman extends Unit{
    protected int moveRange = 4;
    protected int attackRange = 1;
    public Swordsman(int count) {
        super("Мечник", 130, 35, 20, 1, 4, count);
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
        return "⚔\uFE0F ";
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
        return new Swordsman(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
