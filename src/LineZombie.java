import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * Created by Rashid on 07/09/15.
 * The Generic template for the zombies in the game.
 */
class LineZombie extends ZombieModel
{
  LineZombie (Player player, Point2D.Float position)
  {
    super(player, position);
  }

  LineZombie (Player player, float speed, float decisionRate, float smell, Point2D.Float position, double minAngle)
  {
    super (player, speed, decisionRate, smell, position, minAngle);
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
    updateCount++;
  }

  @Override
  public void onCollision (Entity other, CollisionManager collisionManager)
  {
    collision = true;
    if (other instanceof Fire) collisionManager.remove(this);
  }
}
