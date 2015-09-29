import javax.swing.*;

/**
 * Makes a new game
 */
public class NewGame
{
    public static void makeNewGame (UpdateManager updateManager, int levelNum)
    {
        Settings.trapSpawnRate -= levelNum*(0.005f);
        Settings.zombieSpawnRate += levelNum*(0.005f);
        updateManager.add(new House(100, 100, levelNum, updateManager));
        updateManager.add(new UpdateCounter());
        //updateManager.add(new SightDrawer());
    }
}
