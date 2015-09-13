import java.awt.geom.Point2D;

/**
 * Created by arirappaport on 9/11/15.
 */
public class Room extends HouseEntity
{
  int width, height;
  public Room(Point2D startPoint, int width, int height)
  {
    super(startPoint);
    tiles = new Tile[(int)startPoint.getY()+height][(int)startPoint.getX()+width];
    setWidth(width);
    setHeight(height);
    makeRoom();
  }

  private Tile[][] makeRoom()
  {
    int startX = (int) startPoint.getX();
    int startY = (int) startPoint.getY();

    for(int i = startY; i < getHeight()+startY; i++)
    {
      for(int j = startX; j < getWidth()+startX; j++)
      {
        if(i == startY || j == startX || i == getHeight()+startY-1 || j == getWidth()+startX-1) setTilesAt(i,j,new Tile("tileset/wall", true));
        else setTilesAt(i,j, new Tile("tileset/nonwall", false));
      }
    }
    return getTiles();
  }
}
