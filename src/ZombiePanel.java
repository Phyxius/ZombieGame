import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/8/15.
 */
public class ZombiePanel extends JPanel
{
  private JFrame frame;

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
  }

  @Override
  //This whole method is just to sample
  // all the tiles
  public void paintComponent(Graphics g)
  {
    int tileSize = Settings.tileSize;
    String tilePaths[] = ResourceManager.tilePaths;
    BufferedImage curImg;
    for(int i = 0; i < 20; i++)
    {
      for(int j = 0; j < 20; j++)
      {
        curImg = ResourceManager.tileHashMap.get(tilePaths[i%tilePaths.length]);
        g.drawImage(curImg, j*tileSize, i*tileSize,tileSize, tileSize, null);
      }
    }
  }
}
