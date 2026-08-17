import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.InputStream;

public class KnightRunner extends JPanel implements ActionListener, KeyListener 
{
    // Images
    Image pic, KnightDead, KnightRun, KnightJump, Cactus, Stone, Barrel, Portal;

    // Game objects
    Knight knight;
    ObstacleManager obstacleManager = new ObstacleManager();
    PowerUpManager powerUpManager = new PowerUpManager();
    PortalTimer portalTimer = new PortalTimer();

    // Game state
    ScoreCounter score = new ScoreCounter();
    Timer scoreTimer, gameloop, pauseTimer;
    boolean gameOver = false;
    boolean wheelActive = false;
    AbilityWheel abilityWheel;

    // Layout and scaling
    private final int baseScreenWidth = 800;
    private final int baseScreenHeight = 600;
    double knightScaleRatio = 1 / 4.0;
    int knightWidth, knightHeight;
    int knightX, knightY;
    int gravity = 1;
    int baseJumpVelocity = -12;
    int baseObstacleSpeed = 11;
    int knightSpeed = 8;
    int jumpCount = 0;

    // Obstacle/portal logic
    int spawntimer = 0, spawnInterval = 120, obstacleSpeed = 8;
    int obstaclesCrossed = 0, nextPortalAt = 15;
    boolean portalJustSpawned = false;
    int spawnDelay = 120;

    // Fonts/UI
    private Font pixelFont;
    private int portalTimerX = 0, portalTimerY = 0;

    private boolean knightVisible = true;

    public KnightRunner() 
    {
        setPreferredSize(new Dimension(baseScreenWidth, baseScreenHeight));
        loadBackgroundImage();
        setFocusable(true);
        addKeyListener(this);

        loadFonts();
        loadImages();

        knightX = 100;
        knightY = 300;
        knightWidth = (int) (baseScreenWidth * knightScaleRatio);
        knightHeight = (int) (baseScreenHeight * knightScaleRatio);
        KnightRun = new ImageIcon(getClass().getResource("/ProjectImages/__Run.gif")).getImage();
        KnightJump = new ImageIcon(getClass().getResource("/ProjectImages/__Jump.gif")).getImage();
        KnightDead = new ImageIcon(getClass().getResource("/ProjectImages/__Death.gif")).getImage();

        knight = new Knight(knightX, knightY, knightWidth, knightHeight, KnightRun, KnightJump, KnightDead);

        updateKnightSize();
        updateSpeeds();

        addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) {
                updateKnightSize();
                updateSpeeds();
                repaint();
            }
        });

        gameloop = new Timer(1000 / 60, this);
        gameloop.start();

        scoreTimer = new Timer(1000, e -> {
            score.increment();
            repaint();
        });
        scoreTimer.start();

        pauseTimer = new Timer(1000, e -> {
            gameloop.start();
            scoreTimer.start();
            repaint();
        });
        pauseTimer.setRepeats(false);

        addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
            public void mouseMoved(MouseEvent e) 
            {
                for (Obstacle obs : obstacleManager.getObstacles()) 
                {
                    if (obs instanceof Portal && obs.getBounds().contains(e.getX(), e.getY())) 
                    {
                        portalTimer.start();
                        portalTimerX = e.getX() - 50;
                        portalTimerY = e.getY() - 100;
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseExited(MouseEvent e) 
            {
                portalTimer.stop();
            }
        });
    }

    /**
     * Loads the custom pixel font from resources, or falls back to Arial if unavailable.
     */
    private void loadFonts() 
    {
        try 
        {
            InputStream is = getClass().getResourceAsStream("/ProjectImages/Pixel Emulator.otf");
            if (is != null) 
            {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
            } 
            else 
            {
                pixelFont = new Font("Arial", Font.BOLD, 24);
            }
        } 
        catch (Exception e) 
        {
            pixelFont = new Font("Arial", Font.BOLD, 24);
        }
    }

    /**
     * Loads all images (knight animations, obstacles, portal) from resources.
     */
    private void loadImages() {
        try 
        {
            KnightDead = new ImageIcon(getClass().getResource("/ProjectImages/__Death.gif")).getImage();
            KnightRun = new ImageIcon(getClass().getResource("/ProjectImages/__Run.gif")).getImage();
            KnightJump = new ImageIcon(getClass().getResource("/ProjectImages/__Jump.gif")).getImage();
            Cactus = new ImageIcon(getClass().getResource("/ProjectImages/Prop_5.png")).getImage();
            Barrel = new ImageIcon(getClass().getResource("/ProjectImages/Prop_7.png")).getImage();
            Stone = new ImageIcon(getClass().getResource("/ProjectImages/Prop_6.png")).getImage();
            Portal = new ImageIcon(getClass().getResource("/ProjectImages/portal.gif")).getImage();
        } 
        catch (Exception e) 
        {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    /**
     * Loads the background image from resources.
     */
    private void loadBackgroundImage() 
    {
        try 
        {
            pic = new ImageIcon(getClass().getResource("/ProjectImages/background.jpg")).getImage();
        } 
        catch (Exception e) 
        {
            pic = null;
        }
    }

    /**
     * Updates the knight's width and height based on the current panel size.
     */
    private void updateKnightSize() 
    {
        Dimension size = getSize();
        knightWidth = (int) (size.width * knightScaleRatio);
        knightHeight = (int) (size.height * knightScaleRatio);
        if (knight != null) 
        {
            knight.width = knightWidth;
            knight.height = knightHeight;
        }
    }

    /**
     * Updates the obstacle and knight speeds based on the current panel size.
     */
    private void updateSpeeds() 
    {
        Dimension size = getSize();
        double widthScale = (double) size.width / baseScreenWidth;
        double heightScale = (double) size.height / baseScreenHeight;
        obstacleSpeed = (int)(baseObstacleSpeed * widthScale);
        if (obstacleSpeed < 5) obstacleSpeed = 5;
        baseJumpVelocity = (int)(-15 * heightScale);
        if (baseJumpVelocity > -8) baseJumpVelocity = -8;
    }

    /**
     * Removes the ability wheel from the panel and resets related state.
     */
    private void cleanupAbilityWheel() 
    {
        if (abilityWheel != null) 
        {
            remove(abilityWheel);
            abilityWheel = null;
        }
        wheelActive = false;
        repaint();
    }

    /**
     * Resets the game state to start a new game.
     */
    private void restartGame() 
    {
        score.reset();
        gameOver = false;
        knight.y = 300;
        knight.setState(Knight.State.RUNNING);
        obstacleManager.getObstacles().clear();
        spawntimer = 0;
        nextPortalAt = 15;           // Reset portal spawn threshold
        portalJustSpawned = false;   // Reset portal spawn state
        spawnDelay = 120;            // Reset spawn delay if you use it
        updateSpeeds();
        gameloop.restart();
        scoreTimer.restart();
        repaint();
    }

    /**
     * Paints the game components, including overlays, background, obstacles, knight, and UI.
     * @param g the Graphics context to use for painting
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        if (powerUpManager.isInvincible) 
        {
            g.setColor(new Color(0, 0, 255, 50));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(pixelFont);
            String text = "INVINCIBLE";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            g.drawString(text, (getWidth() - textWidth) / 2, (getHeight() - textHeight) / 2);
        }
        if (portalTimer.active) 
        {
            portalTimer.draw(g, portalTimerX, portalTimerY, pixelFont.deriveFont(Font.BOLD, 120f));
        }
        if (powerUpManager.flashActive) 
        {
            powerUpManager.drawFlash(g, pixelFont, getWidth(), getHeight());
            if (powerUpManager.flashActive) return;
            if (!gameloop.isRunning()) gameloop.start();
            if (!scoreTimer.isRunning()) scoreTimer.start();
        }

        if (pic != null) g.drawImage(pic, 0, 0, getWidth(), getHeight(), this);

        obstacleManager.draw(g, this);
        knight.draw(g, this);
        drawUI(g);
    }

    /**
     * Draws the UI elements such as score, high score, and game over messages.
     * @param g the Graphics context to use for painting
     */
    private void drawUI(Graphics g) 
    {
        if (!gameOver) 
        {
            g.setColor(Color.WHITE);
            Font scoreFont = (pixelFont != null)
                ? pixelFont.deriveFont(Font.BOLD, (float)(24 * (getWidth() / (double)baseScreenWidth)))
                : new Font("Arial", Font.BOLD, (int)(24 * (getWidth() / (double)baseScreenWidth)));
            g.setFont(scoreFont);
            g.drawString("Score: " + score.getScore(), 20, 40);
        }

        if (gameOver)
        {
            Font gameOverFont = pixelFont.deriveFont(Font.BOLD, (float)(64 * (getWidth() / (double)baseScreenWidth)));
            g.setFont(gameOverFont);
            FontMetrics metrics = g.getFontMetrics();
            String scoreText = "Score: " + score.getScore();
            int textWidth = metrics.stringWidth(scoreText);
            int textHeight = metrics.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2 + textHeight / 2 + 40;

            String highScoreText = "High Score: " + score.getHighScore();
            Font highScoreFont = pixelFont.deriveFont(Font.BOLD, (float)(28 * (getWidth() / (double)baseScreenWidth)));
            g.setFont(highScoreFont);
            FontMetrics highScoreMetrics = g.getFontMetrics();
            int highScoreTextWidth = highScoreMetrics.stringWidth(highScoreText);
            int highScoreTextHeight = highScoreMetrics.getHeight();

            String restartText = "Press Enter to Restart";
            Font restartFont = pixelFont.deriveFont(Font.BOLD, (float)(20 * (getWidth() / (double)baseScreenWidth)));
            g.setFont(restartFont);
            FontMetrics restartMetrics = g.getFontMetrics();
            int restartTextWidth = restartMetrics.stringWidth(restartText);
            int restartTextHeight = restartMetrics.getHeight();

            String instructionText = "Press ESC to leave game";
            Font instructionFont = pixelFont.deriveFont(Font.PLAIN, (float)(16 * (getWidth() / (double)baseScreenWidth)));
            g.setFont(instructionFont);
            FontMetrics instructionMetrics = g.getFontMetrics();
            int instructionTextWidth = instructionMetrics.stringWidth(instructionText);
            int instructionTextHeight = instructionMetrics.getHeight();

            int maxTextWidth = Math.max(Math.max(textWidth, highScoreTextWidth), Math.max(restartTextWidth, instructionTextWidth));
            int totalHeight = highScoreTextHeight + textHeight + restartTextHeight + instructionTextHeight + 60;
            int padding = (int)(40 * (getWidth() / (double)baseScreenWidth));
            int boxX = (getWidth() - maxTextWidth) / 2 - padding;
            int boxY = y - highScoreTextHeight - textHeight - restartTextHeight - instructionTextHeight / 2 - padding / 2 + 30;
            int boxWidth = maxTextWidth + padding * 2;
            int boxHeight = totalHeight + padding;

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(boxX, boxY, boxWidth, boxHeight);

            int currentY = boxY + highScoreTextHeight + padding / 2;

            g.setFont(highScoreFont);
            g.setColor(Color.WHITE);
            g.drawString(highScoreText, (getWidth() - highScoreTextWidth) / 2, currentY);
            currentY += highScoreTextHeight + 40;

            g.setFont(gameOverFont);
            g.drawString(scoreText, (getWidth() - textWidth) / 2, currentY);
            currentY += textHeight + 10;

            g.setFont(restartFont);
            g.drawString(restartText, (getWidth() - restartTextWidth) / 2, currentY);
            currentY += restartTextHeight + 10;

            g.setFont(instructionFont);
            g.drawString(instructionText, (getWidth() - instructionTextWidth) / 2, currentY);
        }
    }

    /**
     * Updates the game state, moves the knight and obstacles, handles collisions and portal logic.
     */
    public void move() 
    {
        knight.move(gravity, getHeight() - knight.height - 100);

        if (wheelActive) {
            // Don't spawn or update obstacles while wheel is active
            return;
        }

        int speedIncrease = score.getScore() / 5;
        obstacleSpeed = (int)(baseObstacleSpeed * ((double)getWidth() / baseScreenWidth)) + speedIncrease;
        if (obstacleSpeed < 5) obstacleSpeed = 5;

        knightSpeed = 8 + score.getScore() / 10;
        if (knightSpeed > 20) knightSpeed = 20;

        spawntimer++;
        if (spawntimer >= spawnInterval) 
        {
            if (!portalJustSpawned) 
            {
                obstacleManager.spawnObstacle(getWidth(), getHeight(), Cactus, Stone, Barrel, obstacleSpeed);
                spawntimer = 0;
            } 
            else 
            {
                spawnDelay--;
                if (spawnDelay <= 0) 
                {
                    portalJustSpawned = false;
                    spawnDelay = 120;
                }
            }
        }

        obstaclesCrossed += obstacleManager.update();

        if (score.getScore() >= nextPortalAt) 
        {
            obstacleManager.spawnPortal(getWidth(), getHeight(), Portal, score.getScore());
            nextPortalAt += 15;
            portalJustSpawned = true;
        }

        Rectangle knightRect = knight.getBounds();
        for (Obstacle obstacle : obstacleManager.getObstacles()) 
        {
            if (obstacle.isPortal()) {
                continue; // Skip collision for portals
            }
            if (knight.getBounds().intersects(obstacle.getBounds())) {
                // handle collision
            }
        }

        if (score.getScore() >= nextPortalAt) 
        {
            obstacleManager.spawnPortal(getWidth(), getHeight(), Portal, score.getScore());
            nextPortalAt += 15;
            portalJustSpawned = true;
        }

        for (Obstacle obs : obstacleManager.getObstacles()) 
        {
            if (obstacleManager.isPortal(obs)) 
            {
                if (knightRect.intersects(obs.getBounds()) && !wheelActive) 
                {
                    obstacleManager.getObstacles().clear(); // Remove all obstacles
                    cleanupAbilityWheel();
                    wheelActive = true;
                    abilityWheel = new AbilityWheel(() -> {
                        AbilityWheel.Ability chosen = abilityWheel.getSelectedAbility();
                        if (chosen == AbilityWheel.Ability.INVINCIBILITY) 
                        {
                            powerUpManager.triggerFlash("INVINCIBILITY!", new Color(135, 206, 250));
                        } 
                        else if (chosen == AbilityWheel.Ability.DOUBLE_JUMP) 
                        {
                            powerUpManager.triggerFlash("DOUBLE JUMP!", new Color(255, 215, 0));
                        }
                        powerUpManager.applyPowerUp(chosen);
                        SoundManager.playSound("PowerUp.wav");
                        cleanupAbilityWheel();
                        knightVisible = true;
                        repaint();
                    });
                    abilityWheel.setBounds(0, 0, 400, 400);
                    abilityWheel.setLocation((getWidth() - 400) / 2, (getHeight() - 400) / 2);
                    setLayout(null);
                    add(abilityWheel, JLayeredPane.POPUP_LAYER);
                    abilityWheel.setVisible(true);
                    abilityWheel.startSpinning();
                    abilityWheel.repaint();
                    knightVisible = false;
                    repaint();
                    return;
                }
            } 
            else if (!portalTimer.active && !powerUpManager.isInvincible && knightRect.intersects(obs.getBounds())) 
            {
                gameloop.stop();
                scoreTimer.stop();
                knight.setState(Knight.State.DEAD);
                SoundManager.playSound("Death.wav"); // Play death sound
                gameOver = true;
                break;
            }
        }

        if (knight.y >= getHeight() - knight.height - 100 && !gameOver) 
        {
            knight.setState(Knight.State.RUNNING);
        }
    }

    /**
     * Handles timer events for the game loop, updating frames and repainting.
     * @param e the ActionEvent triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (!gameOver) 
        {
            knight.updateFrame();
            move();
            repaint();
        }
    }

    /**
     * Handles key press events for jumping, restarting, and exiting the game.
     * @param e the KeyEvent triggered by the user
     */
    @Override
    public void keyPressed(KeyEvent e) 
    {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            {
                restartGame();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) 
            {
                System.exit(0);
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!knight.isJumping()) {
                int jumpVelocity = baseJumpVelocity;
                if (powerUpManager.hasDoubleJump()) { // <-- Add this check (implement hasDoubleJump() if needed)
                    jumpVelocity = (int)(baseJumpVelocity * 1.2); // 20% higher jump
                }
                knight.jump(jumpVelocity);
                knight.setState(Knight.State.JUMPING);
                SoundManager.playSound("Jump.wav"); // Play jump sound
            }
        }
    }

    /**
     * Handles key release events (not used).
     * @param e the KeyEvent triggered by the user
     */
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Handles key typed events (not used).
     * @param e the KeyEvent triggered by the user
     */
    @Override
    public void keyTyped(KeyEvent e) {}
}



