
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
    int gameslost = 0;
    int gameswon = 0;

    /**
     * Konstruktor für Objekte der Klasse User
     */
    public User(String pusername, String ppassword, String pip, int pport)
    {
        username = pusername;
        password = ppassword;
        ip = pip;
        port = pport;
    }

    private String getUsername(){
        return username;
    }

    private String getPassword(){
        return password;
    }

    private String getIp(){
        return ip;
    }

    private int getPort(){
        return port;
    }

    private Game getGame(){
        return game;
    }

    private int getWins(){
        return gameswon;
    }

    private void increaseWins(){}

    private int getLosses(){
        return gameslost;
    }

    private void increaseLosses(){}

    private void setPort(int pport){
        port = pport;
    }

    private void setIp(String pip){
        ip = pip;
    }

    private void setGame(Game pgame){
        game = pgame;
    }

}
