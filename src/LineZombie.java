import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
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
      if (playerPosition == null)
      {
        if (collision)
        {

        }
      }
      else
      {

      }
    }
    else
    {

    }
    // update collision status
    updateCount++;
  }

  @Override
  public void onCollision (Entity other, CollisionManager collisionManager)
  {
    collision = true;
//    if (other instanceof Fire) collisionManager.remove(this);
  }
}
