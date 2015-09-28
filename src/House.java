import javafx.scene.shape.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.jar.JarFile;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  public static Graph<Point> graphOfGrid;
  private Tile[][] fullGrid;
  private EntityManager entityManager;
  private ArrayList<Room> roomList;
  private ArrayList<Line2D.Float> connections;
  private ArrayList<Doorway> initDoorways;
  private Player player;
  private MasterZombie master;
  private BufferedImage houseImg;
  private JFrame frame;
  private Room startRoom;
  private int gridHeight, gridWidth;
  private int prevHallDoorX, prevHallDoorY;

  /**
   * Makes a new House object of specified height and width at (0,0).
   * Uses recursion to generate a set of rooms of Rooms of random
   * widths and height, in random positions, completely connected by
   * hallways. The number of Rooms + Hallways guaranteed to be generated
   * is determined by numObjectsGenerated in Settings.
   * @param gridWidth Width of House
   * @param gridHeight Height of House
   * @param entityManager a reference to global EntityManager
   */
  public House(int gridWidth, int gridHeight, EntityManager entityManager, JFrame frame)
  {
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    this.entityManager = entityManager;
    this.frame = frame;
    connections = new ArrayList<>();
    int tileSize = Settings.tileSize;
    roomList = new ArrayList<>();
    initDoorways = new ArrayList<>();
    graphOfGrid = new Graph<>();
    fullGrid = new Tile[gridHeight][gridWidth];
    //houseImg = new BufferedImage(gridWidth*tileSize, gridHeight*tileSize, BufferedImage.TYPE_INT_ARGB);
    int roomStartX = 40;
    int roomStartY = 40;
    int startWidth = 8;
    int startHeight = 10;
    startRoom = makeInitRoom(roomStartX, roomStartY, startWidth, startHeight, entityManager);
    roomList.add(startRoom);
    Doorway randStartDoor = initDoorways.get(0);
    generateRoomList(randStartDoor, randStartDoor.getSideOfRoom(), startRoom, 1);
    //Doorway randStartDoor2 = initDoorways.get(1);
    //generateRoomList(randStartDoor2, randStartDoor2.getSideOfRoom(), startRoom, 1);
    startRoom.addDoorways();
    startRoom.init();
    copyObjectsToGrid();
    makeGraph();
    makeMasterZombie();
    if(!makeExit(true)) makeExit(false);
    //generateBuffImgHouse();
  }

  /**
   * Enum for convenience when describing 2D directions
   * NORTH, WEST, SOUTH, EAST
   */
  public enum Direction
  {
    NORTH,WEST,SOUTH,EAST
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    //local.drawImage(houseImg, 0, 0, houseImg.getWidth(), houseImg.getHeight(), null);
    int tileSize = Settings.tileSize;
    Point2D.Float curOrigin = drawingManager.getCameraOrigin();
    int curStartY = (int) curOrigin.getY()/tileSize;
    int curStartX = (int) curOrigin.getX()/tileSize;
    int curEndY = curStartY + frame.getHeight()/tileSize;
    int curEndX = curStartX + frame.getWidth()/tileSize;
    for(int i = curStartY; i < curEndY+1; i++)
    {
      for(int j = curStartX; j < curEndX+1; j++)
      {
        if(fullGrid[i][j] == null) continue; //fullGrid[i][j] = new Tile("tileset/outofbounds1", true);
        local.drawImage(fullGrid[i][j].getTileImg(), j*tileSize, i*tileSize, tileSize, tileSize, null);
      }
    }
    local.setColor(Color.green);
    connections.forEach(Line -> local.draw(Line));
  }

  public Point2D.Float getPosition() //returns upper left point of the object
  {
    return new Point2D.Float(0,0);
  }

  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    Point2D.Float position = getPosition();
    if (position == null) return null;
    return new Rectangle2D.Float(position.x, position.y,
                                 gridWidth*Settings.tileSize, gridHeight*Settings.tileSize);
  }
  public boolean isSolid() //solid objects cannot move into each other
  {
    return false;
  }
  public void keyPressed(KeyEvent e) {}
  public void keyReleased(KeyEvent e)
  {

  }
  public void update(UpdateManager e) //called for each update tick, EntityManager contains methods to add/remove/etc entities
  {
  }
  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {
  }

  public int getDepth() //lower numbers are drawn above higher numbers
  {
    return -100;
  }

  public static Point calculateAStar(Point startPoint, Point endPoint)
  {
    return graphOfGrid.findPath(startPoint, endPoint,  (Point p1, Point p2) -> ((float) p1.distance(p2))).get().getNodes().get(0);
  }

  private Room makeNewHallway(int prevRoomDoorX, int prevRoomDoorY, Direction comingFrom, int prevRoomDoorSize)
  {
    ArrayList<Doorway> doorwayList = new ArrayList<>();
    Doorway entrance = new Doorway();
    Doorway exit = new Doorway();
    Room newHallway = null;
    Random generator = new Random();
    int offsetFromCenter = 1;//generator.nextInt(2)+2;
    int newWidth;
    int newHeight;
    switch(comingFrom)
    {

      case NORTH:
        newWidth = prevRoomDoorSize+2;
        newHeight = 4+generator.nextInt(4);
        newHallway = new Room(prevRoomDoorX-offsetFromCenter, prevRoomDoorY-newHeight,newWidth, newHeight,true,player, entityManager);
        prevHallDoorX = prevRoomDoorX;
        prevHallDoorY = prevRoomDoorY-newHeight;
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
        newWidth = 4+generator.nextInt(4);
        newHeight = prevRoomDoorSize+2;
        newHallway = new Room(prevRoomDoorX-newWidth, prevRoomDoorY-offsetFromCenter,newWidth, newHeight, true,player, entityManager);
        prevHallDoorX = prevRoomDoorX-newWidth;
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
        newWidth = prevRoomDoorSize+2;
        newHeight = 4+generator.nextInt(4);
        newHallway = new Room(prevRoomDoorX-offsetFromCenter, prevRoomDoorY+1,newWidth, newHeight, true,player, entityManager);
        prevHallDoorX = prevRoomDoorX;
        prevHallDoorY = prevRoomDoorY+newHeight;
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          entrance.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY + 1));
          entrance.setLengthOfDoorway(prevRoomDoorSize);
          entrance.setSideOfRoom(Direction.NORTH);
          exit.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY+newHeight));
          exit.setLengthOfDoorway(prevRoomDoorSize);
          exit.setSideOfRoom(Direction.NORTH);
        }
      break;
      case EAST:
        newWidth = 4+generator.nextInt(4);
        newHeight = prevRoomDoorSize+2;
        newHallway = new Room(prevRoomDoorX+1, prevRoomDoorY-offsetFromCenter,newWidth, newHeight,true,player, entityManager);
        prevHallDoorX = prevRoomDoorX+newWidth;
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

  private Room makeInitRoom(int startX, int startY, int width, int height, EntityManager entityManager)
  {
    int tileSize = Settings.tileSize;
    player = new Player();
    entityManager.add(player);
    entityManager.setEntityToFollow(player);
    Room startRoom = new Room(startX, startY, width, height,false,player, entityManager);
    ArrayList<Direction> directions = new ArrayList<>();
    Collections.addAll(directions, Direction.values());
    Collections.shuffle(directions);
    Direction dir = directions.get(0);
    Direction dir1 = directions.get(1);
    //for (Direction dir: directions)
    //{
      initDoorways.add(makeNewDoorway(dir, startX, startY, width, height));
      //initDoorways.add(makeNewDoorway(dir1, startX, startY, width, height));
    //}
    startRoom.setDoorways(initDoorways);
    return startRoom;
  }

  private Room makeNewRoom(int prevRoomDoorX, int prevRoomDoorY, Direction comingFrom,
                                  Doorway firstdoor, int prevRoomDoorSize)
  {
    Room newRoom = null;
    Random generator = new Random();
    int offsetFromCenter = 2;//generator.nextInt(2)+2;
    int newWidth = generator.nextInt(6)+8;
    int newHeight = generator.nextInt(6)+8;
    switch(comingFrom)
    {
      case NORTH:
        newRoom = new Room(prevRoomDoorX-offsetFromCenter, prevRoomDoorY-newHeight,newWidth, newHeight,false, player, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY - 1));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.NORTH);
        }
      break;
      case WEST:
        newRoom = new Room(prevRoomDoorX-newWidth, prevRoomDoorY-offsetFromCenter,newWidth, newHeight, false, player, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
            firstdoor.addPoint(new Point2D.Float(prevRoomDoorX - 1, prevRoomDoorY+i));
            firstdoor.setLengthOfDoorway(prevRoomDoorSize);
            firstdoor.setSideOfRoom(Direction.WEST);
        }
      break;
      case SOUTH:
        newRoom = new Room(prevRoomDoorX-offsetFromCenter, prevRoomDoorY+1,newWidth, newHeight, false, player, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX + i, prevRoomDoorY + 1));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.NORTH);
        }
      break;
      case EAST:
        newRoom = new Room(prevRoomDoorX+1, prevRoomDoorY-offsetFromCenter,newWidth, newHeight,false, player, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          firstdoor.addPoint(new Point2D.Float(prevRoomDoorX +1 , prevRoomDoorY+i));
          firstdoor.setLengthOfDoorway(prevRoomDoorSize);
          firstdoor.setSideOfRoom(Direction.WEST);
        }
    }
    return newRoom;
  }

  private int calculateNumDoors(int depth)
  {
    int numDoors = 0;
    double rand = Math.random();
    if(depth == 1) numDoors = 4;
    if(depth == 2) //|| depth == 3)
    {
      if(rand >= 0.5) numDoors = 4;
      else if(rand >= 0.25) numDoors = 3;
      else if(rand >= 0.125) numDoors = 2;
      else if(rand >= 0.0625) numDoors = 1;
    }
    else if(depth > 2)
    {
      if(rand >= 0.5) numDoors = 0;
      else if(rand >= 0.25) numDoors = 1;
      else if(rand >= 0.125) numDoors = 2;
      else if(rand >= 0.0625) numDoors = 3;
    }
    if(numDoors == 0 && roomList.size() < Settings.numObjectsGuranteed) numDoors = 4;
    return numDoors;
  }

  private Doorway makeDoorwayAt(Direction dir , int startX, int startY, int widthOrHeight)
  {
    Random generator = new Random();
    Doorway newDoorway = new Doorway();
    int doorwaySize = generator.nextInt(2)+2;
    if(dir == Direction.EAST || dir == Direction.WEST)
    {
      int startDoorY = generator.nextInt(widthOrHeight - 4) + startY + 2;
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
      startDoorX = generator.nextInt(widthOrHeight-4)+startX+2;
      for (int i = 0; i < doorwaySize; i++)
      {
        //Corner
        if(startDoorX + i ==  startX+ widthOrHeight-1)
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
  private Doorway makeNewDoorway(Direction curSide, int startX, int startY, int width, int height)
  {
    Doorway newDoorway = new Doorway();
    switch(curSide)
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

  private Direction oppositeDirOf(Direction dir)
  {
    switch(dir)
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

  //Will be very sophisticated at some point
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
    Room newRoom = makeNewRoom(prevHallDoorX, prevHallDoorY, comingFrom,
                                      doorwayList.get(0), prevRoomDoorSize);
    //Room newRoom = makeNewRoom(prevRoomDoorX, prevRoomDoorY, comingFrom,
    //                                  doorwayList.get(0), prevRoomDoorSize);

    //Hitting another already made room
    for(Room other: roomList)
    {
      if(newRoom.getBoundingBox().intersects(other.getBoundingBox()) ||
         newHallway.getBoundingBox().intersects(other.getBoundingBox()))
      {
        prevRoom.removeDoorway(prevDoorway);
        roomList.remove(newHallway);
        roomList.remove(newRoom);
        return;
      }
    }
    //Going outside the house
    if(!this.getBoundingBox().contains(newRoom.getBoundingBox())||
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
    if(!newRoom.isAHallway())
    {
      for (int i = 1; i < numDoors; i++)
      {
        Direction curSide = directions.get(i-1);
        doorwayList.add(makeNewDoorway(curSide, newRoom.getStartX(), newRoom.getStartY(),
           newRoom.getWidth(), newRoom.getHeight()));
      }
    }
    newRoom.setDoorways(doorwayList);
    //if(depth >= 5) return;
    if(!newRoom.isAHallway())
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
    newRoom.spawnZombies();
  }

  private void copyObjectsToGrid()
  {
    int startX, startY;
    int width, height;
    for(Room room: roomList)
    {
      startX = room.getStartX();
      startY = room.getStartY();
      width = room.getWidth()+startX;
      height = room.getHeight()+startY;

      for(int i = startY; i < height; i++)
      {
        for(int j = startX; j < width; j++)
        {
          fullGrid[i][j] = room.getTileAt(i,j);
        }
      }
    }
  }

  private void makeGraph()
  {
    int startX, startY;
    int width, height;
    for(Room room: roomList)
    {
      startX = room.getStartX();
      startY = room.getStartY();
      width = room.getWidth() + startX;
      height = room.getHeight() + startY;

      for (int i = startY; i < height; i++)
      {
        for (int j = startX; j < width; j++)
        {
          Point newPoint = new Point(j,i);
          for (int k = 0; k < Util.NUM_DIRECTIONS; k++)
          {
            int dx = Util.dx[k];
            int dy = Util.dy[k];
            if(j + dx > -1 && i + dy > -1 && j + dx < gridWidth && i + dy < gridHeight)
            {
              Point neighbor = new Point(j + dx, i + dy);
              if (fullGrid[i + dy][j + dx] == null) continue;
              if (fullGrid[i + dy][j + dx].isSolid() ||
                  fullGrid[i][j].isSolid())
              {
                //graphOfGrid.setEdge(newPoint, neighbor, Float.MAX_VALUE);
                //Point2D.Float newPointTile = new Point2D.Float((float) newPoint.getX()*Settings.tileSize+(Settings.tileSize/2),
                //             (float)newPoint.getY()*Settings.tileSize+(Settings.tileSize/2));
                //Point2D.Float neigbTile = new Point2D.Float((float) neighbor.getX()*Settings.tileSize+(Settings.tileSize/2),
                //                                            (float)neighbor.getY()*Settings.tileSize+(Settings.tileSize/2));
                //connections.add(new Line2D.Float(newPointTile,neigbTile));

              }
              else
              {
                //Point newPointTile = new Point((int) newPoint.getX()*Settings.tileSize+(Settings.tileSize/2),
                //             (int)newPoint.getY()*Settings.tileSize+(Settings.tileSize/2));
                //Point neigbTile = new Point((int) neighbor.getX()*Settings.tileSize+(Settings.tileSize/2),
                //                                            (int)neighbor.getY()*Settings.tileSize+(Settings.tileSize/2));
                //connections.add(new Line2D.Float(newPointTile,neigbTile));
                graphOfGrid.setEdge(newPoint, neighbor, 1.0f);
              }
            }
          }
        }
      }
    }
  }

  private void makeMasterZombie()
  {
    ArrayList<Entity> zombies = new ArrayList<>();
    entityManager.getAllEntities(true).forEach(entity ->
    {
      if (entity instanceof ZombieModel) zombies.add(entity);
    });

    if (zombies.size() > 0)
    {
      int index = Util.rng.nextInt(zombies.size());
      Entity zombie = zombies.get(index);
      master = new MasterZombie(player, new Point2D.Float(zombie.getPosition().x, zombie.getPosition().y));
      entityManager.remove(zombie);
      zombies.remove(index);
      zombies.forEach(entity -> ((ZombieModel) entity).setMasterZombie(master));
      entityManager.add(master);
    }
  }

  private boolean makeExit(boolean makeFarAway)
  {
    Collections.shuffle(roomList);
    Point roomStart = new Point();
    for(Room room: roomList)
    {
      roomStart.setLocation(room.getStartX()*Settings.tileSize, room.getStartY()*Settings.tileSize);
      Collections.shuffle(room.wallList);
      if(room.isLeaf() && !room.equals(startRoom) &&
         (!makeFarAway || roomStart.distance(player.getPosition()) > Settings.distanceAwayExit * Settings.tileSize))
      {
        for (Wall wall : room.wallList) {
          if (wall != null) {
            entityManager.add(new Exit(wall.getDirection(), wall.getStartX(), wall.getStartY(), wall.getEndX(), wall.getEndY()));
            return true;
          }
        }
      }
    }
    return false;
  }
}
