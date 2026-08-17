import javax.swing.*;
import java.awt.*;


/**
* The main application entry point for Knight Runner.
* Sets up the main window, card layout, and handles switching between the start screen and the game.
*/
public class App {
   /**
    * Launches the Knight Runner application.
    * @param args command-line arguments (not used)
    */
   public static void main(String[] args)
   {
       Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
       int width = screenDimension.width - 10;
       int height = screenDimension.height - 100;


       JFrame frame = new JFrame("Base Runner");
       frame.setSize(width, height);
       frame.setLocationRelativeTo(null);
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


       CardLayout cardLayout = new CardLayout();
       JPanel mainPanel = new JPanel(cardLayout);


       KnightRunner knightRunner = new KnightRunner();
       StartScreen startScreen = new StartScreen(e -> 
       {
           cardLayout.show(mainPanel, "game");
           knightRunner.requestFocusInWindow();
           SoundManager.setBackgroundMusicVolume(-10.0f); 
       });


       mainPanel.add(startScreen, "start");
       mainPanel.add(knightRunner, "game");


       frame.add(mainPanel);
       frame.setVisible(true);


       SoundManager.playBackgroundMusic("ThemeMusic.wav", 0.0f);


       startScreen.requestFocusInWindow();
   }
}

