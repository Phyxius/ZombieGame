import java.awt.*;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public interface Entity
{
  void draw(Graphics2D local, Graphics2D screen); //local = origin centered at upper-left corner of object, screen = origin at upper-left corner of screen
  Point[] getPosition(); //returns array of grid squares occupied by object
  boolean isSolid(); //solid objects cannot move into each other
  void keyPressed(keyEventArgs e);
  void update(UpdateManager e); //called for each update tick, EntityManager contains methods to add/remove/etc entities
  void onCollision(Entity other); //called when collided with other entity
}
