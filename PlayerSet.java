
/**
 * Beschreiben Sie hier die Klasse PlayerSet.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
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

    public String player1()
    {
        return player1.getUsername();
    }
    public String player2()
    {
        return player2.getUsername();
    }
}
