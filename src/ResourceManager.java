import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/8/15.
 * Class name and description go here.
 */
public class ResourceManager
{
  public static HashMap<String, BufferedImage> imageHashMap;
  public static ArrayList<String> imagePaths;
  static
  {
    imageHashMap = new HashMap<>();
    imagePaths = new ArrayList<>();
    imagePaths.add("tileset/nonwall.png");
    imagePaths.add("tileset/wall.png");
    imagePaths.add("tileset/outofbounds.png");
    imagePaths.add("fire/firetrap.png");
    imagePaths.add("fire/firebackground.png");
    for (int i = 0; i <= 16; i++)
    {
      imagePaths.add("animation/zombie/idle_" + i + ".png");
      imagePaths.add("animation/zombie/move_" + i + ".png");
    }
  }

  public static void populateImageHashMap()
  {
    imagePaths.forEach(ResourceManager::getImage);
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