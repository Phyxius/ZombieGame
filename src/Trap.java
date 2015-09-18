import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Trap extends Entity
{
  protected int tileSize;
  protected Point2D.Float position;
  private BufferedImage trapImg;
  private EntityManager entityManager;
  private int[] dx = {-1, 0, 1,-1, 0, 1,-1, 0, 1};
  private int[] dy = {-1,-1,-1, 0, 0, 0, 1, 1, 1};

  public Trap(Point2D.Float position, EntityManager entityManager)
  {
    this.position = position;
    this.entityManager = entityManager;
    tileSize = Settings.tileSize;
    trapImg = ResourceManager.getImage("stills/firetrap.png");
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.setColor(Color.red);
    local.drawImage(trapImg, 0,0, tileSize, tileSize, null);
  }

  @Override
  public Point2D.Float getPosition() //returns center point of object
  {
    return position;
  }

  @Override
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return super.getBoundingBox();
  }

  public boolean isSolid() //solid objects cannot move into each other
  {
    return false;
  }

  @Override
  public int getDepth()
  {
    return 100;
  }

  @Override
  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {
    if (other instanceof LineZombie)
    {
      detonate();
      c.remove(this);
    }
    if (other instanceof Player)
    {
      //Player player = (Player) other;
      //if (player.isRunning())
      //{
        detonate();
      //} else if (player.isPickingUp())
      //{
        c.remove(this);
      //}
    }
  }

  private void detonate()
  {
    for(int i = 0; i < 9; i++)
    {
      int curDx = dx[i];
      int curDy = dy[i];
      Rectangle2D.Float explosionArea = new Rectangle2D.Float(position.x+tileSize*curDx, position.y+tileSize*curDy, tileSize, tileSize);
      entityManager.add(new Fire(explosionArea));
    }
    //Rectangle2D.Float explosionArea = new Rectangle2D.Float(position.x-tileSize, position.y-tileSize, tileSize*3, tileSize*3);
    //entityManager.add(new Fire(explosionArea));

  }
}
