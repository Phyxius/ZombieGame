/**
 * Created by Mohammad R. Yousefi on 07/09/15.
 * Basic model for the zombies in the Zombie House game.
 *
 * @param aStarWorked The success state of path finding.
 * @param bumpSound The bump audio for the zombie.
 * @param collision Collision state of the zombie object.
 * @param decisionRate The delay between each decision.
 * @param directionAngle The current direction of movement.
 * @param distanceFromPlayer The current distance from player.
 * @param audibleFootSteps Current audio playback state. Zombies play sound when they are audibleFootSteps.
 * @param master The master zombie that the zombie reports to when detecting the player.
 * @param minAngle Minimum change in heading upon collisions.
 * @param moving The movement state of the zombie.
 * @param player The player object that the zombie tracks.
 * @param playerPosition The last known location of the player.
 * @param position Location of the zombie.
 * @param smell Maximum detection distance for the zombie.
 * @param speed Movement speed of the zombie.
 * @param triedAStar Indicates whether path finding has beed attempted.
 * @param updateCount The number of update calls.
 * @param zombieStep The walking audio for the zombie.
 */

import java.awt.*;
import java.awt.geom.Point2D;

abstract class ZombieModel extends Entity implements Detonator
{
  protected final SoundEffect bumpSound = new SoundEffect("soundfx/bump.mp3");
  protected final Player player; // Reference to the player on the current board.
  protected final SoundEffect zombieStep = new SoundEffect("soundfx/zombiefoot.mp3");
  protected boolean aStarWorked;
  protected boolean audibleBump;
  protected boolean audibleFootSteps;
  protected boolean collision;
  protected int decisionRate; // New decision every decisionRate updates.
  protected double directionAngle; // 0 - 2 * PI
  protected double distanceFromPlayer;
  protected MasterZombie master;
  protected double minAngle;
  protected boolean moving = true;
  protected int numFailedAttempts = 0;
  protected Point2D.Float playerPosition = null; // if (!= null) player is within smell range.
  protected Point2D.Float position = new Point2D.Float(); // Top Left Corner.
  protected float smell; // smell radius = smell * tiles
  protected float speed; // displacement = speed * tiles
  protected boolean triedAStar;
  protected int updateCount = 0; // New decision when updateCount % (decisionRate * frameRate) == 0

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
   * Determines whether the object is solid.
   *
   * @return By default all zombies are solid.
   */
  @Override
  public boolean isSolid()
  {
    return true;
  }

  /**
   * Informs the zombie to react to a collision.
   *
   * @param other            The object that collides with this object.
   * @param collisionManager The manager for this collision.
   */
  @Override
  public void onCollision(Entity other, CollisionManager collisionManager)
  {
    if (other.isSolid())
    {
      collision = true;
      moving = false;
    }
    if (other instanceof Fire)
    {
      playerPosition = null;
      collisionManager.remove(this);
    }
  }

  public void setMasterZombie(MasterZombie master)
  {
    this.master = master;
  }

  protected void pathFinding()
  {
    // A* to player
    int tileSize = Settings.tileSize;
    Point zombieCenter = findCurrentlyOccupiedTile(numFailedAttempts);
    Point playerCenter = new Point((int) (playerPosition.getX() + tileSize / 2) / tileSize,
        (int) (playerPosition.getY() + tileSize / 2) / tileSize);
    Point pointToAimAt = House.calculateAStar(zombieCenter, playerCenter);
    if (pointToAimAt != null) directionAngle =
        Math.atan2((pointToAimAt.getY() - zombieCenter.getY()), (pointToAimAt.getX() - zombieCenter.getX()));
    moving = true;
    triedAStar = true;
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
      if (master != null) master.reportPlayer(playerPosition);
    }
    else
    {
      playerPosition = null;
    }
    audibleFootSteps = distanceFromPlayer <= Settings.playerHearing;
    audibleBump = distanceFromPlayer <= 2 * Settings.playerHearing;
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
  }
}
