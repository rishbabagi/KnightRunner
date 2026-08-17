import java.awt.*;
import javax.swing.JPanel;

public class Knight 
{
    public int x, y, width, height, velocityY;
    public boolean isJumping;
    private Image runImage;
    private Image jumpImage;
    private Image deadImage;

    public enum State { RUNNING, JUMPING, DEAD }
    private State state = State.RUNNING;

    /**
     * Constructs a Knight with position, size, and animation images.
     * @param x The x position of the knight.
     * @param y The y position of the knight.
     * @param width The width of the knight.
     * @param height The height of the knight.
     * @param runImage The running animation image (GIF).
     * @param jumpImage The jumping animation image (GIF).
     * @param deadImage The dead animation image (GIF).
     */
    public Knight(int x, int y, int width, int height, Image runImage, Image jumpImage, Image deadImage) 
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.runImage = runImage;
        this.jumpImage = jumpImage;
        this.deadImage = deadImage;
        this.isJumping = false;
        this.state = State.RUNNING;
    }

    /**
     * Sets the current state of the knight (RUNNING, JUMPING, DEAD).
     * @param state The new state.
     */
    public void setState(State state) 
    {
        this.state = state;
    }

    /**
     * Updates the animation frame. (No-op for GIF animation.)
     */
    public void updateFrame() 
    {
        // No manual frame update needed for GIF animation
    }

    /**
     * Draws the knight in its current state.
     * @param g The Graphics context.
     * @param panel The JPanel to draw on.
     */
    public void draw(Graphics g, JPanel panel) 
    {
        if (state == State.JUMPING && jumpImage != null) 
        {
            g.drawImage(jumpImage, x, y, width, height, panel);
        } 
        else if (state == State.DEAD && deadImage != null) 
        {
            g.drawImage(deadImage, x, y, width, height, panel);
        } 
        else if (runImage != null) 
        {
            g.drawImage(runImage, x, y, width, height, panel);
        }
    }

    /**
     * Makes the knight jump if not already jumping.
     * @param jumpVelocity The velocity to apply for the jump.
     */
    public void jump(int jumpVelocity) 
    {
        if (!isJumping) {
            velocityY = jumpVelocity;
            isJumping = true;
        }
    }

    /**
     * Returns whether the knight is currently jumping.
     * @return true if jumping, false otherwise.
     */
    public boolean isJumping() 
    {
        return isJumping;
    }

    /**
     * Returns the bounding rectangle for the knight's hitbox.
     * @return The hitbox as a Rectangle.
     */
    public Rectangle getBounds() 
    {
        double hitboxWidthRatio = 0.3;
        double hitboxHeightRatio = 0.8;
        int hitboxWidth = (int)(width * hitboxWidthRatio);
        int hitboxHeight = (int)(height * hitboxHeightRatio);
        int offsetX = (width - hitboxWidth) / 2;
        int offsetY = (height - hitboxHeight);
        return new Rectangle(x + offsetX, y + offsetY, hitboxWidth, hitboxHeight);
    }

    /**
     * Moves the knight according to gravity and ground position.
     * @param gravity The gravity to apply.
     * @param groundY The y position of the ground.
     */
    public void move(int gravity, int groundY) 
    {
        velocityY += gravity;
        y += velocityY;
        if (y >= groundY) 
        {
            y = groundY;
            velocityY = 0;
            isJumping = false;
        }
    }

    /**
     * Checks collision with obstacles, excluding portals.
     * @param obstacleManager The manager handling obstacles.
     */
    public void checkCollision(ObstacleManager obstacleManager) {
        for (Obstacle obs : obstacleManager.getObstacles()) {
            if (obstacleManager.isPortal(obs)) continue; // Skip portal for ALL collision checks
            // ... collision logic ...
        }
    }
}



