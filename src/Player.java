import java.awt.geom.Point2D;

/**
 * Created by Rashid on 07/09/15.
 * Player information and behavior.
 */
public class Player extends Entity
{
  private Point2D.Float position;

  void setPosition(Point2D.Float position)
  {
    this.position = position;
  }

  @Override
  public Point2D.Float getPosition()
  {
    return this.position;
  }
}
