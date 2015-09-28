import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
    UpdateManager updateManager = new UpdateManager(entityManager);
    NewGame.makeNewGame(updateManager);
    //entityManager.add(new House(100, 100, updateManager));
    ////entityManager.add(new Trap(new Point2D.Float(Settings.tileSize*4,Settings.tileSize*4),entityManager));
    ////entityManager.add(new Fire(new Rectangle2D.Float(Settings.tileSize*2,Settings.tileSize*2, Settings.tileSize*3, Settings.tileSize*3)));
    //entityManager.add(new UpdateCounter());
    //entityManager.add(new SightDrawer());
    addComponentListener(new ResizeHandler());
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
    entityManager.draw((Graphics2D) g, getGraphicsConfiguration());
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

  private class ResizeHandler extends ComponentAdapter
  {
    @Override
    public void componentResized(ComponentEvent componentEvent)
    {
      Settings.updateTileSize((int)(componentEvent.getComponent().getWidth() / 1080f * Settings.DEFAULT_TILE_SIZE));
    }
  }
}
