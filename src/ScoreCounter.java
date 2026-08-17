public class ScoreCounter {
    private int score;
    private int highScore;
    private static ScoreCounter instance = null;

    /**
     * Constructs a ScoreCounter, initializing score and high score to zero.
     */
    ScoreCounter() 
    {
        score = 0;
        highScore = 0;
    }

    /**
     * Returns the singleton instance of ScoreCounter.
     * @return instance of ScoreCounter
     */
    public static ScoreCounter getInstance() 
    {
        if (instance == null) 
        {
            instance = new ScoreCounter();
        }
        return instance;
    }

    /**
     * Resets the score to zero for a new game.
     */
    public void reset() 
    {
        score = 0;
    }

    /**
     * Updates the high score if the current score is greater than the previous high score.
     */
    public void updateHighScore() 
    {
        if (score > highScore) 
        {
            highScore = score;
        }
    }

    /**
     * Returns the current high score.
     * @return the high score
     */
    public int getHighScore() 
    {
        return highScore;
    }

    /**
     * Increments the score by one and updates the high score if necessary.
     */
    public void increment() 
    {
        score++;
        if (score > highScore) 
        {
            highScore = score;
        }
    }

    /**
     * Returns the current score for the ongoing game.
     * @return the current score
     */
    public int getScore() 
    {
        return score;
    }
}



