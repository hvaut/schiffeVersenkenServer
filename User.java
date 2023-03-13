
/**
 * Beschreiben Sie hier die Klasse User.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class User
{
    String username;
    String password;
    String ip;
    int port;
    Game game;
    int gamesLost = 0;
    int gamesWon = 0;

    /**
     * Konstruktor für Objekte der Klasse User
     */
    public User(String _username, String _password, String _ip, int _port)
    {
        username = _username;
        password = _password;
        ip = _ip;
        port = _port;
    }

    private String getUsername()
    {
        return username;
    }

    private String getPassword()
    {
        return password;
    }

    private String getIp()
    {
        return ip;
    }

    private int getPort()
    {
        return port;
    }

    private Game getGame()
    {
        return game;
    }

    private int getWins()
    {
        return gameswon;
    }

    private void increaseWins()
    {
        gamesWon++;
    }

    private int getLosses()
    {
        return gameslost;
    }

    private void increaseLosses()
    {
        gamesLost++;
    }

    private void setPort(int _port){
        port = _port;
    }

    private void setIp(String _ip){
        ip = _ip;
    }

    private void setGame(Game _game){
        game = _game;
    }

}
