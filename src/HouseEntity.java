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
  public Tile[][] tiles;
  public Entity[][] obstacles;
}
