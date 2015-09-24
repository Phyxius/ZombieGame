import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity implements LightSource, Detonator
{
  private Point2D.Float position = new Point2D.Float(44*Settings.tileSize, 45*Settings.tileSize);
  private Point2D.Float center = new Point2D.Float();
  private SoundEffect playerFootsteps;
  private boolean isRunning = false;
  private float stamina;
  private boolean staminaDepleted = false;
  private boolean isPickingUp;
  private int pickUpCounter = 0;
  private int soundCounter = 0;
  private int trapsInInventory = 0;
  private Trap collidingTrap = null;

  public Player()
  {
    playerFootsteps = new SoundEffect("soundfx/player_footstep.mp3");
    stamina = Settings.playerStamina;
    this.setLightLocation(getBoundingBox());
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
      if (e.isKeyPressed(KeyEvent.VK_P))
      {
        pickUpCounter++;
        if (pickUpCounter % (5 * Settings.frameRate) == 0)
        {
          pickUpCounter = 0;
          trapsInInventory++;
          e.remove(collidingTrap);
          isPickingUp = false;
        }
      }
      else
      {
        isPickingUp = false;
        pickUpCounter = 0;
      }
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
        isRunning = e.isKeyPressed(KeyEvent.VK_R) && !isStaminaDepleted();
        move((float) getPosition().getX(), (float) getPosition().getY(), xMovement, yMovement, isRunning, e);
      }

      if (e.isKeyPressed(KeyEvent.VK_P) && collidingTrap != null)
      {
        isPickingUp = true;
        pickUpCounter++;
      }
      else
      {
        isPickingUp = false;
      }

      if (e.isKeyPressed(KeyEvent.VK_T) && trapsInInventory > 0 && collidingTrap == null)
      {
        e.add(new Trap(new Point2D.Float((int) (center.getX() / Settings.tileSize) * Settings.tileSize, (int) (center.getY() / Settings.tileSize) * Settings.tileSize)));
        trapsInInventory--;
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
    collidingTrap = null;
    manager.getCollidingEntities(this.getBoundingBox()).forEach((Entity entity) -> {
      if (entity != this && entity.isSolid())
      {
        this.position.x = xPosition;
        this.position.y = yPosition;
        returnValue[0] = true;
      }
      if (entity instanceof Trap && entity.getBoundingBox().contains(center)) collidingTrap = (Trap) entity;
    });
    return returnValue[0];
  }

  private boolean move(float x, float y, float xMovement, float yMovement, boolean isRunning, UpdateManager manager)
  {
    boolean returnValue = false;
    if (isRunning)
    {
      position.setLocation(x + xMovement * Settings.playerRun, y + yMovement * Settings.playerRun);
      if (returnValue = checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY()))
      {
        position.setLocation(x, y);
      }
      else // successful movement
      {
        setLightLocation(getBoundingBox());
        if (soundCounter % (Settings.frameRate / 4) == 0) playerFootsteps.play(0.0, 10);
        soundCounter++;
        stamina--;
      }
    }
    else
    {
      replenishStamina();
      position.setLocation(x + xMovement * Settings.playerWalk, y + yMovement * Settings.playerWalk);
      if (returnValue = checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY()))
      {
        position.setLocation(x, y);
      }
      else //successful movement
      {
        setLightLocation(getBoundingBox());
        if (soundCounter % (Settings.frameRate / 3) == 0) playerFootsteps.play(0.0, 10);
        soundCounter++;
      }
    }
    return returnValue;
  }

  @Override
  public Point2D.Float getLightLocation()
  {
    return center;
  }

  @Override
  public void setLightLocation(Point2D.Float location)
  {
    center.setLocation(location.getX(), location.getY());
  }

  @Override
  public void setLightLocation(Rectangle2D.Float boundingBox)
  {
    center.setLocation(boundingBox.getCenterX(), boundingBox.getCenterY());
  }

  @Override
  public boolean trigger()
  {
    return isRunning();
  }
}
