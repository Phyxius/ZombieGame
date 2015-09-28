import javax.swing.*;
import java.awt.*;

/**
 * Shea Polansky
 * Main: Creates the game's GUI
 */
public class Main
{
  /**
   * Creates the main game window.
   * @param args Command line arguments. Currently ignored.
   */
  public static void main(String[] args)
  {
    //System.setProperty("sun.java2d.opengl", "true");
    new SettingsWindow();
    JFrame zombieFrame = new JFrame();
    ZombiePanel zombiePanel = new ZombiePanel(zombieFrame);
    zombiePanel.init();
    zombieFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    zombieFrame.setSize(new Dimension(20 * Settings.tileSize, 20 * Settings.tileSize));
    zombieFrame.setContentPane(zombiePanel);
    zombieFrame.setVisible(true);
  }
}
