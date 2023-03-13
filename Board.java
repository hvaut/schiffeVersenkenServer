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
    private final int boardSize = 10;
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
        for(int i = 0;i<boardSize;i++) {
            for(int j = 0;i<boardSize;j++) {
                field[i][j] = new Water();
            }
        }
    }

    /**
     * TODO
     * 
     * @return       
     */
    private boolean checkPlacement(int x1,int y1,int x2,int y2)
    {
        //TODO
        return true;
    }

    /**
     * TODO
     * 
     * @return       
     */
    public void placeShip(int x1,int y1,int x2,int y2)
    {
        if(checkPlacement(x1,x2,y1,y2)){
            Ship pShip = new Ship(x1,x2,y1,y2,shipLength(x1,x2,y1,y2));
            for(int i=x1;i<=x2;i++) {
                for(int j=y1;j<=y2;j++) {
                    field[i][j] = new ShipField(pShip);
                }    
            }
        }
    }

    /**
     * Checks if the shot is vaild. 
     * 
     * @param  x   The x Coordinate of the attempted shot
     * @param  y   The y Coordinate of the attempted shot
     * @return        Valid
     */
    public boolean checkShot(int x, int y)
    {
        if(x>=boardSize || y>=boardSize) {
            return false;
        }
        if(field[x][y].isHit()) {
            return false;    
        }
        return true;
    }

    /**
     * Ein Beispiel einer Methode - ersetzen Sie diesen Kommentar mit Ihrem eigenen
     * 
     * @param  x   The x Coordinate of the attempted shot
     * @param  y   The y Coordinate of the attempted shot
     * @return  int 0: Failed to shoot, 1: Water, 2: Ship hit
     * 
     */
    public int processShot(int x, int y)
    {
        if(checkShot(x,y)){
            return 0;
        }
        field[x][y].hit();
        if(Water == field[x][y].getClass())  {
            return 1;
        } else if(ShipField ==field[x][y].getClass())  {
            ShipField shipField = field[x][y];
            if(shipField.getShip().)
            return 2;
        }
        return 0;
    }
    public boolean checkEnd() {
        return false;
    }
}