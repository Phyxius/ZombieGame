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
    imagePaths.add("objects/bookshelf02.png");
    imagePaths.add("fire/firetrap.png");
    imagePaths.add("fire/firebackground.png");
    imagePaths.add("gui/label_stamina.png");
    imagePaths.add("gui/progress_border_green.png");
    imagePaths.add("gui/progress_border_red.png");
    imagePaths.add("gui/progress_fill_green.png");
    imagePaths.add("gui/progress_fill_red.png");
    for (int i = 0; i <= 16; i++)
    {
      imagePaths.add("animation/zombie/idle_" + i + ".png");
      imagePaths.add("animation/zombie/move_" + i + ".png");
    }

    //for (int i = 0; i <= 13; i++)
    //{
    //  imagePaths.add("animation/player/move_" + i + ".png");
    //}
    //for (int i = 0; i <= 8; i++)
    //{
    //  imagePaths.add("animation/player/idle_" + i + ".png");
    //}

    for (int i = 0; i < 10; i++)
    {
      imagePaths.add("animation/player1/moving-" + i + ".png");
    }
    for (int i = 0; i <= 1; i++)
    {
      imagePaths.add("animation/player1/idle-" + i + ".png");
    }

    imagePaths.add("gui/trapIcon.png");
    for (int i = 0; i < 10; i++)
    {
      imagePaths.add("gui/digit_" + i + ".png");
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