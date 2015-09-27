import java.awt.*;
import java.awt.geom.*;
import java.awt.image.VolatileImage;
import java.util.*;
import java.util.List;
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
  private ArrayList<Point2D.Float> lightSources = new ArrayList<>();
  private final HashMap<Point2D.Float, HashSet<Point2D.Float>> relevantCorners = new HashMap<>();
  //private VolatileImage screenOverlay = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    //TODO: use a do-while loop for this using contentsLost()
    //if (screenOverlay.getWidth() != screen.getClipBounds().width || screenOverlay.getHeight() != screen.getClipBounds().height)
    //screenOverlay = new BufferedImage(screen.getClipBounds().width, screen.getClipBounds().height, BufferedImage.TYPE_INT_ARGB);
    GraphicsConfiguration graphicsConfiguration = drawingManager.getGraphicsConfiguration();
    VolatileImage screenOverlay = graphicsConfiguration.createCompatibleVolatileImage(screen.getClipBounds().width, screen.getClipBounds().height, VolatileImage.TRANSLUCENT);
    screenOverlay.validate(graphicsConfiguration);
    Graphics2D overlayDrawer = screenOverlay.createGraphics();
    overlayDrawer.setColor(Color.black);
    overlayDrawer.fillRect(0, 0, screenOverlay.getWidth(), screenOverlay.getHeight());
    overlayDrawer.setComposite(AlphaComposite.SrcIn);
    float cameraY = (float)drawingManager.getCameraOrigin().getY();
    float cameraX = (float)drawingManager.getCameraOrigin().getX();
    for (Map.Entry<Point2D.Float, HashSet<Point2D.Float>> entry : relevantCorners.entrySet())
    {
      Point2D.Float lightSource = entry.getKey();
      lightSource.setLocation(lightSource.getX() - cameraX,
          lightSource.getY() - cameraY);
      RadialGradientPaint p = new RadialGradientPaint(lightSource, Settings.playerSightRadius, GRADIENT_FRACTIONS, GRADIENT_COLORS);
      overlayDrawer.setPaint(p);
      /*LIGHT_ELLIPSE.setFrameFromCenter(lightSource,
          new Point2D.Float((float) (lightSource.getX() + Settings.playerSightRadius),
              (float) (lightSource.getY() + Settings.playerSightRadius)));
      overlayDrawer.fill(LIGHT_ELLIPSE);*/
      List<Point2D.Float> points = entry.getValue().parallelStream()
          .map(point -> new Point2D.Float(point.x - cameraX, point.y - cameraY))
          .sorted(Comparator.comparing((Point2D.Float point) -> Math.atan2(point.y - lightSource.y, point.x - lightSource.x)).thenComparing(point -> lightSource.distanceSq(point)))
          .collect(Collectors.toList());
      //overlayDrawer.setColor(Color.green);
      Polygon litArea = new Polygon();
      for (Point2D.Float point : points)
      {
        litArea.addPoint(((int) point.x), ((int) point.y));
      }
      overlayDrawer.fillPolygon(litArea);
    }
    overlayDrawer.dispose();
    screen.drawImage(screenOverlay, 0, 0, null);
    /*screen.setColor(Color.green);
    for (Map.Entry<Point2D.Float, HashSet<Point2D.Float>> mapping : relevantCorners.entrySet()) {
      Point2D.Float lightSource = mapping.getKey();
      for (Point2D.Float corner : mapping.getValue())
      {
        screen.drawLine((int)(corner.x - drawingManager.getCameraOrigin().x),(int)(corner.y - drawingManager.getCameraOrigin().y), (int)(lightSource.x), (int)(lightSource.y));
      }
    }*/
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
      Collection<Entity> potentiallyLitEntities = e.getCollidingEntities(new Ellipse2D.Float(lightSource.x - Settings.playerSightRadius, lightSource.y - Settings.playerSightRadius,
          2 * Settings.playerSightRadius, 2 * Settings.playerSightRadius)).stream().filter(Entity::blocksLight).collect(Collectors.toList());
      /*
        The following code is using the Streams API even though a foreach loop would be more readable because
        Streams provide free parallelism. It takes the list of 'interesting' (i.e. light blocking within sight range) entities,
        flat-maps them into a stream of Point2D's (one for each corner), then checks to see if the rays from the center of the
        light source intersect any light blocking entity. If they do intersect, they are removed from the stream.
        Yes, I have profiled this method and it does benefit from the parallelism.
      */
      HashSet<Point2D.Float> lightPoints = new HashSet<>(potentiallyLitEntities.parallelStream()
              .map(Entity::getBoundingBox)
              .flatMap(rect -> Stream.of(
                  new Point2D.Float(rect.x, rect.y),
                  new Point2D.Float(rect.x + rect.width, rect.y),
                  new Point2D.Float(rect.x, rect.y + rect.height),
                  new Point2D.Float(rect.x + rect.width, rect.y + rect.height)))
              .distinct()
              .map(p -> castRays(lightSource, p, potentiallyLitEntities))
                  /*.filter(p -> potentiallyLitEntities.stream()
                      .noneMatch(ent -> ent.getBoundingBox().intersectsLine(
                          Util.getLongerLine(new Line2D.Float(lightSource, p), -2)))))*/
              .flatMap(tuple -> tuple.value2 == null ? Stream.of(tuple.value1) : Stream.of(tuple.value1, tuple.value2))
              .collect(Collectors.toList()));
      relevantCorners.put(lightSource, lightPoints);
    }
  }

  private boolean doRectanglesContainPoint(float x, float y, Collection<Rectangle2D.Float> rectangles)
  {
    return rectangles.parallelStream().anyMatch(r -> r.contains(x, y));
  }

  private Tuple<Point2D.Float, Point2D.Float> castRays(Point2D.Float origin, Point2D.Float towardsPoint, Collection<Entity> interestingEntities)
  {
    List<Rectangle2D.Float> rectangles = interestingEntities.parallelStream().map(Entity::getBoundingBox).collect(Collectors.toList());
    double angle = Math.atan2(towardsPoint.y - origin.y, towardsPoint.x - origin.x);
    float dx = (float)Math.cos(angle), dy = (float)Math.sin(angle);
    float x = origin.x, y = origin.y;
    int iterations = 0;
    Point2D.Float previousPoint = null;
    while (iterations < Settings.playerSightRadius)
    {
      if ((doRectanglesContainPoint(x, y, rectangles)))
      {
        if (doRectanglesContainPoint(x + dx, y + dy, rectangles)) break;
        previousPoint = new Point2D.Float(x, y);
      }
      iterations++;
      x += 3 * dx;
      y += 3 * dy;
    }
    return new Tuple<>(new Point2D.Float(x, y), previousPoint);
  }

  @Override
  public int getDepth()
  {
    return 1000;
  }
}
