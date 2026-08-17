import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;




public class StartScreen extends JPanel
{
   private Image background;
   private Font pixelFont;
   private JButton startButton;


   /**
    * this the constructor for the HomeScreen
    * also gets the background for our HomeScreen
    * makes the Start Button here as well
    * @param startListener keeps track of User's movement in the UI
    */
   public StartScreen(ActionListener startListener)
   {
       setLayout(null); // We'll position the button manually
       setFocusable(true);
       background = new ImageIcon(getClass().getResource("/ProjectImages/background.jpg")).getImage();
       try {
           InputStream is = getClass().getResourceAsStream("/ProjectImages/Pixel Emulator.otf");
           if (is != null)
           {
               pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
           }
           else
           {
               pixelFont = new Font("Arial", Font.BOLD, 24); // fallback
           }
       }
       catch (Exception e)
       {
           pixelFont = new Font("Arial", Font.BOLD, 24); // fallback
       }


       // Create and style the Start button
       startButton = new JButton("Start");
       int buttonWidth = 300; 
       int buttonHeight = 90; 
       startButton.setBounds(0, 0, buttonWidth, buttonHeight);
       startButton.setFont((pixelFont != null) ? pixelFont.deriveFont(Font.BOLD, 40f) : new Font("Arial", Font.BOLD, 40));


       startButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
       startButton.setOpaque(true);
       startButton.setBackground(new Color(139, 69, 19)); // brown color we can change later if we want to
       startButton.setForeground(Color.WHITE);
       startButton.setFocusPainted(false);
       startButton.setBorderPainted(false);


       startButton.addActionListener(startListener);
       add(startButton);
   }


   @Override
   /**
    * centers out the Button and the name of the game
    */
   public void doLayout()
   {
       super.doLayout();
       // Center the button horizontally and place it lower on the screen
       int buttonWidth = startButton.getWidth();
       int buttonHeight = startButton.getHeight();
       int x = (getWidth() - buttonWidth) / 2;
       int y = getHeight() - buttonHeight - 120;
       startButton.setLocation(x, y);
   }


  
   @Override
   /**
    * This method draws everything on the HomeScreen as well as the Name of our game in large print
    * @param g used to use Graphic's draw method
    */
   protected void paintComponent(Graphics g)
   {
       super.paintComponent(g);


       // Draw background
       if (background != null)
       {
           g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
       }
       else
       {
           g.setColor(Color.BLACK);
           g.fillRect(0, 0, getWidth(), getHeight());
       }


       // Draw "Knight Runner" centered with pixel font
       String title = "Knight Runner";
       g.setColor(Color.WHITE);
       int titleFontSize = Math.max(48, getWidth() / 14);
       Font titleFont = (pixelFont != null) ? pixelFont.deriveFont(Font.BOLD, titleFontSize) : new Font("Arial", Font.BOLD, titleFontSize);
       g.setFont(titleFont);
       FontMetrics titleMetrics = g.getFontMetrics(titleFont);
       int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;
       int titleY = getHeight() / 2 - titleMetrics.getHeight() / 2 + titleMetrics.getAscent() - 300;
       g.drawString(title, titleX, titleY);
   }
}

