import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity
{
  private Point2D.Float position;

  void setPosition(Point2D.Float position)
  {
    this.position = position;
  }

  @Override
  public void draw (Graphics2D local, Graphics2D global)
  {
    int tileSize = SettingsManager.getInteger("tileSize");
    local.setColor(Color.YELLOW);
    local.drawRoundRect(0, 0, tileSize, tileSize, tileSize/10, tileSize/10);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }
}
