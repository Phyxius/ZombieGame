import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Animation
{
  int maxFrame;
  int offset;
  HashMap<String, BufferedImage> imageHashMap;
  String filePath;
  public Animation(String truncatedFilePath, int maxFrame)
  {
    filePath = "resources/" + truncatedFilePath;
    offset = 0;
    this.maxFrame = maxFrame;
    this.imageHashMap = ResourceManager.imageHashMap;
  }

  public BufferedImage nextFrame(boolean reset)
  {
    if(reset) offset = 0;
    BufferedImage img = imageHashMap.get(filePath + "_" + offset + ".png");
    if((offset = (offset%(maxFrame+1))) != 0) offset++;
    return img;
  }
}
