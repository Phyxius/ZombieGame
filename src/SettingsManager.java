import java.util.HashMap;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class SettingsManager
{
  private final static HashMap<String, String> stringSettings = new HashMap<>();
  private final static HashMap<String, Float> numericSettings = new HashMap<>();

  static //defaults go here
  {
    set("framerate", 60);
  }

  public static void set(String name, float value)
  {
    numericSettings.put(name, value);
  }

  public static void set(String name, String value)
  {
    stringSettings.put(name, value);
  }

  public static String getString(String name)
  {
    return stringSettings.getOrDefault(name, null);
  }

  public static Float getFloat(String name)
  {
    return numericSettings.getOrDefault(name, null);
  }
}
