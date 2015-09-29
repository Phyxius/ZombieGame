/**
 * Created by arirappaport on 9/10/15.
 * The animation frame is given by the ResourceManager using concatenated string, resources/ + filePath + index[0 - maxFrameIndex] + .png
 *
 * @param filePath The path to the frames for the animation.
 * @param maxFrameIndex Greatest index of the animation frames.
 * @param frame Current animation frame.
 * @param frameIndex Index of the current frame.
 * @param synchronize Indicates if the frames should synchronize with Settings.frameRate. Where maxFrameIndex > Settings.frameRate the synchronization is turned off.
 * @param syncOffset Used for synchronization of the frameRate. When syncOffset % syncTarget == 0 update frameIndex.
 * @param syncTarget Used for synchronization of the frameRate. syncTarget = Settings.frameRate / (maxFrameIndex + 1) && syncTarget > 1 otherwise synchronization is turned off.
 */

import java.awt.image.BufferedImage;

public class Animation
{
  private final String filePath;
  private final int maxFrameIndex;
  private BufferedImage frame;
  private int frameIndex = 0;
  private int syncOffset;
  private int syncTarget;
  private boolean synchronize = false;

  /**
   * Creates an animation object with no synchronization.
   *
   * @param truncatedFilePath The filePath for the animation.
   * @param maxFrameIndex     The maxFrameIndex for the animation.
   */
  public Animation(String truncatedFilePath, int maxFrameIndex)
  {
    filePath = truncatedFilePath;
    this.maxFrameIndex = maxFrameIndex;
    frame = ResourceManager.getImage(filePath + frameIndex + ".png");
  }

  /**
   * Creates an animation object with no synchronization.
   *
   * @param truncatedFilePath The filePath for the animation.
   * @param maxFrameIndex     The maxFrameIndex for the animation.
   * @param synchronize       The frame rate synchronization state for the animation.
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
   *
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
      if (syncOffset == 0) frameIndex++;
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
