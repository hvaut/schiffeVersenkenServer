
/**
 * Beschreiben Sie hier die Klasse Game.
 * 
 * @author Leon Stobbe/Georg Seiler 
 * @version 01.03.2023
 */
public class Game
{

    private Board board1;
    private Board board2;
    private User player1;
    private User player2;
    private Lobby server;
    private int state = 0;//0 = place; 1 = game; 2 = end
    public Game(User _player1, User _player2, Lobby _server)
    {
        player1 = _player1;
        player2 = player2;
        board1 = new Board(player1, this);
        board2 = new Board(player2, this);
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
        if(player.getName().equals(player1.getName()))
        {
            if(!board1.checkShot(x, y))
            {
                sendNextMove(player1);
                return;
            }
            server.send(player.getIp(), player.getPort(), "FIELDUPDATE:" + x + ":" + y + board1.processShot(x, y));
            sendNextMove(player2);
        }
        else
        {
            if(!board2.checkShot(x, y))
            {
                sendNextMove(player2);
                return;
            }
            server.send(player.getIp(), player.getPort(), "FIELDUPDATE:" + x + ":" + y + board2.processShot(x, y));
            sendNextMove(player1);
        }
    }

    /**
     * Methode sendNextMove
     * gives a notification to the player
     */
    public void sendNextMove(User player)
    {
        server.send(player.getIp(), player.getPort(), "ACTIVEUSER:" + player.getName());
    }

    /**
     * Methode checkEnd
     * checks if the game has ended
     * @return true: game has ended; false: game has not ended yet
     */
    public boolean checkEnd()
    {
        //if(board1.checkShips()) return false;
        //if(board2.checkShips()) return false;
        //Field[][] board = board1.getBoard();
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(!board[i][j].isHit()) return false;
            }
        }
        return true;
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
