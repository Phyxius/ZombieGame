/**
 * Created by arirappaport on 9/7/15.
 * Trap objects explode upon contact with Detonators with active trigger.
 *
 * @see Detonator
 */

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Trap extends Entity
{
  private Point2D.Float position;
  private final int tileSize;
  private final int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1}; // The Center is a special case
  private final int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1}; // The Center is a special case
  private final BufferedImage trapImg;

  /**
   * Makes a Trap at the specified position
   *
   * @param position The Location of the trap.
   */
  public Trap(Point2D.Float position)
  {
    this.position = position;
    tileSize = Settings.tileSize;
    trapImg = ResourceManager.getImage("fire/firetrap.png");
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.drawImage(trapImg, 0, 0, tileSize, tileSize, null);
  }

  @Override
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return super.getBoundingBox();
  }

  @Override
  public int getDepth()
  {
    return 0;
  }

  @Override
  public Point2D.Float getPosition() //returns center point of object
  {
    return position;
  }

  /**
   * Sets the position of the specified trap.
   *
   * @param position The new position.
   */
  public void setPosition(Point2D.Float position)
  {
    this.position = position;
  }

  @Override
  public boolean isSolid() //solid objects cannot move into each other
  {
    return false;
  }

  @Override
  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {
    if (other instanceof Detonator && ((Detonator) other).trigger())
    {
      detonate(c);
      c.remove(this);
    }
  }

  private void detonate(CollisionManager c)
  {
    Rectangle2D.Float explosionArea = new Rectangle2D.Float(position.x, position.y, tileSize, tileSize);
    c.add(new Fire(explosionArea, true));
    for (int i = 0; i < dx.length; i++)
    {
      int curDx = dx[i];
      int curDy = dy[i];
      explosionArea =
          new Rectangle2D.Float(position.x + tileSize * curDx, position.y + tileSize * curDy, tileSize, tileSize);
      c.add(new Fire(explosionArea, false));
    }
    //Rectangle2D.Float explosionArea = new Rectangle2D.Float(position.x-tileSize, position.y-tileSize, tileSize*3, tileSize*3);
    //entityManager.add(new Fire(explosionArea));

  }
}
