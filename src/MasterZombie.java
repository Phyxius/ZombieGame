import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * Created by Mohammad R. Yousefi on 9/27/2015.
 * A simple zombie that moves in straight lines. It can be informed of the location of player by other zombies.
 */
public class MasterZombie extends ZombieModel implements Detonator
{
  private Animation idleAnimation = new Animation("animation/zombie/idle_", 16, true);
  private Animation moveAnimation = new Animation("animation/zombie/move_", 16, true);
  private SoundEffect zombieStep = new SoundEffect("soundfx/zombiefoot.mp3");
  private int soundCounter = 0;
  private Point2D.Float reportedPosition;

  MasterZombie(Player player, Point2D.Float position)
  {
    super(player, position);
  }

  @Override
  public void update(UpdateManager e)
  {

    if (updateCount % decisionRate == 0)
    {
      detectPlayer();
      if (playerPosition == null)
      {
        if (collision)
        {
          directionAngle += minAngle + Util.rng.nextDouble() * 2 * (Math.PI - minAngle);
          directionAngle %= 2 * Math.PI;
          moving = true;
          collision = false;
        }
      }
      else
      {
        pathFinding();
      }
    }

    if (moving)
    {
      float lastX = position.x;
      float lastY = position.y;

      // Change Position
      float velocity = Util.tilesPerSecondToPixelsPerFrame(speed);
      position.setLocation((float) (lastX + Math.cos(directionAngle) * velocity),
          (float) (lastY + Math.sin(directionAngle) * velocity));
      aStarWorked = true;

      // Check for collisions after moving
      Collection<Entity> collisions = e.getCollidingEntities(this.getBoundingBox());
      collisions.forEach((Entity entity) -> {
        if (entity != this && entity.isSolid())
        {
          if (entity instanceof Fire)
          {
            e.remove(this);
            return;
          }
          if (triedAStar)
          {
            numFailedAttempts++;
            aStarWorked = false;
          }
          position.setLocation(lastX, lastY); // Cancel last move and no that it collided.
          collision = true;
          moving = false;
        }
      });
      if (aStarWorked) numFailedAttempts = 0;
    }

    if (!moving)
    {
      idleAnimation.nextFrame(moving);
      moving = false;
    }
    else
    {
      moveAnimation.nextFrame(!moving);
      moving = true;

      //double volume = 10;
      if (soundCounter % ((Settings.frameRate / 3) + 10) == 0 && loud)
      {
        double balance = (this.getPosition().x - player.getPosition().x) / Settings.tileSize;
        zombieStep.play(balance, 0.5 / distanceFromPlayer);
      }
    }
    updateCount++;
    reportedPosition = null;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    local.setColor(new Color(255, 0, 0, 50));
    local.fillOval(0, 0, (int) getBoundingBox().getWidth(), (int) getBoundingBox().getHeight());
    AffineTransform transformer = new AffineTransform();
    transformer.rotate(directionAngle, Settings.tileSize / 2,
        Settings.tileSize / 2); // must rotate first then scale otherwise it will cause a bug
    transformer.scale((double) Settings.tileSize / 80, (double) Settings.tileSize / 80);
    local.drawImage((moving ? moveAnimation.getFrame() : idleAnimation.getFrame()), transformer, null);
  }

  @Override
  public void onCollision(Entity other, CollisionManager collisionManager)
  {
    if (other.isSolid())
    {
      collision = true;
      moving = false;
    }
    if (other instanceof Fire) collisionManager.remove(this);
  }

  /**
   * Used by normal zombies to notify the master zombie of the player position.
   *
   * @param position current player position.
   */
  public void reportPlayer(Point2D.Float position)
  {
    reportedPosition = position;
  }

  @Override
  void detectPlayer()
  {
    super.detectPlayer();
    if (playerPosition == null) playerPosition = reportedPosition;
  }

  @Override
  public int getDepth()
  {
    return super.getDepth() + 100;
  }
}
