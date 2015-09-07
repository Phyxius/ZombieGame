import java.util.HashMap;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class SettingsManager
{
  private final static HashMap<String, String> stringSettings = new HashMap<>();
  private final static HashMap<String, Float> floatSettings = new HashMap<>();
  private final static HashMap<String, Integer> integralSettings = new HashMap<>();

  static //defaults go here
  {
    set("framerate", 60);
    set("zombiespeed", 0.50f);
    set("zombiedecisionrate", 2.0f);
    set("zombiesmell", 7.0f);
    set("tileSize", 80);
  }

  public static void set(String name, float value)
  {
    floatSettings.put(name, value);
  }

  public static void set(String name, String value)
  {
    stringSettings.put(name, value);
  }

  public static void set(String name, int value)
  {
    integralSettings.put(name, value);
  }

  public static String getString(String name)
  {
    return stringSettings.getOrDefault(name, null);
  }

  public static Float getFloat(String name)
  {
    return floatSettings.getOrDefault(name, null);
  }

  public static Integer getInteger(String name)
  {
    return integralSettings.getOrDefault(name, null);
  }
}
