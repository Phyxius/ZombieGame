import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public interface Entity
{
  void draw(Graphics2D local, Graphics2D screen); //local = origin centered at upper-left corner of object, screen = origin at upper-left corner of screen
  Point2D.Float getPosition(); //returns center point of object
  Rectangle2D.Float getBoundingBox(); //returns bounding box of object
  boolean isSolid(); //solid objects cannot move into each other
  void keyPressed(KeyEvent e);
  void update(UpdateManager e); //called for each update tick, EntityManager contains methods to add/remove/etc entities
  void onCollision(Entity other); //called when collided with other entity
}
