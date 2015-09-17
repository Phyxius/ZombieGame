// Maybe we should change the maxFrameIndex to totalFrames and save number of the frames instead of index.
// The value of the maxFrameIndex is never used without + 1.

// Do we want to have an animation duration for one time animations such as the trap?

/**
 * Created by arirappaport on 9/10/15.
 *
 * @param filePath The path to the frames for the animation.
 * The animation frame is given by the ResourceManager using concatenated string, resources/ + filePath + index[0 - maxFrameIndex] + .png
 * @param maxFrameIndex Greatest index of the animation frames.
 * @param frame Current animation frame.
 * @param frameIndex Index of the current frame.
 * @param synchronize Indicates if the frames should synchronize with Settings.frameRate. Where maxFrameIndex > Settings.frameRate the synchronization is turned off.
 * @param syncOffset Used for synchronization of the frameRate. When syncOffset % syncTarget == 0 update framIndex.
 * @param syncTarget Used for synchronization of the frameRate. syncTarget = Settings.frameRate / (maxFrameIndex + 1) && syncTarget > 1 otherwise synchronization is turned off.
 */
import java.awt.image.BufferedImage;

public class Animation // Modified 9-17 FrameRateSync
{
  protected final String filePath;
  protected final int maxFrameIndex;
  private BufferedImage frame;
  private int frameIndex;
  private boolean synchronize = false;
  private int syncOffset;
  private int syncTarget;

  /**
   * Creates an animation object with no synchronization.
   * @param truncatedFilePath The filePath for the animation.
   * @param maxFrameIndex The maxFrameIndex for the animation.
   */
  public Animation(String truncatedFilePath, int maxFrameIndex)
  {
    filePath = truncatedFilePath;
    this.maxFrameIndex = maxFrameIndex;
    frameIndex = 0;
    frame = ResourceManager.getImage(filePath + frameIndex + ".png");
  }

  /**
   * Creates an animation object with no synchronization.
   * @param truncatedFilePath The filePath for the animation.
   * @param maxFrameIndex The maxFrameIndex for the animation.
   * @param synchronize The frame rate synchronization state for the animation.
   */
  public Animation(String truncatedFilePath, int maxFrameIndex, boolean synchronize)
  {
    this(truncatedFilePath, maxFrameIndex);
    syncFrameRate(synchronize);
  }

  /**
   * @return The current animation frame.
   */
  public BufferedImage getFrame()
  {
    return frame;
  }

  /**
   * Updates the animation frame.
   * @param reset Resets the animation to the first frame.
   * @return The current frame.
   */
  public BufferedImage nextFrame(boolean reset)
  {
    if (reset) frameIndex = 0;
    frame = ResourceManager.getImage(filePath + frameIndex + ".png");
    if (synchronize)
    {
      syncOffset = (syncOffset + 1) % syncTarget;
      if (syncOffset % syncTarget == 0) frameIndex++;
    }
    else
    {
      frameIndex++;
    }
    frameIndex %= maxFrameIndex + 1;
    return frame;
  }

  /**
   * @param synchronize The frame rate synchronization state for the animation.
   */
  private void syncFrameRate(boolean synchronize)
  {
    this.synchronize = synchronize;
    if (synchronize)
    {
      syncTarget = Settings.frameRate / (maxFrameIndex + 1);
      if (syncTarget <= 1)
      {
        syncTarget = 1;
        this.synchronize = false;
      }
    }
  }
}
