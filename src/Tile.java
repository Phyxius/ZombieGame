import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Tile extends Entity
{
  private BufferedImage tileImg;
  private boolean isSolid;

  public Tile(String imgName, boolean isSolid)
  {
    tileImg = ResourceManager.getImage(imgName + ".png");
    this.isSolid = isSolid;
  }

  public BufferedImage getTileImg()
  {
    return tileImg;
  }

  @Override
  public boolean isSolid()
  {
    return isSolid;
  }
}
