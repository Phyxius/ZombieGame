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

  LineZombie (Player player, float speed, float decisionRate, float smell, Point2D.Float position)
  {
    super (player, speed, decisionRate, smell, position);
  }

  @Override
  public void update (UpdateManager e)
  {
    if (updateCount % decisionRate == 0)
    {
      speed = 0.5f;
      if (playerPosition == null)
      {
        if (collision)
        {
          directionAngle += Math.PI/4 + Util.rng.nextDouble() * 1.5 * Math.PI;
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
        position.setLocation(lastX, lastY); // Cancel last move and no that it collided.
        collision = true;
      }
    }) ;
    // update collision status
    updateCount++;
  }

  @Override
  public void onCollision (Entity other, CollisionManager collisionManager)
  {
    collision = true;
    if (other instanceof Fire) collisionManager.remove(this);
  }
}
