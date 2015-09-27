import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public abstract class Entity
{
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager) //local = origin centered at upper-left corner of object, screen = origin at upper-left corner of screen
  {
  }
  public Point2D.Float getPosition() //returns upper left point of the object
  {
    return null;
  }
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    Point2D.Float position = getPosition();
    if (position == null) return null;
    return new Rectangle2D.Float(position.x, position.y,
        Settings.tileSize, Settings.tileSize);
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

  public int getDepth() //lower numbers are drawn above higher numbers
  {
    return 0;
  }

  public boolean isLightSource()
  {
    return false;
  }

  public boolean blocksLight()
  {
    return false;
  }
}
