import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Room class is for making both rooms and
 * hallways. Hallways are just skinny rooms,
 * they have the same functionality.
 */
public class Room
{
  private UpdateManager updateManager;
  private int width, height;
  private int startX, startY;
  private Tile[][] tiles;
  private boolean[][] doorways;
  private boolean isHallway;
  private Rectangle2D boundingBox;
  private ArrayList<Doorway> doorwayList;
  public ArrayList<Wall> wallList;
  private Player player;
  private boolean isLeaf;

  /**
   * Constructs a Room object at the given location
   * @param startX The starting X coord of the Room.
   * @param startY The starting X coord of the Room.
   * @param width The width of the Room.
   * @param height The height of the Room.
   * @param isHallway Whether the Room is a Hallway or not
   * @param player The player, needed for added zombies
   * @param updateManager referenence to the global entityManger for adding new Entities
   *
   */
  public Room(int startX, int startY, int width, int height, boolean isHallway, Player player, UpdateManager updateManager)
  {
    this.isHallway = isHallway;
    this.player = player;
    this.updateManager = updateManager;
    isLeaf = false;
    if(startX+width > 0 && startY+height > 0)
    {
      tiles = new Tile[startY + height][startX + width];
      doorways = new boolean[startY + height][startX + width];
    }
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;
    wallList = new ArrayList<>();
    boundingBox = new Rectangle2D.Float(startX*Settings.tileSize, startY*Settings.tileSize,width*Settings.tileSize, height*Settings.tileSize);
  }

  /**
   *
   * @param doorwayList the List of all Doorways randomly
   *                    generated in House
   */
  public void setDoorways(ArrayList<Doorway> doorwayList)
  {
    this.doorwayList = new ArrayList<>();
    for(Doorway d: doorwayList)
    {
      this.doorwayList.add(d);
    }
    if(doorwayList.size() == 1) isLeaf = true;
  }

  /**
   *
   * @param toRemove the doorway to be removed
   */
  public void removeDoorway(Doorway toRemove)
  {
    doorwayList.remove(toRemove);
  }

  /**
   * Copies Doorways from the local ArrayList containing
   * Door objects to a 2D array of booleans that is used when
   * making walls
   */
  public void addDoorways()
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

  /**
   * Called to build the Room once the Doorways
   * have been copied.
   */
  public void init()
  {
    makeRoom();
    makeWalls();
  }

  public boolean isAHallway()
  {
    return isHallway;
  }

  /**
   *
   * @param y coord of Tile object to be assigned
   * @param x coord of Tile object to be assigned
   * @param tile Tile to assign
   */
  public void setTileAt(int y, int x, Tile tile) {tiles[y][x] = tile;}

  /**
   *
   * @param y coord of Tile object to be retrieved
   * @param x coord of Tile object to be retrieved
   * @return the corresponding Tile
   */
  public Tile getTileAt(int y, int x){return tiles[y][x];}

  /**
   * Getter for room's startX
   * @return startX
   */
  public int getStartX(){return startX;}

  /**
   * Getter for room's startY
   * @return startY
   */
  public int getStartY(){return startY;}

  /**
   * Getter for room's width
   * @return width
   */
  public int getWidth(){return width;}

  /**
   * Getter for room's height
   * @return height
   */
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
        else
        {
          double rng = Util.rng.nextDouble();
          if (rng < 0.3) setTileAt(i, j, new Tile("tileset/nonwall", false));
          else if (rng > 0.3 && rng < 0.6) setTileAt(i, j, new Tile("tileset/nonwall2", false));
          else setTileAt(i, j, new Tile("tileset/nonwall3", false));
        }
      }
    }
  }

  /**
   *
   * @return Returns the boundingbox.
   */
  public Rectangle2D.Float getBoundingBox()
  {
    return (Rectangle2D.Float) boundingBox;
  }


  /**
   * Given a random chance, makes a zombie
   * on the given tile.
   *
   * @return Returns all the zombies made in the
   * Room.
   */
  public List<ZombieModel> spawnZombies()
  {
    List<ZombieModel> list = new LinkedList<>();
    for (int i = startY+1; i < (startY+height-2); i++)
    {
      for (int j = startX+1; j < (startX+width-2); j++)
      {
        if(Util.rng.nextDouble() < Settings.zombieSpawnRate)
        {
          if (Util.rng.nextBoolean())
          {
            list.add(new LineZombie(player, new Point2D.Float(j*Settings.tileSize,i*Settings.tileSize)));
          }
          else
          {
            list.add(new RandomZombie(player, new Point2D.Float(j*Settings.tileSize,i*Settings.tileSize)));
          }
        }
      }
    }
    return list;
  }

  /**
   * Given a random chance < 0.01 for each tile in the room,
   * make a firetrap on that tile
   */
  public List<Trap> spawnFireTraps()
  {
    ArrayList<Trap> traps = new ArrayList<>();
    for (int i = startY+1; i < (startY+height-2); i++)
    {
      for (int j = startX+1; j < (startX+width-2); j++)
      {
        if(Util.rng.nextDouble() < Settings.trapSpawnRate)
        {
          Trap trap = new Trap(new Point2D.Float(j*Settings.tileSize, i*Settings.tileSize));
          traps.add(trap);
          updateManager.add(trap);
        }
      }
    }
    return traps;
  }

  /**
   *
   * @return Returns whether or not the
   * room has multiple doors.
   */
  public boolean isLeaf()
  {
    return isLeaf;
  }

  //Makes a horizontal wall
  private void makeWallHoriz(int startY, House.Direction dir)
  {
    int tileSize = Settings.tileSize;
    int endX = (startX+1)+(width-1);
    int tmpStartX = (startX+1)*tileSize;
    int tmpStartY = startY*tileSize;
    int tmpEndX;
    Wall newWall = null;
    boolean hasDoor = false;
    for (int i = startX+1; i <= endX-2; i++)
    {
      if(doorways[startY][i])
      {
        hasDoor = true;
        tmpEndX = (i)*tileSize;
        newWall = new Wall(tmpStartX, tmpEndX, tmpStartY, tmpStartY+tileSize, dir);
        updateManager.add(newWall);
        tmpStartX = (i+1)*tileSize;
      }
      if(i == endX-2)
      {
        newWall = new Wall(tmpStartX, (endX-1)*tileSize, tmpStartY, tmpStartY+tileSize, dir);
        updateManager.add(newWall);
        if(!hasDoor) wallList.add(newWall);
      }
    }
  }

  //Makes a vertical wall
  private void makeWallVert(int startX, House.Direction dir)
  {
    int tileSize = Settings.tileSize;
    int endY = startY+(height-1);
    int tmpStartY = startY*tileSize;
    int tmpEndY;
    boolean hasDoor = false;
    Wall newWall = null;
    for (int i = startY; i <= endY; i++)
    {
      if(doorways[i][startX])
      {
        hasDoor = true;
        tmpEndY = (i)*tileSize;
        newWall = new Wall(startX*tileSize,(startX+1)*tileSize, tmpStartY, tmpEndY, dir);
        updateManager.add(newWall);
        tmpStartY = (i+1)*tileSize;
      }
      if(i == endY)
      {
        newWall = new Wall(startX*tileSize, (startX+1)*tileSize, tmpStartY, (endY+1)*tileSize, dir);
        updateManager.add(newWall);
        if(!hasDoor) wallList.add(newWall);
      }
    }
  }

  //Make walls, taking into account
  //the doorways
  private void makeWalls()
  {
    int endX = startX+(width-1);
    int endY = startY+(height-1);
    makeWallHoriz(startY, House.Direction.NORTH);
    makeWallHoriz(endY, House.Direction.SOUTH);
    makeWallVert(startX, House.Direction.WEST);
    makeWallVert(endX, House.Direction.EAST);
  }
}
