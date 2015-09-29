import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Shea on 2015-09-07.
 * Entity: Base class of all Entities
 */
public abstract class Entity
{
  /**
   * @return true if this object blocks light, false otherwise.
   */
  boolean blocksLight()
  {
    return false;
  }

  /**
   * Draws the entity to the given canvas.
   * No guarantee is provided how often it will be called relative to Update(), so
   * <b>do not</b> change any state in this method.
   *
   * @param local          a Graphics2D object clipped to the bounding box reported by the entity
   * @param screen         a Graphics2D object clipped to the screen
   * @param drawingManager An interface that allows read-only access to the global game state
   */
  public void draw(Graphics2D local, Graphics2D screen,
                   DrawingManager drawingManager) //local = origin centered at upper-left corner of object, screen = origin at upper-left corner of screen
  {
  }

  /**
   * @return The object's bounding box. By default returns null if getPosition() returns null,
   * or a rectangle whose upper left corner is returned by getPosition() and whose width and height
   * are equal to one tile in size.
   */
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    Point2D.Float position = getPosition();
    if (position == null) return null;
    return new Rectangle2D.Float(position.x, position.y, Settings.tileSize, Settings.tileSize);
  }

  /**
   * Get's the Entity's 'depth'. Entities with higher depths are drawn above entities
   * with lower depths.
   *
   * @return the depth.
   */
  public int getDepth() //lower numbers are drawn above higher numbers
  {
    return 0;
  }

  /**
   * @return the upper left corner of this object
   */
  Point2D.Float getPosition() //returns upper left point of the object
  {
    return null;
  }

  /**
   * @return true if this object should emanate light, false otherwise.
   */
  boolean isLightSource()
  {
    return false;
  }

  /**
   * @return true if this object is 'solid' and false otherwise. It is up to
   * other entities to make use of this information; no default behavior is specified.
   */
  boolean isSolid() //solid objects cannot move into each other
  {
    return false;
  }

  /**
   * Called when a key is pressed.
   *
   * @param e the corresponding KeyEvent object.
   */
  public void keyPressed(KeyEvent e)
  {

  }

  /**
   * Called when a key is pressed.
   *
   * @param e the corresponding KeyEvent object.
   */
  public void keyReleased(KeyEvent e)
  {

  }

  /**
   * Called after updates have been resolved each tick, once for each object this object
   * intersects with.
   *
   * @param other the object being collided with
   * @param c     used to access global state
   */
  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {

  }

  /**
   * Called when the entity is removed from the game
   */
  public void onRemoved()
  {

  }

  /**
   * Updates the object's state, and allows read-write access to global
   * state via an UpdateManager.
   * Called as close to Settings.framerate times per second as is feasible.
   *
   * @param e the UpdateManager to use
   */
  public void update(
      UpdateManager e) //called for each update tick, EntityManager contains methods to add/remove/etc entities
  {

  }
}
