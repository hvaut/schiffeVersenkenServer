
/**
 * Beschreiben Sie hier die Klasse PlayerSet.
 * 
 * @author Georg/Leon 
 * @version 1.0
 */
public class PlayerSet
{
    private User player1;
    private User player2;
    private boolean rematch = false;
    public PlayerSet(User _player1, User _player2)
    {
        player1 = _player1;
        player2 = _player2;
    }

<<<<<<< HEAD
    public User getPlayer1()
=======
    /**
     * Methode player1
     *
     * @return returns the Username of player1
     */
    public String player1()
>>>>>>> f0f8931f33af253bdfc1ecf0d69dbae12614192a
    {
        return player1;
    }
<<<<<<< HEAD
    public User getPlayer2()
=======
    /**
     * Methode player2
     *
     * @return returns the Username of player2
     */
    public String player2()
>>>>>>> f0f8931f33af253bdfc1ecf0d69dbae12614192a
    {
        return player2;
    }
    
    /**
     * Methode setRematch
     * sets the rematch boolean to the given value
     * @param _rematch 
     */
    public void setRematch(boolean _rematch)
    {
        rematch = _rematch;
    }
    /**
     * Methode getRematch
     *
     * @return returns the value of rematch
     */
    public boolean getRematch()
    {
        return rematch;
    }
}
