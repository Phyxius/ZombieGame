import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by arirappaport on 9/11/15.
 */
public class SquareRoom extends RoomModel
{
  private int width;
  private int height;
  public SquareRoom(Point2D startPoint)
  {
    width = 8;
    height = 8;
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
          if(j == startX) tiles[i][j] = new Tile("upperleft");
          else if(j == width) tiles[i][j] = new Tile("upperright");
          else tiles[i][j] = new Tile("top");
        }
        else if(i == height)
        {
          if(j == startX) tiles[i][j] = new Tile("lowerleft");
          else if(j == width) tiles[i][j] = new Tile("lowerright");
          else tiles[i][j] = new Tile("down");
        }
        else tiles[i][j] = new Tile("center");
      }
    }
    return tiles;
  }
}
