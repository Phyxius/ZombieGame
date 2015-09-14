import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by arirappaport on 9/11/15.
 */
public class Room extends HouseEntity
{
  private EntityManager entityManager;
  public Room(Point2D startPoint, int width, int height, EntityManager entityManager)
  {
    super(startPoint);
    this.entityManager = entityManager;
    tiles = new Tile[(int)startPoint.getY()+height][(int)startPoint.getX()+width];
    setWidth(width);
    setHeight(height);
    makeRoom();
    makeWalls();
  }

  private Tile[][] makeRoom()
  {
    int startX = (int) startPoint.getX();
    int startY = (int) startPoint.getY();

    for(int i = startY; i < getHeight()+startY; i++)
    {
      for(int j = startX; j < getWidth()+startX; j++)
      {
        if(i == startY || j == startX || i == getHeight()+startY-1 || j == getWidth()+startX-1)
        {
          setTilesAt(i,j,new Tile("tileset/wall", true));
        }
        else setTilesAt(i,j, new Tile("tileset/nonwall", false));
      }
    }
    return getTiles();
  }

  private void makeWalls()
  {
    int tileSize = Settings.tileSize;
    int startX = (int) startPoint.getX()*tileSize;
    int startY = (int) startPoint.getY()*tileSize;
    int endX = startX+(getWidth()-1)*tileSize;
    int endY = startY+(getHeight()-1)*tileSize;

    Wall wall1 = new Wall(startX,startX + tileSize,startY, endY);
    Wall wall2 = new Wall(startX,endX,startY, startY + tileSize);
    Wall wall3 = new Wall(endX,endX + tileSize,startY,endY);
    Wall wall4 = new Wall(startX,endX,endY,endY+tileSize);
    entityManager.add(wall1);
    entityManager.add(wall2);
    entityManager.add(wall3);
    entityManager.add(wall4);
  }
}
