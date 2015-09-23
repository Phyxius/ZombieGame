import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Shea on 2015-09-23.
 * Class name and description go here.
 */
public class Obstacle extends Entity
{
  private final BufferedImage sprite;
  private final Point2D.Float position;
  private final Rectangle2D.Float boundingBox;

  public Obstacle(BufferedImage sprite, Rectangle2D.Float boundingBox)
  {
    this.boundingBox = boundingBox;
    this.sprite = sprite;
    position = new Point2D.Float(boundingBox.x, boundingBox.y);
  }

  public Obstacle(BufferedImage sprite, Point2D.Float position)
  {
    this(sprite, new Rectangle2D.Float());
  }

  @Override
  public Point2D.Float getPosition()
  {
    return position;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.drawImage(sprite, 0, 0, null);
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    return boundingBox;
  }

  @Override
  public boolean isSolid()
  {
    return true;
  }
}
