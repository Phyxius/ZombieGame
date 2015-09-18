import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-17.
 * Class name and description go here.
 */
public class DrawingManager
{
  private final EntityManager entityManager;

  public DrawingManager(EntityManager entityManager)
  {
    this.entityManager = entityManager;
  }

  public Entity getEntityToFollow()
  {
    return entityManager.getEntityToFollow();
  }

  public void setEntityToFollow(Entity e)
  {
    entityManager.setEntityToFollow(e);
  }

  public Point2D.Float getCameraOrigin()
  {
    return entityManager.getCameraOrigin();
  }
}
