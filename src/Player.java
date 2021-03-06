/**
 * Created by Mohammad R. Yousefi on 07/09/15.
 * Provides the functionality and characteristic of the human controlled player character in the game.
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends Entity implements Detonator
{
  private final Point2D.Float center;
  private final Animation moveAnimation = new Animation("animation/player1/moving_small_", 9, true);
  private final SoundEffect runSFX = new SoundEffect("soundfx/player_footstep_run.mp3");
  private final ProgressBar staminaBar = new ProgressBar("gui/label_stamina.png", true);
  private final ProgressBar trapBar = new ProgressBar(null, false);
  private final GuiCounter trapCounter = new GuiCounter("gui/trapIcon.png");
  private final SoundEffect trapSFX = new SoundEffect("soundfx/player_pickup.mp3");
  private final SoundEffect walkSFX = new SoundEffect("soundfx/player_footstep.mp3");
  private Trap collidingTrap = null;
  private double directionAngle;
  private final House house;
  private boolean isRunning = false;
  private boolean moving;
  private final Point2D.Float position;
  private int progressCounter = 0;
  private boolean resetFlag;
  private float stamina = Settings.playerStamina;
  private boolean staminaDepleted;
  private boolean trapAction;
  private int trapsInInventory = Settings.playerTraps;

  /**
   * Constructs a default player.
   *
   * @param house    The house that the player is in.
   * @param position The location of the player.
   */
  public Player(House house, Point2D.Float position)
  {
    this.position = position;
    center = new Point2D.Float((float) getBoundingBox().getCenterX(), (float) getBoundingBox().getCenterY());
    trapCounter.setValue(trapsInInventory);
    this.house = house;
    walkSFX.loop();
    runSFX.loop();
    trapSFX.loop();
  }

  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    // Drawing the character
    AffineTransform transformer =
        new AffineTransform(); // must rotate first then scale otherwise it will not draw properly.
    transformer.rotate(directionAngle, getBoundingBox().getWidth() / 2, getBoundingBox().getHeight() / 2);
    transformer.scale((double) Settings.tileSize / 80, (double) Settings.tileSize / 80);
    local.drawImage(
        (moving ? moveAnimation.getFrame() : ResourceManager.getImage("animation/player1/moving_small_0.png")),
        transformer, null);

    // Drawing the trap progress bar
    if (trapAction)
    {
      trapBar.setPosition(position.x - Settings.tileSize / 2, position.y - Settings.tileSize / 4);
      trapBar.draw(global, drawingManager);
    }

    // Drawing the stamina bar
    staminaBar.setPosition(Settings.tileSize * drawingManager.getScale(),
        (float) (global.getClipBounds().getHeight() - Settings.tileSize * drawingManager.getScale()));
    staminaBar.draw(global, drawingManager);
    trapCounter.setPosition(Settings.tileSize * drawingManager.getScale(),
        (float) global.getClipBounds().getHeight() - 2.15f * Settings.tileSize * drawingManager.getScale());
    trapCounter.draw(global, drawingManager);
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    Point2D.Float position = getPosition();
    if (position == null) return null;
    return new Rectangle2D.Float(position.x, position.y,
        (int) ((65.0 / Settings.DEFAULT_TILE_SIZE) * Settings.tileSize),
        (int) ((65.0 / Settings.DEFAULT_TILE_SIZE) * Settings.tileSize));
  }

  @Override
  public int getDepth()
  {
    return 1100; // Highest graphics layer since gui is attached to player.
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  @Override
  public boolean isLightSource()
  {
    return true;
  }

  @Override
  public void onCollision(Entity other, CollisionManager c)
  {
    if (resetFlag) return;
    if (other instanceof Fire || other instanceof ZombieModel)
    {
      resetFlag = true;
      triggerHouseReset(c);
    }
  }

  @Override
  public void onRemoved()
  {
    for (SoundEffect sfx : new SoundEffect[]{walkSFX, runSFX, trapSFX})
    {
      if (sfx.isPlaying()) sfx.stop();
    }
  }

  @Override
  public boolean trigger()
  {
    return isRunning();
  }

  @Override
  public void update(UpdateManager e)
  {
    resetFlag = false;
    if (e.isKeyPressed(KeyEvent.VK_P)) // Picking up or dropping traps.
    {
      increaseStamina();
      moving = false;

      if (collidingTrap == null) // Drop trap.
      {
        if (trapsInInventory > 0)
        {
          trapAction = true;
          progressCounter++;
          if (!trapSFX.isPlaying()) trapSFX.play(0.0, 1.0);
          updatePickUpBar();
        }
        if (progressCounter % (5 * Settings.frameRate) == 0) // Successfully dropped trap.
        {
          trapCounter.setValue(--trapsInInventory);
          Trap trap = new Trap(new Point2D.Float((int) (center.getX() / Settings.tileSize) * Settings.tileSize,
              (int) (center.getY() / Settings.tileSize) * Settings.tileSize));
          e.add(trap);
          collidingTrap = trap;
          trapSFX.stop();
          progressCounter = 0;
          trapAction = false;
        }
      }
      else // Pick up trap.
      {
        trapAction = true;
        progressCounter++;
        if (!trapSFX.isPlaying()) trapSFX.play(0.0, 1.0);
        updatePickUpBar();
        if (progressCounter % (5 * Settings.frameRate) == 0) // Successfully picked trap.
        {
          trapCounter.setValue(++trapsInInventory);
          e.remove(collidingTrap);
          collidingTrap = null;
          trapSFX.stop();
          progressCounter = 0;
          trapAction = false;
        }
      }
    }
    else // No trap inter actions. Check for movement input.
    {
      progressCounter = 0;
      trapAction = false;
      if (trapSFX.isPlaying()) trapSFX.stop();
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
        directionAngle = Math.atan2(yMovement, xMovement);
        isRunning = e.isKeyPressed(KeyEvent.VK_R) && !staminaDepleted;
        move((float) getPosition().getX(), (float) getPosition().getY(), xMovement, yMovement, isRunning, e);
        moveAnimation.nextFrame(!moving);
        moving = true;
      }
      else
      {
        walkSFX.stop();
        runSFX.stop();
        moving = false;
        increaseStamina();
      }

    }
  }

  private boolean checkCollision(UpdateManager manager, float xPosition, float yPosition)
  {
    final boolean[] returnValue = {false};
    collidingTrap = null;
    manager.getCollidingEntities(this.getBoundingBox()).forEach((Entity entity) -> {
      if (entity != this && entity.isSolid()) {
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
    if (stamina > 0)
    {
      stamina--;
    }
    else
    {
      stamina = 0;
    }
    isStaminaDepleted();
    updateStaminaBar();
  }

  private void increaseStamina()
  {
    if ((stamina += Settings.playerStaminaRegen) > Settings.playerStamina) stamina = Settings.playerStamina;
    isStaminaDepleted();
    updateStaminaBar();
  }

  private boolean isRunning()
  {
    return isRunning;
  }

  private boolean isStaminaDepleted()
  {
    staminaDepleted =
        stamina == 0 || (staminaDepleted && stamina < Settings.playerStaminaRegen * Settings.frameRate * 2);
    staminaBar.changeColor(staminaDepleted);
    return staminaDepleted;
  }

  private void move(float x, float y, float xMovement, float yMovement, boolean isRunning, UpdateManager manager)
  {
    if (isRunning)
    {
      position.setLocation(x + xMovement * Settings.playerRun, y + yMovement * Settings.playerRun);
      if (checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY()))
      {
        runSFX.stop();
        position.setLocation(x, y);
        increaseStamina();
      }
      else // successful movement
      {
        if (walkSFX.isPlaying()) walkSFX.stop();
        if (!runSFX.isPlaying()) runSFX.play(0.0, 1.0);
        center.setLocation(getBoundingBox().getCenterX(), getBoundingBox().getCenterY());
        decreaseStamina();
      }
    }
    else
    {
      increaseStamina();
      position.setLocation(x + xMovement * Settings.playerWalk, y + yMovement * Settings.playerWalk);
      if (checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY()))
      {
        walkSFX.stop();
        position.setLocation(x, y);
      }
      else //successful movement
      {
        if (runSFX.isPlaying()) runSFX.stop();
        if (!walkSFX.isPlaying()) walkSFX.play(0.0, 1.0);
        center.setLocation(getBoundingBox().getCenterX(), getBoundingBox().getCenterY());
      }
    }
  }

  synchronized private void triggerHouseReset(UpdateManager manager)
  {
    manager.cancelAdditions();
    manager.getAllEntities().stream().filter(e -> !(e instanceof Wall) &&
        !(e instanceof Exit) &&
        !(e instanceof House)).forEach(manager::remove);
    house.resetHouse();
  }

  private void updatePickUpBar()
  {
    trapBar.setCurrentValue(progressCounter);
    trapBar.setMaxValue(5 * Settings.frameRate);
  }

  private void updateStaminaBar()
  {
    staminaBar.setCurrentValue((int) stamina);
    staminaBar.setMaxValue((int) Settings.playerStamina);
  }

  private class GuiCounter // Used for Trap counter.
  {

    private final BufferedImage[] IMAGE_LIST = new BufferedImage[12];
    private final Point2D.Float POSITION = new Point2D.Float();
    private final int imageHeight = Settings.tileSize;
    private int imageWidth = 0;
    private int value;

    private GuiCounter(String imagePath)
    {

      for (int i = 0; i < 10; i++)
      {
        IMAGE_LIST[i] = ResourceManager.getImage("gui/digit_" + i + ".png");
      }
      if (imagePath != null) IMAGE_LIST[10] = ResourceManager.getImage(imagePath);
      setValue(0);
    }

    private void draw(Graphics2D global, DrawingManager drawingManager)
    {
      global.drawImage(IMAGE_LIST[11], (int) POSITION.getX(), (int) POSITION.getY(),
          (int) (imageWidth * drawingManager.getScale()), (int) (imageHeight * drawingManager.getScale()), null);
    }

    private void setPosition(float xPosition, float yPosition)
    {
      POSITION.setLocation(xPosition, yPosition);
    }

    private void setValue(int value)
    {
      if (value >= 0)
      {
        this.value = value;
        updateImage();
      }
      else
      {
        this.value = 0;
      }
    }

    private void updateImage()
    {
      int digitWidth = (int) (((float) IMAGE_LIST[0].getWidth() / Settings.DEFAULT_TILE_SIZE) * Settings.tileSize);
      int iconWidth = 0;

      if (IMAGE_LIST[10] != null)
      {
        iconWidth = (int) (((float) IMAGE_LIST[10].getWidth() / Settings.DEFAULT_TILE_SIZE) * Settings.tileSize);
      }

      int value = this.value;
      int order;
      if (value == 0)
      {
        order = 1;
      }
      else
      {
        order = 1 + (int) Math.log10(value);
      }

      imageWidth = iconWidth + order * digitWidth;
      IMAGE_LIST[11] = new BufferedImage(imageWidth, imageHeight, IMAGE_LIST[0].getType());
      Graphics2D graphics = IMAGE_LIST[11].createGraphics();
      if (IMAGE_LIST[10] != null) graphics.drawImage(IMAGE_LIST[10], 0, 0, iconWidth, imageHeight, null);
      int digit;
      int offset = iconWidth;
      while (order > 0)
      {
        int div = (int) Math.pow(10, order - 1);
        digit = (value / div) % 10;
        value -= digit * div;
        graphics.drawImage(IMAGE_LIST[digit], offset, 0, digitWidth, imageHeight, null);
        order--;
        offset += digitWidth;
      }
    }
  }

  private class ProgressBar // Green progress bars for the gui.
  {
    private final BufferedImage[] IMAGE_LIST = new BufferedImage[5];
    private final Point2D.Float POSITION;
    private final boolean absolutePosition;
    private boolean changeColor;
    private int currentValue;
    private int maxValue;

    private ProgressBar(String labelPath, boolean absolutePositioning)
    {
      absolutePosition = absolutePositioning;
      if (labelPath != null) IMAGE_LIST[0] = ResourceManager.getImage(labelPath);
      IMAGE_LIST[1] = ResourceManager.getImage("gui/progress_border_green.png");
      IMAGE_LIST[2] = ResourceManager.getImage("gui/progress_fill_green.png");
      IMAGE_LIST[3] = ResourceManager.getImage("gui/progress_border_red.png");
      IMAGE_LIST[4] = ResourceManager.getImage("gui/progress_fill_red.png");
      setCurrentValue(-1);
      setMaxValue(-1);
      this.POSITION = new Point2D.Float();
    }

    private void changeColor(boolean changeColor)
    {
      this.changeColor = changeColor;
    }

    private void draw(Graphics2D global, DrawingManager drawingManager)
    {
      int xPosition;
      int yPosition;
      if (absolutePosition)
      {
        xPosition = (int) POSITION.getX();
        yPosition = (int) POSITION.getY();
      }
      else
      {
        xPosition = (int) (drawingManager.gameXToScreenX(POSITION.x));// - drawingManager.getCameraOrigin().getX());
        yPosition = (int) (drawingManager.gameYToScreenY(POSITION.y));// - drawingManager.getCameraOrigin().getY());
      }

      int imageWidth = (int) (2f * Settings.tileSize * drawingManager.getScale());
      int imageHeight = (int) (0.375f * Settings.tileSize * drawingManager.getScale());
      int offset = 0;
      if (IMAGE_LIST[0] != null)
      {
        offset = (int) (IMAGE_LIST[0].getWidth() * drawingManager.getScale());
        global.drawImage(IMAGE_LIST[0], xPosition, yPosition, offset, imageHeight, null);
      }
      int fillWidth = (int) ((float) currentValue / maxValue * imageWidth);
      if (changeColor)
      {
        global.drawImage(IMAGE_LIST[3], xPosition + offset, yPosition, imageWidth, imageHeight, null);
        global.drawImage(IMAGE_LIST[4], xPosition + offset, yPosition, fillWidth, imageHeight, null);
      }
      else
      {
        global.drawImage(IMAGE_LIST[1], xPosition + offset, yPosition, imageWidth, imageHeight, null);
        global.drawImage(IMAGE_LIST[2], xPosition + offset, yPosition, fillWidth, imageHeight, null);
      }
    }

    private void setCurrentValue(int currentValue)
    {
      this.currentValue = currentValue;
    }

    private void setMaxValue(int maxValue)
    {
      this.maxValue = maxValue;
    }

    private void setPosition(float x, float y)
    {
      this.POSITION.setLocation(x, y);
    }
  }
}
