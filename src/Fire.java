import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Fire extends Entity
{
  private Rectangle2D explosionArea;
  public Fire(Rectangle2D.Float explosionArea)
  {
    this.explosionArea = explosionArea;
  }

  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return (Rectangle2D.Float) explosionArea;
  }

  public int getDepth()
  {
    return 3;
  }

  public void draw(Graphics2D local, Graphics2D screen) //local = origin centered at upper-left corner of object, screen = origin at upper-left corner of screen
  {
    //Animate fire
    local.drawRect((int) explosionArea.getX(),(int) explosionArea.getY(),
                   (int) explosionArea.getWidth(),(int) explosionArea.getHeight());
    //Draw ash
  }
}
