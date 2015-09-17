import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Tile
{
  private BufferedImage tileImg;

  public Tile(String imgName, boolean isSolid)
  {
    tileImg = ResourceManager.getImage(imgName + ".png");
  }

  public BufferedImage getTileImg()
  {
    return tileImg;
  }
}
