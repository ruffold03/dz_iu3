package game;

public class Records {
    private String playerName;
    private double score;

    public Records(String playerName, double score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }


    public double getScore() {
        return score;
    }
}
