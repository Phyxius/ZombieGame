import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Fire extends Entity
{
  private Rectangle2D explosionArea;
  private int numFireFrames;
  private BufferedImage frame;
  Animation fireAnimation;
  public Fire(Rectangle2D.Float explosionArea)
  {
    this.explosionArea = explosionArea;
    numFireFrames = 318;
    fireAnimation = new Animation("animation/fireAnimation/fire-", numFireFrames);
  }

  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return (Rectangle2D.Float) explosionArea;
  }

  public int getDepth()
  {
    return 3;
  }

  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager) //local = origin centered at upper-left corner of object, screen = origin at upper-left corner of screen
  {
    //Animate fire
    local.drawImage(frame, 0, 0, Settings.tileSize, Settings.tileSize, null);
    //Draw ash
  }
  @Override
  public void update(UpdateManager updateManager)
  {
    frame = fireAnimation.nextFrame(false);
  }
}
