import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Tile
{
  private BufferedImage tileImg;
  private boolean isSolid;

  /**
   *
   * @param imgName The name of the image, either a wall or non-wall Tile
   *                 there are multiple non-wall images for variety but only
   *                 one wall tile. The .png is left out of the name for
   *                 convenience.
   * @param isSolid Wall tiles are solid, non-wall tiles are not.
   */
  public Tile(String imgName, boolean isSolid)
  {
    tileImg = ResourceManager.getImage(imgName + ".png");
    this.isSolid = isSolid;
  }

  /**
   * Getter for the actual BufferedImage associated with the tile
   * @return tileImg
   */
  public BufferedImage getTileImg()
  {
    return tileImg;
  }

  public boolean isSolid()
  {
    return isSolid;
  }
}
