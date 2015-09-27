import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Shea Polansky
 * Class name and description goes here
 */
public class SightDrawer extends Entity
{
  private final static Color TRANSPARENT = new Color(0, 0, 0, 0);
  private final static float[] GRADIENT_FRACTIONS = new float[] {0, 1f};
  private final static Color[] GRADIENT_COLORS = new Color[] {TRANSPARENT, Color.black};
  private static final Ellipse2D.Float LIGHT_ELLIPSE = new Ellipse2D.Float(0, 0, Settings.playerSight * 2, Settings.playerSight * 2);
  ArrayList<Point2D.Float> lightSources = new ArrayList<>();

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    BufferedImage screenOverlay = new BufferedImage(screen.getClipBounds().width, screen.getClipBounds().height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D overlayDrawer = screenOverlay.createGraphics();
    overlayDrawer.setColor(Color.black);
    overlayDrawer.fillRect(0, 0, screenOverlay.getWidth(), screenOverlay.getHeight());
    overlayDrawer.setComposite(AlphaComposite.SrcIn);
    for (Point2D lightSource : lightSources)
    {
      lightSource.setLocation(lightSource.getX() - drawingManager.getCameraOrigin().getX(),
          lightSource.getY() - drawingManager.getCameraOrigin().getY());
      RadialGradientPaint p = new RadialGradientPaint(lightSource, Settings.playerSight, GRADIENT_FRACTIONS, GRADIENT_COLORS);
      overlayDrawer.setPaint(p);
      LIGHT_ELLIPSE.setFrameFromCenter(lightSource,
          new Point2D.Float((float) (lightSource.getX() + Settings.playerSight),
              (float) (lightSource.getY() + Settings.playerSight)));
      overlayDrawer.fill(LIGHT_ELLIPSE);
    }
    overlayDrawer.dispose();
    screen.drawImage(screenOverlay, 0, 0, null);
  }

  @Override
  public void update(UpdateManager e)
  {
    lightSources.clear();
    lightSources.addAll(e.getAllEntities().parallelStream()
        .filter(Entity::isLightSource)
        .map(Entity::getBoundingBox)
        .map(Util::getRectangleCenterPoint)
        .collect(Collectors.toList()));
  }

  @Override
  public int getDepth()
  {
    return 1000;
  }
}
