/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class Settings
{
  public static int frameRate; //frames/sec
  public static int floorDepth = -1;
  public static int playerDepth = 1;
  public static int zombieDepth = 2;
  public static float zombieSpeed = 0.5f; //tiles/sec
  public static float zombieDecisionRate = 2.0f; //sec
  public static float zombieSmellRadius = 7f; //tiles
  public static int tileSize; //pixels
  public static float playerWalk; // pixels/sec
  public static float playerRun; // pixels/sec
  public static float playerStamina; // sec * frameRate
  public static double minAngle; // minimumTurnAngle = Math.PI * minAngle

  static
  {
    tileSize = 10;
    frameRate = 60;
    playerWalk = Util.tilesPerSecondToPixelsPerFrame(1);
    playerRun = Util.tilesPerSecondToPixelsPerFrame(2);
    playerStamina = 5 * Settings.frameRate;
    minAngle = 0.25;
  }
}
