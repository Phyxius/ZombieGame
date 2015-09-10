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
  public static HashMap<String, BufferedImage> tileHashMap;
  public static ArrayList<String> tilePaths;
  public ResourceManager()
  {
    tileHashMap = new HashMap<>();
    tilePaths = new ArrayList<>();
    tilePaths.add("resources/center.png");
    tilePaths.add("resources/horizhall.png");
    tilePaths.add("resources/leftwall.png");
    tilePaths.add("resources/rightwall.png");
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
      tileHashMap.put(imagePath, img);
    }
  }
}
