import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by arirappaport on 9/8/15.
 */
class ZombiePanel extends JPanel
{
  private final EntityManager entityManager;
  private final JFrame frame;

  public ZombiePanel(JFrame frame, EntityManager entityManager)
  {
    this.entityManager = entityManager;
    this.frame = frame;
  }

  public void init()
  {
    setSize(frame.getSize());
    ResourceManager.populateImageHashMap();
    repaint();
    frame.addKeyListener(new KeyboardHandler());
    //entityManager.add(new House(100, 100, updateManager));
    ////entityManager.add(new Trap(new Point2D.Float(Settings.tileSize*4,Settings.tileSize*4),entityManager));
    ////entityManager.add(new Fire(new Rectangle2D.Float(Settings.tileSize*2,Settings.tileSize*2, Settings.tileSize*3, Settings.tileSize*3)));
    //entityManager.add(new UpdateCounter());
    //entityManager.add(new SightDrawer());
    frame.addComponentListener(new ResizeHandler());
    new Timer(1000 / Settings.frameRate, this::timerTick).start();
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

  private void timerTick(ActionEvent actionEvent)
  {
    entityManager.update();
    repaint();
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
      //Settings.updateTileSize((int)(componentEvent.getComponent().getWidth() / 1080f * Settings.DEFAULT_TILE_SIZE));
      entityManager.setScale(componentEvent.getComponent().getWidth() / 1920f);
    }
  }
}
