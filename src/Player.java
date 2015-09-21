import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity
{
  private Point2D.Float position = new Point2D.Float(44*Settings.tileSize, 45*Settings.tileSize);
  private SoundEffect playerFootsteps;
  private boolean isRunning = false;
  private float stamina;
  private boolean staminaDepleted = false;
  private boolean isPickingUp;
  private int soundCounter = 0;
  private int trapsInInventory = 0;

  public Player()
  {
    playerFootsteps = new SoundEffect("soundfx/player_footstep.mp3");
    stamina = Settings.playerStamina;
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
    local.fillRoundRect(0, 0, tileSize, tileSize, tileSize / 10, tileSize / 10);
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
    if (isPickingUp())
    {
      return;
    }
    else
    {
      float xMovement = 0, yMovement = 0;
      if (e.isKeyPressed(KeyEvent.VK_LEFT) || e.isKeyPressed(KeyEvent.VK_A)) xMovement -= 1;
      if (e.isKeyPressed(KeyEvent.VK_RIGHT) || e.isKeyPressed(KeyEvent.VK_D)) xMovement += 1;
      if (e.isKeyPressed(KeyEvent.VK_UP) || e.isKeyPressed(KeyEvent.VK_W)) yMovement -= 1;
      if (e.isKeyPressed(KeyEvent.VK_DOWN) || e.isKeyPressed(KeyEvent.VK_S)) yMovement += 1;

      // Normalize movement vector.
      double movementMagnitude = Math.sqrt(xMovement * xMovement + yMovement * yMovement);
      xMovement /= movementMagnitude;
      yMovement /= movementMagnitude;

      if (movementMagnitude != 0)
      {
        float xPosition = position.x;
        float yPosition = position.y;
        isRunning = e.isKeyPressed(KeyEvent.VK_R) && !isStaminaDepleted();
        if (isRunning)
        {
          stamina--;
          position.setLocation(position.x + xMovement * Settings.playerRun, position.y + yMovement * Settings.playerRun);
          if (!checkCollision(e, xPosition, yPosition))
          {
            if (soundCounter % (Settings.frameRate / 4) == 0) playerFootsteps.play(0.0, 10);
            soundCounter++;

          }
          else
          {
            replenishStamina();
          }
        }
        else
        {
          replenishStamina();
          position.setLocation(position.x + xMovement * Settings.playerWalk, position.y + yMovement * Settings.playerWalk);
          if (!checkCollision(e, xPosition, yPosition))
          {
            if (soundCounter % (Settings.frameRate / 3) == 0) playerFootsteps.play(0.0, 10);
            soundCounter++;
          }
        }
      }
      else
      {
        replenishStamina();
      }
    }
  }

  public boolean isPickingUp()
  {
    return isPickingUp;
  }

  /**
   * Used to indicate that stamina was recently depleted.
   *
   * @return Returns a true value while stamina < Settings.frameRate * 2 (2 seconds) after the player stamina reaches zero.
   */
  public boolean isStaminaDepleted()
  {
    if (stamina == 0 || (staminaDepleted && stamina < Settings.frameRate * 2)) staminaDepleted = true;
    else staminaDepleted = false;
    return staminaDepleted;
  }

  private void replenishStamina()
  {
    if (stamina < Settings.playerStamina) stamina++;
  }

  private boolean checkCollision(UpdateManager manager, float xPosition, float yPosition)
  {
    final boolean[] returnValue = {false};
    manager.getCollidingEntities(this.getBoundingBox()).forEach((Entity entity) -> {
      if (entity != this && entity.isSolid())
      {
        this.position.x = xPosition;
        this.position.y = yPosition;
        returnValue[0] = true;
      }
    });
    return returnValue[0];
  }
}
