import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  private static Graph<Point> graphOfGrid;
  public static boolean isResetting = false;
  public static int levelNum = 1;
  public static long seed;
  private final java.util.List<Bookshelf> books = new ArrayList<>();
  private final boolean[][] bookshelves;
  private final Tile[][] fullGrid;
  private final int gridHeight;
  private final int gridWidth;
  private final ArrayList<Doorway> initDoorways;
  private final HashMap<Class, ArrayList<Point2D.Float>> initialPosition;
  private MasterZombie master;
  private Player player;
  private int prevHallDoorX, prevHallDoorY;
  private final ArrayList<Room> roomList;
  private final Room startRoom;
  private final java.util.List<Trap> traps = new ArrayList<>();
  private final UpdateManager updateManager;
  private final java.util.List<ZombieModel> zombies = new LinkedList<>();

  /**
   * Makes a new House object of specified height and width at (0,0).
   * Uses recursion to generate a set of rooms of Rooms of random
   * widths and height, in random positions, completely connected by
   * hallways. The number of Rooms + Hallways guaranteed to be generated
   * is determined by numObjectsGenerated in Settings.
   *
   * @param gridWidth     Width of House
   * @param gridHeight    Height of House
   * @param updateManager a reference to global EntityManager
   */
  public House(int gridWidth, int gridHeight, int levelNum, UpdateManager updateManager)
  {
    seed = System.currentTimeMillis();
    Util.rng = new Random(seed);
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    this.updateManager = updateManager;
    bookshelves = new boolean[gridHeight][gridWidth];
    initialPosition = new HashMap<>();
    roomList = new ArrayList<>();
    initDoorways = new ArrayList<>();
    graphOfGrid = new Graph<>();
    fullGrid = new Tile[gridHeight][gridWidth];
    int roomStartX = 40;
    int roomStartY = 40;
    int startWidth = 8;
    int startHeight = 10;
    startRoom = makeInitRoom(roomStartX, roomStartY, startWidth, startHeight, updateManager);
    roomList.add(startRoom);
    Doorway randStartDoor = initDoorways.get(0);
    generateRoomList(randStartDoor, randStartDoor.getSideOfRoom(), startRoom, 1);
    startRoom.addDoorways();
    startRoom.init();
    copyObjectsToGrid();
    makeBookcases();
    makeGraph();
    makeMasterZombie();
    initializeMap();
    updateManager.setEntityToFollow(player);
    if (levelNum < 3)
    {
      makeExit(false);
    }
  }

  /**
   * Enum for convenience when describing 2D directions
   * NORTH, WEST, SOUTH, EAST
   */
  public enum Direction
  {
    NORTH, WEST, SOUTH, EAST
  }

  /**
   * Given two points in the house this
   * uses the Graph class to calculated
   * the best path between them. Returns
   * the first point in the path.
   *
   * @param startPoint Starting node in graph.
   * @param endPoint   Ending node in graph.
   * @return The first point in the Path.
   */
  public static Point calculateAStar(Point startPoint, Point endPoint)
  {
    Point firstPoint = null;
    ArrayList<Point> solution = null;
    Optional<Path<Point>> path =
        graphOfGrid.findPath(startPoint, endPoint, (Point p1, Point p2) -> ((float) p1.distance(p2)));
    if (path.isPresent()) solution = path.get().getNodes();
    if (solution != null) firstPoint = solution.get(0);
    return firstPoint;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    int tileSize = Settings.tileSize;
    Point2D.Float curOrigin = drawingManager.getCameraOrigin();
    int curStartY = (int) curOrigin.getY() / tileSize;
    int curStartX = (int) curOrigin.getX() / tileSize;
    int curEndY = (int) (curStartY + (drawingManager.screenHeight / tileSize));
    int curEndX = (int) (curStartX + (drawingManager.screenWidth / tileSize));
    for (int i = curStartY; i < curEndY + 2; i++)
    {
      for (int j = curStartX; j < curEndX + 2; j++)
      {
        float screenX = drawingManager.gameXToScreenX(j * tileSize);
        float screenY = drawingManager.gameYToScreenY(i * tileSize);
        if (i < 0 || j < 0 || fullGrid.length <= i || fullGrid[i].length <= j || fullGrid[i][j] == null) continue;
        screen.drawImage(fullGrid[i][j].getTileImg(), (int) screenX, (int) screenY,
            (int) (tileSize * drawingManager.getScale()), (int) (tileSize * drawingManager.getScale()), null);
      }
    }
  }

  @Override
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    Point2D.Float position = getPosition();
    if (position == null) return null;
    return new Rectangle2D.Float(position.x, position.y, gridWidth * Settings.tileSize, gridHeight * Settings.tileSize);
  }

  @Override
  public int getDepth() //lower numbers are drawn above higher numbers
  {
    return -100;
  }

  @Override
  public Point2D.Float getPosition() //returns upper left point of the object
  {
    return new Point2D.Float(0, 0);
  }

  @Override
  public boolean isSolid() //solid objects cannot move into each other
  {
    return false;
  }

  /**
   * Resets the houses entites when
   * the player dies. Everything goes
   * back to its original position, including
   * dead entities.
   */
  public void resetHouse()
  {
    for (Class c : initialPosition.keySet())
    {
      if (c.equals(Player.class))
      {
        player = new Player(this, new Point2D.Float(44 * Settings.tileSize, 44 * Settings.tileSize));
        updateManager.add(player);
      }
    }
    updateManager.setEntityToFollow(player);
    for (Class c : initialPosition.keySet())
    {
      if (c.equals(LineZombie.class))
      {
        for (Point2D.Float p : initialPosition.get(c))
        {
          updateManager.add(new LineZombie(player, p));
        }
      }
      if (c.equals(RandomZombie.class))
      {
        for (Point2D.Float p : initialPosition.get(c))
        {
          updateManager.add(new RandomZombie(player, p));
        }
      }
      if (c.equals(Trap.class))
      {
        for (Point2D.Float p : initialPosition.get(c))
        {
          updateManager.add(new Trap(p));
        }
      }
      if (c.equals(Bookshelf.class))
      {
        for (Point2D.Float p : initialPosition.get(c))
        {
          updateManager.add(new Bookshelf(p));
        }
      }
    }
    updateManager.add(new SightDrawer());
  }

  //Makes a number of doors based on
  //a function of depth. Also, makes
  //sure there are enough doors.
  private int calculateNumDoors(int depth)
  {
    int numDoors = 0;
    double rand = Util.rng.nextDouble();
    if (depth == 1) numDoors = 4;
    if (depth == 2) //|| depth == 3)
    {
      if (rand >= 0.5) numDoors = 4;
      else if (rand >= 0.25) numDoors = 3;
      else if (rand >= 0.125) numDoors = 2;
      else if (rand >= 0.0625) numDoors = 1;
    }
    else if (depth > 2)
    {
      if (rand >= 0.5) numDoors = 0;
      else if (rand >= 0.25) numDoors = 1;
      else if (rand >= 0.125) numDoors = 2;
      else if (rand >= 0.0625) numDoors = 3;
    }
    if (numDoors == 0 && roomList.size() < Settings.numObjectsGuranteed) numDoors = 4;
    return numDoors;
  }

  private void copyObjectsToGrid()
  {
    int startX, startY;
    int width, height;
    for (Room room : roomList)
    {
      startX = room.getStartX();
      startY = room.getStartY();
      width = room.getWidth() + startX;
      height = room.getHeight() + startY;

      for (int i = startY; i < height; i++)
      {
        for (int j = startX; j < width; j++)
        {
          fullGrid[i][j] = room.getTileAt(i, j);
          graphOfGrid.add(new Point(j, i));
        }
      }
    }
  }

  //Recursive room generation
  private void generateRoomList(Doorway prevDoorway, Direction comingFrom, Room prevRoom, int depth)
  {
    ArrayList<Doorway> doorwayList = new ArrayList<>();
    ArrayList<Direction> directions = new ArrayList<>();
    doorwayList.add(new Doorway());
    Point2D.Float startOfDoor = prevDoorway.getPointAt(0);
    int prevRoomDoorX = (int) startOfDoor.getX();
    int prevRoomDoorY = (int) startOfDoor.getY();
    int prevRoomDoorSize = prevDoorway.getLengthOfDoorway();
    int numDoors;
    Room newHallway = makeNewHallway(prevRoomDoorX, prevRoomDoorY, comingFrom, prevRoomDoorSize);
    Room newRoom = makeNewRoom(prevHallDoorX, prevHallDoorY, comingFrom, doorwayList.get(0), prevRoomDoorSize);
    //Room newRoom = makeNewRoom(prevRoomDoorX, prevRoomDoorY, comingFrom,
    //                                  doorwayList.get(0), prevRoomDoorSize);

    //Hitting another already made room
    for (Room other : roomList)
    {
      if (newRoom.getBoundingBox().intersects(other.getBoundingBox()) ||
          newHallway.getBoundingBox().intersects(other.getBoundingBox()))
      {
        prevRoom.removeDoorway(prevDoorway);
        roomList.remove(newHallway);
        roomList.remove(newRoom);
        return;
      }
    }
    //Going outside the house
    if (!this.getBoundingBox().contains(newRoom.getBoundingBox()) ||
        !this.getBoundingBox().contains(newHallway.getBoundingBox()))
    {
      prevRoom.removeDoorway(prevDoorway);
      roomList.remove(newHallway);
      roomList.remove(newRoom);
      return;
    }
    roomList.add(newHallway);
    roomList.add(newRoom);
    Collections.addAll(directions, Direction.values());
    directions.remove(oppositeDirOf(comingFrom));
    Collections.shuffle(directions);
    numDoors = calculateNumDoors(depth);
    if (!newRoom.isAHallway())
    {
      for (int i = 1; i < numDoors; i++)
      {
        Direction curSide = directions.get(i - 1);
        doorwayList.add(
            makeNewDoorway(curSide, newRoom.getStartX(), newRoom.getStartY(), newRoom.getWidth(), newRoom.getHeight()));
      }
    }
    newRoom.setDoorways(doorwayList);
    //if(depth >= 5) return;
    if (!newRoom.isAHallway())
    {
      //skip going backwards
      for (int i = 1; i < numDoors; i++)
      {
        //if(roomList.size() > 8) return;
        Doorway curDoorway = doorwayList.get(i);
        generateRoomList(curDoorway, curDoorway.getSideOfRoom(), newRoom, ++depth);
      }
    }
    newHallway.addDoorways();
    newHallway.init();
    newRoom.addDoorways();
    newRoom.init();
    zombies.addAll(newRoom.spawnZombies());
    traps.addAll(newRoom.spawnFireTraps());
  }

  //Makes map from entities classes in
  //room to their initial positions
  private void initializeMap()
  {
    ArrayList<Point2D.Float> lineZombiePoints = new ArrayList<>();
    ArrayList<Point2D.Float> randomZombiePoints = new ArrayList<>();
    ArrayList<Point2D.Float> trapPoints = new ArrayList<>();
    ArrayList<Point2D.Float> bookPoints = new ArrayList<>();
    ArrayList<Point2D.Float> playerPoints = new ArrayList<>();
    for (ZombieModel zombie : zombies)
    {
      if (zombie instanceof LineZombie)
      {
        lineZombiePoints.add(new Point2D.Float((int) zombie.getPosition().getX(), (int) zombie.getPosition().getY()));
      }
      if (zombie instanceof RandomZombie)
      {
        randomZombiePoints.add(new Point2D.Float((int) zombie.getPosition().getX(), (int) zombie.getPosition().getY()));
      }
    }
    initialPosition.put(LineZombie.class, lineZombiePoints);
    initialPosition.put(RandomZombie.class, randomZombiePoints);
    for (Trap trap : traps)
    {
      trapPoints.add(new Point2D.Float((int) trap.getPosition().getX(), (int) trap.getPosition().getY()));
    }
    initialPosition.put(Trap.class, trapPoints);
    for (Bookshelf book : books)
    {
      bookPoints.add(new Point2D.Float((int) book.getPosition().getX(), (int) book.getPosition().getY()));
    }
    initialPosition.put(Bookshelf.class, bookPoints);
    playerPoints.add(new Point2D.Float((int) player.getPosition().getX(), (int) player.getPosition().getY()));
    initialPosition.put(Player.class, playerPoints);
  }

  //Makes bookcases at random locations, checks for no zombies
  private void makeBookcases()
  {
    int numBookcases = 0;
    int startX, startY;
    int width, height;
    while (numBookcases < 5)
    {
      for (Room room : roomList)
      {
        if (room.equals(startRoom)) continue;
        startX = room.getStartX();
        startY = room.getStartY();
        width = room.getWidth() + startX;
        height = room.getHeight() + startY;

        for (int i = startY; i < height; i++)
        {
          for (int j = startX; j < width; j++)
          {
            if (Util.rng.nextDouble() < 0.005 && !room.isAHallway() &&
                i != startY && j != startX && i != height - 1 && j != width - 1)
            {
              numBookcases++;
              Bookshelf b = new Bookshelf(new Point2D.Float(j * Settings.tileSize, i * Settings.tileSize));
              books.add(b);
              updateManager.add(b);
              bookshelves[i][j] = true;
            }
          }
        }
      }
    }
    for (ZombieModel zombie : zombies)
    {
      int x = (int) (zombie.getPosition().getX() / Settings.tileSize);
      int y = (int) (zombie.getPosition().getY() / Settings.tileSize);
      if (bookshelves[y][x])
      {
        bookshelves[y][x] = false;
        updateManager.remove(new Bookshelf(new Point2D.Float(x * Settings.tileSize, y * Settings.tileSize)));
      }
    }
  }

  //Makes a Doorway at the specified location
  private Doorway makeDoorwayAt(Direction dir, int startX, int startY, int widthOrHeight)
  {
    Doorway newDoorway = new Doorway();
    int doorwaySize = Util.rng.nextInt(2) + 2;
    if (dir == Direction.EAST || dir == Direction.WEST)
    {
      int startDoorY = Util.rng.nextInt(widthOrHeight - 4) + startY + 2;
      for (int i = 0; i < doorwaySize; i++)
      {
        if (startDoorY + i == startY + widthOrHeight - 1)
        {
          doorwaySize--;
          break;
        }
        newDoorway.addPointInDoorway(i, new Point2D.Float(startX, startDoorY + i));
      }
      newDoorway.setSideOfRoom(dir);
      newDoorway.setLengthOfDoorway(doorwaySize);
      return newDoorway;
    }
    else
    {
      int startDoorX;
      startDoorX = Util.rng.nextInt(widthOrHeight - 4) + startX + 2;
      for (int i = 0; i < doorwaySize; i++)
      {
        //Corner
        if (startDoorX + i == startX + widthOrHeight - 1)
        {
          doorwaySize--;
          break;
        }
        newDoorway.addPointInDoorway(i, new Point2D.Float(startDoorX + i, startY));
      }
      newDoorway.setSideOfRoom(dir);
      newDoorway.setLengthOfDoorway(doorwaySize);
      return newDoorway;
    }
  }

  //Makes exit far away if possible, else
  //wherever it can
  private boolean makeExit(boolean makeFarAway)
  {
    Collections.shuffle(roomList);
    Point roomStart = new Point();
    for (Room room : roomList)
    {
      roomStart.setLocation(room.getStartX() * Settings.tileSize, room.getStartY() * Settings.tileSize);
      Collections.shuffle(room.wallList);
      if (room.isLeaf() && !room.equals(startRoom) &&
          (!makeFarAway || roomStart.distance(player.getPosition()) > Settings.distanceAwayExit * Settings.tileSize))
      {
        for (Wall wall : room.wallList)
        {
          if (wall != null)
          {
            Exit exit =
                new Exit(wall.getDirection(), wall.getStartX(), wall.getStartY(), wall.getEndX(), wall.getEndY());
            updateManager.add(exit);
            return true;
          }
        }
      }
    }
    return false;
  }

  //Makes a graph of nodes that are tiles in the House
  private void makeGraph()
  {
    int startX, startY;
    int width, height;
    for (Room room : roomList)
    {
      startX = room.getStartX();
      startY = room.getStartY();
      width = room.getWidth() + startX;
      height = room.getHeight() + startY;

      for (int i = startY; i < height; i++)
      {
        for (int j = startX; j < width; j++)
        {
          Point newPoint = new Point(j, i);
          for (int k = 0; k < Util.NUM_DIRECTIONS; k++)
          {
            int dx = Util.dx[k];
            int dy = Util.dy[k];
            if (j + dx > -1 && i + dy > -1 && j + dx < gridWidth && i + dy < gridHeight)
            {
              Point neighbor = new Point(j + dx, i + dy);
              if (fullGrid[i + dy][j + dx] == null) continue;
              if (!(fullGrid[i + dy][j + dx].isSolid() ||
                  fullGrid[i][j].isSolid() ||
                  bookshelves[i][j] || bookshelves[i + dy][j + dx]))
              {
                graphOfGrid.setEdge(newPoint, neighbor, 1.0f);
              }
            }
          }
        }
      }
    }
  }

  //Make the inital room, all other rooms are created around this one
  private Room makeInitRoom(int startX, int startY, int width, int height, UpdateManager updateManager)
  {
    player = new Player(this,
        new Point2D.Float(44 * Settings.tileSize, 44 * Settings.tileSize));//Put player in middle of init room
    updateManager.add(player);

    Room startRoom = new Room(startX, startY, width, height, false, player, updateManager);
    ArrayList<Direction> directions = new ArrayList<>();
    Collections.addAll(directions, Direction.values());
    Collections.shuffle(directions);
    Direction dir = directions.get(0);
    initDoorways.add(makeNewDoorway(dir, startX, startY, width, height));
    startRoom.setDoorways(initDoorways);
    return startRoom;
  }

  private void makeMasterZombie()
  {
    if (zombies.size() == 0) return;
    int index = Util.rng.nextInt(zombies.size());
    ZombieModel zombie = zombies.get(index);
    master = new MasterZombie(player, new Point2D.Float(zombie.getPosition().x, zombie.getPosition().y));
    zombies.remove(zombie);
    zombies.forEach(entity -> entity.setMasterZombie(master));
    zombies.add(master);
    updateManager.add(zombies);
    //zombies = null;
  }

  //Makes a new Doorway on the side of the room specified
  //at a random location
  private Doorway makeNewDoorway(Direction curSide, int startX, int startY, int width, int height)
  {
    Doorway newDoorway = new Doorway();
    switch (curSide)
    {
      case NORTH:
        newDoorway = makeDoorwayAt(Direction.NORTH, startX, startY, width);
        break;
      case WEST:
        newDoorway = makeDoorwayAt(Direction.WEST, startX, startY, height);
        break;
      case SOUTH:
        newDoorway = makeDoorwayAt(Direction.SOUTH, startX, startY + (height - 1), width);
        break;
      case EAST:
        newDoorway = makeDoorwayAt(Direction.EAST, startX + (width - 1), startY, height);
        break;
    }
    return newDoorway;
  }

  private Room makeNewHallway(int prevRoomDoorX, int prevRoomDoorY, Direction comingFrom, int prevRoomDoorSize)
  {
    ArrayList<Doorway> doorwayList = new ArrayList<>();
    Doorway entrance = new Doorway();
    Doorway exit = new Doorway();
    Room newHallway = null;
    int offsetFromCenter = 1;//generator.nextInt(2)+2;
    int newWidth;
    int newHeight;
    switch (comingFrom)
    {

      case NORTH:
        newWidth = prevRoomDoorSize + 2;
        newHeight = 4 + Util.rng.nextInt(4);
        newHallway =
            new Room(prevRoomDoorX - offsetFromCenter, prevRoomDoorY - newHeight, newWidth, newHeight, true, player,
                updateManager);
        prevHallDoorX = prevRoomDoorX;
        prevHallDoorY = prevRoomDoorY - newHeight;
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          entrance.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY - 1));
          entrance.setLengthOfDoorway(prevRoomDoorSize);
          entrance.setSideOfRoom(Direction.NORTH);
          exit.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY - newHeight));
          exit.setLengthOfDoorway(prevRoomDoorSize);
          exit.setSideOfRoom(Direction.NORTH);
        }
        break;
      case WEST:
        newWidth = 4 + Util.rng.nextInt(4);
        newHeight = prevRoomDoorSize + 2;
        newHallway =
            new Room(prevRoomDoorX - newWidth, prevRoomDoorY - offsetFromCenter, newWidth, newHeight, true, player,
                updateManager);
        prevHallDoorX = prevRoomDoorX - newWidth;
        prevHallDoorY = prevRoomDoorY;
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          entrance.addPoint(new Point2D.Float(prevRoomDoorX - 1, prevRoomDoorY + i));
          entrance.setLengthOfDoorway(prevRoomDoorSize);
          entrance.setSideOfRoom(Direction.WEST);
          exit.addPoint(new Point2D.Float(prevRoomDoorX - newWidth, prevRoomDoorY + i));
          exit.setLengthOfDoorway(prevRoomDoorSize);
          exit.setSideOfRoom(Direction.WEST);
        }
        break;
      case SOUTH:
        newWidth = prevRoomDoorSize + 2;
        newHeight = 4 + Util.rng.nextInt(4);
        newHallway = new Room(prevRoomDoorX - offsetFromCenter, prevRoomDoorY + 1, newWidth, newHeight, true, player,
            updateManager);
        prevHallDoorX = prevRoomDoorX;
        prevHallDoorY = prevRoomDoorY + newHeight;
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          entrance.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY + 1));
          entrance.setLengthOfDoorway(prevRoomDoorSize);
          entrance.setSideOfRoom(Direction.NORTH);
          exit.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY + newHeight));
          exit.setLengthOfDoorway(prevRoomDoorSize);
          exit.setSideOfRoom(Direction.NORTH);
        }
        break;
      case EAST:
        newWidth = 4 + Util.rng.nextInt(4);
        newHeight = prevRoomDoorSize + 2;
        newHallway = new Room(prevRoomDoorX + 1, prevRoomDoorY - offsetFromCenter, newWidth, newHeight, true, player,
            updateManager);
        prevHallDoorX = prevRoomDoorX + newWidth;
        prevHallDoorY = prevRoomDoorY;
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          entrance.addPoint(new Point2D.Float(prevRoomDoorX + 1, prevRoomDoorY + i));
          entrance.setLengthOfDoorway(prevRoomDoorSize);
          entrance.setSideOfRoom(Direction.WEST);
          exit.addPoint(new Point2D.Float(prevRoomDoorX + newWidth, prevRoomDoorY + i));
          exit.setLengthOfDoorway(prevRoomDoorSize);
          exit.setSideOfRoom(Direction.WEST);
        }
    }
    doorwayList.add(entrance);
    doorwayList.add(exit);
    newHallway.setDoorways(doorwayList);

    return newHallway;
  }

  //Called by generateRoomList, makes a room and initial doorway
  //in the opposite direction so there is an opening on both sides
  private Room makeNewRoom(int prevRoomDoorX, int prevRoomDoorY, Direction comingFrom, Doorway firstdoor,
                           int prevRoomDoorSize)
  {
    Room newRoom = null;
    int offsetFromCenter = 2;//generator.nextInt(2)+2;
    int newWidth = Util.rng.nextInt(6) + 8;
    int newHeight = Util.rng.nextInt(6) + 8;
    switch (comingFrom)
    {
      case NORTH:
        newRoom =
            new Room(prevRoomDoorX - offsetFromCenter, prevRoomDoorY - newHeight, newWidth, newHeight, false, player,
                updateManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY - 1));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.NORTH);
        }
        break;
      case WEST:
        newRoom =
            new Room(prevRoomDoorX - newWidth, prevRoomDoorY - offsetFromCenter, newWidth, newHeight, false, player,
                updateManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX - 1, prevRoomDoorY + i));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.WEST);
        }
        break;
      case SOUTH:
        newRoom = new Room(prevRoomDoorX - offsetFromCenter, prevRoomDoorY + 1, newWidth, newHeight, false, player,
            updateManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY + 1));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.NORTH);
        }
        break;
      case EAST:
        newRoom = new Room(prevRoomDoorX + 1, prevRoomDoorY - offsetFromCenter, newWidth, newHeight, false, player,
            updateManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX + 1, prevRoomDoorY + i));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.WEST);
        }
    }
    return newRoom;
  }

  //returns the opposite of the direciton specified
  private Direction oppositeDirOf(Direction dir)
  {
    switch (dir)
    {
      case NORTH:
        return Direction.SOUTH;
      case WEST:
        return Direction.EAST;
      case SOUTH:
        return Direction.NORTH;
      case EAST:
        return Direction.WEST;
    }
    return null;
  }
}
