import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Mohammad R. Yousefi on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity implements Detonator
{
  public static final Font UI_FONT = new Font("SansSerif", Font.PLAIN, Settings.tileSize / 2);
  private final SoundEffect walkSFX = new SoundEffect("soundfx/player_footstep.mp3");
  private final SoundEffect runSFX = new SoundEffect("soundfx/player_footstep_run.mp3");
//  private final Animation IDLE_ANIMATION = new Animation("animation/player1/idle-", 1, true);
  private final Animation MOVE_ANIMATION = new Animation("animation/player1/moving_small_", 9, true);
  //  private final Animation MOVE_ANIMATION = new Animation("animation/player1/moving-", 9, true);
  //private final Animation IDLE_ANIMATION = new Animation("animation/player/idle_", 8, true);
  //private final Animation MOVE_ANIMATION = new Animation("animation/player/move_", 13, true);
  private final ProgressBar STAMINA_BAR = new ProgressBar("gui/label_stamina.png");
  private final SoundEffect TRAP_SFX = new SoundEffect("soundfx/player_pickup.mp3");
  private final ProgressBar TRAP_BAR = new ProgressBar("");
  private final GuiCounter TRAP_COUNTER = new GuiCounter("gui/trapIcon.png");
  private final Point2D.Float CENTER;
  private Trap collidingTrap = null;
  private boolean flipAnimation;
  private boolean isPickingUp;
  private boolean isPlacing;
  private boolean isRunning = false;
  private boolean moving;
  private Point2D.Float position = new Point2D.Float(42 * Settings.tileSize, 43 * Settings.tileSize);
  private int progressCounter = 0;
  private int soundCounter = 0;
  private float stamina = Settings.playerStamina;
  private boolean staminaDepleted;
  private int trapsInInventory = Settings.playerTraps;
  private double directionAngle;

  public Player()
  {
    CENTER = new Point2D.Float((float) getBoundingBox().getCenterX(), (float) getBoundingBox().getCenterY());
    TRAP_COUNTER.setValue(trapsInInventory);
    walkSFX.loop();
    runSFX.loop();
    TRAP_SFX.loop();
  }
  @Override
  public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager)
  {
    AffineTransform transformer = new AffineTransform();
    transformer.rotate(directionAngle, getBoundingBox().getWidth() / 2,
        getBoundingBox().getHeight()/2); // must rotate first then scale otherwise it will cause a bug
    transformer.scale((double) Settings.tileSize / 80, (double) Settings.tileSize / 80);
    local.drawImage((moving ? MOVE_ANIMATION.getFrame() : ResourceManager.getImage("animation/player1/moving_small_0.png")), transformer, null); // IDLE_ANIMATION.getFrame()), transformer, null);
//    local.drawImage((moving ? MOVE_ANIMATION.getFrame() : IDLE_ANIMATION.getFrame()), 0,0, (int) getBoundingBox().getWidth(), (int) getBoundingBox().getHeight(), null);


//    AffineTransform transformer = new AffineTransform();
//    if (flipAnimation)
//    {
//      BufferedImage frame = (moving ? MOVE_ANIMATION.getFrame() : IDLE_ANIMATION.getFrame());
//      transformer.scale(-1, 1);
//      transformer.translate(-frame.getWidth(), 0);
//      AffineTransformOp opTransformer = new AffineTransformOp(transformer, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//      frame = opTransformer.filter(frame, null);
//      transformer.setToScale((double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE,
//          (double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE);
//      local.drawImage(frame, transformer, null);
//    }
//    else
//    {
//      transformer.setToScale((double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE,
//          (double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE);
//      local.drawImage((moving ? MOVE_ANIMATION.getFrame() : IDLE_ANIMATION.getFrame()), transformer, null);
//    }
    if (isPickingUp() || isPlacing)
    {
      TRAP_BAR
          .setPosition((float) (position.x - drawingManager.getCameraOrigin().getX() - Settings.tileSize / 2),
              (float) (position.y - drawingManager.getCameraOrigin().getY() - Settings.tileSize / 4));
      TRAP_BAR.draw(global);
    }
    STAMINA_BAR.setPosition(Settings.tileSize, (float) global.getClipBounds().getHeight() -
            Settings.tileSize);
    STAMINA_BAR.draw(global);
    TRAP_COUNTER.setPosition(Settings.tileSize, (float) global.getClipBounds().getHeight() -
        2.15f * Settings.tileSize);
    TRAP_COUNTER.draw(global);
  }

  @Override
  public int getDepth()
  {
    return 1100;
  }

  @Override
  public boolean isLightSource()
  {
    return true;
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
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
//      IDLE_ANIMATION.nextFrame(moving);
      moving = false;
      if (e.isKeyPressed(KeyEvent.VK_P))
      {
        progressCounter++;
        updatePickUpBar();
        if (progressCounter % (5 * Settings.frameRate) == 0)
        {
          TRAP_COUNTER.setValue(++trapsInInventory);
          e.remove(collidingTrap);
          collidingTrap = null;
          isPickingUp = false;
          TRAP_SFX.stop();
        }
      }
      else if (e.isKeyPressed(KeyEvent.VK_T))
      {
        progressCounter++;
        updatePickUpBar();
        if (progressCounter % (5 * Settings.frameRate) == 0)
        {
          TRAP_COUNTER.setValue(--trapsInInventory);
          Trap trap = new Trap(new Point2D.Float((int) (CENTER.getX() / Settings.tileSize) * Settings.tileSize,
              (int) (CENTER.getY() / Settings.tileSize) * Settings.tileSize));
          e.add(trap);
          collidingTrap = trap;
          isPlacing = false;
          TRAP_SFX.stop();
        }
      }
      else
      {
        isPlacing = false;
        isPickingUp = false;
        TRAP_SFX.stop();
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
        directionAngle = Math.atan2(yMovement, xMovement);
        isRunning = e.isKeyPressed(KeyEvent.VK_R) && !staminaDepleted;
        move((float) getPosition().getX(), (float) getPosition().getY(), xMovement, yMovement, isRunning, e);
        MOVE_ANIMATION.nextFrame(!moving);
        moving = true;
      }
      else
      {
//        IDLE_ANIMATION.nextFrame(moving);
        walkSFX.stop();
        runSFX.stop();
        moving = false;
        increaseStamina();
      }

      if (e.isKeyPressed(KeyEvent.VK_P) && collidingTrap != null)
      {
        isPickingUp = true;
        progressCounter++;
        TRAP_SFX.play(0.0, 1.0);
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
        TRAP_SFX.play(0.0, 1.0);
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
      if (entity instanceof Trap && entity.getBoundingBox().contains(CENTER)) collidingTrap = (Trap) entity;
      if(entity instanceof ZombieModel)
      {
        manager.remove(manager.getAllEntities());
        NewGame.makeNewGame(manager, House.levelNum);
      }
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
    if ((stamina += Settings.playerStaminaRegen) > Settings.playerStamina) stamina = Settings.playerStamina;
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
    staminaDepleted = stamina == 0 || (staminaDepleted && stamina < Settings.playerStaminaRegen * Settings.frameRate * 2);
    STAMINA_BAR.changeColor(staminaDepleted);
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
        soundCounter++;
        CENTER.setLocation(getBoundingBox().getCenterX(), getBoundingBox().getCenterY());
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
        soundCounter++;
        CENTER.setLocation(getBoundingBox().getCenterX(), getBoundingBox().getCenterY());
      }
    }
  }

  private void updatePickUpBar()
  {
    TRAP_BAR.setCurrentValue(progressCounter);
    TRAP_BAR.setMaxValue(5 * Settings.frameRate);
  }

  private void updateStaminaBar()
  {
    STAMINA_BAR.setCurrentValue((int) stamina);
    STAMINA_BAR.setMaxValue((int) Settings.playerStamina);
  }

  private class ProgressBar
  {
    private final BufferedImage[] IMAGE_LIST = new BufferedImage[5];
    private final Point2D.Float POSITION;
    private boolean changeColor;
    private int currentValue;
    private int maxValue;

    ProgressBar(String labelPath)
    {

      if (labelPath != null) IMAGE_LIST[0] =  ResourceManager.getImage(labelPath);
      IMAGE_LIST[1] = ResourceManager.getImage("gui/progress_border_green.png");
      IMAGE_LIST[2] = ResourceManager.getImage("gui/progress_fill_green.png");
      IMAGE_LIST[3] = ResourceManager.getImage("gui/progress_border_red.png");
      IMAGE_LIST[4] = ResourceManager.getImage("gui/progress_fill_red.png");
      setCurrentValue(-1);
      setMaxValue(-1);
      this.POSITION = new Point2D.Float();
    }

    public void draw(Graphics2D global)
    {
      int xPosition = (int) POSITION.getX();
      int yPosition = (int) POSITION.getY();
      int imageWidth = 2 * Settings.tileSize;
      int imageHeight = (int) (0.375 * Settings.tileSize);
      int offset = 0;
      if (IMAGE_LIST[0] != null)
      {
        offset = imageWidth;
        global.drawImage(IMAGE_LIST[0], xPosition, yPosition, imageWidth, imageHeight, null);
      }
      int fillWidth = (int) ((float) currentValue / maxValue * imageWidth);
      if (changeColor)
      {
        global.drawImage(IMAGE_LIST[3], xPosition + offset, yPosition, imageWidth, imageHeight, null);
        global.drawImage(IMAGE_LIST[4], xPosition + offset, yPosition, fillWidth , imageHeight, null);
      }
      else
      {
        global.drawImage(IMAGE_LIST[1], xPosition + offset, yPosition, imageWidth, imageHeight, null);
        global.drawImage(IMAGE_LIST[2], xPosition + offset, yPosition, fillWidth, imageHeight, null);
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

  private class GuiCounter
  {

    private final BufferedImage[] IMAGE_LIST = new BufferedImage[12];
    private final Point2D.Float POSITION = new Point2D.Float();
    private int value;
    int imageHeight = Settings.tileSize;
    int imageWidth = 0;


    GuiCounter (String imagePath)
    {

      for (int i = 0; i < 10; i++)
      {
        IMAGE_LIST[i] = ResourceManager.getImage("gui/digit_" + i + ".png");
      }
      if (imagePath != null) IMAGE_LIST[10] = ResourceManager.getImage(imagePath);
      setValue(0);
    }

    public int getValue()
    {
      return value;
    }

    public void setValue(int value)
    {
      if (value >= 0)
      {
        this.value = value;
        updateImage();
      }
      else this.value = 0;
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
      if (value == 0) order = 1;
      else order = 1 + (int) Math.log10(value);

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

    public void draw (Graphics2D global)
    {
      global.drawImage(IMAGE_LIST[11], (int) POSITION.getX(), (int) POSITION.getY(), imageWidth, imageHeight, null);
    }

    public void setPosition(float xPosition, float yPosition)
    {
      POSITION.setLocation(xPosition, yPosition);
    }
  }

  @Override
  public Rectangle2D.Float getBoundingBox()
  {
    Point2D.Float position = getPosition();
    if (position == null) return null;
    return new Rectangle2D.Float(position.x, position.y,
        (int) ((65.0 / Settings.DEFAULT_TILE_SIZE) * Settings.tileSize), (int) ((65.0 / Settings.DEFAULT_TILE_SIZE) * Settings.tileSize));
  }
}
