import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity
{
  private Point2D.Float position = new Point2D.Float(800, 800);
  private SoundEffect playerFootsteps;
  private boolean isRunning;
  private boolean isPickingUp;
  private int soundCounter;

  public Player()
  {
    playerFootsteps = new SoundEffect("soundfx/player_footstep.mp3");
    soundCounter = 0;
  }

  void setPosition(Point2D.Float position)
  {
    this.position = position;
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    int tileSize = Settings.tileSize;
    local.setColor(Color.YELLOW);
    local.fillRoundRect(0, 0, tileSize, tileSize, tileSize/10, tileSize/10);
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
    if (e.isKeyPressed(KeyEvent.VK_LEFT)) horizontalSpeed -= 1;
    if (e.isKeyPressed(KeyEvent.VK_RIGHT)) horizontalSpeed += 1;
    if (e.isKeyPressed(KeyEvent.VK_UP)) veriticalSpeed -= 1;
    if (e.isKeyPressed(KeyEvent.VK_DOWN)) veriticalSpeed += 1;
    float magnitude = horizontalSpeed * horizontalSpeed + veriticalSpeed * veriticalSpeed;
    if (magnitude != 0)
    {
      if(soundCounter % 10 == 0)playerFootsteps.play(0.0,10);
      horizontalSpeed /= magnitude;
      veriticalSpeed /= magnitude;
      position.setLocation(position.x + horizontalSpeed * Settings.playerSpeed, position.y + veriticalSpeed * Settings.playerSpeed);
      soundCounter++;
    }
  }

  public boolean isPickingUp()
  {
    return isPickingUp;
  }
}
