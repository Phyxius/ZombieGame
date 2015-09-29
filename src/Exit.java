import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/24/15.
 * The exit of the House. Drawn differently
 * depending on what side of the room
 * it's on. It is one pixel more inside the
 * room than the walls so the player can touch it.
 */
public class Exit extends Entity
{
  private BufferedImage exitImg;
  private Point2D position;

  /**
   * Makes an exit on a different side
   * of the room depending on dir. EndX
   * and EndY are used for width and height.
   *
   * @param dir    The side of room of the Exit.
   * @param startX The start X of the exit.
   * @param startY The start Y of the exit.
   * @param endX   The point that is the width of the exit.
   * @param endY   The point that is the height of the exit.
   */
  public Exit(House.Direction dir, int startX, int startY, int endX, int endY)
  {
    int tilSz = Settings.tileSize;
    position = new Point2D.Float();
    int width = (endX - startX) / tilSz;
    int height = (endY - startY) / tilSz;
    switch (dir)
    {
      case NORTH:
        exitImg = ResourceManager.getImage("objects/north.png");
        position.setLocation(startX + Util.rng.nextInt(width - 2) * tilSz, startY + 1);
        break;
      case WEST:
        exitImg = ResourceManager.getImage("objects/west.png");
        position.setLocation(startX + 1, startY + tilSz * (1 + Util.rng.nextInt(height - 2)));
        break;
      case EAST:
        exitImg = ResourceManager.getImage("objects/east.png");
        position.setLocation(startX - 1, startY + tilSz * (1 + Util.rng.nextInt(height - 2)));
        break;
      case SOUTH:
        exitImg = ResourceManager.getImage("objects/south.png");
        position.setLocation(startX + Util.rng.nextInt(width - 2), startY - 1);
        break;
    }
  }

  @Override
  public void draw(Graphics2D local, Graphics2D screen, DrawingManager drawingManager)
  {
    local.drawImage(exitImg, 0, 0, Settings.tileSize, Settings.tileSize, null);
  }

  @Override
  public int getDepth()
  {
    return 1001;
  }

  @Override
  public Point2D.Float getPosition()
  {
    return (Point2D.Float) position;
  }

  public void setPosition(Point2D position)
  {
    this.position = position;
  }

  @Override
  public boolean isSolid()
  {
    return false;
  }

  @Override
  public void onCollision(Entity other, CollisionManager c)
  {
    if (other instanceof Player)
    {
      c.remove(c.getAllEntities());
      NewGame.makeNewGame(c, House.levelNum++);
    }
  }
}
