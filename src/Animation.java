import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by arirappaport on 9/10/15.
 */
public class Animation
{
  protected int maxFrame;
  protected int offset;
  protected BufferedImage img;
  protected String filePath;

  public Animation(String truncatedFilePath, int maxFrame)
  {
    filePath = truncatedFilePath;
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
