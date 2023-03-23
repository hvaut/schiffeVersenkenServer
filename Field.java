/**
 * Ein abstraktes Feld
 * 
 * @author Ole und Max
 * @version 1
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
     * Getter for attribute hit
     * 
     * @return the attribute hit   
     */
    public boolean isHit()
    {
        return hit;
    }
    
    /**
     * Set hit to true
     */
    public void hit()
    {
        hit = true;
    }
}
