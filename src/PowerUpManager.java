import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PowerUpManager 
{
    public boolean isInvincible = false;
    public boolean isDoubleJump = false;
    private Timer invincibilityTimer;
    private Timer doubleJumpTimer;

    public boolean flashActive = false;
    public long flashStartTime = 0;
    public String flashText = "";
    public Color flashColor = Color.WHITE;
    public static final int FLASH_DURATION_MS = 3000;

    private boolean doubleJumpActive = false;

    public boolean hasDoubleJump() {
        return doubleJumpActive;
    }

    /**
     * Applies the selected power-up ability and starts its timer.
     * @param ability The ability to apply (INVINCIBILITY or DOUBLE_JUMP).
     */
    public void applyPowerUp(AbilityWheel.Ability ability)
     {
        if (ability == AbilityWheel.Ability.INVINCIBILITY) 
        {
            isInvincible = true;
            if (invincibilityTimer != null) invincibilityTimer.cancel();
            invincibilityTimer = new Timer();
            invincibilityTimer.schedule(new TimerTask() 
            {
                @Override
                public void run() 
                {
                    isInvincible = false;
                }
            }, 5000);
        } 
        else if (ability == AbilityWheel.Ability.DOUBLE_JUMP) 
        {
            isDoubleJump = true;
            doubleJumpActive = true;
            if (doubleJumpTimer != null) doubleJumpTimer.cancel();
            doubleJumpTimer = new Timer();
            doubleJumpTimer.schedule(new TimerTask() 
            {
                @Override
                public void run() 
                {
                    isDoubleJump = false;
                    doubleJumpActive = false;
                }
            }, 5000);
        }
    }

    /**
     * Triggers a flash overlay with the given text and color.
     * @param text The text to display during the flash.
     * @param color The background color of the flash.
     */
    public void triggerFlash(String text, Color color) 
    {
        flashActive = true;
        flashStartTime = System.currentTimeMillis();
        flashText = text;
        flashColor = color;
    }

    /**
     * Draws the flash overlay if active.
     * @param g The Graphics context.
     * @param pixelFont The font to use for the flash text.
     * @param width The width of the area to fill.
     * @param height The height of the area to fill.
     */
    public void drawFlash(Graphics g, Font pixelFont, int width, int height) 
    {
        if (flashActive) 
        {
            long elapsed = System.currentTimeMillis() - flashStartTime;
            if (elapsed < FLASH_DURATION_MS) 
            {
                g.setColor(flashColor);
                g.fillRect(0, 0, width, height);
                g.setFont(pixelFont.deriveFont(Font.BOLD, height / 6f));
                g.setColor(Color.BLACK);
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(flashText);
                int textHeight = fm.getAscent();
                g.drawString(flashText, (width - textWidth) / 2, (height + textHeight) / 2);
            } 
            else 
            {
                flashActive = false;
            }
        }
    }

    /**
     * Resets the power-up manager to its default state.
     */
    public void reset() {
        isInvincible = false;
        isDoubleJump = false;
        doubleJumpActive = false;
        if (invincibilityTimer != null) {
            invincibilityTimer.cancel();
            invincibilityTimer = null;
        }
        if (doubleJumpTimer != null) {
            doubleJumpTimer.cancel();
            doubleJumpTimer = null;
        }
    }
}



