import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/8/15.
 */
public class ResourceManager
{
  public static HashMap<String, BufferedImage> imageHashMap;
  public static ArrayList<String> tilePaths;
  public static ArrayList<String> zombiePaths;
  public ResourceManager()
  {
    imageHashMap = new HashMap<>();
    tilePaths = new ArrayList<>();
    zombiePaths = new ArrayList<>();
    tilePaths.add("tileset/nonwall.png");
    tilePaths.add("tileset/wall.png");
    tilePaths.add("tileset/outofbounds.png");
    for (int i = 0; i <= 16; i++)
    {
      zombiePaths.add("animation/zombie/idle_" + i + ".png");
      zombiePaths.add("animation/zombie/move_" + i + ".png");
    }
  }

  public void populateImageHashMap()
  {
    BufferedImage img = null;
    ClassLoader c1;
    InputStream in;
    for(String imagePath: tilePaths)
    {
      try
      {
        c1 = this.getClass().getClassLoader();
        in = c1.getResourceAsStream(imagePath);
        img = ImageIO.read(in);
      } catch (IOException ex)
      {
        ex.printStackTrace();
      }
      imageHashMap.put(imagePath, img);
    }

    for(String imagePath: zombiePaths)
    {
      try
      {
        c1 = this.getClass().getClassLoader();
        in = c1.getResourceAsStream(imagePath);
        img = ImageIO.read(in);
      } catch (IOException ex)
      {
        ex.printStackTrace();
      }
      imageHashMap.put(imagePath, img);
    }
  }
}
