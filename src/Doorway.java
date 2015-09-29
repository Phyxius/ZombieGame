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

  /**
   * Adds a point of the Doorway
   * to the specified location
   *
   * @param point The Point to be added.
   */
  public void addPoint(Point2D.Float point)
  {
    pointsinDoor.add(point);
  }

  /**
   * Adds a point in the doorway at a specified location
   *
   * @param i     Location of point.
   * @param point Point is used for Coords.
   */
  public void addPointInDoorway(int i, Point2D.Float point)
  {
    pointsinDoor.add(i, point);
  }

  /**
   * Gets the length of the Doorway.
   *
   * @return Returns the length of the Doorway.
   */
  public int getLengthOfDoorway()
  {
    return lengthOfDoor;
  }

  /**
   * @param lengthOfDoor The length of the doorway.
   */
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
