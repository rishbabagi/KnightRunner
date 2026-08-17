/**
* Represents a portal obstacle in the game.
* Extends Obstacle and is used to trigger special portal effects.
*/
public class Portal extends Obstacle
{
   /**
    * Constructs a Portal object.
    * @param x the x position of the portal
    * @param y the y position of the portal
    * @param width the width of the portal
    * @param height the height of the portal
    * @param image the image to display for the portal
    * @param speed the speed of the portal
    */
   public Portal(int x, int y, int width, int height, java.awt.Image image, int speed)
   {
       super(x, y, width, height, image, speed);
   }

   /**
    * Indicates that this obstacle is a portal.
    * @return true, since this is a portal
    */
   @Override
   public boolean isPortal()
   {
       return true;
   }

   /**
    * Returns a smaller bounding rectangle for the portal for collision detection.
    * @return a smaller Rectangle representing the portal's hitbox
    */
   @Override
   public java.awt.Rectangle getBounds()
   {
       int shrinkX = (int)(width * 0.2);   // 20% margin on each side
       int shrinkY = (int)(height * 0.2);  // 20% margin on top/bottom
       int newWidth = width - 2 * shrinkX;
       int newHeight = height - 2 * shrinkY;
       return new java.awt.Rectangle(x + shrinkX, y + shrinkY, newWidth, newHeight);
   }
}

