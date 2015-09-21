import java.util.ArrayList;

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
  private boolean isHallway;
  private Room eastNeighbor;
  private Room westNeighbor;
  private Room southNeighbor;
  private Room northNeighbor;

  public Room(int startX, int startY, int width, int height, boolean isHallway, EntityManager entityManager)
  {
    this.isHallway = isHallway;
    this.entityManager = entityManager;
    tiles = new Tile[startY+height][startX+width];
    doorways = new boolean[startY+height][startX+width];
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;
  }

  public void setDoorways(ArrayList<Doorway> doorwayList)
  {
    for(Doorway doorway: doorwayList)
    {
      for (int i = 0; i < doorway.getLengthOfDoorway(); i++)
      {
        int x = (int) doorway.getPointAt(i).x;
        int y = (int) doorway.getPointAt(i).y;
        doorways[y][x] = true;
      }
    }
  }

  public void init()
  {
    makeRoom();
    makeWalls();
  }

  public boolean isAHallway()
  {
    return isHallway;
  }

  public void setTileAt(int y, int x, Tile tile) {tiles[y][x] = tile;}
  public Tile getTileAt(int y, int x){return tiles[y][x];}
  public int getStartX(){return startX;}
  public int getStartY(){return startY;}
  public int getWidth(){return width;}
  public int getHeight(){return height;}

  private void makeRoom()
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
  }

  public void setNeighbor(House.Direction dir, Room neighbor)
  {
    switch(dir)
    {
      case NORTH:
        northNeighbor = neighbor;
        break;
      case WEST:
        westNeighbor = neighbor;
        break;
      case SOUTH:
        southNeighbor = neighbor;
        break;
      case EAST:
        eastNeighbor = neighbor;
    }
  }
  private void makeWallHoriz(int startY)
  {
    int tileSize = Settings.tileSize;
    int endX = startX+(width-1);
    int tmpStartX = startX*tileSize;
    int tmpStartY = startY*tileSize;
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
  }

  private void makeWallVert(int startX)
  {
    int tileSize = Settings.tileSize;
    int endY = startY+(height-1);
    int tmpStartY = startY*tileSize;
    int tmpEndY;
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
  }

  //Make walls, taking into account
  //the doorways
  private void makeWalls()
  {
    int endX = startX+(width-1);
    int endY = startY+(height-1);
    makeWallHoriz(startY);
    makeWallHoriz(endY);
    makeWallVert(startX);
    makeWallVert(endX);
  }
}
