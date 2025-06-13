package units;

public class Halberdier extends Unit{
    protected int moveRange = 5;
    protected int attackRange = 1;
    public Halberdier(int count) {
        super("Алебардщик", 130, 20, 10, 1, 5, count);
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
        return "\uD83E\uDE93 ";
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
        return new Halberdier(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
