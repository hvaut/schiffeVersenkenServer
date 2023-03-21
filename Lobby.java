/**
 * @author Nikita Funk und John Braun
 * @version 16.03.2023
 */
public class Lobby extends Server
{
    private List<User> userlist;
    private List<User> playerLobby;
    private List<Game> games;
    private List<PlayerSet> rematch;
    private List<User> leaderboard;

    /**
     * Constructor for objects from class Lobby
     * Includes:
     * An userlist to list all existing user
     * A List called playerLobby that includes every user who you can play with
     * A List called games that includes all active games
     */
    public Lobby(int pPort)
    {
        super(pPort);
        userlist = new List<User>();
        playerLobby = new List<User>();
        games = new List<Game>();
        rematch = new List<PlayerSet>();
        leaderboard = new List<User>();
    }

    /**
     * Method login logs an User in and adds him to the playerLobby list
     * If the User tries to log in the first time, he will be added to the userlist
     * After the User logged in successfully he will be added to the playerLobby
     * @param tmp is the current User that tries to connect
     */
    public void login (String pUsername, String pPassword, String pIP, int pPort){
        User tmp = new User(pUsername, pPassword, pIP, pPort);
        boolean name = false;
        boolean password = false;

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
        userlist.toFirst();
    }

    /**
     * Method logout logs an User out and removes him from the playerLobby list
     * We need to remove the user from the playerLobby list so that he isn't available for any new games
     * @param disconnect is the current User that tries to disconnect
     */
    public void logout (String pIP, int pPort)
    {
        closeConnection(pIP,pPort);
        User disconnect = getPlayer(pIP);
        playerLobby.toFirst();
        while (playerLobby.hasAccess()){
            if (playerLobby.getContent() == disconnect){
                playerLobby.remove();
                break;
            }
            else{playerLobby.next();}
        }
        playerLobby.toFirst();
    }

    /**
     * Method getPlayer returns a User
     * Requires the users IP to identify the correct User
     */
    public User getPlayer(String pIP){
        User player = userlist.getContent();
        userlist.toFirst();
        while (userlist.hasAccess()){
            if (userlist.getContent().getIP() == pIP){
                player = userlist.getContent();
                break;
            }
            else{userlist.next();}
        }
        userlist.toFirst();
        return player;
    }

    /**
     * Method getPlayerbyName returns a User
     * Requires the username to identify the correct User
     */
    public User getPlayerByName(String pName){
        User player = userlist.getContent();
        userlist.toFirst();
        while (userlist.hasAccess()){
            if (userlist.getContent().getUsername() == pName){
                player = userlist.getContent();
                break;
            }
            else{userlist.next();}
        }
        userlist.toFirst();
        return player;
    }

    /**
     * Method request sends a request to a User for a new game
     * To check if the User is available for a game we search in the playerLobby list for the User
     * When the User is not in the list we know that the User is not connected to the server or in a game right now
     * @param p1 is the User that sends the request
     * @param p2 is the User that receives the request
     */
    public void request(User p1, User p2){
        boolean playerIsAvailable = false;
        playerLobby.toFirst();

        //Identifying if the user is connected to the server and not in a game
        while(playerLobby.hasAccess()){
            if (playerLobby.getContent() == p2){
                playerIsAvailable = true; //Setting the boolean to true, so we know that the player is available
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
        playerLobby.toFirst();
    }

    /**
     * Method leaderboard gives us the leaderboard
     */
    public String leaderboard(){
        String leaderboard = "+LEADERBOARD:";
        userlist.toFirst();
        while (!userlist.isEmpty()) 
        {
            User usr = userlist.getContent();
            leaderboard += usr.getUsername() + "," + usr.getScore() + ":";
            userlist.next();
        }
        userlist.toFirst();

        leaderboard = leaderboard.substring(0, leaderboard.length() - 1);

        return leaderboard;
    }

    public void sortLeaderboard(List<User> pList){
        List<User> left = new List<User>();
        List<User> right = new List<User>();

        pList.toFirst();
        int i = 0;
        while (pList.hasAccess()){
            i++;
            pList.next();
        }
        pList.toFirst();
        User pivot = pList.getContent();
        pList.remove();

        while (pList.hasAccess()){
            if (pivot.getScore() <= pList.getContent().getScore()){
                left.append(pList.getContent());
            }
            else{
                right.append(pList.getContent());
            }
            pList.next();
        }
        left.append(pivot);

        if (i <= 1){
            left.concat(right);
            leaderboard = left;
        }
        else{
            sortLeaderboard(left);
            sortLeaderboard(right);
        }
    }

    /**
     * Method enemies gives us the enemies
     */
    public String enemies(){
        String enemies = "+GETENEMIES:";
        playerLobby.toFirst();
        while (!playerLobby.isEmpty()) 
        {
            User usr = playerLobby.getContent();
            enemies += usr.getUsername() + ",";
            userlist.next();
        }
        playerLobby.toFirst();

        enemies = enemies.substring(0, enemies.length() - 1);

        return enemies;
    }

    /**
     * Method findGame finds the game that contains the two players
     * @param p1 is one of the two player
     */
    public Game findGame(User p1){
        games.toFirst();
        while (games.hasAccess()){
            Game tmp = games.getContent();
            if (games.getContent().getPlayer1() == p1 || games.getContent().getPlayer2() == p1){
                return games.getContent();
            }
            else{games.next();}
        }
        return null;
    }

    /**
     * Method removeGame removes a game from the games list that ended
     * @param p1 is on of the two player
     */
    public void removeGame(User p1){
        games.toFirst();
        while (games.hasAccess()){
            Game tmp = games.getContent();
            if (games.getContent().getPlayer1() == p1 || games.getContent().getPlayer2() == p1){
                games.remove();
                return;
            }
            else{games.next();}
        }
    }

    /**
     * Method removePlayerSet removes a PlayerSet
     *
     * @param p1 is on of the two players that is in the PlayerSet
     */
    public void removePlayerSet(User p1){
        rematch.toFirst();
        while(rematch.hasAccess()){
            PlayerSet tmp = rematch.getContent();
            if (tmp.getPlayer1().getUsername() == p1.getUsername() || tmp.getPlayer2().getUsername() == p1.getUsername()){
                playerLobby.append(tmp.getPlayer1());
                playerLobby.append(tmp.getPlayer2());
                User player1 = tmp.getPlayer1();
                User player2 = tmp.getPlayer2();
                send(player1.getIP(), player1.getPort(), "STATUS:LOBBY");
                send(player2.getIP(), player2.getPort(), "STATUS:LOBBY");
                rematch.remove();
                return;
            }
            else{rematch.next();}
        }
    }

    /**
     * Method startGame starts a new game and adds the two Users to the games list
     * To avoid getting invalid requests we remove both users from the playerLobby list
     * Requires a lobby and two Users to start a new game
     * @param count counts the users we removed from the playerLobby
     */
    public void startGame(User p1, User p2) {
        Game newGame = new Game(p1, p2, this);

        //Adding both users to the games list
        games.append(newGame);

        //Removing both users from the playerLobby list
        int count = 0;
        playerLobby.toFirst();
        while (playerLobby.hasAccess()){
            if (playerLobby.getContent() == p1){
                playerLobby.remove();
                count++;
                if (count == 2){
                    break;
                }
                else{playerLobby.next();}
            }

            else if (playerLobby.getContent() == p2){
                playerLobby.remove();
                count++;
                if (count == 2){
                    break;
                }
                else{playerLobby.next();}
            }

            else{playerLobby.next();}
        }
        playerLobby.toFirst();
    }

    /**
     * Method endGame ends a game
     * Requires two Users
     */
    public void endGame(User p1, User p2, boolean pWon){
        //Adding both players to the rematch list
        rematch.append(new PlayerSet(p1,p2));

        //Removing both users from the games list
        removeGame(p1);
    }

    public void Rematch(User p1){
        rematch.toFirst();
        String pIP;
        int pPort;
        while (rematch.hasAccess()){
            PlayerSet tmp = rematch.getContent();
            if (tmp.getPlayer1().getUsername() == p1.getUsername()){
                if(tmp.getRematch()){
                    startGame(tmp.getPlayer1(), tmp.getPlayer2());
                    return;
                }

                pIP = tmp.getPlayer2().getIP();
                pPort = tmp.getPlayer2().getPort();
                send(pIP, pPort, "GETREMATCH");
                tmp.setRematch(true);
                return;

            }
            else if(tmp.getPlayer2().getUsername() == p1.getUsername()){
                if(tmp.getRematch()){
                    startGame(tmp.getPlayer1(), tmp.getPlayer2());
                    return;
                }

                pIP = tmp.getPlayer1().getIP();
                pPort = tmp.getPlayer1().getPort();
                send(pIP, pPort, "GETREMATCH");
                tmp.setRematch(true);
                return;
            }
            else{rematch.next();}
        }
    }

    public void startRematch(User p1){
        rematch.toFirst();
        while(rematch.hasAccess()){
            PlayerSet tmp = rematch.getContent();
            if (tmp.getPlayer1().getUsername() == p1.getUsername() || tmp.getPlayer2().getUsername() == p1.getUsername()){
                startGame(tmp.getPlayer1(), tmp.getPlayer2());
                return;
            }
            else{rematch.next();}
        }
    }

    /**
     * Method processMessage
     *
     * @param pIP is the users ip that sent the message
     * @param pPort is the users port that sent the message
     * @param pMessage is the message (duh)
     * @param tmp is sending the message
     */
    public void processMessage(String pIP, int pPort, String pMessage){
        String[] Message = pMessage.split(":");
        User tmp = getPlayer(pIP);

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
                User enemy = getPlayerByName(Message[1]);
                request(tmp, enemy);
                break;

            case "+GETREQUEST":
                if("true".equals(Message[1])){
                    User player2 = getPlayerByName(Message[2]); //Saving the second player
                    startGame(tmp, player2); //starting the game
                    send(pIP, pPort, "STATUS:GAME"); //updating the STATUS from player 1
                    send(player2.getIP(), player2.getPort(), "STATUS:GAME"); //updating the STATUS from player 2
                    break;
                }
                else{
                    send(pIP, pPort, "-REQUESTENEMY:enemy rejected");
                    break;
                }
            case "LEADERBOARD":
                sortLeaderboard(userlist);
                send(pIP, pPort, leaderboard());
                break;

            case "GETENEMIES":
                send(pIP, pPort, enemies());
                break;

            case "REMATCH":
                Rematch(tmp);
                break;

            case "+GETREMATCH":
                if("true".equals(Message[1])){
                    startRematch(tmp);
                }
                else{
                    send(pIP, pPort, "-GETREMATCH:false");
                    removePlayerSet(tmp);
                }
                break;
        }
    }

    /**
     * Method processNewConnection updates the STATUS from the User that connected to the server
     *
     * @param pIP is the users ip that connected to the server
     * @param pPort is the users port that connected to the server
     */
    public void processNewConnection(String pClientIP, int pClientPort){send(pClientIP, pClientPort, "STATUS:LOGIN");}

    public void processClosingConnection(String pClientIP, int pClientPort){}
}