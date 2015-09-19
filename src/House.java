import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  public static Tile[][] fullGrid;
  private EntityManager entityManager;
  private ArrayList<Room> roomList;
  public static BufferedImage houseImg;
  private int gridHeight, gridWidth;

  public House(int gridWidth, int gridHeight, EntityManager entityManager)
  {
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    this.entityManager = entityManager;
    int tileSize = Settings.tileSize;
    roomList = new ArrayList<>();
    fullGrid = new Tile[gridHeight][gridWidth];
    houseImg = new BufferedImage(gridWidth*tileSize, gridHeight*tileSize, BufferedImage.TYPE_INT_ARGB);
    int roomStartX = 15;
    int roomStartY = 15;
    int startWidth = 8;
    int startHeight = 10;
    Room startRoom = new Room(roomStartX,roomStartY,startWidth, startHeight, entityManager);
    startRoom.init();
    roomList.add(startRoom);
    //generateRoomList(doorwayX, doorwayY, 3, Direction.NORTH, 1);
    copyObjectsToGrid();
    //TODO: Hallways
    generateBuffImgHouse();
  }

  private enum Direction
  {
    NORTH,WEST,SOUTH,EAST;
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
    Room startRoom = new Room(startX, startY, width, height, entityManager);
    startRoom.init();
    ArrayList<Direction> directions = new ArrayList<>();
    ArrayList<Doorway> doorwayList = new ArrayList<>();
    Collections.addAll(directions, Direction.values());
    for (Direction dir: directions)
    {
      addDoorwayOn(dir, startX, startY, width, height, doorwayList);
    }
    startRoom.setDoorways(doorwayList);
    return startRoom;
  }

  private Room makeNewRoom(int prevRoomDoorX, int prevRoomDoorY, Direction comingFrom,
                           ArrayList<Point2D.Float> doorwayList, int prevRoomDoorSize)
  {
    Room newRoom = null;
    Random generator = new Random();
    int offsetFromCenter = generator.nextInt(5)+5;
    int newWidth = generator.nextInt(4)+12;
    int newHeight = generator.nextInt(4)+12;
    switch(comingFrom)
    {
      case NORTH:
        newRoom = new Room(prevRoomDoorX-offsetFromCenter, prevRoomDoorY-newHeight,newWidth, newHeight, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          doorwayList.add(new Point2D.Float(prevRoomDoorX+i,prevRoomDoorY-1));
        }
      break;
      case WEST:
        newRoom = new Room(prevRoomDoorX-newWidth, prevRoomDoorY-offsetFromCenter,newWidth, newHeight, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          doorwayList.add(new Point2D.Float(prevRoomDoorX-1,prevRoomDoorY-+i));
        }
      break;
      case SOUTH:
        newRoom = new Room(prevRoomDoorX-offsetFromCenter, prevRoomDoorY+1,newWidth, newHeight, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          doorwayList.add(new Point2D.Float(prevRoomDoorX-+i,prevRoomDoorY+1));
        }
      break;
      case EAST:
        newRoom = new Room(prevRoomDoorX+1, prevRoomDoorY-offsetFromCenter,newWidth, newHeight, entityManager);
        for (int i = 0; i < prevRoomDoorSize; i++)
        {
          doorwayList.add(new Point2D.Float(prevRoomDoorX+1,prevRoomDoorY-+i));
        }
      break;
    }
    return newRoom;
  }

  private int calculateNumDoors(int depth)
  {
    int numDoors = 0;
    double rand = Math.random();
    if(depth == 1) numDoors = 4;
    if(depth == 2)
    {
      if(rand >= 0.5) numDoors = 4;
      if(rand >= 0.25) numDoors = 3;
      if(rand >= 0.125) numDoors = 2;
      if(rand >= 0.0625) numDoors = 1;
    }
    else if(depth > 2)
    {
      double probab = (1/(4^depth));
      if(rand >= probab) numDoors = 4;
      if(rand >= (probab/2)) numDoors = 3;
      if(rand >= (probab/4)) numDoors = 2;
      if(rand >= (probab/8)) numDoors = 1;
    }
    return numDoors;
  }

  private void addDoorwayOn(Direction curSide, int startX, int startY, int width, int height,ArrayList<Doorway> doorwayList)
  {
    Random generator = new Random();
    int doorwaySize = generator.nextInt(2)+2;
    switch(curSide)
    {
      case NORTH:
        //generator.nextInt(startX
    }
  }


  //Will be very sophisticated at some point
  private void generateRoomList(int prevRoomDoorX,int prevRoomDoorY, int prevRoomDoorSize, Direction comingFrom, int depth)
  {
    ArrayList<Doorway> doorwayList = new ArrayList<>();
    ArrayList<Direction> directions = new ArrayList<>();
    int numDoors;
    Room newRoom = makeNewRoom(prevRoomDoorX, prevRoomDoorY, comingFrom,
                               doorwayList.get(0).getPointsinDoor(), prevRoomDoorSize);
    Collections.addAll(directions, Direction.values());
    directions.remove(comingFrom);
    Collections.shuffle(directions);
    numDoors = calculateNumDoors(depth);
    for (int i = 1; i < numDoors; i++)
    {
      Direction curSide = directions.get(i);
      addDoorwayOn(curSide, newRoom.getStartX(), newRoom.getStartY(),
                   newRoom.getWidth(), newRoom.getHeight(), doorwayList);
    }
    newRoom.setDoorways(doorwayList);
    newRoom.init();
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
