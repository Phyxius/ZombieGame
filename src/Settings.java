/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class Settings
{
  static final int ashOpacity = 64;
  static final int DEFAULT_TILE_SIZE = 80;
  public static int frameRate; //frames/sec
  public static float zombieSpeed = 0.5f; //tiles/sec
  public static float zombieDecisionRate = 2.0f; //sec
  public static float zombieSmellRadius = 7f; //tiles
  public static int tileSize; //pixels
  public static float playerWalk; // pixels/sec
  public static float playerRun; // pixels/sec
  public static float playerStamina; // sec * frameRate
  public static float playerHearing;
  public static float playerStaminaRegen;
  public static double minAngle; // in Radians
  public static int numObjectsGuranteed; //Rooms + hallways
  public static int fireDuration; //frames
  public static int playerSightRadius; //pixels

  static //ensures correct order of assignment
  {
    tileSize = 80;
    frameRate = 60;
    numObjectsGuranteed = 11;
    playerWalk = Util.tilesPerSecondToPixelsPerFrame(1);
    playerRun = Util.tilesPerSecondToPixelsPerFrame(2);
    playerStamina = 5f * Settings.frameRate;
    playerStaminaRegen = 0.20f * Settings.frameRate;
    minAngle = 0.25 * Math.PI;
    fireDuration = 15 * frameRate;
    playerHearing = 10;
    playerSightRadius = 5 * tileSize;
  }
}
