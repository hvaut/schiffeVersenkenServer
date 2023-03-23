
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
    int score = 0;

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

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getIP()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }

    public Game getGame()
    {
        return game;
    }

    public int getWins()
    {
        return gamesWon;
    }

    public void increaseWins()
    {
        gamesWon++;
        score = gamesWon/gamesLost;
    }

    public int getLosses()
    {
        return gamesLost;
    }

    public void increaseLosses()
    {
        gamesLost++;
        score = gamesWon/gamesLost;
    }

    public int getScore()
    {
        return score;
    }

    public void setPort(int _port){
        port = _port;
    }

    public void setIp(String _ip){
        ip = _ip;
    }

    public void setGame(Game _game){
        game = _game;
    }
}
