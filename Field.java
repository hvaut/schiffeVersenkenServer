/**
 * Ein abstraktes Feld
 * 
 * @author Ole und Max
 * @version 0.1
 */
public abstract class Field
{
    private boolean hit;
    
    /**
     * Construktor for objectes of the class Field
     */
    public Field()
    {
        hit = false;
    }

    /**
     * Getter für Attribut hit
     * 
     * @return Gibt das Attribut hit zurück.       
     */
    public boolean isHit()
    {
        return hit;
    }
    
    /**
     * Setzt das Attribut hit auf true
     */
    public void hit()
    {
        hit = true;
    }
}
