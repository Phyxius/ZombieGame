import java.util.Random;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class Util
{
  static final Random rng = new Random(System.currentTimeMillis());
  static float secondsToFrames(float seconds)
  {
    return seconds / Settings.frameRate;
  }
  static float tilesPerSecondToPixelsPerFrame(float tilesPerSec)
  {
    return tilesPerSec * Settings.tileSize / Settings.frameRate;
  }
}
