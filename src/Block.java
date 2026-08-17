import java.awt.*;

public class Block 
{
    public int x, y, width, height;
    public Image image;
    public boolean isPortal;  // Flag to identify if this is a portal block
    
    /**
     * Constructor for Blocks (things on the UI) 
     * holds all important information
     * @param x
     * @param y
     * @param width
     * @param height
     * @param image
     */
    public Block(int x, int y, int width, int height, Image image) 
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }
}