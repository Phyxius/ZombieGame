import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 * Created by Shea on 2015-09-07.
 * UpdateManager: Wrapper class to allow Entities limited access to global game state
 * The methods in the class are a direct subset of the ones available in EntityManager; see that class
 * for documentation.
 */
public class UpdateManager
{
  private final EntityManager entityManager;

  /**
   * Creates a new UpdateManager wrapping the given EntityManager
   * @param entityManager the EntityManager to wrap
   */
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

  public void add(Collection<? extends Entity> entities)
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

  public Collection<Entity> getAllEntities(boolean includeUnprocessed)
  {
    return entityManager.getAllEntities(includeUnprocessed);
  }

  public Collection<Entity> getCollidingEntities(Shape boundingBox)
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

  public Point2D.Float getCameraOrigin()
  {
    return entityManager.getCameraOrigin();
  }

  public void setCameraOrigin(Point2D.Float cameraOrigin)
  {
    entityManager.setCameraOrigin(cameraOrigin);
  }

  public Entity getEntityToFollow()
  {
    return entityManager.getEntityToFollow();
  }

  public void setEntityToFollow(Entity entityToFollow)
  {
    entityManager.setEntityToFollow(entityToFollow);
  }
}
