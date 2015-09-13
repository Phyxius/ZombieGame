import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Animation
{
  private int maxFrame;
  private int offset;
  private BufferedImage img;
  private String filePath;

  public Animation(String truncatedFilePath, int maxFrame)
  {
    filePath = "resources/" + truncatedFilePath;
    offset = 0;
    this.maxFrame = maxFrame;
    img = ResourceManager.getImage(filePath + offset + ".png");
  }

  public BufferedImage nextFrame(boolean reset)
  {
    if(reset) offset = 0;
    img = ResourceManager.getImage(filePath + offset + ".png");
    offset = (offset+1) % (maxFrame+1);
    return img;
  }

  public BufferedImage getFrame()
  {
    return img;
  }

}
