import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 *
 */
public class Animation // Modified 9-17 FrameRateSync
{
  protected int maxFrame;
  protected int offset;
  protected int syncCount;
  protected int syncTarget;
  protected boolean syncRate = false;
  protected BufferedImage img;
  protected String filePath;


  public Animation(String truncatedFilePath, int maxFrame)
  {
    filePath = truncatedFilePath;
    offset = 0;
    this.maxFrame = maxFrame;
    img = ResourceManager.getImage(filePath + offset + ".png");
  }

  public Animation(String truncatedFilePath, int maxFrame, boolean syncRate)
  {
    this(truncatedFilePath, maxFrame);
    syncFrameRate(syncRate);
  }

  public BufferedImage nextFrame(boolean reset)
  {
    if (reset) offset = 0;
    img = ResourceManager.getImage(filePath + offset + ".png");
    if (syncRate)
    {
      if (reset) syncCount = 0;
      syncCount = (syncCount + 1) % syncTarget;
      if (syncCount == 0) offset ++;
    }
    else
    {
      offset++;
    }
    offset %= maxFrame + 1;
    return img;
  }

  public BufferedImage getFrame()
  {
    return img;
  }

  private void syncFrameRate(boolean syncRate)
  {
    this.syncRate = syncRate;
    if (syncRate)
    {
      syncTarget = Settings.frameRate / (maxFrame + 1);
      if (syncTarget <= 1)
      {
        syncTarget = 1;
        this.syncRate = false;
      }
    }
  }
}
