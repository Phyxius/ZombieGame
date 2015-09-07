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
  protected Point2D.Float position;

  ZombieModel (Point2D.Float position)
  {
    speed = SettingsManager.getFloat("zombiespeed");
    decisionRate = (int) (SettingsManager.getFloat("zombiedecision") * SettingsManager.getInteger("framerate"));
    smell = SettingsManager.getFloat("zombiesmell");
    this.position.setLocation(position.getX(), position.getY());
  }

  ZombieModel (float speed, float decisionRate, float smell, Point2D.Float position)
  {
    this(position);
    this.speed = speed;
    this.decisionRate = (int) (decisionRate * SettingsManager.getInteger("framerate"));
    this.smell = smell;
  }

  @Override
  public void draw (Graphics2D local, Graphics2D global)
  {
    int tileSize = SettingsManager.getInteger("tilesize");
    local.setColor(Color.GREEN);
    local.drawRoundRect(0, 0, tileSize, tileSize, tileSize/10, tileSize/10);
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }

  @Override
  public void onCollision (Entity other)
  {
  }
}
