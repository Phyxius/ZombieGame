/**
 * Created by arirappaport on 9/7/15.
 * Provides the functionality and characteristics of a fire.
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Fire extends Entity implements Detonator
{
  private final Animation fireAnimation;
  private final boolean lightSource;
  private int age = 0; //frames
  private final Rectangle2D explosionArea;
  private final BufferedImage fireBackground;
  private BufferedImage frame;

  /**
   * Creates a new fire object.
   *
   * @param explosionArea The area covered by the fire.
   * @param isLightSource Indicates that this fire provides light.
   */
  public Fire(Rectangle2D.Float explosionArea, boolean isLightSource)
  {
    this.explosionArea = explosionArea;
    lightSource = isLightSource;
    int numFireFrames = 318;
    fireBackground = ResourceManager.getImage("fire/firebackground.png");
    fireAnimation = new Animation("animation/fireAnimation/fire-", numFireFrames);
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    //Animate fire
    local.drawImage(fireBackground, 0, 0, Settings.tileSize, Settings.tileSize, null);
    local.drawImage(frame, 0, 0, Settings.tileSize, Settings.tileSize, null);
  }

  @Override
  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return (Rectangle2D.Float) explosionArea;
  }

  @Override
  public int getDepth()
  {
    return 3;
  }

  @Override
  public boolean isLightSource()
  {
    return lightSource;
  }

  @Override
  public boolean trigger()
  {
    return true;
  }

  @Override
  public void update(UpdateManager updateManager)
  {
    frame = fireAnimation.nextFrame(false);
    if (++age > Settings.fireDuration)
    {
      updateManager.remove(this);
      updateManager.add(new Ash(getBoundingBox()));
    }
  }
}
