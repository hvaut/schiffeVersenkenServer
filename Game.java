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
        currentBoard = board1;
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
            case VALID:
                int[] ships = tempBoard.getShips();
                String shipString = "";
                for(int i = 0; i < ships.length; i++)
                {
                    shipString += ":" + ships[i];
                }
                server.send(tempPlayer.getIP(), tempPlayer.getPort(), "+PLACE" + shipString);
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
        //User otherPlayer;
        //Board currentBoard;
        /*if(player.getUsername().equals(player1.getUsername()))
        {
        currentPlayer = player1;
        otherPlayer = player2;
        currentBoard = board1;
        } else {
        currentPlayer = player2;
        otherPlayer = player1;
        currentBoard = board2;
        }*/
        if(player.getUsername().equals(currentPlayer.getUsername()))
        {
            if(!currentBoard.checkShot(x, y))//checks if the shot is invalid
            {
                server.send(player.getIP(), player.getPort(), "-SHOOT:position invalid");
                //sendNextMove(player);
                return;
            }
            ShotEvent result = currentBoard.processShot(x, y); //result of the shot given as an integer: 0 = miss, 1 = hit, 2 = ship down
            switch (result){
                case MISS:
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "+SHOOT: miss");
                    break;
                case HIT:
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "+SHOOT: hit");
                    break;
                case SUNK:
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "+SHOOT: ship down");
                    break;
                case FAILED:
                    server.send(currentPlayer.getIP(), currentPlayer.getPort(), "-SHOOT");
                    break;
                default:
                    break;
            }
            //Update the current Player 
            //Kannst du den Zeichencodes am Ende erklären
            server.send(currentPlayer.getIP(), currentPlayer.getPort(), "FIELDUPDATE:" + x + ":" + y + ":2:" + result);
            //Update the other Player
            server.send(otherPlayer.getIP(), otherPlayer.getPort(), "FIELDUPDATE:" + x + ":" + y + ":1:" + result);

            //changes the active player
            if(player1==currentPlayer){
                currentPlayer = player2;
                otherPlayer = player1;
                currentBoard = board2;
            } else {
                currentPlayer = player1;
                otherPlayer = player2;
                currentBoard = board1;
            }

            if(
            checkEnd())
            {
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

    // } else
    // {
    // if(!player1Turn)//checks if it's the players turn
    // {
    // if(!board2.checkShot(x, y))//checks if the shot is invalid
    // {
    // server.send(player.getIP(), player.getPort(), "-SHOOT:position invalid");
    // //sendNextMove(player);/should be done with the above command
    // return;
    // }
    // int result = board2.processShot(x, y);//result of the shot given as an integer: 0 = miss, 1 = hit, 2 = ship down
    // switch (result){
    // case 0:
    // server.send(player2.getIP(), player1.getPort(), "+SHOOT: miss");
    // return;
    // case 1:
    // server.send(player2.getIP(), player1.getPort(), "+SHOOT: hit");
    // return;
    // case 2:
    // server.send(player2.getIP(), player1.getPort(), "+SHOOT: ship down");
    // return;
    // default:
    // }
    // server.send(player1.getIP(), player1.getPort(), "FIELDUPDATE:" + x + ":" + y + ":1:" + result);
    // server.send(player2.getIP(), player2.getPort(), "FIELDUPDATE:" + x + ":" + y + ":2:" + result);
    // player1Turn = true;//changes the active player
    // if(checkEnd())
    // {
    // endGame();
    // return;
    // }
    // sendNextMove(player1);//sends the other player a notification
    // }
    // else
    // {
    // server.send(player.getIP(), player.getPort(), "-SHOOT:Not your turn");
    // }
    // }

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
}
