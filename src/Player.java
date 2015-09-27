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
public class Player extends Entity implements Detonator {
    public static final Font UI_FONT = new Font("SansSerif", Font.PLAIN, Settings.tileSize / 2);
    private final SoundEffect footstepSfx = new SoundEffect("soundfx/player_footstep.wav");
    private final Animation idleAnimation = new Animation("animation/player/idle_", 8, true);
    private final Animation moveAnimation = new Animation("animation/player/move_", 13, true);
    private final ProgressBar staminaProgressBar;
    private final String trapIconPath = "fire/firetrap.png";
    private final SoundEffect trapInteractionSfx = new SoundEffect("soundfx/player_pickup.mp3");
    private final ProgressBar trapProgressBar;
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

    public Player(ProgressBar staminaBar, ProgressBar progressBar) {
        this.staminaProgressBar = staminaBar;
        staminaProgressBar.setVisible(true);
        this.trapProgressBar = progressBar;
    }

    @Override
    public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager) {
        AffineTransform transformer = new AffineTransform();
        if (flipAnimation) {
            BufferedImage frame = (moving ? moveAnimation.getFrame() : idleAnimation.getFrame());
            transformer.scale(-1, 1);
            transformer.translate(-frame.getWidth(), 0);
            AffineTransformOp opTransformer = new AffineTransformOp(transformer, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            frame = opTransformer.filter(frame, null);
            transformer.setToScale((double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE,
                    (double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE);
            local.drawImage(frame, transformer, null);
        } else {
            transformer.setToScale((double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE,
                    (double) Settings.tileSize / Settings.DEFAULT_TILE_SIZE);
            local.drawImage((moving ? moveAnimation.getFrame() : idleAnimation.getFrame()), transformer, null);
        }
        if (isPickingUp() || isPlacing) {
            trapProgressBar.setVisible(true);
            trapProgressBar
                    .setPosition((float) (position.x - drawingManager.getCameraOrigin().getX() - Settings.tileSize / 2),
                            (float) (position.y - drawingManager.getCameraOrigin().getY() - Settings.tileSize / 4));
            trapProgressBar.draw(null, global, null);
        } else {
            trapProgressBar.setVisible(false);
        }
        staminaProgressBar.setPosition(Settings.tileSize, (float) global.getClipBounds().getHeight() -
                Settings.tileSize);
        staminaProgressBar.draw(null, global, null);

        global.drawImage(ResourceManager.getImage(trapIconPath), Settings.tileSize,
                (int) (global.getClipBounds().getHeight() - 2.5 * Settings.tileSize), Settings.tileSize, Settings.tileSize,
                null);
        global.setFont(UI_FONT);
        global.setColor(Color.GREEN);
        global.drawString("X " + trapsInInventory, (int) (2.3 * Settings.tileSize),
                (int) (global.getClipBounds().getHeight() - 1.8 * Settings.tileSize));
    }

    @Override
    public int getDepth() {
        return 110;
    }

    @Override
    public boolean isLightSource() {
        return true;
    }

    @Override
    public Point2D.Float getPosition() {
        return this.position;
    }

    @Override
    public boolean trigger() {
        return isRunning();
    }

    @Override
    public void update(UpdateManager e) {
        if (isPickingUp() || isPlacing) {
            increaseStamina();
            idleAnimation.nextFrame(moving);
            moving = false;
            if (e.isKeyPressed(KeyEvent.VK_P)) {
                progressCounter++;
                updatePickUpBar();
                if (progressCounter % (5 * Settings.frameRate) == 0) {
                    trapsInInventory++;
                    e.remove(collidingTrap);
                    collidingTrap = null;
                    isPickingUp = false;
                    trapInteractionSfx.stop();
                }
            } else if (e.isKeyPressed(KeyEvent.VK_T)) {
                progressCounter++;
                updatePickUpBar();
                if (progressCounter % (5 * Settings.frameRate) == 0) {
                    trapsInInventory--;
                    Trap trap = new Trap(new Point2D.Float((int) (center.getX() / Settings.tileSize) * Settings.tileSize,
                            (int) (center.getY() / Settings.tileSize) * Settings.tileSize));
                    e.add(trap);
                    collidingTrap = trap;
                    isPlacing = false;
                    trapInteractionSfx.stop();
                }
            } else {
                isPlacing = false;
                isPickingUp = false;
                trapInteractionSfx.stop();
                progressCounter = 0;
            }
        } else {
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

            if (movementMagnitude != 0) {
                isRunning = e.isKeyPressed(KeyEvent.VK_R) && !staminaDepleted;
                move((float) getPosition().getX(), (float) getPosition().getY(), xMovement, yMovement, isRunning, e);
                moveAnimation.nextFrame(!moving);
                moving = true;
            } else {
                idleAnimation.nextFrame(moving);
                moving = false;
                increaseStamina();
            }

            if (e.isKeyPressed(KeyEvent.VK_P) && collidingTrap != null) {
                isPickingUp = true;
                progressCounter++;
                trapInteractionSfx.play(0.0, 1.0);
                updatePickUpBar();
            } else {
                progressCounter = 0;
                isPickingUp = false;
            }

            if (e.isKeyPressed(KeyEvent.VK_T) && trapsInInventory > 0 && collidingTrap == null) {
                progressCounter++;
                isPlacing = true;
                trapInteractionSfx.play(0.0, 1.0);
                updatePickUpBar();
            } else {
                progressCounter = 0;
                isPlacing = false;
            }
        }
    }

    private boolean checkCollision(UpdateManager manager, float xPosition, float yPosition) {
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

    private void decreaseStamina() {
        if (stamina > 0) stamina--;
        else stamina = 0;
        isStaminaDepleted();
        updateStaminaBar();
    }

    private void increaseStamina() {
        if (++stamina > Settings.playerStamina) stamina = Settings.playerStamina;
        isStaminaDepleted();
        updateStaminaBar();
    }

    private boolean isPickingUp() {
        return isPickingUp;
    }

    private boolean isRunning() {
        return isRunning;
    }

    /**
     * Used to indicate that stamina was recently depleted.
     *
     * @return Returns a true value while stamina < Settings.frameRate * 2 (2 seconds) after the player stamina reaches zero.
     */
    private boolean isStaminaDepleted() {
        staminaDepleted = stamina == 0 || (staminaDepleted && stamina < Settings.frameRate * 2);
        staminaProgressBar.changeColor(staminaDepleted);
        return staminaDepleted;
    }

    private void move(float x, float y, float xMovement, float yMovement, boolean isRunning, UpdateManager manager) {
        if (isRunning) {
            position.setLocation(x + xMovement * Settings.playerRun, y + yMovement * Settings.playerRun);
            if (checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY())) {
                position.setLocation(x, y);
                increaseStamina();
            } else // successful movement
            {
                if (soundCounter % (Settings.frameRate / 2) == 0) footstepSfx.play(0.0, 10);
                soundCounter++;
                decreaseStamina();
            }
        } else {
            increaseStamina();
            position.setLocation(x + xMovement * Settings.playerWalk, y + yMovement * Settings.playerWalk);
            if (checkCollision(manager, (float) getPosition().getX(), (float) getPosition().getY())) {
                position.setLocation(x, y);
            } else //successful movement
            {
                if (soundCounter % Settings.frameRate == 0) footstepSfx.play(0.0, 10);
                soundCounter++;
            }
        }
    }

    private void updatePickUpBar() {
        trapProgressBar.setCurrentValue(progressCounter);
        trapProgressBar.setMaxValue(5 * Settings.frameRate);
    }

    private void updateStaminaBar() {
        staminaProgressBar.setCurrentValue((int) stamina);
        staminaProgressBar.setMaxValue((int) Settings.playerStamina);
    }
}
