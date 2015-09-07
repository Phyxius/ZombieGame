import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Trap extends Entity
{
  protected Point2D.Float upperLeftCorner;
  protected Point2D.Float center;
  protected boolean isTriggered;

  public Trap(Point2D.Float upperLeftCorner)
  {
    this.upperLeftCorner = upperLeftCorner;
    int centerX = (int) upperLeftCorner.getX() + SettingsManager.getInteger("tileSize") / 2;
    int centerY = (int) upperLeftCorner.getY() + SettingsManager.getInteger("tileSize") / 2;
    center.setLocation(centerX, centerY);
  }

  public void setIsTriggered(boolean isTriggered)
  {
    this.isTriggered = isTriggered;
  }

  public void draw(Graphics2D local, Graphics2D screen)
  {

  }
  public Point2D.Float getPosition() //returns center point of object
  {
    return center;
  }
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return null;
  }
  public boolean isSolid() //solid objects cannot move into each other
  {
    return false;
  }
  public void keyPressed(KeyEvent e)
  {

  }
  public void keyReleased(KeyEvent e)
  {

  }
  public void update(UpdateManager e) //called for each update tick, EntityManager contains methods to add/remove/etc entities
  {

  }
  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {

  }
}
