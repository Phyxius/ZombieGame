/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class Util
{
  static float secondsToFrames(float seconds)
  {
    return seconds / SettingsManager.getFloat("framerate");
  }
}
