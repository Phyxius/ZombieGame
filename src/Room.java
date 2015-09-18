
/**
 * Created by arirappaport on 9/11/15.
 */
public class Room
{
  private EntityManager entityManager;
  private Entity[][] obstacles;
  private int width, height;
  private int startX, startY;
  public Tile[][] tiles;

  public Room(int startX, int startY, int width, int height, EntityManager entityManager)
  {
    this.entityManager = entityManager;
    tiles = new Tile[startY+height][startX+width];
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;
    makeRoom();
    makeWalls();
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
        if(i == startY || j == startX || i == height+startY-1 || j == width+startX-1)
        {
          setTileAt(i,j,new Tile("tileset/wall", true));
        }
        else setTileAt(i,j, new Tile("tileset/nonwall", false));
      }
    }
    return tiles;
  }

  private void makeWalls()
  {
    int tileSize = Settings.tileSize;
    startX *= tileSize;
    startY *= tileSize;
    int endX = startX+(width-1)*tileSize;
    int endY = startY+(height-1)*tileSize;

    Wall wall1 = new Wall(startX,startX + tileSize,startY, endY);
    Wall wall2 = new Wall(startX+tileSize,endX,startY, startY + tileSize);
    Wall wall3 = new Wall(endX,endX + tileSize,startY,endY);
    Wall wall4 = new Wall(startX,endX+tileSize,endY,endY+tileSize);
    entityManager.add(wall1);
    entityManager.add(wall2);
    entityManager.add(wall3);
    entityManager.add(wall4);
    startX/=tileSize;
    startY/=tileSize;
  }
}
