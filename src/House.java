import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  public static Tile[][] fullGrid;
  private EntityManager entityManager;
  private ArrayList<Room> roomList;
  private ArrayList<Doorway> initDoorways;
  private Player player;
  public static BufferedImage houseImg;
  private int gridHeight, gridWidth;
  private int prevHallDoorX, prevHallDoorY;

  public House(int gridWidth, int gridHeight, EntityManager entityManager)
  {
    Random generator = new Random();
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    this.entityManager = entityManager;
    int tileSize = Settings.tileSize;
    roomList = new ArrayList<>();
    initDoorways = new ArrayList<>();
    fullGrid = new Tile[gridHeight][gridWidth];
    houseImg = new BufferedImage(gridWidth*tileSize, gridHeight*tileSize, BufferedImage.TYPE_INT_ARGB);
    int roomStartX = 40;
    int roomStartY = 40;
    int startWidth = 8;
    int startHeight = 10;
    Room startRoom = makeInitRoom(roomStartX, roomStartY, startWidth, startHeight, entityManager);
    roomList.add(startRoom);
    Doorway randStartDoor = initDoorways.get(0);
    Point2D.Float startPoint = randStartDoor.getPointAt(0);
    generateRoomList((int) startPoint.getX(), (int) startPoint.getY(), randStartDoor.getLengthOfDoorway(),
       randStartDoor.getSideOfRoom(), startRoom, 1);
    Doorway randStartDoor2 = initDoorways.get(1);
    Point2D.Float startPoint2 = randStartDoor2.getPointAt(0);
    generateRoomList((int) startPoint2.getX(), (int)startPoint2.getY(),randStartDoor2.getLengthOfDoorway(),
                     randStartDoor2.getSideOfRoom(), startRoom, 1);
    copyObjectsToGrid();
    generateBuffImgHouse();
  }

  public enum Direction
  {
    NORTH,WEST,SOUTH,EAST
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.drawImage(houseImg, 0, 0, houseImg.getWidth(), houseImg.getHeight(), null);
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
            position.x + gridWidth*Settings.tileSize, position.y + gridHeight*Settings.tileSize);
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
    return -1;
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
      initDoorways.add(makeNewDoorway(dir1, startX, startY, width, height));
    //}
    startRoom.setDoorways(initDoorways);
    startRoom.init();
    return startRoom;
  }

  private Room makeNewRoom(int prevRoomDoorX, int prevRoomDoorY, Direction comingFrom,
                                  Doorway firstdoor, int prevRoomDoorSize)
  {
    Room newRoom = null;
    Random generator = new Random();
    int offsetFromCenter = 2;//generator.nextInt(2)+2;
    int newWidth = generator.nextInt(8)+6;
    int newHeight = generator.nextInt(8)+6;
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
    newRoom.spawnZombies();
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
    //else if(depth > 2)
    //{
    //  double probab = (1/(4^depth));
    //  if(rand <= probab) numDoors = 4;
    //  else if(rand <= (probab*2)) numDoors = 3;
    //  else if(rand <= (probab*4)) numDoors = 2;
    //  else if(rand <= (probab*8)) numDoors = 1;
    //}
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
  private void generateRoomList(int prevRoomDoorX,int prevRoomDoorY, int prevRoomDoorSize, Direction comingFrom,
                                Room prevRoom, int depth)
  {
    ArrayList<Doorway> doorwayList = new ArrayList<>();
    ArrayList<Direction> directions = new ArrayList<>();
    doorwayList.add(new Doorway());
    int numDoors;
    //Room newHallway = makeNewHallway(prevRoomDoorX, prevRoomDoorY, comingFrom, prevRoomDoorSize);
    //Room newRoom = makeNewRoom(prevHallDoorX, prevHallDoorY, comingFrom,
    //                                  doorwayList.get(0), prevRoomDoorSize);
    Room newRoom = makeNewRoom(prevRoomDoorX, prevRoomDoorY, comingFrom,
                                      doorwayList.get(0), prevRoomDoorSize);
    prevRoom.setNeighbor(comingFrom,newRoom);
    Collections.addAll(directions, Direction.values());
    directions.remove(oppositeDirOf(comingFrom));
    Collections.shuffle(directions);
    numDoors = calculateNumDoors(depth);
    if(!newRoom.isAHallway())
    {
      for (int i = 1; i < numDoors - 1; i++)
      {
        Direction curSide = directions.get(i);
        doorwayList.add(makeNewDoorway(curSide, newRoom.getStartX(), newRoom.getStartY(),
           newRoom.getWidth(), newRoom.getHeight()));
      }
    }
    //if(depth >= 5) return;
    if(!newRoom.isAHallway())
    {
      for (int i = 1; i < numDoors-1; i++)
      {
        //if(roomList.size() > 8) return;
        Doorway curDoorway = doorwayList.get(i);
        Point2D.Float startOfDoor = curDoorway.getPointAt(0);
        generateRoomList((int) startOfDoor.x, (int) startOfDoor.y,
           curDoorway.getLengthOfDoorway(), curDoorway.getSideOfRoom(), newRoom, ++depth);
      }
    }
    //newHallway.init();
    newRoom.setDoorways(doorwayList);
    newRoom.init();
    //roomList.add(newHallway);
    roomList.add(newRoom);
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

  private void generateBuffImgHouse()
  {
    int tileSize = Settings.tileSize;

    for(int i = 0; i < gridHeight; i++)
    {
      for(int j = 0; j < gridWidth; j++)
      {
        if(fullGrid[i][j] == null) continue; //fullGrid[i][j] = new Tile("tileset/outofbounds1", true);
        houseImg.createGraphics().drawImage(fullGrid[i][j].getTileImg(), j*tileSize, i*tileSize, tileSize, tileSize, null);
      }
    }
  }
}
