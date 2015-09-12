import java.awt.geom.Point2D;

/**
 * Created by arirappaport on 9/10/15.
 */
public abstract class HouseEntity
{
  //X and Y of where the room starts
  //relative to rest of house
  public Point2D startPoint;
  //List of tiles associated with object
  private Entity[][] obstacles;
  private int width, height;
  public Tile[][] tiles;

  public HouseEntity(Point2D startPoint)
  {
    this.startPoint = startPoint;
  }

  public Point2D getStartPoint()
  {
    return startPoint;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public void setWidth(int width)
  {
    this.width = width;
  }

  public void setHeight(int height)
  {
    this.height = height;
  }

  public Tile[][] getTiles()
  {
    return tiles;
  }

  public void setTilesAt(int y, int x, Tile tile)
  {
    tiles[y][x] = tile;
  }

  public Tile getTileAt(int y, int x)
  {
    return tiles[y][x];
  }

  public Entity[][] getObstacles()
  {
    return obstacles;
  }
}
