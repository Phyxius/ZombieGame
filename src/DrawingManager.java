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
   * the screen width and height, taking scale into account
   */
  public final float screenWidth, screenHeight;

  /**
   * Creates a new DrawingManager wrapping the arguments.
   * @param entityManager the EntityManager to wrap
   * @param graphicsConfiguration the GraphicsConfiguration to wrap
   * @param screenWidth the screen width, taking window scale into account
   * @param screenHeight the screen height the screen height, taking scale into account
   */
  public DrawingManager(EntityManager entityManager, GraphicsConfiguration graphicsConfiguration, float screenWidth, float screenHeight)
  {
    this.entityManager = entityManager;
    this.graphicsConfiguration = graphicsConfiguration;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
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
