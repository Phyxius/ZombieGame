import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/8/15.
 */
public class ResourceManager
{
  public static HashMap<String, BufferedImage> tileHashMap;
  public static String tilePaths[] = {"resources/tileset/center.png", "resources/tileset/horizhall.png",
                                      "resources/tileset/leftwall.png", "resources/tileset/lowerleft.png"};

  public ResourceManager()
  {
    tileHashMap = new HashMap<>();
  }

  public void populateImageHashMap()
  {
    BufferedImage img = null;
    ClassLoader c1;
    InputStream in;
    for(int i = 0; i < tilePaths.length; i++)
    {
      String imagePath = tilePaths[i];
      try
      {
        c1 = this.getClass().getClassLoader();
        in = c1.getResourceAsStream(imagePath);
        img = ImageIO.read(in);
      } catch (IOException ex)
      {
        ex.printStackTrace();
      }
      tileHashMap.put(imagePath, img);
    }
  }
}
