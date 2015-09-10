import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class UpdateManager
{
  private final EntityManager entityManager;

  public UpdateManager(EntityManager entityManager)
  {
    this.entityManager = entityManager;
  }

  public void add(Entity e)
  {
    entityManager.add(e);
  }

  public void add(Entity[] entities)
  {
    entityManager.add(entities);
  }

  public void add(Collection<Entity> entities)
  {
    entityManager.add(entities);
  }

  public void remove(Entity e)
  {
    entityManager.remove(e);
  }

  public void remove(Entity[] entities)
  {
    entityManager.remove(entities);
  }

  public void remove(Collection<Entity> entities)
  {
    entityManager.remove(entities);
  }

  public Collection<Entity> getAllEntities()
  {
    return entityManager.getAllEntities();
  }

  public Collection<Entity> getCollidingEntities(Rectangle2D.Float boundingBox)
  {
    return entityManager.getCollidingEntities(boundingBox);
  }

  public Collection<Entity> getIntersectingEntities(Point2D.Float point)
  {
    return entityManager.getIntersectingEntities(point);
  }

  public boolean isKeyPressed(int keyCode)
  {
    return entityManager.isKeyPressed(keyCode);
  }
}
