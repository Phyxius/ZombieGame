import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by arirappaport on 9/13/15.
 */
public class Hallway extends HouseEntity
{
  public Hallway(Point2D startPoint)
  {
    super(startPoint);
  }

  public Hallway(Point2D startPoint, int width, int height)
  {
    super(startPoint);
    tiles = new Tile[(int)startPoint.getY()+height][(int)startPoint.getX()+width];
    setWidth(width);
    setHeight(height);
    makeHallway();
  }

  private Tile[][] makeHallway()
  {
    int startX = (int) startPoint.getX();
    int startY = (int) startPoint.getY();

    for(int i = startY; i < getHeight()+startY; i++)
    {
      for(int j = startX; j < getWidth()+startX; j++)
      {
        if(i == startY || j == startX || i == getHeight()+startY-1 || j == getWidth()+startX-1) setTilesAt(i,j,new Tile("tileset/wall", false));
        else setTilesAt(i,j, new Tile("tileset/nonwall", true));
      }
    }
    return getTiles();
  }

}
