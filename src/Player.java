import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity implements LightSource, Detonator
{
  private final SoundEffect FOOTSTEP_SFX = new SoundEffect("soundfx/player_footstep.mp3");
  ;
  private final Animation IDLE_ANIMATION = new Animation("animation/player/idle_", 8, true);
  private final Animation MOVE_ANIMATION = new Animation("animation/player/move_", 13, true);
  private final ProgressBar STAMINA_PROGRESS_BAR = new ProgressBar("Stamina ", new Font("SansSerif", Font.PLAIN, Settings.tileSize / 4));
  private final SoundEffect TRAP_INTERACTION_SFX = new SoundEffect("soundfx/player_pickup.mp3");
  private final ProgressBar TRAP_PROGRESS_BAR = new ProgressBar("", new Font("SansSerif", Font.PLAIN, Settings.tileSize / 4));
  private Point2D.Float center = new Point2D.Float();
  private Trap collidingTrap = null;
  private boolean flipAnimation;
  private boolean isPickingUp;
  private boolean isPlacing;
  private boolean isRunning = false;
  private boolean moving;
  private Point2D.Float position = new Point2D.Float(44 * Settings.tileSize, 45 * Settings.tileSize);
  private int progressCounter = 0;
  private int soundCounter = 0;
  private float stamina = Settings.playerStamina;
  private boolean staminaDepleted;
  private int trapsInInventory = 1;

  public Player()
  {
    this.setLightLocation(getBoundingBox());
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    AffineTransform transformer = new AffineTransform();
    if (flipAnimation)
    {
      BufferedImage frame = (moving ? MOVE_ANIMATION.getFrame() : IDLE_ANIMATION.getFrame());
      transformer.scale(-1, 1);
      transformer.translate(-frame.getWidth(), 0);
      AffineTransformOp opTransformer = new AffineTransformOp(transformer, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      frame = opTransformer.filter(frame, null);
      transformer.setToScale((double) Settings.tileSize / 80, (double) Settings.tileSize / 80);
      local.drawImage(frame, transformer, null);
    }
    else
    {
      transformer.setToScale((double) Settings.tileSize / 80, (double) Settings.tileSize / 80);
      local.drawImage((moving ? MOVE_ANIMATION.getFrame() : IDLE_ANIMATION.getFrame()), transformer, null);
    }
    if (isPickingUp() || isPlacing)
    {
      TRAP_PROGRESS_BAR.setPosition((float) (position.x - drawingManager.getCameraOrigin().getX() - Settings.tileSize / 2), (float) (position.y - drawingManager.getCameraOrigin().getY() - Settings.tileSize / 4));
      TRAP_PROGRESS_BAR.setWidth(2 * Settings.tileSize);
      TRAP_PROGRESS_BAR.setHeight(Settings.tileSize / 4);
      TRAP_PROGRESS_BAR.draw(null, global, null);
    }

    STAMINA_PROGRESS_BAR.setPosition(Settings.tileSize, (float) global.getClipBounds().getHeight() - Settings.tileSize);
    STAMINA_PROGRESS_BAR.setWidth(2 * Settings.tileSize);
    STAMINA_PROGRESS_BAR.setHeight(Settings.tileSize / 4);
    STAMINA_PROGRESS_BAR.draw(null, global, null);
  }

  @Override
  public int getDepth()
  {
    return 110;
  }

  @Override
  public Point2D.Float getLightLocation()
  {
    return center;
  }

  @Override
  public void setLightLocation(Rectangle2D.Float boundingBox)
  {
    center.setLocation(boundingBox.getCenterX(), boundingBox.getCenterY());
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  @Override
  public void setLightLocation(Point2D.Float location)
  {
    center.setLocation(location.getX(), location.getY());
  }

  @Override
  public boolean trigger()
  {
    return isRunning();
  }

  @Override
  public void update(UpdateManager e)
  {
    if (isPickingUp() || isPlacing)
    {
      increaseStamina();
      IDLE_ANIMATION.nextFrame(moving);
      moving = false;
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
          TRAP_INTERACTION_SFX.stop();
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
          TRAP_INTERACTION_SFX.stop();
        }
      }
      else
      {
        isPlacing = false;
        isPickingUp = false;
        TRAP_INTERACTION_SFX.stop();
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
      if (xMovement > 0) flipAnimation = false;
      else if (xMovement < 0) flipAnimation = true;
      // Normalize movement vector.
      double movementMagnitude = Math.sqrt(xMovement * xMovement + yMovement * yMovement);
      xMovement /= movementMagnitude;
      yMovement /= movementMagnitude;

      if (movementMagnitude != 0)
      {
        isRunning = e.isKeyPressed(KeyEvent.VK_R) && !staminaDepleted;
        move((float) getPosition().getX(), (float) getPosition().getY(), xMovement, yMovement, isRunning, e);
        MOVE_ANIMATION.nextFrame(!moving);
        moving = true;
      }
      else
      {
        IDLE_ANIMATION.nextFrame(moving);
        moving = false;
        increaseStamina();
      }

      if (e.isKeyPressed(KeyEvent.VK_P) && collidingTrap != null)
      {
        isPickingUp = true;
        progressCounter++;
        TRAP_INTERACTION_SFX.play(0.0, 1.0);
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
        TRAP_INTERACTION_SFX.play(0.0, 1.0);
        updatePickUpBar();
      }
      else
      {
        progressCounter = 0;
        isPlacing = false;
      }
    }
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

  private void decreaseStamina()
  {
    if (stamina > 0) stamina--;
    else stamina = 0;
    isStaminaDepleted();
    updateStaminaBar();
  }

  private void increaseStamina()
  {
    if (++stamina > Settings.playerStamina) stamina = Settings.playerStamina;
    isStaminaDepleted();
    updateStaminaBar();
  }

  private boolean isPickingUp()
  {
    return isPickingUp;
  }

  private boolean isRunning()
  {
    return isRunning;
  }

  /**
   * Used to indicate that stamina was recently depleted.
   *
   * @return Returns a true value while stamina < Settings.frameRate * 2 (2 seconds) after the player stamina reaches zero.
   */
  private boolean isStaminaDepleted()
  {
    staminaDepleted = stamina == 0 || (staminaDepleted && stamina < Settings.frameRate * 2);
    STAMINA_PROGRESS_BAR.setColor(staminaDepleted ? Color.RED : Color.GREEN);
    return staminaDepleted;
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
        if (soundCounter % (Settings.frameRate / 2) == 0) FOOTSTEP_SFX.play(0.0, 10);
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
        if (soundCounter % Settings.frameRate == 0) FOOTSTEP_SFX.play(0.0, 10);
        soundCounter++;
      }
    }
  }

  private void updatePickUpBar()
  {
    TRAP_PROGRESS_BAR.setCurrentValue(progressCounter);
    TRAP_PROGRESS_BAR.setMaxValue(5 * Settings.frameRate);
  }

  private void updateStaminaBar()
  {
    STAMINA_PROGRESS_BAR.setCurrentValue((int) stamina);
    STAMINA_PROGRESS_BAR.setMaxValue((int) Settings.playerStamina);
  }

  private class ProgressBar
  {
    private final String LABEL;
    private final Font LABEL_FONT;
    private final Point2D.Float POSITION;
    private Color color;
    private int currentValue;
    private int height;
    private int maxValue;
    private int width;

    ProgressBar(String label, Font font)
    {
      setCurrentValue(-1);
      setMaxValue(-1);
      setWidth(2 * Settings.tileSize);
      setHeight(Settings.tileSize / 4);
      setColor(Color.GREEN);
      this.LABEL = label;
      this.POSITION = new Point2D.Float();
      this.LABEL_FONT = font;
    }

    public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
    {
      if (currentValue > -1)
      {
        Font originalFont = global.getFont();
        global.setFont(LABEL_FONT);
        Color originalColor = global.getColor();
        global.setColor(color);
        int x = (int) POSITION.x;
        int y = (int) POSITION.y;
        global.drawString(LABEL, x, y);
        x += global.getFontMetrics().stringWidth(LABEL);
        y -= global.getFontMetrics().getHeight() / 2;
        global.drawRect(x, y, width, height);
        global.fillRect(x + 2, y + 2, (int) ((double) currentValue / maxValue * (width - 4)), height - 4);
        global.setColor(Color.BLACK);
        global.drawRect(x + 1, y + 1, width - 2, height - 2);
        global.setFont(originalFont);
        global.setColor(originalColor);
      }
    }

    public void setColor(Color color)
    {
      this.color = color;
    }

    public void setCurrentValue(int currentValue)
    {
      this.currentValue = currentValue;
    }

    public void setMaxValue(int maxValue)
    {
      this.maxValue = maxValue;
    }

    public void setPosition(float x, float y)
    {
      this.POSITION.setLocation(x, y);
    }

    public void setWidth(int width)
    {
      this.width = (width < 5 ? 5 : width);
    }

    private void setHeight(int height)
    {
      this.height = (height < 5 ? 5 : height);
    }
  }
}
