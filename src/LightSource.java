import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by rashid on 9/23/2015.
 */
public interface LightSource
{
  Point2D.Float getLightLocation();
  void setLightLocation(Point2D.Float location);
  void setLightLocation(Rectangle2D.Float boundingBox);
}
