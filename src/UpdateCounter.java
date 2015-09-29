import javax.swing.*;
import java.awt.*;

/**
 * Created by Shea on 2015-09-13.
 * UpdateCounter: keeps a count of how many update ticks occur
 * each second.
 */
public class UpdateCounter extends Entity
{
  int updates;

  public UpdateCounter()
  {
    new Timer(1000, (e -> updates = 0)).start();
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

  @Override
  public void update(UpdateManager e)
  {
    updates++;
  }
}
