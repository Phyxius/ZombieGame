import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-23.
 * Ash: Overlays a transparent black square over burned squares
 */
public class Ash extends Entity
{
  private final Rectangle2D.Float boundingBox;
  private final Point2D.Float position;

  /**
   * Creates a new Ash with the given bounding box
   * @param boundingBox the bounding box to use
   */
  public Ash(Rectangle2D.Float boundingBox)
  {
    this.boundingBox = boundingBox;
    position = new Point2D.Float(boundingBox.x, boundingBox.y);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return position;
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    return boundingBox;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.setColor(new Color(0, 0, 0, Settings.ashOpacity));
    local.fillRect(0, 0, ((int) boundingBox.width), ((int) boundingBox.height));
  }

  @Override
  public int getDepth()
  {
    return -10;
  }
}
