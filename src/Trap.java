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
  private static int[] dx = {-1, 0, 1,-1, 1,-1, 0, 1};
  private static int[] dy = {-1,-1,-1, 0, 0, 1, 1, 1};

  public Trap(Point2D.Float upperLeftCorner)
  {
    this.upperLeftCorner = upperLeftCorner;
    tileSize = SettingsManager.getInteger("tilesize");
    boundingRect = new Rectangle((int) upperLeftCorner.getX(),(int) upperLeftCorner.getY(),tileSize , tileSize);
    int centerX = (int) upperLeftCorner.getX() + SettingsManager.getInteger("tilesize") / 2;
    int centerY = (int) upperLeftCorner.getY() + SettingsManager.getInteger("tilesize") / 2;
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

  private void dentonate()
  {
    //Detonation animation
    for(int i = 0; i < 8; i++)
    {
      int curDx = dx[i];
      int curDy = dy[i];
    }

  }

  public void onCollision(Entity other, CollisionManager c) //called when collided with other entity
  {
    if(other instanceof ZombieModel)
    {
      detonate();
    }

  }
}
