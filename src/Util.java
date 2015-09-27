import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class Util
{
  static final Random rng = new Random();
  static float secondsToFrames(float seconds)
  {
    return seconds / Settings.frameRate;
  }
  static float tilesPerSecondToPixelsPerFrame(float tilesPerSec)
  {
    return tilesPerSec * Settings.tileSize / Settings.frameRate;
  }

  /**
   * Clamps a given value to be at most max, and at least min.
   * Assumes min < max
   * @param value the value to clamp
   * @param min the minimum value
   * @param max the maximum value
   * @return min if value < min, max if value > max, value otherwise
   */
  public static int clampInteger(int value, int min, int max)
  {
    return Math.min(max, Math.max(min, value));
  }

  public static Point2D.Float getRectangleCenterPoint(Rectangle2D.Float rect)
  {
    return new Point2D.Float((float)rect.getCenterX(), (float)rect.getCenterY());
  }
}
