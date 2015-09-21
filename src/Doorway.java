import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by arirappaport on 9/19/15.
 */
public class Doorway
{
  private ArrayList<Point2D.Float> pointsinDoor;
  private int lengthOfDoor;
  private House.Direction sideOfRoom;

  public Doorway()
  {
    pointsinDoor = new ArrayList<>();
  }

  public void setLengthOfDoorway(int lengthOfDoor) {this.lengthOfDoor = lengthOfDoor;}

  public int getLengthOfDoorway() {return lengthOfDoor;}

  public ArrayList<Point2D.Float> getPointsinDoor() {return pointsinDoor;}

  public void addPointInDoorway(int i, Point2D.Float point){pointsinDoor.add(i,point); }

  public Point2D.Float getPointAt(int i) {return pointsinDoor.get(i);}

  public void addPoint(Point2D.Float point) {pointsinDoor.add(point);}

  public House.Direction getSideOfRoom() {return sideOfRoom;}

  public void setSideOfRoom(House.Direction side) {sideOfRoom = side;}
}
