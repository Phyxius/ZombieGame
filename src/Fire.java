import java.awt.geom.Rectangle2D;

/**
 * Created by arirappaport on 9/7/15.
 */
public class Fire extends Entity
{
  private Rectangle2D explosionArea;
  public Fire(Rectangle2D.Float explosionArea)
  {
    this.explosionArea = explosionArea;
  }

}
