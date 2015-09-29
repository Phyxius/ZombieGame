import javax.swing.*;

/**
 * Makes a new game
 */
public class NewGame
{
    /**
     * Makes a new Game or Level.
     * @param updateManager UpdateManager is passed off to the house.
     * @param levelNum levelNum changes the difficulty
     *                 by making more zombies and fewer traps.
     */
    public static void makeNewGame (UpdateManager updateManager, int levelNum)
    {
        Settings.trapSpawnRate -= levelNum*(0.005f);
        Settings.zombieSpawnRate += levelNum*(0.005f);
        updateManager.add(new House(100, 100, levelNum, updateManager));
        updateManager.add(new UpdateCounter());
        //updateManager.add(new SightDrawer());
    }
}
