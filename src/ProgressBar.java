import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Mohammad R. Yousefi on 9/26/2015.
 */
public class ProgressBar extends Entity
{
    private final BufferedImage[] imageList = new BufferedImage[5];
    private final Point2D.Float POSITION;
    private boolean changeColor;
    private boolean visibility;
    private int currentValue;
    private int maxValue;

    public ProgressBar(String labelPath) {

        if (!labelPath.equals("")) imageList[0] = ResourceManager.getImage(labelPath);
        imageList[1] = ResourceManager.getImage("gui/progress_border_green.png");
        imageList[2] = ResourceManager.getImage("gui/progress_fill_green.png");
        imageList[3] = ResourceManager.getImage("gui/progress_border_red.png");
        imageList[4] = ResourceManager.getImage("gui/progress_fill_red.png");
        setCurrentValue(-1);
        setMaxValue(-1);
        this.POSITION = new Point2D.Float();
    }

    @Override
    public void draw(Graphics2D local, Graphics2D global, DrawingManager drawingManager) {
        if (!visibility) return;
        int xPosition = (int) POSITION.getX();
        int yPosition = (int) POSITION.getY();
        int imageWidth = 2 * Settings.tileSize;
        int imageHeight = (int) (0.375 * Settings.tileSize);
        int offset = 0;
        if (imageList[0] != null) {
            offset = imageWidth;
            global.drawImage(imageList[0], xPosition, yPosition, imageWidth, imageHeight, null);
        }
        int fillWidth = (int) ((float) currentValue / maxValue * imageWidth);
        if (changeColor) {
            global.drawImage(imageList[3], xPosition + offset, yPosition, imageWidth, imageHeight, null);
            global.drawImage(imageList[4], xPosition + offset, yPosition, fillWidth, imageHeight, null);
        } else {
            global.drawImage(imageList[1], xPosition + offset, yPosition, imageWidth, imageHeight, null);
            global.drawImage(imageList[2], xPosition + offset, yPosition, fillWidth, imageHeight, null);
        }
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setPosition(float x, float y) {
        this.POSITION.setLocation(x, y);
    }

    public void changeColor(boolean changeColor) {
        this.changeColor = changeColor;
    }

    @Override
    public int getDepth() {
        return 1100;
    }

    @Override
    public Rectangle2D.Float getBoundingBox() {
        return null;
    }

    public void setVisible(boolean visibility)
    {
        this.visibility = visibility;
    }
}
