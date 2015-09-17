import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Rashid on 07/09/15.
 * The Template for zombies.
 */

abstract class ZombieModel extends Entity
{
  protected float speed; // displacement = speed * tiles
  protected int decisionRate; // New decision every decisionRate updates.
  protected float smell; // smell radius = smell * tiles
  protected int updateCount = 0; // Used in decision making. New decision when updateCount % (decitionRate * frameRate) == 0
  protected Player player; // Reference to the player on the current board.
  protected Point2D.Float playerPosition = null; // if (!= null) player is within smell range.
  protected Point2D.Float position = new Point2D.Float(); // Top Left Corner.
  protected double directionAngle; // 0 - 2 * PI
  protected boolean collision;
  protected double minAngle;

  ZombieModel (Player player, Point2D.Float position)
  {
    speed = Settings.zombieSpeed;
    decisionRate = (int) (Settings.zombieDecisionRate * Settings.frameRate);
    smell = Settings.zombieSmellRadius;
    this.position.setLocation(position.getX(), position.getY());
    this.player = player;
    this.minAngle = Settings.minAngle;
    this.directionAngle = 2 * Util.rng.nextDouble() * Math.PI;
  }

  ZombieModel (Player player, float speed, float decisionRate, float smell, Point2D.Float position, double minAngle)
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
    local.drawRoundRect(0, 0, tileSize, tileSize, tileSize/10, tileSize/10);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  void detectPlayer()
  {
    Rectangle2D.Float playerBox = player.getBoundingBox();
    Rectangle2D.Float zombieBox = this.getBoundingBox();
    Point2D.Double playerCenter = new Point2D.Double(playerBox.getCenterX(), playerBox.getCenterY());
    Point2D.Double zombieCenter = new Point2D.Double(zombieBox.getCenterX(), zombieBox.getCenterY());

    if (playerCenter.distance(zombieCenter) <= smell)
    {
      playerPosition = new Point2D.Float((float) playerCenter.x, (float) playerCenter.y);
    }
    else
    {
      playerPosition = null;
    }
  }

  @Override
  public boolean isSolid()
  {
    return true;
  }
}
