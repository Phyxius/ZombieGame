import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  public static Tile[][] fullGrid;
  private ArrayList<RoomModel> roomList;
  private BufferedImage houseImg;
  private int gridHeight, gridWidth;

  public House(int gridWidth, int gridHeight)
  {
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    roomList  = new ArrayList<>();
    fullGrid = new Tile[gridHeight][gridWidth];
    houseImg = null;
    generateBuffImgHouse();
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen)
  {
    local.drawImage(houseImg, 0, 0, null);
  }

  private void generateRoomList()
  {
    SquareRoom room1 = new SquareRoom(new Point(0,0));
    roomList.add(room1);
  }

  private void copyRoomsToGrid()
  {
    int startX, startY;
    int width, height;
    for(RoomModel room: roomList)
    {
      startX = (int)room.getStartPoint().getX();
      startY = (int)room.getStartPoint().getY();
      width = room.getWidth();
      height = room.getHeight();

      for(int i = startY; i < height; i++)
      {
        for(int j = startX; j < width; j++)
        {
          fullGrid[i][j] = room.getTileAt(i,j);
        }
      }
    }
  }
  //Will be very sophisticated at some point
  private void generateBuffImgHouse()
  {
    int tileSize = Settings.tileSize;

    for(int i = 0; i < gridHeight; i++)
    {
      for(int j = 0; j < gridWidth; j++)
      {
        houseImg.createGraphics().drawImage(fullGrid[i][j].getTileImg(), i*tileSize, j*tileSize, null);
      }
    }
  }
}
