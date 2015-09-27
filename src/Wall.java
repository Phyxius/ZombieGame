import java.awt.geom.Rectangle2D;

/**
 * Created by arirappaport on 9/13/15.
 */
public class Wall extends Entity
{
  Rectangle2D.Float box;
  private House.Direction direction;
  private int startX, startY;
  private int endX, endY;
  public Wall(int startX, int endX,int startY, int endY, House.Direction direction)
  {
    this.direction = direction;
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
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
    box = new Rectangle2D.Float(startX, startY, endX-startX, endY-startY);
  }

  public House.Direction getDirection()
  {
    return direction;
  }

  public int getStartX()
  {
    return startX;
  }

  public int getStartY()
  {
    return startY;
  }

  public int getEndX()
  {
    return endX;
  }

  public int getEndY()
  {
    return endY;
  }

  @Override
  public boolean blocksLight() {
    return true;
  }
}
