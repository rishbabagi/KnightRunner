import java.awt.*;
import javax.swing.*;


public class Obstacle extends Block
{
   private int speed;


   /**
    * Constructor for the Obstacles to Draw them on the UI
    * @param x
    * @param y
    * @param width
    * @param height
    * @param image
    * @param speed the speed at which this obstacle moves
    */
   public Obstacle(int x, int y, int width, int height, Image image, int speed)
   {
       super(x, y, width, height, image);
       this.speed = speed;
   }


   /**
    * Moves the obstacle based on its own speed
    */
   public void move()
   {
       x -= speed;
   }


   /**
    * Sets the speed of the obstacle
    * @param speed the new speed for the obstacle
    */
   public void setSpeed(int speed) 
   {
       this.speed = speed;
   }


   /**
    * Gets the current speed of the obstacle
    * @return the speed of the obstacle
    */
   public int getSpeed() 
   {
       return speed;
   }


   /**
    * Draws the Obstacles on the UI
    * @param g this is for the Graphics class to use the draw method
    * @param panel this is the UI panel we will draw on
    */
   public void draw(Graphics g, JPanel panel)
   {
       g.drawImage(image, x, y, width, height, panel);
   }


   /**
    *
    * @return true or false, whether the obstacle is on the screen or not
    */
   public boolean isOffScreen()
   {
       return x + width<0;
   }


   /**
    *
    * @return a Rectangle making a red box around the obstacle to see it's borders
    */
   public Rectangle getBounds()
   {
       return new Rectangle(x,y, width, height);
   }


   public boolean isPortal()
   {
       return false;
   }


   /**
    * Gets the width of the obstacle
    * @return the width of the obstacle
    */
   public int getWidth() 
   {
       return width;
   }


   /**
    * Gets the x position of the obstacle
    * @return the x position of the obstacle
    */
   public int getX() 
   {
       return x;
   }
}







