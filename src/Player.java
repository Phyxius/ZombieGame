import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
  private final ProgressBar STAMINA_PROGRESS_BAR = new ProgressBar("gui/label_stamina.png");
  private final String TRAP_ICON_PATH = "fire/firetrap.png";
  private final SoundEffect TRAP_INTERACTION_SFX = new SoundEffect("soundfx/player_pickup.mp3");
  private final ProgressBar TRAP_PROGRESS_BAR = new ProgressBar("");
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
      transformer.setToScale((double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE,
          (double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE);
      local.drawImage(frame, transformer, null);
    }
    else
    {
      transformer.setToScale((double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE,
          (double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE);
      local.drawImage((moving ? MOVE_ANIMATION.getFrame() : IDLE_ANIMATION.getFrame()), transformer, null);
    }
    if (isPickingUp() || isPlacing)
    {
      TRAP_PROGRESS_BAR
          .setPosition((float) (position.x - drawingManager.getCameraOrigin().getX() - Settings.tileSize / 2),
              (float) (position.y - drawingManager.getCameraOrigin().getY() - Settings.tileSize / 4));
      TRAP_PROGRESS_BAR.draw(null, global, null);
    }
      STAMINA_PROGRESS_BAR.setPosition(Settings.tileSize, (float) global.getClipBounds().getHeight() -
          Settings.tileSize);
    STAMINA_PROGRESS_BAR.draw(null, global, null);

    global.drawImage(ResourceManager.getImage(TRAP_ICON_PATH), Settings.tileSize,
        (int) (global.getClipBounds().getHeight() - 2.5 * Settings.tileSize), Settings.tileSize, Settings.tileSize,
        null);
    Font font = new Font("SansSerif", Font.PLAIN, Settings.tileSize / 2);
    global.setFont(font);
    global.setColor(Color.GREEN);
    global.drawString("X " + trapsInInventory, (int) (2.3 * Settings.tileSize),
        (int) (global.getClipBounds().getHeight() - 1.8 * Settings.tileSize));
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
  public void setLightLocation(Point2D.Float location)
  {
    center.setLocation(location.getX(), location.getY());
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
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
          Trap trap = new Trap(new Point2D.Float((int) (center.getX() / Settings.tileSize) * Settings.tileSize,
              (int) (center.getY() / Settings.tileSize) * Settings.tileSize));
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
    STAMINA_PROGRESS_BAR.changeColor(staminaDepleted);
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
    private final BufferedImage[] imageList = new BufferedImage[5];
    private final Point2D.Float POSITION;
    private boolean changeColor;
    private int currentValue;
    private int maxValue;

    ProgressBar(String labelPath)
    {

      if (!labelPath.equals("")) imageList[0] =  ResourceManager.getImage(labelPath);
      imageList[1] = ResourceManager.getImage("gui/progress_border_green.png");
      imageList[2] = ResourceManager.getImage("gui/progress_fill_green.png");
      imageList[3] = ResourceManager.getImage("gui/progress_border_red.png");
      imageList[4] = ResourceManager.getImage("gui/progress_fill_red.png");
      setCurrentValue(-1);
      setMaxValue(-1);
      this.POSITION = new Point2D.Float();
    }

    public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
    {
      int xPosition = (int) POSITION.getX();
      int yPosition = (int) POSITION.getY();
      int imageWidth = 2 * Settings.tileSize;
      int imageHeight = (int) (0.375 * Settings.tileSize);
      int offset = 0;
      if (imageList[0] != null)
      {
        offset = imageWidth;
        global.drawImage(imageList[0], xPosition, yPosition, imageWidth, imageHeight, null);
      }
      int fillWidth = (int) ((float) currentValue / maxValue * imageWidth);
      if (changeColor)
      {
        global.drawImage(imageList[3], xPosition + offset, yPosition, imageWidth, imageHeight, null);
        global.drawImage(imageList[4], xPosition + offset, yPosition, fillWidth , imageHeight, null);
      }
      else
      {
        global.drawImage(imageList[1], xPosition + offset, yPosition, imageWidth, imageHeight, null);
        global.drawImage(imageList[2], xPosition + offset, yPosition, fillWidth, imageHeight, null);
      }
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

    public void changeColor (boolean changeColor)
    {
      this.changeColor = changeColor;
    }
  }
}
