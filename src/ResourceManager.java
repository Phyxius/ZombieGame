/**
 * Created by arirappaport on 9/8/15.
 * ResourceManager: Handles loading and memoization of resources.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourceManager
{
  public static HashMap<String, BufferedImage> imageHashMap;
  public static ArrayList<String> imagePaths;

  /**
   * Populates imagePaths with known resources so they can
   * be loaded in a batch.
   */
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

    for (int i = 0; i < 10; i++)
    {
      imagePaths.add("animation/player1/moving_small_" + i + ".png");
    }

    imagePaths.add("gui/trapIcon.png");
    for (int i = 0; i < 10; i++)
    {
      imagePaths.add("gui/digit_" + i + ".png");
    }
  }

  /**
   * Loads known resources to reduce in-game stuttering.
   */
  public static void populateImageHashMap()
  {
    imagePaths.forEach(ResourceManager::getImage);
  }

  /**
   * Gets Image at the given path, loading from the filesystem if it has not already
   * been loaded.
   *
   * @param path the path to the image
   * @return the loaded image
   */
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