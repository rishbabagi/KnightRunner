import java.awt.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JPanel;


/**
* Manages all obstacles and portals in the game.
* Handles spawning, updating, drawing, and identifying obstacles and portals.
*/
public class ObstacleManager 
{
   private Queue<Obstacle> obstacles = new LinkedList<>();
   private Random rand = new Random();
   private int obstacleCounter = 0;
   private int nextPortalAt = 15;


   /**
    * Spawns a random obstacle (cactus, stone, or barrel) at the right edge of the panel.
    * @param panelWidth the width of the game panel
    * @param panelHeight the height of the game panel
    * @param cactus the cactus image
    * @param stone the stone image
    * @param barrel the barrel image
    */
   public void spawnObstacle(int panelWidth, int panelHeight, Image cactus, Image stone, Image barrel, int score) 
   {
       boolean canSpawn = true;
       int fastestSpeed = 0;


       for (Obstacle obs : obstacles) 
       {
           if (!obs.isOffScreen()) 
           {
               canSpawn = false;
               fastestSpeed = Math.max(fastestSpeed, obs.getSpeed());
           }
       }


       if (obstacles.isEmpty() || fastestSpeed > 6) 
       {
           canSpawn = true;
       }


       if (canSpawn) 
       {
           int choice = rand.nextInt(3);
           Image img;
           double scaleRatio = 0.2;
           int imgWidth;
           int imgHeight;
           int baseSpeed = 18;
           int speedVariation = rand.nextInt(6) + 6;
           int obstacleSpeed = baseSpeed + speedVariation;


           switch (choice) {
               case 0:
                   img = cactus;
                   imgHeight = (int) (panelHeight * scaleRatio);
                   imgWidth = (int) (imgHeight * 0.6);
                   break;
               case 1:
                   img = stone;
                   imgHeight = (int) (panelHeight * scaleRatio * 0.8);
                   imgWidth = (int) (imgHeight * 1.2);
                   break;
               case 2:
                   img = barrel;
                   imgHeight = (int) (panelHeight * scaleRatio);
                   imgWidth = (int) (imgHeight * 0.8);
                   break;
               default:
                   img = cactus;
                   imgHeight = (int) (panelHeight * scaleRatio);
                   imgWidth = (int) (imgHeight * 0.6);
                   break;
           }


           int x = panelWidth;
           int y = panelHeight - imgHeight - 100;
           Obstacle newObs = new Obstacle(x, y, imgWidth, imgHeight, img, obstacleSpeed);
           obstacles.offer(newObs); // Use offer for Queue
       }
   }


   /**
    * Spawns a portal at the right edge of the panel.
    * @param panelWidth the width of the game panel
    * @param panelHeight the height of the game panel
    * @param portalImage the portal image
    */
   public void spawnPortal(int panelWidth, int panelHeight, Image portalImage, int score) 
   {
       double scaleRatio = 0.3;
       int imgHeight = (int) (panelHeight * scaleRatio);
       int imgWidth = (int) (imgHeight * 0.8);


       boolean canSpawn = true;
       int portalOffset = 0;
       int x = panelWidth + portalOffset;
       int y = panelHeight - imgHeight - 40;


       for (Obstacle obs : obstacles) 
       {
           if (!obs.isOffScreen()) 
           {
               int minDistance = Math.max(imgWidth, obs.getWidth()) + 150;
               if (Math.abs(x - obs.getX()) < minDistance) 
               {
                   canSpawn = false;
                   break;
               }
           }
       }


       for (Obstacle obs : obstacles) 
       {
           if (!obs.isOffScreen() && obs.getX() > x - 500)
            {
               canSpawn = false;
               break;
           }
       }


       if (canSpawn) 
       {
           int baseSpeed = 12;
           int speedVariation = rand.nextInt(5) + 3;
           int scoreSpeedBoost = (score / 100) + 1;
           int portalSpeed = baseSpeed + speedVariation + scoreSpeedBoost;


           Portal portal = new Portal(x, y, imgWidth, imgHeight, portalImage, portalSpeed);
           obstacles.offer(portal); // Use offer for Queue
       }
       System.out.println("Portal added to obstacles list. Total obstacles: " + obstacles.size());
   }


   /**
    * Updates all obstacles, moves them, and removes any that are off-screen.
    * @param obstacleSpeed the speed at which obstacles move
    * @return the number of obstacles removed (passed)
    */
   public int update() 
   {
       int removed = 0;
       Iterator<Obstacle> it = obstacles.iterator();
       while (it.hasNext()) 
       {
           Obstacle obs = it.next();
           obs.move();
           if (obs.isOffScreen()) 
           {
               it.remove();
               removed++;
           }
       }
       return removed;
   }


   /**
    * Draws all obstacles and portals on the panel.
    * @param g the Graphics context
    * @param c the component to draw on
    */
   public void draw(Graphics g, JPanel panel) 
   {
       for (Obstacle obs : obstacles) 
       {
           obs.draw(g, panel);
       }
   }


   /**
    * Returns the queue of all obstacles (including portals).
    * @return the queue of obstacles
    */
   public Queue<Obstacle> getObstacles() 
   {
       return obstacles;
   }


   /**
    * Checks if the given obstacle is a portal.
    * @param obstacle the obstacle to check
    * @return true if the obstacle is a portal, false otherwise
    */
   public boolean isPortal(Obstacle obstacle) 
   {
       return obstacle != null && obstacle.isPortal();
   }


   /**
    * (Optional/Legacy) Updates obstacles and spawns a portal after a certain number have been passed.
    * Not used if portal spawning is handled in KnightRunner.
    * @param obstacleSpeed the speed at which obstacles move
    * @param panelWidth the width of the game panel
    * @param panelHeight the height of the game panel
    * @param portalImage the portal image
    */
   public void updateObstaclesCrossed(int obstacleSpeed, int panelWidth, int panelHeight, Image portalImage) 
   {
       obstacleCounter += update();


       if (obstacleCounter >= nextPortalAt) 
       {
           spawnPortal(panelWidth, panelHeight, portalImage, obstacleCounter);
           nextPortalAt += rand.nextInt(5) + 10; // Set the next milestone
       }
   }
}



