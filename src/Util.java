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
  static final int NUM_DIRECTIONS = 8;
  static final int[] dx = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
  static final int[] dy = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
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
}
