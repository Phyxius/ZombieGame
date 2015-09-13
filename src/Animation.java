import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Animation
{
  int maxFrame;
  HashMap<String, BufferedImage> imageHashMap;
  String filePath;
  public Animation(String truncatedFilePath, int maxFrame)
  {
    filePath = "resources/" + truncatedFilePath;
    this.maxFrame = maxFrame;
    this.imageHashMap = ResourceManager.imageHashMap;
  }

  public void animate(Graphics2D local)
  {
    BufferedImage img;
    for(int i = 0; i < maxFrame; i++)
    {
      //if(i < 10)
      img = imageHashMap.get(filePath + "_" + i + ".png");
      //else img = imageHashMap.get(filePath + i + ".png");
      local.drawImage(img, 0 , 0, img.getWidth(), img.getHeight(), null);
    }
  }
}
