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
  public final int screenPixelWidth, screenPixelHeight;
  /**
   * the screen width and height, taking scale into account
   */
  public final float screenWidth, screenHeight;
  private final EntityManager entityManager;
  private final GraphicsConfiguration graphicsConfiguration;

  /**
   * Creates a new DrawingManager wrapping the arguments.
   *
   * @param entityManager         the EntityManager to wrap
   * @param graphicsConfiguration the GraphicsConfiguration to wrap
   * @param screenWidth           the screen width, taking window scale into account
   * @param screenHeight          the screen height the screen height, taking scale into account
   * @param screenPixelWidth
   * @param screenPixelHeight
   */
  public DrawingManager(EntityManager entityManager, GraphicsConfiguration graphicsConfiguration, float screenWidth,
                        float screenHeight, int screenPixelWidth, int screenPixelHeight)
  {
    this.entityManager = entityManager;
    this.graphicsConfiguration = graphicsConfiguration;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.screenPixelHeight = screenPixelHeight;
    this.screenPixelWidth = screenPixelWidth;
  }

  /**
   * Converts a given point from game local coordinates to screen coordinates, taking into account
   * camera origin and scale
   *
   * @param pos the Point2D.Float to convert
   * @return the converted value
   */
  public Point2D.Float gamePositionToScreenPosition(Point2D.Float pos)
  {
    return new Point2D.Float(gameXToScreenX(pos.x), gameYToScreenY(pos.y));
  }

  /**
   * Converts a given X value from game local coordinates to screen coordinates, taking into account
   * camera origin and scale
   *
   * @param x the x to convert
   * @return the converted value
   */
  public float gameXToScreenX(float x)
  {
    return (x - getCameraOrigin().x) / screenWidth * screenPixelWidth;
  }

  /**
   * Converts a given y value from game local coordinates to screen coordinates, taking into account
   * camera origin and scale
   *
   * @param y the y to convert
   * @return the converted value
   */
  public float gameYToScreenY(float y)
  {
    return (y - getCameraOrigin().y) / screenHeight * screenPixelHeight;
  }

  public Point2D.Float getCameraOrigin()
  {
    return entityManager.getCameraOrigin();
  }

  public Entity getEntityToFollow()
  {
    return entityManager.getEntityToFollow();
  }

  public void setEntityToFollow(Entity e)
  {
    entityManager.setEntityToFollow(e);
  }

  /**
   * @return the GraphicsConfiguration associated with the current canvas
   */
  public GraphicsConfiguration getGraphicsConfiguration()
  {
    return graphicsConfiguration;
  }

  /**
   * @return the current scale factor
   */
  public float getScale()
  {
    return entityManager.getScale();
  }
}
