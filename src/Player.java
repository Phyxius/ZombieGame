import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity
{
  private Point2D.Float position;
  private boolean isRunning;
  private boolean isPickingUp;

  void setPosition(Point2D.Float position)
  {
    this.position = position;
  }

  @Override
  public void draw (Graphics2D local, Graphics2D global)
  {
    int tileSize = Settings.tileSize;
    local.setColor(Color.YELLOW);
    local.drawRoundRect(0, 0, tileSize, tileSize, tileSize/10, tileSize/10);
  }

  @Override
  public Point2D.Float getPosition()

  {
    return this.position;
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  @Override
  public void update(UpdateManager e)
  {
    float horizontalSpeed = 0, veriticalSpeed = 0;
    if (e.isKeyPressed(KeyEvent.VK_LEFT)) horizontalSpeed -= Settings.playerSpeed;
    if (e.isKeyPressed(KeyEvent.VK_RIGHT)) horizontalSpeed += Settings.playerSpeed;
    if (e.isKeyPressed(KeyEvent.VK_UP)) veriticalSpeed -= Settings.playerSpeed;
    if (e.isKeyPressed(KeyEvent.VK_DOWN)) veriticalSpeed += Settings.playerSpeed;
    float magnitude = horizontalSpeed * horizontalSpeed + veriticalSpeed * veriticalSpeed;
    horizontalSpeed /= magnitude;
    veriticalSpeed /= magnitude;
    position.setLocation(position.x + horizontalSpeed, position.y + veriticalSpeed);
  }

  public boolean isPickingUp()
  {
    return isPickingUp;
  }
}
