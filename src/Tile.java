import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Tile extends Entity
{
  private BufferedImage tileImg;

  public Tile(String imgName)
  {
    tileImg = ResourceManager.imageHashMap(imgName);
  }
}
