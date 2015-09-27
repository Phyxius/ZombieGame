import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2015-09-07.
 * Class name and description go here.
 */
public class EntityManager
{
  private ArrayList<Entity> entities = new ArrayList<>();
  private ArrayList<Entity> entitiesToRemove = new ArrayList<>();
  private ArrayList<Entity> entitiesToAdd = new ArrayList<>();
  private Comparator<Entity> depthComparator = Comparator.comparingInt(Entity::getDepth);
  private HashMap<Integer, Boolean> keyDict = new HashMap<>();
  public boolean debugModeEnabled = true;
  private Entity entityToFollow = null;
  private Point2D.Float cameraOrigin = new Point2D.Float(0, 0);

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

  public void draw(Graphics2D g)
  {
    if (entityToFollow != null) updateCameraOrigin(g.getClipBounds());
    DrawingManager drawingManager = new DrawingManager(this);
    for (Entity entity : entities)
    {
      final Rectangle2D.Float boundingBox = entity.getBoundingBox();
      Graphics2D screen = (Graphics2D) g.create();
      if (boundingBox != null)
      {
        Graphics2D local = (Graphics2D) g.create((int)(boundingBox.x - cameraOrigin.x), (int) (boundingBox.y - cameraOrigin.y),
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

  private void updateCameraOrigin(Rectangle cameraBounds)
  {
    cameraOrigin.setLocation(entityToFollow.getBoundingBox().getCenterX() - cameraBounds.getWidth() / 2,
        entityToFollow.getBoundingBox().getCenterY() - cameraBounds.getHeight() / 2);
  }

  public void keyPressed(KeyEvent e)
  {
    keyDict.put(e.getKeyCode(), true);
    entities.forEach(ent -> ent.keyPressed(e));
  }

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

  public void remove(Entity entity)
  {
    entitiesToRemove.add(entity);
  }

  public void remove(Entity[] entities)
  {
    Collections.addAll(entitiesToRemove, entities);
  }

  public void remove(Collection<Entity> entities)
  {
    entitiesToRemove.addAll(entities);
  }

  public void add(Entity e)
  {
    entitiesToAdd.add(e);
  }

  public void add(Entity[] entities)
  {
    Collections.addAll(entitiesToAdd, entities);
  }

  public void add(Collection<Entity> entities)
  {
    entitiesToAdd.addAll(entities);
  }

  public Collection<Entity> getAllEntities()
  {
    return new ArrayList<>(entities);
  }

  public Collection<Entity> getCollidingEntities(Shape boundingBox)
  {
    return entities.parallelStream().filter(e -> e.getBoundingBox() != null)
        .filter(e -> boundingBox.intersects(e.getBoundingBox())).collect(Collectors.toList());
  }

  public Collection<Entity> getIntersectingEntities(Point2D.Float point)
  {
    return entities.parallelStream().filter(e -> e.getBoundingBox() != null)
        .filter(e -> e.getBoundingBox().contains(point)).collect(Collectors.toList());
  }

  public boolean isKeyPressed(int keyCode)
  {
    return keyDict.get(keyCode);
  }

  public Point2D.Float getCameraOrigin()
  {
    return new Point2D.Float(cameraOrigin.x, cameraOrigin.y);
  }

  public void setCameraOrigin(Point2D.Float cameraOrigin)
  {
    this.cameraOrigin = cameraOrigin;
  }

  public Entity getEntityToFollow()
  {
    return entityToFollow;
  }

  public void setEntityToFollow(Entity entityToFollow)
  {
    this.entityToFollow = entityToFollow;
  }
}
