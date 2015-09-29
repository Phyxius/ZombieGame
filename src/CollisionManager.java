/**
 * Created by Shea on 2015-09-07.
 * CollisionManager: Wraps an EntityManager to expose a subset
 * of its methods to Entities on collision events.
 */
class CollisionManager extends UpdateManager
{
  public CollisionManager(EntityManager entityManager)
  {
    super(entityManager);
  }
}
