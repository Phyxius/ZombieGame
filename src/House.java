import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  public static Tile[][] fullGrid;
  private ArrayList<Room> roomList;
  public static BufferedImage houseImg;
  private int gridHeight, gridWidth;

  public House(int gridWidth, int gridHeight)
  {
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    int tileSize = Settings.tileSize;
    roomList  = new ArrayList<>();
    fullGrid = new Tile[gridHeight][gridWidth];
    houseImg = new BufferedImage(gridWidth*tileSize, gridHeight*tileSize, BufferedImage.TYPE_3BYTE_BGR);
    generateRoomList();
    copyRoomsToGrid();
    //TODO: Hallways
    generateBuffImgHouse();
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen)
  {
    screen.drawImage(houseImg, 0, 0, houseImg.getWidth(), houseImg.getHeight(), null);
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
            position.x + gridWidth, position.y + gridHeight);
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

  //Will be very sophisticated at some point
  private void generateRoomList()
  {
    Room room1 = new Room(new Point(0,0), 8, 8);
    Room room2 = new Room(new Point(9,12), 4, 4);
    Room room3 = new Room(new Point(3, 20), 9, 4);
    roomList.add(room1);
    roomList.add(room2);
    roomList.add(room3);
  }

  private void copyRoomsToGrid()
  {
    int startX, startY;
    int width, height;
    for(Room room: roomList)
    {
      startX = (int)room.getStartPoint().getX();
      startY = (int)room.getStartPoint().getY();
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
        if(fullGrid[i][j] == null) fullGrid[i][j] = new Tile("tileset/outofbounds1", true);
        houseImg.createGraphics().drawImage(fullGrid[i][j].getTileImg(), j*tileSize, i*tileSize, tileSize, tileSize, null);
      }
    }
  }
}
