import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2015-09-07.
 * EntityManager: The central component of the game engine.
 * Controls global game state and dispatches events to Entities
 */
public class EntityManager
{
  private ArrayList<Entity> entities = new ArrayList<>();
  private ArrayList<Entity> entitiesToRemove = new ArrayList<>();
  private ArrayList<Entity> entitiesToAdd = new ArrayList<>();
  private Comparator<Entity> depthComparator = Comparator.comparingInt(Entity::getDepth);
  private HashMap<Integer, Boolean> keyDict = new HashMap<>();
  public boolean debugModeEnabled = false;
  private Entity entityToFollow = null;
  private Point2D.Float cameraOrigin = new Point2D.Float(0, 0);

  private float scale = 1;

  public EntityManager()
  {
    initializeKeyboardDict();
  }

  //Add every possible key value to the dict, initialized to false
  private void initializeKeyboardDict()
  {
    for (Field field : KeyEvent.class.getDeclaredFields())
    {
      if (!Modifier.isStatic(field.getModifiers()) || !int.class.isAssignableFrom(field.getType())) continue;
      try
      {
        keyDict.put(field.getInt(null), false);
      }
      catch (IllegalAccessException ignored) { }//must not be a public field, so don't need it
    }
  }

  /**
   * Advances the game world one tick
   */
  public void update()
  {
    ProcessAdditionsAndRemovals();
    UpdateManager updateManager = new UpdateManager(this);
    entities.forEach(e -> e.update(updateManager));
    CollisionManager collisionManager = new CollisionManager(this);
    for (Entity entity : entities)
    {
      final Rectangle2D.Float boundingBox = entity.getBoundingBox();
      if (boundingBox == null) continue;
      for (Entity other : entities)
      {
        if (entity == other) continue;
        final Rectangle2D.Float otherBoundingBox = other.getBoundingBox();
        if (otherBoundingBox == null) continue;
        if (otherBoundingBox.intersects(boundingBox))
        {
          entity.onCollision(other, collisionManager);
        }
      }
    }
    ProcessAdditionsAndRemovals();
  }

  /**
   * Draws the game state to the given Graphics2D object
   * @param g the Graphics2D to draw to
   * @param graphicsConfiguration the GraphicsConfiguration associated with the Graphics2D
   */
  public void draw(Graphics2D g, GraphicsConfiguration graphicsConfiguration)
  {
    if (entityToFollow != null) updateCameraOrigin(g.getClipBounds());
    DrawingManager drawingManager = new DrawingManager(this, graphicsConfiguration, g.getClipBounds().width / scale, g.getClipBounds().height / scale, g.getClipBounds().width, g.getClipBounds().height);
    for (Entity entity : entities)
    {
      final Rectangle2D.Float boundingBox = entity.getBoundingBox();
      Graphics2D screen = (Graphics2D) g.create();
      Graphics2D scaledScreen = (Graphics2D) screen.create();
      scaledScreen.scale(scale, scale);
      if (boundingBox != null)
      {
        Graphics2D local = (Graphics2D) scaledScreen.create((int)(boundingBox.x - cameraOrigin.x), (int) (boundingBox.y - cameraOrigin.y),
            ((int) boundingBox.width), ((int) boundingBox.height));
        entity.draw(local, screen, drawingManager);
        if (debugModeEnabled)
        {
          local.setColor(Color.GREEN);
          local.drawRect(0, 0, (int) boundingBox.width - 1, (int) boundingBox.height - 1);
        }
        local.dispose();
      }
      else entity.draw(null, screen, drawingManager);
      screen.setColor(Color.green);
      screen.dispose();
    }
  }

  /**
   * Updates the camera's clipping rectangle with the given bounds
   * @param cameraBounds the bounds to use
   */

  private void updateCameraOrigin(Rectangle cameraBounds)
  {
    cameraOrigin.setLocation(entityToFollow.getBoundingBox().getCenterX() - cameraBounds.getWidth() / scale / 2,
        entityToFollow.getBoundingBox().getCenterY() - cameraBounds.getHeight() / scale / 2);
  }

  /**
   * Sends a KeyPressed event to all entities
   * @param e the KeyEvent's args
   */
  public void keyPressed(KeyEvent e)
  {
    keyDict.put(e.getKeyCode(), true);
    entities.forEach(ent -> ent.keyPressed(e));
  }

  /**
   * Sends a KeyReleased event to all entities
   * @param e the KeyEvent's args
   */
  public void keyReleased(KeyEvent e)
  {
    keyDict.put(e.getKeyCode(), false);
    entities.forEach(ent -> ent.keyReleased(e));
  }

  private void ProcessAdditionsAndRemovals()
  {

    entities.removeAll(entitiesToRemove);
    entitiesToRemove.clear();
    entities.addAll(entitiesToAdd);
    entitiesToAdd.clear();
    entities.sort(depthComparator);
  }

  /**
   * Schedules the given entity for removal next tick
   * @param entity the entity to remove
   */
  public void remove(Entity entity)
  {
    entitiesToRemove.add(entity);
  }

  /**
   * Schedules the given entities for removal next tick
   * @param entities the entities to remove
   */
  public void remove(Entity[] entities)
  {
    Collections.addAll(entitiesToRemove, entities);
  }

  /**
   * Schedules the given entities for removal next tick
   * @param entities the entities to remove
   */
  public void remove(Collection<Entity> entities)
  {
    entitiesToRemove.addAll(entities);
  }

  /**
   * Schedules the given entity for addition next tick
   * @param e the entity to add
   */
  public void add(Entity e)
  {
    entitiesToAdd.add(e);
  }

  /**
   * Schedules the given entities for addition next tick
   * @param entities the entities to add
   */
  public void add(Entity[] entities)
  {
    Collections.addAll(entitiesToAdd, entities);
  }

  /**
   * Schedules the given entities for addition next tick
   * @param entities the entities to add
   */
  public void add(Collection<? extends Entity> entities)
  {
    entitiesToAdd.addAll(entities);
  }

  /**
   * @return A collection of all currently-added entities
   */
  public Collection<Entity> getAllEntities()
  {
    return new ArrayList<>(entities);
  }

  /**
   * Get's all added entities, optionally taking into account as yet un-added/un-removed entities
   * @param includeUnprocessed whether or not to included not-yet-processed entity additions/removals
   * @return the list of entities
   */
  public Collection<Entity> getAllEntities(boolean includeUnprocessed)
  {
    if (!includeUnprocessed) return getAllEntities();
    ArrayList<Entity> entities = new ArrayList<>(this.entities);
    entities.removeAll(entitiesToRemove);
    entities.addAll(entitiesToAdd);
    return entities;
  }

  /**
   * Tests all entities for collision with the given bounding shape and returns the colliding ones
   * @param boundingBox the bounding shape to use
   * @return A collection of all colliding entities
   */
  public Collection<Entity> getCollidingEntities(Shape boundingBox)
  {
    return entities.parallelStream().filter(e -> e.getBoundingBox() != null)
        .filter(e -> boundingBox.intersects(e.getBoundingBox())).collect(Collectors.toList());
  }

  /**
   * Tests all entities to determine if they contain the given point
   * @param point the point to test
   * @return a Collection of all intersecting entities
   */
  public Collection<Entity> getIntersectingEntities(Point2D.Float point)
  {
    return entities.parallelStream().filter(e -> e.getBoundingBox() != null)
        .filter(e -> e.getBoundingBox().contains(point)).collect(Collectors.toList());
  }

  /**
   * Checks whether or not a key has been pressed using the EntityManager's internal
   * mapping.
   * @param keyCode the key code to check (Uses KeyEvent constants)
   * @return true if it is pressed, false otherwise
   */
  public boolean isKeyPressed(int keyCode)
  {
    return keyDict.get(keyCode);
  }

  /**
   * @return the current camera origin
   */
  public Point2D.Float getCameraOrigin()
  {
    return new Point2D.Float(cameraOrigin.x, cameraOrigin.y);
  }

  /**
   * Sets the camera origin. Note: Does not clear the entityToFollow, so may be
   * overridden next draw call
   * @param cameraOrigin the Point to use as the origin
   */
  public void setCameraOrigin(Point2D.Float cameraOrigin)
  {
    this.cameraOrigin = cameraOrigin;
  }

  /**
   * @return the entity the camera is currently following
   */
  public Entity getEntityToFollow()
  {
    return entityToFollow;
  }

  /**
   * Set's the camera's currently followed entity
   * @param entityToFollow the entity to follow
   */
  public void setEntityToFollow(Entity entityToFollow)
  {
    this.entityToFollow = entityToFollow;
  }

  /**
   * Sets the scale factor used when drawing to the screen.
   * @param scale a number between zero (exclusive) and one (inclusive).
   */
  public void setScale(float scale)
  {
    this.scale = scale;
  }

  /**
   * @return the current scale factor
   */
  public float getScale()
  {
    return scale;
  }
}
