
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

    public User getPlayer1(){
        return player1;
    }

    public User getPlayer2(){
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
