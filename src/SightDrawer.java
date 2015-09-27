import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Shea Polansky
 * Class name and description goes here
 */
public class SightDrawer extends Entity
{
  private final static Color TRANSPARENT = new Color(0, 0, 0, 0);
  private final static float[] GRADIENT_FRACTIONS = new float[]{0, 1f};
  private final static Color[] GRADIENT_COLORS = new Color[]{TRANSPARENT, Color.black};
  private static final Ellipse2D.Float LIGHT_ELLIPSE = new Ellipse2D.Float(0, 0, Settings.playerSightRadius * 2, Settings.playerSightRadius * 2);
  private final int halfPlayerSight = Settings.playerSightRadius / 2;
  ArrayList<Point2D.Float> lightSources = new ArrayList<>();
  private final HashMap<Point2D.Float, HashSet<Point2D.Float>> relevantCorners = new HashMap<>();
  //private VolatileImage screenOverlay = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    //if (screenOverlay.getWidth() != screen.getClipBounds().width || screenOverlay.getHeight() != screen.getClipBounds().height)
    //screenOverlay = new BufferedImage(screen.getClipBounds().width, screen.getClipBounds().height, BufferedImage.TYPE_INT_ARGB);
    GraphicsConfiguration graphicsConfiguration = drawingManager.getGraphicsConfiguration();
    VolatileImage screenOverlay = graphicsConfiguration.createCompatibleVolatileImage(screen.getClipBounds().width, screen.getClipBounds().height, VolatileImage.TRANSLUCENT);
    screenOverlay.validate(graphicsConfiguration);
    Graphics2D overlayDrawer = screenOverlay.createGraphics();
    overlayDrawer.setColor(Color.black);
    overlayDrawer.fillRect(0, 0, screenOverlay.getWidth(), screenOverlay.getHeight());
    overlayDrawer.setComposite(AlphaComposite.SrcIn);
    for (Point2D lightSource : lightSources)
    {
      lightSource.setLocation(lightSource.getX() - drawingManager.getCameraOrigin().getX(),
          lightSource.getY() - drawingManager.getCameraOrigin().getY());
      RadialGradientPaint p = new RadialGradientPaint(lightSource, Settings.playerSightRadius, GRADIENT_FRACTIONS, GRADIENT_COLORS);
      overlayDrawer.setPaint(p);
      LIGHT_ELLIPSE.setFrameFromCenter(lightSource,
          new Point2D.Float((float) (lightSource.getX() + Settings.playerSightRadius),
              (float) (lightSource.getY() + Settings.playerSightRadius)));
      overlayDrawer.fill(LIGHT_ELLIPSE);
    }
    overlayDrawer.dispose();
    screen.drawImage(screenOverlay, 0, 0, null);
    screen.setColor(Color.green);
    for (Map.Entry<Point2D.Float, HashSet<Point2D.Float>> mapping : relevantCorners.entrySet())
    {
      Point2D.Float lightSource = mapping.getKey();
      for (Point2D.Float corner : mapping.getValue())
      {
        screen.drawLine((int)(corner.x - drawingManager.getCameraOrigin().x),(int)(corner.y - drawingManager.getCameraOrigin().y), (int)(lightSource.x), (int)(lightSource.y));
      }
    }
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
    relevantCorners.clear();
    for (Point2D.Float lightSource : lightSources)
    {
      HashSet<Point2D.Float> points = new HashSet<>();
      Collection<Entity> potentiallyLitEntities = e.getCollidingEntities(new Ellipse2D.Float(lightSource.x - Settings.playerSightRadius, lightSource.y - Settings.playerSightRadius,
          2 * Settings.playerSightRadius, 2 * Settings.playerSightRadius)).stream().filter(Entity::blocksLight).collect(Collectors.toList());
      /*
        The following code is using the Streams API even though a foreach loop would be more appropriate because
        it has free parallelism. It takes the list of 'interesting' (i.e. light blocking within sight range) entities,
        flat-maps them into a long stream of Point2D's, then checks to see if the rays from the center of the light source
        intersect any light blocking entity. If they do intersect, they are removed from the stream.
      */
      points.addAll(
          potentiallyLitEntities.parallelStream()
              .map(Entity::getBoundingBox)
              .flatMap(rect -> Stream.of(
                  new Point2D.Float(rect.x, rect.y),
                  new Point2D.Float(rect.x + rect.width, rect.y),
                  new Point2D.Float(rect.x, rect.y + rect.height),
                  new Point2D.Float(rect.x + rect.width, rect.y + rect.height))
                  .filter(p ->
                      potentiallyLitEntities.stream().noneMatch(ent -> ent.getBoundingBox().intersectsLine(Util.getShorterLine(new Line2D.Float(lightSource, p), 2)))))
              .collect(Collectors.toList()));
      relevantCorners.put(lightSource, points);
    }
  }

  @Override
  public int getDepth()
  {
    return 1000;
  }
}
