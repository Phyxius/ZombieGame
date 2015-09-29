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
  private final Rectangle2D.Float boundingBox;
  private final Point2D.Float position;
  private final BufferedImage sprite;

  /**
   * Makes a generic obstacle.
   *
   * @param sprite      The image of the obstacle.
   * @param boundingBox The bounding box of the obstacle.
   */
  Obstacle(BufferedImage sprite, Rectangle2D.Float boundingBox)
  {
    this.boundingBox = boundingBox;
    this.sprite = sprite;
    position = new Point2D.Float(boundingBox.x, boundingBox.y);
  }

  /**
   * Alternate constructor, calls other constructor.
   *
   * @param sprite   The image of the obstacle.
   * @param position The position of the upper left
   *                 corner of the bounding box.
   */
  public Obstacle(BufferedImage sprite, Point2D.Float position)
  {
    this(sprite, new Rectangle2D.Float());
  }

  @Override
  public boolean blocksLight()
  {
    return true;
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
  public Point2D.Float getPosition()
  {
    return position;
  }

  @Override
  public boolean isSolid()
  {
    return true;
  }

  @Override
  public void onCollision(Entity other, CollisionManager c)
  {
    if (other instanceof Fire)
    {
      c.remove(this);
      c.add(new Fire(getBoundingBox(), true));
    }
  }
}
