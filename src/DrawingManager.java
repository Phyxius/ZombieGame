import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Shea on 2015-09-17.
 * Class name and description go here.
 */
public class DrawingManager
{
  private final EntityManager entityManager;
  private final GraphicsConfiguration graphicsConfiguration;

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

  public GraphicsConfiguration getGraphicsConfiguration()
  {
    return graphicsConfiguration;
  }
}
