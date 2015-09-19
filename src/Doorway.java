import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by arirappaport on 9/19/15.
 */
public class Doorway
{
  private ArrayList<Point2D.Float> pointsinDoor;
  private Point2D.Float startOfDoor;
  private int lengthOfDoor;

  public void setStartOfDoor(Point2D.Float startOfDoor) {this.startOfDoor = startOfDoor;}

  public Point2D.Float getStartOfDoor() {return startOfDoor;}

  public void setLengthOfDoorway(int lengthOfDoor) {this.lengthOfDoor = lengthOfDoor;}

  public int getLengthOfDoorway() {return lengthOfDoor;}

  public ArrayList<Point2D.Float> getPointsinDoor() {return pointsinDoor;}

  public void setPointinDoorway(int i, Point2D.Float point){pointsinDoor.add(i,point); }

  public Point2D.Float getPointAt(int i) {return pointsinDoor.get(i);}
}
