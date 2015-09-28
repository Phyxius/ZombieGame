import javax.swing.*;

/**
 * Created by arirappaport on 9/28/15.
 */
public class StartGame
{
    public static void makeNewGame (UpdateManager updateManager)
    {
        updateManager.add(new House(100, 100, updateManager));
    }
}
