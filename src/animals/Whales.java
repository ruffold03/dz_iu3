package animals;

public class Whales extends Animals{ ;
    private boolean noSkip = false;
    public Whales(int level) {
        super(level);
    }

    @Override
    public void action(int width, int heigth) {
        switch (level) {
            case 1 : {
                activated = true;
            }
            case 2 : {
                noFine = true;
            }
            case 3 : {
                noSkip = true;
            }
        }
    }
}
