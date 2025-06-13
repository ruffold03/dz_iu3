package units;

public class Cavalryman extends Unit{
    protected int moveRange = 6;
    protected int attackRange = 1;
    public Cavalryman(int count) {
        super("Кавалерист", 150, 50, 30, 1, 6, count);
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
        return "\uD83D\uDC0E ";
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
        return new Cavalryman(newCount);
    }

    @Override
    public void setMoveRange() {
        moveRange = moveRange2;
    }
}
