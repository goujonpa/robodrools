package baguetteSOLO;

public class BattleState {
    private double fieldWidth;
    private double fieldHeight;
    private int roundNumber;
    private int currentRound;
    private long time;
    private int enemyNumber;

    public BattleState(double fieldWidth, double fieldHeight, int roundNumber, int currentRound, long time, int enemyNumber) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.roundNumber = roundNumber;
        this.currentRound = currentRound;
        this.time = time;
        this.enemyNumber = enemyNumber;
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(double fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(double fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public int getEnemyNumber() {
        return enemyNumber;
    }

    public void setEnemyNumber(int enemyNumber) {
        this.enemyNumber = enemyNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }  
}