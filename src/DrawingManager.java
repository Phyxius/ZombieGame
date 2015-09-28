import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Shea on 2015-09-17.
 * DrawingManager: Wraps EntityManager and exposes a subset of its methods.
 * Also contains a GraphicsConfiguration object for the use of the Entities.
 * For documentation about the contained methods, see EntityManager.
 */
public class DrawingManager
{
  private final EntityManager entityManager;
  private final GraphicsConfiguration graphicsConfiguration;

  /**
   * Creates a new DrawingManager wrapping the arguments.
   * @param entityManager the EntityManager to wrap
   * @param graphicsConfiguration the GraphicsConfiguration to wrap
   */
  public DrawingManager(EntityManager entityManager, GraphicsConfiguration graphicsConfiguration)
  {
    this.entityManager = entityManager;
    this.graphicsConfiguration = graphicsConfiguration;
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

  /**
   * @return the GraphicsConfiguration associated with the current canvas
   */
  public GraphicsConfiguration getGraphicsConfiguration()
  {
    return graphicsConfiguration;
  }
}
