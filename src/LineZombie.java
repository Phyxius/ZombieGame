import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * Created by Rashid on 07/09/15.
 * The Generic template for the zombies in the game.
 */
class LineZombie extends ZombieModel
{
  private Animation idleAnimation = new Animation("animation/zombie/idle_", 16);
  private Animation moveAnimation = new Animation("animation/zombie/move_", 16);
  private boolean moving = false;

  LineZombie (Player player, Point2D.Float position)
  {
    super(player, position);
    frame = idleAnimation.nextFrame(true);
  }

  LineZombie (Player player, float speed, float decisionRate, float smell, Point2D.Float position, double minAngle)
  {
    super (player, speed, decisionRate, smell, position, minAngle);
    frame = idleAnimation.nextFrame(true);
  }

  @Override
  public void update (UpdateManager e)
  {
    float speed = this.speed;
    if (updateCount % decisionRate == 0)
    {

      if (playerPosition == null)
      {
        if (collision)
        {
          directionAngle += Math.PI * minAngle + Util.rng.nextDouble() * 2 * (1 - minAngle) * Math.PI;
        }
      }
      else
      {
        // A* to player
      }
    }
    else
    {
       if (collision) speed = 0;
    }
    float lastX = position.x;
    float lastY = position.y;

    // Change Position
    position.setLocation((float) (lastX + Math.cos(directionAngle) * speed * Settings.tileSize),(float)(lastY + Math.sin(directionAngle) * speed * Settings.tileSize));

    // Check for collisions after moving
    Collection<Entity> collisions = e.getCollidingEntities(this.getBoundingBox());
    collisions.forEach((Entity entity) -> {
      if (entity.isSolid())
      {
        if (entity instanceof Fire)
        {
          e.remove(this);
          return;
        }
        position.setLocation(lastX, lastY); // Cancel last move and no that it collided.
        collision = true;
      }
    });

    if (speed == 0)
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
  public void draw (Graphics2D local, Graphics2D global)
  {
    AffineTransform transformer = new AffineTransform();
    transformer.rotate(directionAngle);
    local.drawImage((speed == 0 ? idleAnimation.getFrame() : moveAnimation.getFrame()), transformer,null);
  }

  @Override
  public void onCollision (Entity other, CollisionManager collisionManager)
  {
    collision = true;
    if (other instanceof Fire) collisionManager.remove(this);
  }
}
