import java.awt.geom.Point2D;

/**
 * Created by arirappaport on 9/11/15.
 */
public class Room
{
  private EntityManager entityManager;
  private Entity[][] obstacles;
  private int width, height;
  private int startX, startY;
  private Tile[][] tiles;
  private boolean[][] doorways;

  public Room(int startX, int startY, int width, int height, Point2D.Float[] doorWayLocations, EntityManager entityManager)
  {
    this.entityManager = entityManager;
    tiles = new Tile[startY+height][startX+width];
    doorways = new boolean[startY+height][startX+width];
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;

    for (int i = 0; i < doorWayLocations.length; i++)
    {
      int x = (int)doorWayLocations[i].x;
      int y = (int)doorWayLocations[i].y;
      doorways[y][x] = true;

    }
    makeWalls();
    makeRoom();
  }

  public void setTileAt(int y, int x, Tile tile) {tiles[y][x] = tile;}
  public Tile getTileAt(int y, int x){return tiles[y][x];}
  public int getStartX(){return startX;}
  public int getStartY(){return startY;}
  public int getWidth(){return width;}
  public int getHeight(){return height;}

  private Tile[][] makeRoom()
  {
    for(int i = startY; i < height+startY; i++)
    {
      for(int j = startX; j < width+startX; j++)
      {
        if( i == startY || j == startX || i == height+startY-1 || j == width+startX-1)
        {
          if(!doorways[i][j]) setTileAt(i,j,new Tile("tileset/wall", true));
          else setTileAt(i,j, new Tile("tileset/nonwall", false));
        }
        else setTileAt(i,j, new Tile("tileset/nonwall", false));
      }
    }
    return tiles;
  }

  //private void makeWall(int startX, int startY, int i, int end, int tmpStart, int tmpEnd)
  //{
  //  int tileSize = Settings.tileSize;
  //  if(doorways[startY][startX])
  //   {
  //     tmpEnd = (i)*tileSize;
  //     Wall northWall = new Wall(tmpStart, tmpEnd, tmpStart, tmpStart+tileSize);
  //     entityManager.add(northWall);
  //     tmpStart = (i+1)*tileSize;
  //   }
  //   if(i == end)
  //   {
  //     Wall northWall = new Wall(tmpStart, (end+1)*tileSize, tmpStart, tmpStart+tileSize);
  //     entityManager.add(northWall);
  //   }
  //}

  //Make walls, taking into account
  //the doorways
  private void makeWalls()
  {
    int tileSize = Settings.tileSize;
    int endX = startX+(width-1);
    int endY = startY+(height-1);
    int tmpStartX = startX*tileSize;
    int tmpStartY = startY*tileSize;
    int tmpEndY;
    int tmpEndX;


    for (int i = startX; i <= endX; i++)
    {
      if(doorways[startY][i])
      {
        tmpEndX = (i)*tileSize;
        Wall northWall = new Wall(tmpStartX, tmpEndX, tmpStartY, tmpStartY+tileSize);
        entityManager.add(northWall);
        tmpStartX = (i+1)*tileSize;
      }
      if(i == endX)
      {
        Wall northWall = new Wall(tmpStartX, (endX+1)*tileSize, tmpStartY, tmpStartY+tileSize);
        entityManager.add(northWall);
      }
    }
    tmpStartX = startX*tileSize;

    for (int i = startX; i <= endX; i++)
    {
      if(doorways[endY][i])
      {
        tmpEndX = (i)*tileSize;
        Wall southWall = new Wall(tmpStartX, tmpEndX, endY*tileSize, (endY+1)*tileSize);
        entityManager.add(southWall);
        tmpStartX = (i+1)*tileSize;
      }
      if(i == endX)
      {
        Wall southWall = new Wall(tmpStartX, (endX+1)*tileSize, endY*tileSize, (endY+1)*tileSize);
        entityManager.add(southWall);
      }
    }

    for (int i = startY; i <= endY; i++)
    {
      if(doorways[i][startX])
      {
        tmpEndY = (i)*tileSize;
        Wall westWall = new Wall(startX*tileSize,(startX+1)*tileSize, tmpStartY, tmpEndY);
        entityManager.add(westWall);
        tmpStartY = (i+1)*tileSize;
      }
      if(i == endY)
      {
        Wall westWall = new Wall(startX*tileSize, (startX+1)*tileSize, tmpStartY, (endY+1)*tileSize);
        entityManager.add(westWall);
      }
    }
    tmpStartY = startY*tileSize;

    for (int i = startY; i <= endY; i++)
    {
      if(doorways[i][endX])
      {
        tmpEndY = (i)*tileSize;
        Wall westWall = new Wall(endX*tileSize,(endX+1)*tileSize, tmpStartY, tmpEndY);
        entityManager.add(westWall);
        tmpStartY = (i+1)*tileSize;
      }
      if(i == endY)
      {
        Wall westWall = new Wall(endX*tileSize, (endX+1)*tileSize, tmpStartY, (endY+1)*tileSize);
        entityManager.add(westWall);
      }
    }

  }
}
