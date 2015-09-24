import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created by Rashid on 07/09/15.
 * The Generic template for the zombies in the game.
 */
class LineZombie extends ZombieModel implements Detonator
{
  private Animation idleAnimation = new Animation("animation/zombie/idle_", 16, true);
  private Animation moveAnimation = new Animation("animation/zombie/move_", 16, true);
  private boolean moving = true;

  LineZombie(Player player, Point2D.Float position)
  {
    super(player, position);
  }

  LineZombie(Player player, float speed, float decisionRate, float smell, Point2D.Float position, double minAngle)
  {
    super(player, speed, decisionRate, smell, position, minAngle);
  }

  @Override
  public void update(UpdateManager e)
  {
    if (updateCount % decisionRate == 0)
    {
      if (playerPosition == null)
      {
        if (collision)
        {
          directionAngle += Math.PI * minAngle + Util.rng.nextDouble() * 2 * (1 - minAngle) * Math.PI;
          directionAngle %= 2 * Math.PI;
          moving = true;
          collision = false;
        }
      }
      else
      {
        // A* to player
      }
    }

    if (moving)
    {
      float lastX = position.x;
      float lastY = position.y;

      // Change Position
      float velocity = Util.tilesPerSecondToPixelsPerFrame(speed);
      position.setLocation((float) (lastX + Math.cos(directionAngle) * velocity), (float) (lastY + Math.sin(directionAngle) * velocity));

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
          position.setLocation(lastX, lastY); // Cancel last move and no that it collided.
          collision = true;
          moving = false;
        }
      });
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
    }

    updateCount++;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    AffineTransform transformer = new AffineTransform();
    transformer.rotate(directionAngle, Settings.tileSize /2, Settings.tileSize /2); // must rotate first then scale otherwise it will cause a bug
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

  @Override
  public int getDepth()
  {
    return 100;
  }
}
