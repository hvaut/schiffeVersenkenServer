/**
 * A ship with its start- and endposition
 * 
 * @author Ole und Max
 * @version 0.1
 */
public class Ship
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private int length;
    private int originalLength;
    /**
     * Construktor for objectes of the class Ship
     */
    public Ship(int x1,int x2,int y1,int y2)
    {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.length = shipLength();
        this.originalLength = length;
    }

    public int length() {
        return length;
    }
    
    public int getOriginalLength() {
        return originalLength;
    }

    public int x1() {
        return x1;
    }

    public int x2() {
        return x2;
    }

    public int y1() {
        return y1;
    }

    public int y2() {
        return y2;
    }

    private int shipLength()
    {
        int length = Math.abs(x1-x2)+Math.abs(y1-y2);
        return length;
    }
}
