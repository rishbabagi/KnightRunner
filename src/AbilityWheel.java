import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class AbilityWheel extends JPanel 
{
    public enum Ability { INVINCIBILITY, DOUBLE_JUMP }
    private Ability selectedAbility;
    private boolean spinning = false;
    private double rotationAngle = 0;
    private Timer spinTimer;
    private Runnable onAbilityChosen;
    private static final int FPS = 60;
    private static final double SPIN_SPEED = 15;
    private static final int NUM_SECTIONS = 2;

    /**
     * Constructs an AbilityWheel with a callback for when an ability is chosen.
     * @param onAbilityChosen Runnable to execute after the wheel finishes spinning and an ability is selected
     */
    public AbilityWheel(Runnable onAbilityChosen) 
    {
        this.onAbilityChosen = onAbilityChosen;
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(400, 400));

        addMouseListener(new MouseAdapter()
         {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                if (!spinning) 
                {
                    startSpinning();
                }
            }
        });
    }

    /**
     * Starts spinning the wheel. After the spin duration, randomly selects an ability and calls the callback.
     */
    public void startSpinning() 
    {
        spinning = true;
        Random rand = new Random();
        int targetSection = rand.nextInt(NUM_SECTIONS);
        int sectionStart = targetSection * 180;
        int sectionOffset = rand.nextInt(180);
        double targetAngle = sectionStart + sectionOffset + 5 * 360; // 5 full spins + random in section

        final double[] currentSpeed = {SPIN_SPEED * 2}; // Start fast
        final double deceleration = 0.15; // Adjust for slower/faster stop

        spinTimer = new Timer(1000 / FPS, null);
        spinTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!spinning) return;

                // Decelerate
                if (currentSpeed[0] > 1.2) {
                    currentSpeed[0] -= deceleration;
                } else {
                    currentSpeed[0] = 1.2;
                }

                // Move towards target angle
                if (rotationAngle < targetAngle - currentSpeed[0]) {
                    rotationAngle += currentSpeed[0];
                    repaint();
                } else {
                    rotationAngle = targetAngle;
                    spinning = false;
                    spinTimer.stop();

                    int pointerAngle = (int)((rotationAngle + 90) % 360);
                    if (pointerAngle < 0) pointerAngle += 360;
                    int section = pointerAngle / 180;
                    selectedAbility = Ability.values()[section];

                    repaint();
                    // Only call the callback ONCE, and after a short delay
                    new Timer(1000, ev -> {
                        if (onAbilityChosen != null) onAbilityChosen.run();
                    }).start();
                }
            }
        });
        spinTimer.start();
    }

    /**
     * Returns the ability selected by the wheel after spinning.
     * @return the selected Ability
     */
    public Ability getSelectedAbility() 
    {
        return selectedAbility;
    }

    /**
     * Paints the spinning ability wheel, its sections, labels, and the pointer.
     * @param g the Graphics context to use for painting
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        int wheelDiameter = (int)(Math.min(getWidth(), getHeight()) * 0.8);
        int wheelRadius = wheelDiameter / 2;
        int wheelX = (getWidth() - wheelDiameter) / 2;
        int wheelY = (getHeight() - wheelDiameter) / 2;

        // Use a smaller font size for better fit
        Font pixelFont;
        try 
        {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/ProjectImages/Pixel Emulator.otf"))
                .deriveFont(Font.BOLD, wheelDiameter / 16f); // Smaller font
        } 
        catch (Exception e) 
        {
            pixelFont = new Font("Arial", Font.BOLD, wheelDiameter / 16);
        }
        g.setFont(pixelFont);

        Color[] colors = {new Color(135, 206, 250), new Color(255, 215, 0)};
        String[][] labels = 
        {
            {"INVINCI", "BILITY"},
            {"DOUBLE", "JUMP"}
        };

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(wheelX + wheelRadius, wheelY + wheelRadius);
        g2d.rotate(Math.toRadians(rotationAngle));

        for (int i = 0; i < NUM_SECTIONS; i++) 
        {
            g2d.setColor(colors[i]);
            g2d.fillArc(-wheelRadius, -wheelRadius, wheelDiameter, wheelDiameter, i * 180 - 90, 180);
        }

        // Draw labels, each as two lines, centered in its section
        for (int i = 0; i < NUM_SECTIONS; i++) 
        {
            double angle = Math.toRadians(i * 180 - 90 + 90); // Center of each section
            int labelX = (int)(Math.cos(angle) * wheelRadius * 0.55);
            int labelY = (int)(Math.sin(angle) * wheelRadius * 0.35); // Move closer to center vertically

            g2d.setColor(Color.BLACK);
            FontMetrics fm = g2d.getFontMetrics();
            int textHeight = fm.getAscent();

            // Draw first line
            String line1 = labels[i][0];
            int textWidth1 = fm.stringWidth(line1);
            g2d.drawString(line1, labelX - textWidth1 / 2, labelY);

            // Draw second line, slightly below the first
            String line2 = labels[i][1];
            int textWidth2 = fm.stringWidth(line2);
            g2d.drawString(line2, labelX - textWidth2 / 2, labelY + textHeight);
        }

        g2d.dispose();

        // Move the pointer up a bit more so the bottom half is on the spinner and the top is not
        int pointerWidth = wheelDiameter / 10;
        int pointerHeight = wheelDiameter / 8;
        int px = wheelX + wheelRadius;
        // Increase the subtraction to move the pointer further up
        int py = wheelY - pointerHeight / 3; // Move up by a third of its height above the wheel center

        Polygon pointer = new Polygon();
        pointer.addPoint(px, py + pointerHeight); // Tip (down)
        pointer.addPoint(px - pointerWidth / 2, py); // Left base
        pointer.addPoint(px + pointerWidth / 2, py); // Right base

        g.setColor(Color.RED);
        g.fillPolygon(pointer);
        g.setColor(Color.BLACK);
        g.drawPolygon(pointer);
    }
}



