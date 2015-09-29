/**
 * Created by arirappaport on 9/19/15.
 * Provides doorways used in generating a house.
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Doorway
{
  private int lengthOfDoor;
  private ArrayList<Point2D.Float> pointsinDoor;
  private House.Direction sideOfRoom;

  /**
   * Creates empty doorway object.
   */
  public Doorway()
  {
    pointsinDoor = new ArrayList<>();
  }

  public void addPoint(Point2D.Float point)
  {
    pointsinDoor.add(point);
  }

  public void addPointInDoorway(int i, Point2D.Float point)
  {
    pointsinDoor.add(i, point);
  }

  public int getLengthOfDoorway()
  {
    return lengthOfDoor;
  }

  public void setLengthOfDoorway(int lengthOfDoor)
  {
    this.lengthOfDoor = lengthOfDoor;
  }

  public Point2D.Float getPointAt(int i)
  {
    return pointsinDoor.get(i);
  }

  public ArrayList<Point2D.Float> getPointsinDoor()
  {
    return pointsinDoor;
  }

  public House.Direction getSideOfRoom()
  {
    return sideOfRoom;
  }

  public void setSideOfRoom(House.Direction side)
  {
    sideOfRoom = side;
  }
}
