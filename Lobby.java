/**
 * @author Nikita Funk und John Braun
 * @version 13.03.2023
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
        boolean name, password = false;

        //Verifying username
        userlist.toFirst();
        while (userlist.hasAccess()){
            if (userlist.getContent().getUsername() == tmp.getUsername())
            {
                name = true;
                break;
            }
            else{userlist.next();}
        }
        //Verifying password
        userlist.toFirst();
        while (userlist.hasAccess()){
            if (userlist.getContent().getPassword() == tmp.getPassword()){
                password = true;
                break;
            }
            else{userlist.next();}
        }
        //Checking if the user is new, the password is wrong or if the username is not available
        if (name == true && password == true){
            playerLobby.append(tmp);
        }

        else if (name == false){
            userlist.append(tmp);
            playerLobby.append(tmp);
        }
        else if (name == true && password == false){
            send(pIP, pPort, "-LOGIN: Username is not available or your password is wrong");
        }
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
        User player = userlist.getContent();
        userlist.toFirst();
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
     * Method request sends a request to a User for a new game
     *
     * @param p1 is the User that sends the request
     * @param p2 is the User that receives the request
     */
    public void request(User p1, User p2){
        boolean playerIsAvailable = false;
        playerLobby.toFirst();
        while(playerLobby.hasAccess()){
            if (playerLobby.getContent() == p2){
                playerIsAvailable = true;
                break;
            }
            else{playerLobby.next();}
        }
        if (playerIsAvailable == true){
            send(p2.getIP(), p2.getPort(), "GETREQUEST:" + p1);
        }
        else{
            send(p1.getIP(), p1.getPort(), "-GETREQUEST:user not available");
        }
    }

    /**
     * Method startGame starts a new game and adds the two Users to the games list
     * To avoid getting invalid requests we remove both users from the playerLobby list
     * Requires a lobby and two Users to start a new game
     */
    public void startGame(User p1, User p2) {
        Game newGame = new Game(p1, p2, this);

        //Adding both users to the games list
        games.append(p1);
        games.append(p2);

        //Removing both users from the playerLobby list
        playerLobby.toFirst();
        while (playerLobby.hasAccess()){
            if (playerLobby.getContent() == p1){
                playerLobby.remove();
                break;
            }
            else{playerLobby.next();}
        }
        playerLobby.toFirst();
        while (playerLobby.hasAccess()){
            if (playerLobby.getContent() == p2){
                playerLobby.remove();
                break;
            }
            else{playerLobby.next();}
        }
    }

    /**
     * Method endGame ends a game
     * Requires two Users
     * 
     */
    public void endGame(User p1, User p2, boolean pWon){
        //Adding both users to the playerLobby list
        playerLobby.append(p1);
        playerLobby.append(p2);

        //Removing both users from the games list
        games.toFirst();
        while (games.hasAccess()){
            if (games.getContent() == p1){
                games.remove();
            }
            else{games.next();}
        }
        games.toFirst();
        while (games.hasAccess()){
            if (games.getContent() == p2){
                games.remove();
            }
            else{games.next();}
        }
    }

    /**
     * Method processMessage
     *
     * @param pIP Ein Parameter
     * @param pPort Ein Parameter
     * @param pMessage Ein Parameter
     */
    public void processMessage(String pIP, int pPort, String pMessage){
        String[] Message = pMessage.split(":");
        User tmp = getPlayer(pIP,pPort);

        switch (Message[0]){
            case "LOGIN":
                login(Message[1], Message[2], pIP, pPort);
                send(pIP, pPort, "STATUS:LOBBY");
                break;

            case "LOGOUT":
                logout(pIP, pPort);
                send(pIP, pPort, "STATUS:LOGIN");
                break;

            case "REQUESTENEMY":
                request(tmp, Message[1]);
                break;

            case "+GETREQUEST":
                if("true".equals(Message[1])){
                    startGame(tmp, Message[2]); //tmp is the first user and Message[2] is the second user
                    send(pIP, pPort, "STATUS:GAME");
                    send(Message[2].getIP, Message[2].getPort, "STATUS:GAME");
                }
                else{
                    send(pIP, pPort, "-REQUESTENEMY:enemy rejected");
                }

        }
    }

    public void processNewConnection(String pClientIP, int pClientPort){}

    public void processClosingConnection(String pClientIP, int pClientPort){}
}