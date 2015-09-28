import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/27/15.
 */
public class Bookshelf extends Obstacle
{
    /**
     * Makes a Bookshelf object at
     * the specified position.
     * @param position Specified position.
     */
    public Bookshelf(Point2D.Float position)
    {
        super(ResourceManager.getImage("objects/book.png"), new Rectangle2D.Float(position.x, position.y, Settings.tileSize, Settings.tileSize));
    }
}
