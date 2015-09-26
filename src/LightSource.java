import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by rashid on 9/23/2015.
 */
public interface LightSource
{
  /**
   * Returns the location of the light source.
   * @return The location of the light source.
   */
  Point2D.Float getLightLocation();

  /**
   * Sets the origin point of the light source.
   * @param location Location of the light source.
   */
  void setLightLocation(Point2D.Float location);

  /**
   * Sets the origin point of the light source.
   * @param boundingBox Bounding box for the light source object.
   */
  void setLightLocation(Rectangle2D.Float boundingBox);
}
