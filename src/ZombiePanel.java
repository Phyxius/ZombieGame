import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/8/15.
 */
public class ZombiePanel extends JPanel
{
  private JFrame frame;
  EntityManager entityManager = new EntityManager();

  public ZombiePanel(JFrame frame)
  {
    this.frame = frame;
  }

  public void init()
  {
    setSize(frame.getSize());
    ResourceManager manager = new ResourceManager();
    manager.populateImageHashMap();
    repaint();
    addKeyListener(new KeyboardHandler());
    new Timer(1000 / Settings.frameRate, this::timerTick);
  }

  private void timerTick(ActionEvent actionEvent)
  {
    entityManager.update();
    repaint();
  }

  @Override
  //This whole method is just to sample
  // all the tiles
  public void paintComponent(Graphics g)
  {
    entityManager.draw((Graphics2D)g);
  }

  private class KeyboardHandler extends KeyAdapter
  {
    @Override
    public void keyPressed(KeyEvent e)
    {
      entityManager.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
      entityManager.keyReleased(e);
    }
  }
}
