import java.awt.geom.Rectangle2D;

/**
 * Created by arirappaport on 9/13/15.
 */
public class Wall extends Entity
{
  Rectangle2D.Float box;
  public Wall(int startX, int endX,int startY, int endY)
  {
    makeBoundingBox(startX, endX, startY, endY);
  }

  @Override
  public boolean isSolid()
  {
    return true;
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    return box;
  }

  private void makeBoundingBox(int startX, int endX,int startY, int endY)
  {
    box = new Rectangle2D.Float(startX, endX, startY, endY);
  }
}
