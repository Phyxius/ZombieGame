/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class Settings
{
  static final int ashOpacity = 64;
  public static int frameRate; //frames/sec
  public static float zombieSpeed = 0.5f; //tiles/sec
  public static float zombieDecisionRate = 2.0f; //sec
  public static float zombieSmellRadius = 7f; //tiles
  public static int tileSize; //pixels
  public static float playerWalk; // pixels/sec
  public static float playerRun; // pixels/sec
  public static float playerStamina; // sec * frameRate
  public static double minAngle; // in Radians
  public static int fireDuration; //frames

  static //ensures correct order of assignment
  {
    tileSize = 10;
    frameRate = 60;
    playerWalk = Util.tilesPerSecondToPixelsPerFrame(1);
    playerRun = Util.tilesPerSecondToPixelsPerFrame(2);
    playerStamina = 5 * Settings.frameRate;
    minAngle = 0.25 * Math.PI;
    fireDuration = 15 * frameRate;
  }
}
