/**
 * Ein Feld mit einem Verweis auf ein Schiff
 * 
 * @author Ole und Max
 * @version 0.1
 */
public class ShipField extends Field
{
    private Ship ship;
    /**
     * Construktor for objectes of the class ShipField
     */
    public ShipField(Ship ship)
    {
        this.ship = ship;
    }
    public Ship getShip() {
        return ship;
    }
}
