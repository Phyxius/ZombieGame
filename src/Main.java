import javax.swing.*;
import java.awt.*;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Main
{
  public static void main(String[] args)
  {
    System.setProperty("sun.java2d.opengl", "true");
    JFrame zombieFrame = new JFrame();
    ZombiePanel zombiePanel = new ZombiePanel(zombieFrame);
    zombiePanel.init();
    zombieFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    zombieFrame.setVisible(true);
    zombieFrame.setSize(new Dimension(20 * Settings.tileSize, 20 * Settings.tileSize));
    zombieFrame.setContentPane(zombiePanel);
  }
}
