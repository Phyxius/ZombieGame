import java.awt.image.BufferedImage;

/**
 * Created by arirappaport on 9/10/15.
 */
public class House extends Entity
{
  public static Tile[][] fullGrid;
  private BufferedImage houseImg;
  private int gridHeight, gridWidth;

  public House(int gridWidth, int gridHeight)
  {
    this.gridHeight = gridHeight;
    this.gridWidth = gridWidth;
    fullGrid = new Tile[gridHeight][gridWidth];
  }
  //Will be very sophisticated at some point
  public void generateBuffImgHouse()
  {
    int tileSize = Settings.tileSize;

    for(int i = 0; i < gridHeight; i++)
    {
      for(int j = 0; j < gridWidth; j++)
      {
        houseImg.createGraphics().drawImage(fullGrid[i][j].getTileImg(), i * set);
      }
    }
  }
}
