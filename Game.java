/**
 * Beschreiben Sie hier die Klasse Game.
 * 
 * @author Leon Stobbe/Georg Seiler 
 * @version 08.03.2023
 */
public class Game
{

    private Board board1;
    private Board board2;
    private User player1;
    private User player2;
    private Lobby server;
    private int state = 0;//0 = place; 1 = game; 2 = end
    private User currentPlayer;//saves the current Player (used in the shoot method)
    private User otherPlayer;
    private Board currentBoard;
    public Game(User _player1, User _player2, Lobby _server)
    {
        player1 = _player1;
        player2 = _player2;
        currentPlayer = player1;
        otherPlayer = player2;
        board1 = new Board(player1, this);
        board2 = new Board(player2, this);
        currentBoard = board2;
        server = _server;
    }

    /**
     * Methode place
     * places a ship between the given positions if possible
     * @param x1 first x coordinate
     * @param y1 first y coordinate
     * @param x2 second x coordinate
     * @param y2 second y coordinate
     */
    public void place(int x1, int y1, int x2, int y2, User player)
    {
        User tempPlayer;
        Board tempBoard;
        if(player.getUsername().equals(player1.getUsername()))
        {
            tempPlayer = player1;
            tempBoard = board1;
        } else {
            tempPlayer = player2;
            tempBoard = board2;
        }
        PlacementEvent result = tempBoard.placeShip(x1,y1,x2,y2);//saves the success of the placement
        switch(result)
        {
            case SHIP:
                int[] ships = tempBoard.getShips();
                String shipString = "";
                for(int i = 0; i < ships.length; i++)
                {
                    shipString += ":" + ships[i];
                }
                for(int i=x1;i<=x2;i++) {
                    for(int j=y1;j<=y2;j++) {
                        server.send(tempPlayer.getIP(), tempPlayer.getPort(), "FIELDUPDATE:" + i + ":" + j + ":1:" + result);
                        //TODO get the right Field ID
                    }    
                }
                server.send(tempPlayer.getIP(), tempPlayer.getPort(), "+PLACE" + shipString);
                for(int i=x1;i<=x2;i++) {
                    for(int j=y1;j<=y2;j++) {
                        server.send(tempPlayer.getIP(), tempPlayer.getPort(), "FIELDUPDATE:" + i + ":" + j + ":1:" + result);
                        //TODO get the right Field ID
                    }    
                }
                return;
            case NOSHIP:
                server.send(tempPlayer.getIP(), tempPlayer.getPort(), "-PLACE: ship does not exist");
                return;
            case ALREADY_PLACED:
                server.send(tempPlayer.getIP(), tempPlayer.getPort(), "-PLACE: ship is already placed");
                return;
            case OUT_OF_BOUNDS:
                server.send(tempPlayer.getIP(), tempPlayer.getPort(), "-PLACE: ship is out of bounds");
                return;
            case INVALID:
                server.send(tempPlayer.getIP(), tempPlayer.getPort(), "-PLACE: placement is invalid");
                return;
            default:
                return;
        }
    }

    /**
     * Methode shoot
     * shoots the selected position
     * @param x x coordinate
     * @param y y coordinate
     * @param player User that send the shot
     */
    public void shoot(int x, int y, User player)
    {
        if(player.getUsername().equals(currentPlayer.getUsername()))
        {
            ShotEvent result = currentBoard.processShot(x, y); //result of the shot given as an integer: 0 = miss, 1 = hit, 2 = ship down
            switch (result){
                case FAILED:
                    server.send(player.getIP(), player.getPort(), "-SHOOT:position invalid");
                    break;
                case MISS:
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "+SHOOT: miss");
                    break;
                case HIT:
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "+SHOOT: hit");
                    break;
                case SUNK:
                    int[] temp = currentBoard.getSunkenShip(x, y);
                    int x1 = temp[0];
                    int x2 = temp[1];
                    int y1 = temp[2];
                    int y2 = temp[3];
                    sinkShip(x1, x2, y1, y2);
                    //mark ship
                    for(int i = x1 + 1; i <= x2 - 1; i++) {
                        for(int j = y1 + 1; j <= y2 - 1; j++) {
                            //Update the current Player
                            server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + i + ":" + j + ":2:" + result);//2 = board of the enemy
                            //Update the other Player
                            server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + i + ":" + j + ":1:" + result);// 1
                        }    
                    }
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "+SHOOT: ship down");
                    break;
                default:
                    break;
            }

            if(!ShotEvent.FAILED.equals(result))//following code is only executed if the shot was legal
            {
                //Update the current Player
                server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + x + ":" + y + ":2:" + result);//2 = board of the enemy
                //Update the other Player
                server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + x + ":" + y + ":1:" + result);// 1 = own board

                //changes the active player
                if(player1==currentPlayer){
                    currentPlayer = player2;
                    otherPlayer = player1;
                    currentBoard = board1;
                } else {
                    currentPlayer = player1;
                    otherPlayer = player2;
                    currentBoard = board2;
                }
            }

            if(checkEnd())//checks if the game has ended
            {
                //Player 1 lost
                if(board1.checkEnd())
                {
                    server.send(player1.getIP(), player1.getPort(), "RESULT: you loose");
                    server.send(player2.getIP(), player2.getPort(), "RESULT: you win");
                }
                else if (board2.checkEnd()) 
                {
                    server.send(player2.getIP(), player2.getPort(), "RESULT: you loose");
                    server.send(player1.getIP(), player1.getPort(), "RESULT: you win");
                }

                endGame();
                return;
            }

            sendNextMove(currentPlayer);//sends the other player a notification
        }
        else
        {
            server.send(player.getIP(), player.getPort(), "-SHOOT:Not your turn");
        }
    }

    /**
     * Methode sinkShip
     * marks the fields around the sunken ship
     * @param x1 
     * @param x2 
     * @param y1 
     * @param y2 
     */
    public void sinkShip(int x1, int x2, int y1, int y2)
    {
        //mark fields around the ship
        if((y1 > -1) && (y1 < 11))
        {
            for(int i=x1;i<=x2;i++) {
                //Update the current Player
                server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + i + ":" + y1 + ":2:" + ShotEvent.MISS);//2 = board of the enemy
                //Update the other Player
                server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + i + ":" + y1 + ":1:" + ShotEvent.MISS);// 1 = own board   
            }
        }
        if((y2 > -1) && (y2 < 11))
        {
            for(int i=x1;i<=x2;i++) {
                //Update the current Player
                server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + i + ":" + y2 + ":2:" + ShotEvent.MISS);//2 = board of the enemy
                //Update the other Player
                server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + i + ":" + y2 + ":1:" + ShotEvent.MISS);// 1 = own board   
            }
        }
        if((x1 > -1) && (x1 < 11))
        {
            for(int i=y1;i<=y2;i++) {
                //Update the current Player
                server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + x1 + ":" + i + ":2:" + ShotEvent.MISS);//2 = board of the enemy
                //Update the other Player
                server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + x1 + ":" + i + ":1:" + ShotEvent.MISS);// 1 = own board   
            }
        }
        if((x2 > -1) && (x2 < 11))
        {
            for(int i=x1;i<=x2;i++) {
                //Update the current Player
                server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + x2 + ":" + i + ":2:" + ShotEvent.MISS);//2 = board of the enemy
                //Update the other Player
                server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + x2 + ":" + i + ":1:" + ShotEvent.MISS);// 1 = own board   
            }
        }
    }

    /**
     * Methode sendNextMove
     * gives a notification to the player that it's his turn
     * gives a notification to the player
     * @param player player that has to make a move now
     */
    public void sendNextMove(User player)
    {
        server.send(player.getIP(), player.getPort(), "ACTIVEUSER:" + player.getUsername());
    }

    /**
     * Methode checkEnd
     * checks if the game has ended
     * @return true: game has ended; false: game has not ended yet
     */
    public boolean checkEnd()
    {
        if(board1.checkEnd()) return true;
        if(board2.checkEnd()) return true;
        return false;
    }

    /**
     * Methode endGame
     * notifys the Server to end the game
     */
    public void endGame()
    {
        server.endGame(player1, player2, board1.checkEnd());
    }

    /**
     * Methode getState
     * returns the current state of the game
     * @return 0 = place; 1 = game; 2 = end
     */
    public int getState()
    {
        return state;
    }

    /**
     * Method getPlayer1
     * returns the first player, required for the Lobby class
     * @return User
     */
    public User getPlayer1() 
    {
        return player1;
    }

    /**
     * Method getPlayer2
     * returns the second player, required for the Lobby class
     * @return User
     */
    public User getPlayer2() 
    {
        return player2;
    }
}