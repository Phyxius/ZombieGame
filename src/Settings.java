/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
class Settings
{
  static final int DEFAULT_TILE_SIZE = 80;
  static final int ashOpacity = 64;
  public static int distanceAwayExit = 30; //tiles
  public static int fireDuration; //frames
  public static int frameRate; //frames/sec
  public static double minAngle; // in Radians
  public static final int numObjectsGuranteed; //Rooms + hallways
  public static float playerHearing;
  public static float playerRun; // pixels/sec
  public static int playerSightRadius; //pixels
  public static float playerStamina; // sec * frameRate
  public static float playerStaminaRegen;
  public static int playerTraps = 1;
  public static float playerWalk; // pixels/sec
  public static int tileSize; //pixels
  public static float trapSpawnRate = 0.01f;
  public static float zombieDecisionRate = 2.0f; //sec
  public static float zombieSmellRadius = 7f; //tiles
  public static float zombieSpawnRate = 0.01f;
  public static float zombieSpeed = 0.5f; //tiles/sec

  static //ensures correct order of assignment
  {
    tileSize = 80;
    frameRate = 60;
    numObjectsGuranteed = 11;
    playerWalk = Util.tilesPerSecondToPixelsPerFrame(1);
    playerRun = Util.tilesPerSecondToPixelsPerFrame(2);
    playerStamina = 5f * Settings.frameRate;
    playerStaminaRegen = 0.20f;
    minAngle = 0.25 * Math.PI;
    fireDuration = 15 * frameRate;
    playerHearing = 10;
    playerSightRadius = 5 * tileSize;
  }

  public static void updateFrameRate(int value)
  {
    float pStamina = playerStamina / Settings.frameRate;
    float pRegen = playerStaminaRegen / Settings.frameRate;
    float pWalk = playerWalk * Settings.frameRate / Settings.tileSize;
    float pRun = playerRun * Settings.frameRate / Settings.tileSize;
    int fDuration = fireDuration / Settings.frameRate;
    Settings.frameRate = value;
    playerStamina = pStamina * Settings.frameRate;
    playerStaminaRegen = pRegen * Settings.frameRate;
    fireDuration = fDuration * Settings.frameRate;
    playerWalk = Util.tilesPerSecondToPixelsPerFrame(pWalk);
    playerRun = Util.tilesPerSecondToPixelsPerFrame(pRun);
  }

  public static void updateTileSize(int value)
  {
    float pWalk = playerWalk * Settings.frameRate / Settings.tileSize;
    float pRun = playerRun * Settings.frameRate / Settings.tileSize;
    int pSight = playerSightRadius / Settings.tileSize;
    Settings.tileSize = value;
    playerWalk = Util.tilesPerSecondToPixelsPerFrame(pWalk);
    playerRun = Util.tilesPerSecondToPixelsPerFrame(pRun);
    playerSightRadius = pSight * Settings.tileSize;
  }
}
