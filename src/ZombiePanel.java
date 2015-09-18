import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    ResourceManager.populateImageHashMap();
    repaint();
    frame.addKeyListener(new KeyboardHandler());
    Player player = new Player();
    entityManager.add(player);
    entityManager.add(new LineZombie(player, new Point2D.Float(80, 80)));
    entityManager.add(new House(32, 32, entityManager));
    entityManager.add(new Trap(new Point2D.Float(Settings.tileSize*4,Settings.tileSize*4),entityManager));
    //entityManager.add(new Fire(new Rectangle2D.Float(Settings.tileSize*2,Settings.tileSize*2, Settings.tileSize*3, Settings.tileSize*3)));
    entityManager.add(new UpdateCounter());
    entityManager.setEntityToFollow(player);
    new Timer(1000 / Settings.frameRate, this::timerTick).start();
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
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());
    //House house = new House(32,32);
    //g.drawImage(house.houseImg, 0, 0, house.houseImg.getWidth(), house.houseImg.getHeight(), null);
    entityManager.draw((Graphics2D) g);
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
