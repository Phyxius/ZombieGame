import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-13.
 * Class name and description go here.
 */
public class TestObstacle extends Entity
{
  private Rectangle2D.Float bounds;
  private Color color;

  public TestObstacle(Rectangle2D.Float bounds, Color color)
  {
    this.bounds = bounds;
    this.color = color;
  }

  @Override
  public boolean isSolid()
  {
    return true;
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

  public void setBounds(Rectangle2D.Float bounds)
  {
    bounds.setRect(bounds);
  }
}
