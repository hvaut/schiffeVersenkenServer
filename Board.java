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
     * Checks if the placement of a ship is vaild. 
     * 
     * @param  pShip   The ship which should be checked
     * @return         Returns true in case of a valid placement 
     *                 and the amount of available ships for the length of pShip is greater than 0
     *                 otherwise returns false.
     */
    private PlacementEvent checkPlacement(Ship pShip)
    {
        //Return false in case it's outside the board
        if (pShip.x1() >= boardSize || pShip.x2() >= boardSize || pShip.y1() >= boardSize || pShip.y2() >= boardSize
        || pShip.x1() < 0 || pShip.x2() < 0 || pShip.y1() < 0 || pShip.y2() < 0)
            return PlacementEvent.OUT_OF_BOUNDS;

        //Invalidate in case the ship is not horizontally/vertically placed
        if (pShip.x1() == pShip.x2() || pShip.y1() == pShip.y2()) 
        {
            //Check if ship length is valid and a ship is available
            if (pShip.length() == 2 && ships[0] > 0
            || pShip.length() == 3 && ships[1] > 0
            || pShip.length() == 4 && ships[2] > 0
            || pShip.length() == 5 && ships[3] > 0)
                return PlacementEvent.VALID;
        } 
        return PlacementEvent.INVALID;
    }
    
    public int[] getShips() 
    {
        return ships;
    }

    /**
     * TODO
     * 
     * @return       
     */
    public PlacementEvent placeShip(int x1,int y1,int x2,int y2)
    {
        Ship pShip = new Ship(x1,x2,y1,y2);
        var event = checkPlacement(pShip);
        if(PlacementEvent.VALID.equals(event)){
            for(int i=x1;i<=x2;i++) {
                for(int j=y1;j<=y2;j++) {
                    field[i][j] = new ShipField(pShip);
                }    
            }
        }
        return event;
    }

    /**
     * Checks if the shot is vaild. 
     * 
     * @param  x   The x Coordinate of the attempted shot
     * @param  y   The y Coordinate of the attempted shot
     * @return        Valid
     */
    public boolean checkShot(int x, int y){
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
     * @return  int 0: Failed to shoot, 1: Water, 2: Ship hit, 3: ship sunk
     */
    public ShotEvent processShot(int x, int y)
    {
        if(checkShot(x,y)){
            return ShotEvent.FAILED;
        }
        field[x][y].hit();
        if(field[x][y]instanceof Water)  {
            return ShotEvent.MISS;
        } else if(field[x][y]instanceof ShipField)
        {
            Ship ship = ((ShipField)field[x][y]).getShip();
            if(ship.length() <=0)
            {
                if (ships[ship.getOriginalLength()] > 0)
                    ships[ship.getOriginalLength()-2]--;
                
                return ShotEvent.SUNK;           
            }
            return ShotEvent.HIT;
        }
        return ShotEvent.FAILED;
    }

    public boolean checkEnd() {
        if (ships[0] == 0 && ships[1] == 0 && ships[2] == 0 && ships[3] == 0)
            return true;
        return false;
    }
}