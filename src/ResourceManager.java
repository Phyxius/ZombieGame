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
  static
  {
    imageHashMap = new HashMap<>();
    tilePaths = new ArrayList<>();
    tilePaths.add("tileset/nonwall.png");
    tilePaths.add("tileset/wall.png");
    tilePaths.add("tileset/outofbounds.png");
    for (int i = 0; i <= 16; i++)
    {
      tilePaths.add("animation/zombie/idle_" + i + ".png");
      tilePaths.add("animation/zombie/move_" + i + ".png");
    }
  }

  public static void populateImageHashMap()
  {

    for(String imagePath: tilePaths)
    {
      getImage(imagePath);
    }
  }

  public static BufferedImage getImage(String path)
  {
    if (imageHashMap.containsKey(path)) return imageHashMap.get(path);
    BufferedImage image = null;
    ClassLoader classLoader = ResourceManager.class.getClassLoader();
    try
    {
      image = ImageIO.read(classLoader.getResourceAsStream(path));
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    imageHashMap.put(path, image);
    return image;
  }
}
