import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created by Shea on 2015-09-07.
 * Util: miscellaneous utility methods.
 */
public class Util
{
  static final Random rng = new Random();
  static float secondsToFrames(float seconds)
  {
    return seconds / Settings.frameRate;
  }
  static final int NUM_DIRECTIONS = 4;
  public static int[] dx = {1, 0, -1, 0};
  public static int[] dy = {0, 1, 0, -1};
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

  /**
   * Returns a line that is in the same direction but some pixels shorter
   * @param line the line to shorten
   * @param lengthenBy the amount to shorten by
   */
  public static Line2D.Float getLongerLine(Line2D line, float lengthenBy)
  {
    float magnitude = (float)line.getP1().distance(line.getP2());
    float angle = ((float) Math.atan2(line.getY2() - line.getY1(), line.getX2() - line.getX1()));
    float shortenedMagnitude = (magnitude + lengthenBy);
    return new Line2D.Float(line.getP1(), new Point2D.Float((float)(line.getX1() + shortenedMagnitude * Math.cos(angle)),
        (float)(line.getY1() + shortenedMagnitude * Math.sin(angle))));
  }
}
