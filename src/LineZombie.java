import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * Created by Mohammad R. Yousefi on 07/09/15.
 * A simple zombie that moves in straight lines until reaching a solid obstacle.
 */
class LineZombie extends ZombieModel
{
  private Animation idleAnimation = new Animation("animation/zombie/idle_", 16, true);
  private Animation moveAnimation = new Animation("animation/zombie/move_", 16, true);
  private SoundEffect zombieStep = new SoundEffect("soundfx/zombiefoot.mp3");
  private int soundCounter = 0;
  private boolean moving = true;

  LineZombie(Player player, Point2D.Float position)
  {
    super(player, position);
  }

  LineZombie(Player player, float speed, float decisionRate, float smell, Point2D.Float position, double minAngle)
  {
    super(player, position, speed, decisionRate, smell, minAngle);
  }

  @Override
  public void update(UpdateManager e)
  {
    Point2D.Float playerPosition = player.getPosition();
    if(!(this.getPosition().distance(playerPosition) < smell*Settings.tileSize)) playerPosition = null;
    if (updateCount % decisionRate == 0)
    {
      if (playerPosition == null)
      {
        if (collision)
        {
          directionAngle += minAngle + Util.rng.nextDouble() * 2 * (Math.PI - minAngle);
          directionAngle %= 2 * Math.PI;
          moving = true;
          collision = false;
        }
      }
      else
      {
        // A* to player
        int tileSize = Settings.tileSize;
        Point zombieCenter = new Point((int)position.getX()/tileSize, (int)position.getY()/tileSize);
        Point playerCenter = new Point((int)playerPosition.getX()/tileSize, (int)playerPosition.getY()/tileSize);
        Point pointToAimAt = House.calculateAStar(zombieCenter, playerCenter);
        directionAngle = Math.atan(pointToAimAt.getY()/pointToAimAt.getX());
        moving = true;

      }
    }

    detectPlayer();
    if (moving)
    {
      float lastX = position.x;
      float lastY = position.y;

      // Change Position
      float velocity = Util.tilesPerSecondToPixelsPerFrame(speed);
      position.setLocation((float) (lastX + Math.cos(directionAngle) * velocity), (float) (lastY + Math.sin(directionAngle) * velocity));

      // Check for collisions after moving
      Collection<Entity> collisions = e.getCollidingEntities(this.getBoundingBox());
      collisions.forEach((Entity entity) -> {
        if (entity != this && entity.isSolid())
        {
          if (entity instanceof Fire)
          {
            e.remove(this);
            return;
          }
          position.setLocation(lastX, lastY); // Cancel last move and no that it collided.
          collision = true;
          moving = false;
        }
      });
    }

    if (!moving)
    {
      idleAnimation.nextFrame(moving);
      moving = false;
    }
    else
    {
      moveAnimation.nextFrame(!moving);
      moving = true;

      //double volume = 10;
      if (soundCounter % ((Settings.frameRate / 3) + 10) == 0 && loud)
      {
        double balance = (this.getPosition().x - player.getPosition().x) / Settings.tileSize ;
        zombieStep.play(balance, 0.5 / distanceFromPlayer);
      }
      //zombieStep.stop();
      soundCounter++;
    }
    updateCount++;
  }

  /**
   * Draws the zombie.
   * @param local Draws at the position of the zombie.
   * @param global Draws at the top left corner of the screen.
   * @param drawingManager The drawing manager for this object.
   */
  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    AffineTransform transformer = new AffineTransform();
    transformer.rotate(directionAngle, Settings.tileSize /2, Settings.tileSize /2); // must rotate first then scale otherwise it will cause a bug
    transformer.scale((double) Settings.tileSize / 80, (double) Settings.tileSize / 80);
    local.drawImage((moving ? moveAnimation.getFrame() : idleAnimation.getFrame()), transformer, null);
  }

  /**
   * Informs the zombie to react to a collision.
   * @param other The object that collides with this object.
   * @param collisionManager The manager for this collision.
   */
  @Override
  public void onCollision(Entity other, CollisionManager collisionManager)
  {
    if (other.isSolid())
    {
      collision = true;
      moving = false;
    }
    if (other instanceof Fire) collisionManager.remove(this);
  }

  @Override
  public int getDepth()
  {
    return 100;
  }
}
