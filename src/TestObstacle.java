import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-13.
 * TestObstacle: Basic test obstacle that draws a filled
 * rectangle of a given color
 * isSolid() returns true.
 */
public class TestObstacle extends Entity
{
  private final Rectangle2D.Float bounds;

  /**
   * Creates a new test obstacle
   *
   * @param bounds the bounds of the obstacle
   * @param color  the color of the obstacle
   */
  public TestObstacle(Rectangle2D.Float bounds, Color color)
  {
    this.bounds = bounds;
    Color color1 = color;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.setColor(Color.red);
    local.fillRect(0, 0, (int) getBoundingBox().width, (int) getBoundingBox().height);
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    return bounds;
  }

  @Override
  public Point2D.Float getPosition()
  {
    return new Point2D.Float(bounds.x, bounds.y);
  }

  public void setPosition(Point2D.Float position)
  {
    bounds.x = position.x;
    bounds.y = position.y;
  }

  @Override
  public boolean isSolid()
  {
    return true;
  }
}
