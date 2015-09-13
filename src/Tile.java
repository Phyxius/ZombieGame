import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Tile
{
  private BufferedImage tileImg;
  private boolean isSolid;

  public Tile(String imgName, boolean isSolid)
  {
    ResourceManager manager = new ResourceManager();
    tileImg = manager.imageHashMap.get(imgName + ".png");
    this.isSolid = isSolid;
  }

  public BufferedImage getTileImg()
  {
    return tileImg;
  }

}
