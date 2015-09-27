import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Set;

/**
 * Created by Mohammad R. Yousefi on 07/09/15.
 * Basic model for the zombies in the Zombie House game.
 */

abstract class ZombieModel extends Entity implements Detonator
{
  protected float speed; // displacement = speed * tiles
  protected int decisionRate; // New decision every decisionRate updates.
  protected float smell; // smell radius = smell * tiles
  protected int updateCount = 0; // New decision when updateCount % (decisionRate * frameRate) == 0
  protected int numFailedAttempts = 0;
  protected boolean triedAStar;
  protected boolean aStarWorked;
  protected final Player player; // Reference to the player on the current board.
  protected Point2D.Float playerPosition = null; // if (!= null) player is within smell range.
  protected Point2D.Float position = new Point2D.Float(); // Top Left Corner.
  protected double directionAngle; // 0 - 2 * PI
  protected boolean collision;
  protected double minAngle;
  protected boolean loud;
  protected double distanceFromPlayer;
  protected boolean moving = true;
  /**
   * Constructs a zombie using default values in Settings.
   *
   * @param player   The player associated with this zombie. Used in detectPlayer.
   * @param position The initial location of the zombie.
   */
  ZombieModel(Player player, Point2D.Float position)
  {
    speed = Settings.zombieSpeed;
    decisionRate = (int) (Settings.zombieDecisionRate * Settings.frameRate);
    smell = Settings.zombieSmellRadius;
    this.position.setLocation(position.getX(), position.getY());
    this.player = player;
    this.minAngle = Settings.minAngle;
    this.directionAngle = 2 * Util.rng.nextDouble() * Math.PI;
  }

  /**
   * Constructs a zombie using given parameters.
   *
   * @param player       The player associated with this zombie. Used in detectPlayer.
   * @param position     The initial location of the zombie.
   * @param speed        The movement speed of the zombie.
   * @param decisionRate The time between each decision a zombie makes in seconds.
   * @param smell        The radius of the area in which a zombie can detect a player.
   * @param minAngle     The minimum angle, in radians, a zombie turns when colliding with solid objects.
   */
  ZombieModel(Player player, Point2D.Float position, float speed, float decisionRate, float smell, double minAngle)
  {
    this(player, position);
    this.speed = speed;
    this.decisionRate = (int) (decisionRate * Settings.frameRate);
    this.smell = smell;
    this.minAngle = minAngle;
    this.directionAngle = 2 * Util.rng.nextDouble() * Math.PI;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    int tileSize = Settings.tileSize;
    local.setColor(Color.GREEN);
    local.drawRoundRect(0, 0, tileSize, tileSize, tileSize / 10, tileSize / 10);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  /**
   * Determines whether the zombie detects the player.
   */
  void detectPlayer()
  {
    distanceFromPlayer = position.distance(player.getPosition()) / Settings.tileSize;
    if (distanceFromPlayer <= smell)
    {
      playerPosition =
          new Point2D.Float((float) player.getBoundingBox().getCenterX(), (float) player.getBoundingBox().getCenterY());
    }
    else
    {
      playerPosition = null;
    }
    loud = distanceFromPlayer <= Settings.playerHearing;
  }

  Point findCurrentlyOccupiedTile(int numFailedAttempts)
  {
    int tileSize = Settings.tileSize;
    int upperLeftX = (int) getPosition().getX();
    int upperLeftY = (int) getPosition().getY();
    if (numFailedAttempts == 0)
      return new Point((upperLeftX + (tileSize / 2)) / tileSize, (upperLeftY + (tileSize / 2)) / tileSize);
    if (numFailedAttempts == 1) return new Point(upperLeftX / tileSize, upperLeftY / tileSize);
    if (numFailedAttempts == 2) return new Point((upperLeftX + tileSize) / tileSize, upperLeftY / tileSize);
    if (numFailedAttempts == 3) return new Point(upperLeftX / tileSize, (upperLeftY + tileSize) / tileSize);
    if (numFailedAttempts == 4)
      return new Point((upperLeftX + tileSize) / tileSize, (upperLeftY + tileSize) / tileSize);
    else return null;
    //int varX = (int) getPosition().getX();
    //if(varX % Settings.tileSize != 0) varX += Settings.tileSize;
    //int varY = (int) getPosition().getY()+Settings.tileSize;
    //if(varY % Settings.tileSize != 0) varY += Settings.tileSize;
    //while(varX % Settings.tileSize != 0) varX--;
    //while(varY % Settings.tileSize != 0) varY--;
    //return new Point(varX/Settings.tileSize, varY/Settings.tileSize);
  }

  /**
   * Determines whether the object is solid.
   *
   * @return By default all zombies are solid.
   */
  @Override
  public boolean isSolid()
  {
    return true;
  }

  protected void pathFinding()
  {
    // A* to player
    int tileSize = Settings.tileSize;
    Point zombieCenter = findCurrentlyOccupiedTile(numFailedAttempts);
    Point playerCenter = new Point((int)(playerPosition.getX()+tileSize/2)/tileSize, (int)(playerPosition.getY()+tileSize/2)/tileSize);
    Point pointToAimAt = House.calculateAStar(zombieCenter, playerCenter);
//        if(pointToAimAt.getY()-zombieCenter.getY() > 0) directionAngle = (Math.PI/2);
//        else if(pointToAimAt.getY()-zombieCenter.getY() < 0) directionAngle = -(Math.PI/2);
//        else if(pointToAimAt.getX()-zombieCenter.getX() > 0) directionAngle = 0;
//        else if(pointToAimAt.getX()-zombieCenter.getX() < 0) directionAngle = Math.PI;
    directionAngle = Math.atan2((pointToAimAt.getY()-zombieCenter.getY()),(pointToAimAt.getX()-zombieCenter.getX()));
    moving = true;
    triedAStar = true;
  }
}
