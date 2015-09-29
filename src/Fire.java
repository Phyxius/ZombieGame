/**
 * Created by arirappaport on 9/7/15.
 * Provides the functionality and characteristics of a fire.
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Fire extends Entity implements Detonator
{
  private final boolean LIGHT_SOURCE;
  private final int numFireFrames;
  Animation fireAnimation;
  private int age = 0; //frames
  private Rectangle2D explosionArea;
  private BufferedImage fireBackground;
  private BufferedImage frame;

  public Fire(Rectangle2D.Float explosionArea, boolean isLightSource)
  {
    this.explosionArea = explosionArea;
    LIGHT_SOURCE = isLightSource;
    numFireFrames = 318;
    fireBackground = ResourceManager.getImage("fire/firebackground.png");
    fireAnimation = new Animation("animation/fireAnimation/fire-", numFireFrames);
  }

  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    //Animate fire
    local.drawImage(fireBackground, 0, 0, Settings.tileSize, Settings.tileSize, null);
    local.drawImage(frame, 0, 0, Settings.tileSize, Settings.tileSize, null);
  }

  public Rectangle2D.Float getBoundingBox() //returns bounding box of object
  {
    return (Rectangle2D.Float) explosionArea;
  }

  public int getDepth()
  {
    return 3;
  }

  @Override
  public boolean isLightSource()
  {
    return LIGHT_SOURCE;
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
