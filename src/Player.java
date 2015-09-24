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
  private Point2D.Float position = new Point2D.Float(44 * Settings.tileSize, 45 * Settings.tileSize);
  private Point2D.Float center = new Point2D.Float();
  private final SoundEffect playerFootsteps;
  private final SoundEffect playerPickup;
  private boolean isRunning = false;
  private float stamina;
  private boolean staminaDepleted = false;
  private boolean isPickingUp;
  private boolean isPlacing;
  private int progressCounter = 0;
  private int soundCounter = 0;
  private int trapsInInventory = 1;
  private Trap collidingTrap = null;
  private final ProgressBar pickUpBar;
  private final ProgressBar staminaBar;

  public Player()
  {
    playerFootsteps = new SoundEffect("soundfx/player_footstep.mp3");
    playerPickup = new SoundEffect("soundfx/player_pickup.mp3");
    pickUpBar = new ProgressBar("", new Font("SansSerif", Font.PLAIN, Settings.tileSize / 4));
    staminaBar = new ProgressBar("Stamina ", new Font("SansSerif", Font.PLAIN, Settings.tileSize / 4));
    stamina = Settings.playerStamina;
    this.setLightLocation(getBoundingBox());
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    int tileSize = Settings.tileSize;
    local.setColor(Color.YELLOW);
    local.fillRoundRect(0, 0, tileSize, tileSize, tileSize / 10, tileSize / 10);
    if (isPickingUp() || isPlacing)
    {
      pickUpBar.setPosition((float) (position.x - drawingManager.getCameraOrigin().getX() - Settings.tileSize / 2), (float) (position.y - drawingManager.getCameraOrigin().getY() - Settings.tileSize / 4));
      pickUpBar.setWidth(2 * Settings.tileSize);
      pickUpBar.setHeight(Settings.tileSize / 4);
      pickUpBar.draw(null, global, null);
    }

    staminaBar.setPosition(Settings.tileSize, (float) global.getClipBounds().getHeight() - Settings.tileSize);
    staminaBar.setWidth(2 * Settings.tileSize);
    staminaBar.setHeight(Settings.tileSize / 4);
    staminaBar.draw(null, global, null);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  private boolean isRunning()
  {
    return isRunning;
  }

  @Override
  public void update(UpdateManager e)
  {
    if (isPickingUp() || isPlacing)
    {
      increaseStamina();
      if (e.isKeyPressed(KeyEvent.VK_P))
      {
        progressCounter++;
        updatePickUpBar();
        if (progressCounter % (5 * Settings.frameRate) == 0)
        {
          trapsInInventory++;
          e.remove(collidingTrap);
          collidingTrap = null;
          isPickingUp = false;
          playerPickup.stop();
        }
      }
      else if (e.isKeyPressed(KeyEvent.VK_T))
      {
        progressCounter++;
        updatePickUpBar();
        if (progressCounter % (5 * Settings.frameRate) == 0)
        {
          trapsInInventory--;
          Trap trap = new Trap(new Point2D.Float((int) (center.getX() / Settings.tileSize) * Settings.tileSize, (int) (center.getY() / Settings.tileSize) * Settings.tileSize));
          e.add(trap);
          collidingTrap = trap;
          isPlacing = false;
          playerPickup.stop();
        }
      }
      else
      {
        isPlacing = false;
        isPickingUp = false;
        playerPickup.stop();
        progressCounter = 0;
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
      else
      {
        increaseStamina();
      }

      if (e.isKeyPressed(KeyEvent.VK_P) && collidingTrap != null)
      {
        isPickingUp = true;
        progressCounter++;
        playerPickup.play(0.0, 1.0);
        updatePickUpBar();
      }
      else
      {
        progressCounter = 0;
        isPickingUp = false;
      }

      if (e.isKeyPressed(KeyEvent.VK_T) && trapsInInventory > 0 && collidingTrap == null)
      {
        progressCounter++;
        isPlacing = true;
        playerPickup.play(0.0, 1.0);
        updatePickUpBar();
      }
      else
      {
        progressCounter = 0;
        isPlacing = false;
      }
    }
  }

  private boolean isPickingUp()
  {
    return isPickingUp;
  }

  /**
   * Used to indicate that stamina was recently depleted.
   *
   * @return Returns a true value while stamina < Settings.frameRate * 2 (2 seconds) after the player stamina reaches zero.
   */
  private boolean isStaminaDepleted()
  {
    staminaDepleted = stamina == 0 || (staminaDepleted && stamina < Settings.frameRate * 2);
    return staminaDepleted;
  }

  private void increaseStamina()
  {
    if (++stamina > Settings.playerStamina) stamina = Settings.playerStamina;
    updateStaminaBar();
  }

  private void decreaseStamina()
  {
    if (stamina > 0) stamina--;
    else stamina = 0;
//    System.out.println (stamina);
    updateStaminaBar();
  }

  private void updateStaminaBar()
  {
    staminaBar.setCurrentValue((int) stamina);
    staminaBar.setMaxValue((int) Settings.playerStamina);
  }

  private void updatePickUpBar()
  {
    pickUpBar.setCurrentValue(progressCounter);
    pickUpBar.setMaxValue(5 * Settings.frameRate);
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

  private void move(float x, float y, float xMovement, float yMovement, boolean isRunning, UpdateManager manager)
  {
    if (isRunning)
    {
      position.setLocation(x + xMovement * Settings.playerRun, y + yMovement * Settings.playerRun);
      if (checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY()))
      {
        position.setLocation(x, y);
        increaseStamina();
      }
      else // successful movement
      {
        setLightLocation(getBoundingBox());
        if (soundCounter % (Settings.frameRate / 4) == 0) playerFootsteps.play(0.0, 10);
        soundCounter++;
        decreaseStamina();
      }
    }
    else
    {
      increaseStamina();
      position.setLocation(x + xMovement * Settings.playerWalk, y + yMovement * Settings.playerWalk);
      if (checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY()))
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

  private class ProgressBar
  {
    private final String label;
    private final Point2D.Float position;
    private final Font font;
    private int fontSize;
    private int currentValue;
    private int maxValue;
    private int width;
    private int height;

    ProgressBar(String label, Font font)
    {
      setFontSize(6);
      setCurrentValue(-1);
      setMaxValue(-1);
      setWidth(2 * Settings.tileSize);
      setHeight(Settings.tileSize / 4);
      this.label = label;
      this.position = new Point2D.Float();
      this.font = font;
    }

    private void setHeight(int height)
    {
      this.height = (height < 5 ? 5 : height);
    }

    public void setWidth(int width)
    {
      this.width = (width < 5 ? 5 : width);
    }

    public void setMaxValue(int maxValue)
    {
      this.maxValue = maxValue;
    }

    public void setCurrentValue(int currentValue)
    {
      this.currentValue = currentValue;
    }

    public void setFontSize(int fontSize)
    {
      if (fontSize < 6) this.fontSize = 6;
      else if (fontSize < 20) this.fontSize = 20;
      else this.fontSize = 20;
    }

    public void setPosition(float x, float y)
    {
      this.position.setLocation(x, y);
    }

    public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
    {
      if (currentValue > -1)
      {
//        if (label.equals("")) System.out.printf ("%s Print Progress, [%5.2f,%5.2f], %d, %d, %d, %d, %d\n", label, position.x, position.y, currentValue, maxValue, width, height, fontSize);
        setFontSize(Settings.tileSize / 4);

        Font originalFont = global.getFont();
        global.setFont(font);

        Color originalColor = global.getColor();
        global.setColor(Color.GREEN);

        int x = (int) position.x;
        int y = (int) position.y;
        global.drawString(label, x, y);
        x += global.getFontMetrics().stringWidth(label);
        y -= global.getFontMetrics().getHeight() / 2;
        if (currentValue == 0)
        {
          global.setColor(Color.RED);
          global.drawRect(x, y, width, height);
        }
        else
        {
          global.drawRect(x, y, width, height);
          global.fillRect(x + 2, y + 2, (int) ((double) currentValue / maxValue * (width - 4)), height - 4);
          global.setColor(Color.BLACK);
          global.drawRect(x + 1, y + 1, width - 2, height - 2);
        }

        global.setFont(originalFont);
        global.setColor(originalColor);
      }
    }
  }
}
