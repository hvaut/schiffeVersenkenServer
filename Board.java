/**
 * Beschreiben Sie hier die Klasse Board.
 * 
 * @author Max und Ole
 * @version 0.1
 */
public class Board
{   
    //Attributes
    private User player;
    private int[] ships;
    private Field[][] field;
    private Game game;
    private int boardSize = 10;
    /**
     * Construktor for objectes of the class Board
     */
    public Board(User player, Game game)
    {
        this.player = player;
        this.game = game;
        field = new Field[boardSize][boardSize];
        ships = new int[4];
        ships[0] = 2;   // Length 2 ships
        ships[1] = 3;   // Length 3 ships
        ships[2] = 2;   // Length 4 ships
        ships[3] = 1;   // Length 5 ships
    }

    /**
     * TODO
     * 
     * @return       
     */
    public boolean checkPlacement()
    {
        //TODO
        return true;
    }
    
    /**
     * TODO
     *   
     * @return        
     */
    public boolean checkShoot()
    {
        //TODO
        return true;
    }
}