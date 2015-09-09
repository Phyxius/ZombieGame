import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Trap extends Entity
{
  protected int tileSize;
  protected Point2D.Float upperLeftCorner;
  protected Point2D.Float center;
  protected Rectangle2D boundingRect;

  public Trap(Point2D.Float upperLeftCorner)
  {
    this.upperLeftCorner = upperLeftCorner;
    tileSize = Settings.tileSize;
    boundingRect = new Rectangle((int) upperLeftCorner.getX(),(int) upperLeftCorner.getY(),tileSize , tileSize);
    int centerX = (int) upperLeftCorner.getX() + (tileSize / 2);
    int centerY = (int) upperLeftCorner.getY() + (tileSize / 2);
    center.setLocation(centerX, centerY);
  }

  public void draw(Graphics2D local, Graphics2D screen)
  {
    local.setColor(Color.red);
    local.drawRect((int) upperLeftCorner.getX(), (int) upperLeftCorner.getY(), tileSize, tileSize);
  }
  public Point2D.Float getPosition() //returns center point of object
  {
    return center;
  }
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return (Rectangle2D.Float) boundingRect;
  }
  public boolean isSolid() //solid objects cannot move into each other
  {
    return true;
  }

  private void detonate()
  {
    int fireX = (int) upperLeftCorner.getX() - tileSize;
    int fireY = (int) upperLeftCorner.getY() - tileSize;
    Rectangle2D.Float explosionArea = new Rectangle2D.Float(fireX, fireY, 3 * tileSize, 3 * tileSize);
    new Fire(explosionArea);
  }

  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {
    if(other instanceof LineZombie)
    {
      detonate();
    }
    if(other instanceof Player)
    {
      Player player = (Player) other;
      if(player.isRunning())
      {
        detonate();
      }
      else if(player.isPickingUp())
      {
        c.remove(this);
      }
    }
  }
}
