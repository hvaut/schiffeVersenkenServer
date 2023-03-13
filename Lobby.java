/**
 * Beschreiben Sie hier die Klasse Lobby.
 * 
 * @author John Braun 
 * @version 1.0
 */
public class Lobby extends Server
{
    private List<User> userlist;
    private List<User> playerLobby;
    private List<User> games;

    /**
     * Constructor for objects from class Lobby
     * Includes:
     * An userlist to list all existing user
     * A List named playerLobby that includes every user who you can play with
     * A List named games that includes all active games
     */
    public Lobby(int pPort)
    {
        super(pPort);
        userlist = new List<User>();
        playerLobby = new List<User>();
        games = new List<User>();
    }

    /**
     * Method login logs an User in and adds him to the playerLobby list
     * If the User tries to log in the first time, he will be added to the userlist
     * After the User logged in successfully he will be added to the playerLobby
     * @param tmp is the current User that tries to connect
     */
    public void login (String pUsername, String pPassword, String pIP, int pPort){
        User tmp = new User(pUsername, pPassword, pIP, pPort);
        userlist.toFirst();
        boolean name = false;
        
        while (userlist.hasAccess()){
        //Name überprüfen
        if (userlist.getContent().getName() == tmp.getName())
        {
            name = true;
        }
        else{userlist.next();}
        }
        
/**
        while (userlist.hasAccess()){
            if (userlist.getContent().getName() == tmp.getName())
            {
                if(userlist.getContent().getPassword() == tmp.getPassword())
                {
                    playerLobby.append(tmp);
                    break;
                }
                else if(userlist.getContent().getPassword() != tmp.getPassword())
                {
                    break;
                    //Falsches Passwort oder Name ist vergeben!
                }
            }
            
            else if (userlist.getContent().getName() != tmp.getName())
            {
                
            }
            
            else{userlist.next();}
        }**/
    }

    /**
     * Method logout logs an User out and removes him from the playerLobby list
     * We need to remove the user from the playerLobby list so that he isn't available for any new games
     * @param disconnect is the current User that tries to disconnect
     */
    public void logout (String pIP, int pPort)
    {
        closeConnection(pIP,pPort);
        User disconnect = getPlayer(pIP,pPort);
        playerLobby.toFirst();
        while (playerLobby.hasAccess()){
            if (playerLobby.getContent() == disconnect){
                playerLobby.remove();
                break;
            }
            else{playerLobby.next();}
        }
    }

    /**
     * Method getPlayer returns a User
     * Requires the users IP and port to identify the correct User
     */
    public User getPlayer(String pIP, int pPort){
        userlist.toFirst();
        User player = userlist.getContent();
        while (userlist.hasAccess()){
            if (userlist.getContent().getIP() == pIP){
                player = userlist.getContent();
                break;
            }
            else{userlist.next();}
        }
        return player;
    }

    /**
     * Methode startGame starts a new game and adds the two Users to the games list
     * Requires a lobby and two Users to start a new game
     */
    public void startGame(Lobby this, User p1, User p2){

    }

    /**
     * Methode endGame ends a game
     * 
     */
    public void endGame(User p1, User p2, boolean pWon){}

    public void processNewConnection(String pClientIP, int pClientPort){}
    public void processMessage(String pClientIP, int pClientPort, String pMessage){}
    public void processClosingConnection(String pClientIP, int pClientPort){}
}