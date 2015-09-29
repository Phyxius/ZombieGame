import javax.swing.*;

/**
 * Created by arirappaport on 9/28/15.
 */
public class NewGame
{
    public static void makeNewGame (UpdateManager updateManager, int levelNum)
    {
        updateManager.add(new House(100, 100, levelNum, updateManager));
        updateManager.add(new UpdateCounter());
        //updateManager.add(new SightDrawer());
    }
}
