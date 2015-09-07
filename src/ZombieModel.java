import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
 * The Template for zombies.
 */
abstract class ZombieModel extends Entity
{
  protected float speed; // displacement = speed * tiles
  protected int decisionRate; // New decision every decisionRate updates.
  protected float smell; // smell radius = smell * tiles
  protected float stamina;
  protected int updateCount = 0;
  protected Player player;
  protected Point2D.Float playerPosition = null;
  protected Point2D.Float position;

  ZombieModel (Player player, Point2D.Float position)
  {
    speed = SettingsManager.getFloat("zombieSpeed");
    decisionRate = (int) (SettingsManager.getFloat("zombieDecision") * SettingsManager.getInteger("frameRate"));
    smell = SettingsManager.getFloat("zombieSmell");
    this.position.setLocation(position.getX(), position.getY());
  }

  ZombieModel (Player player, float speed, float decisionRate, float smell, Point2D.Float position)
  {
    this(player, position);
    this.speed = speed;
    this.decisionRate = (int) (decisionRate * SettingsManager.getInteger("frameRate"));
    this.smell = smell;
  }

  @Override
  public void draw (Graphics2D local, Graphics2D global)
  {
    int tileSize = SettingsManager.getInteger("tileSize");
    local.setColor(Color.GREEN);
    local.drawRoundRect(0, 0, tileSize, tileSize, tileSize/10, tileSize/10);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  void detectPlayer()
  {
    if (player.getPosition().distance(this.position) <= smell)
    {
      playerPosition = player.getPosition();
    }
  }
}
