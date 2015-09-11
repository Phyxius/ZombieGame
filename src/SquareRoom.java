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

    for(int i = startX; i < height; i++)
    {
      for(int j = startY; j < width; j++)
      {
        if(i == startX)
        {

        }
      }
    }
    return null;
  }
}
