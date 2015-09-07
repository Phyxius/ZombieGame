import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

  public void update()
  {
    ProcessAdditionsAndRemovals();
    UpdateManager updateManager = new UpdateManager(this);
    entities.forEach(e -> e.update(updateManager));
    CollisionManager collisionManager = new CollisionManager(this);
    for (Entity entity : entities)
    {
      Rectangle2D.Float boundingBox = entity.getBoundingBox();
      for (Entity other : entities)
      {
        if (entity == other) continue;
        if (other.getBoundingBox().intersects(boundingBox))
        {
          entity.onCollision(other, collisionManager);
        }
      }
    }
    ProcessAdditionsAndRemovals();

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

  public Collection<Entity> getCollidingEntities(Rectangle2D.Float boundingBox)
  {
    return entities.parallelStream().filter(e -> e.getBoundingBox()
        .intersects(boundingBox)).collect(Collectors.toList());
  }

  public Collection<Entity> getIntersectingEntities(Point2D.Float point)
  {
    return entities.parallelStream().filter(e -> e.getBoundingBox()
        .contains(point)).collect(Collectors.toList());
  }
}
