import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by arirappaport on 9/11/15.
 */
public class SquareRoom extends RoomModel
{
  int width, height;
  public SquareRoom(Point2D startPoint)
  {
    super(startPoint);
    setWidth(8);
    setHeight(8);
    width = getWidth();
    height = getHeight();
    makeRoom(startPoint);
  }

  private Tile[][] makeRoom(Point2D startPoint)
  {
    int startX = (int) startPoint.getX();
    int startY = (int) startPoint.getY();

    for(int i = startY; i <= height; i++)
    {
      for(int j = startX; j <= width; j++)
      {
        if(i == startY)
        {
          if(j == startX) setTilesAt(j,i,new Tile("upperleft"));
          else if(j == width) setTilesAt(j,i,new Tile("upperright"));
          else setTilesAt(j,i,new Tile("top"));
        }
        else if(i == height)
        {
          if(j == startX) setTilesAt(j,i,new Tile("lowerleft"));
          else if(j == width) setTilesAt(j,i,new Tile("lowerright"));
          else setTilesAt(j,i,new Tile("down"));
        }
        else setTilesAt(j, i ,new Tile("center"));
      }
    }
    return getTiles();
  }
}
