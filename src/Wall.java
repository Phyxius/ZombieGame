import java.awt.geom.Rectangle2D;

/**
 * Wall is an entity that is solid
 * and makes room for doorways
 * when it it called
 */
public class Wall extends Entity
{
  private Rectangle2D.Float box;
  private final House.Direction direction;
  private final int endX;
  private final int endY;
  private final int startX;
  private final int startY;

  public Wall(int startX, int endX, int startY, int endY, House.Direction direction)
  {
    this.direction = direction;
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    makeBoundingBox(startX, endX, startY, endY);
  }

  @Override
  public boolean blocksLight()
  {
    return true;
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    return box;
  }

  /**
   * @return Returns the side of the Room the wall is on.
   */
  public House.Direction getDirection()
  {
    return direction;
  }

  /**
   * @return Returns the end X coord.
   */
  public int getEndX()
  {
    return endX;
  }

  /**
   * @return Returns the end Y coord.
   */
  public int getEndY()
  {
    return endY;
  }

  /**
   * @return Returns the start X coord.
   */
  public int getStartX()
  {
    return startX;
  }

  /**
   * @return Returns the start Y coord.
   */
  public int getStartY()
  {
    return startY;
  }

  @Override
  public boolean isSolid()
  {
    return true;
  }

  /**
   * @param startX Start X coord of box.
   * @param endX   End X coord of box.
   * @param startY Start Y coord of box.
   * @param endY   End Y coord of box.
   */
  private void makeBoundingBox(int startX, int endX, int startY, int endY)
  {
    box = new Rectangle2D.Float(startX, startY, endX - startX, endY - startY);
  }
}
