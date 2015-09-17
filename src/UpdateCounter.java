import java.awt.*;

/**
 * Created by Shea on 2015-09-13.
 * Class name and description go here.
 */
public class UpdateCounter extends Entity
{
  int updates;

  @Override
  public void update(UpdateManager e)
  {
    updates++;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    screen.setColor(Color.green);
    screen.drawString("" + updates, 100, 100);
  }

  @Override
  public int getDepth()
  {
    return Integer.MAX_VALUE;
  }
}
