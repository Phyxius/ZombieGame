/**
 * Created by Mohammad R. Yousefi on 9/26/2015.
 * Provides access to settings and allows the user to modify them..
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SettingsWindow extends JFrame implements ActionListener
{
  private final Dimension comboBoxDimensions = new Dimension(200, 30);
  private final BoxListener comboBoxListener = new BoxListener();
  private final Dimension textFieldDimensions = new Dimension(75, 30);
  private final FieldListener textFieldListener = new FieldListener();
  private JButton closeButton;
  private JButton launchButton;
  private JPanel content;
  private JPanel controlPanel;
  private JComboBox<String> gameSettingsBox;
  private EntityManager entityManager;
  private JLabel gameSettingsLabel;
  private JPanel gameSettingsPanel;
  private JTextField gameSettingsText;
  private JComboBox<String> playerSettingsBox;
  private JLabel playerSettingsLabel;
  private JPanel playerSettingsPanel;
  private JTextField playerSettingsText;
  private JComboBox<String> zombieSettingsBox;
  private JLabel zombieSettingsLabel;
  private JPanel zombieSettingsPanel;
  private JTextField zombieSettingsText;

  /**
   * Creates and displays a settings window.
   */
  public SettingsWindow()
  {
    House.seed = System.currentTimeMillis();
    initWindow();
    showWindows();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == launchButton)
    {
      JFrame zombieFrame = new JFrame();
      entityManager = new EntityManager();
      ZombiePanel zombiePanel = new ZombiePanel(zombieFrame, entityManager);
      UpdateManager updateManager = new UpdateManager(entityManager);
      NewGame.makeNewGame(updateManager, 1);
      zombiePanel.init();
      zombieFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      zombieFrame.setSize(new Dimension(20 * Settings.tileSize, 20 * Settings.tileSize));
      zombieFrame.setContentPane(zombiePanel);
      zombieFrame.setVisible(true);
    }
    dispose();
  }

  private void changeGameValues(float value)
  {
    switch ((String) gameSettingsBox
        .getSelectedItem()) // "Exit Distance", "Frame Rate", "Sight Radius", "Tile Size", "Trap Spawn", "Zombie Spawn"
    {
      case "Exit Distance":
        Settings.distanceAwayExit = (int) value;
        break;
      case "Frame Rate":
        Settings.updateFrameRate((int) value);
        break;
      case "Sight Radius":
        Settings.playerSightRadius = (int) (value * Settings.tileSize);
        break;
      case "Tile Size":
        Settings.updateTileSize((int) value);
        break;
      case "Trap Spawn":
        Settings.trapSpawnRate = value;
        break;
      case "Zombie Spawn":
        Settings.zombieSpawnRate = value;
        break;
    }
    updateGameTextField();
  }

  private void changePlayerValues(float value)
  {
    switch ((String) playerSettingsBox
        .getSelectedItem()) // "Hearing", "Stamina", "Regeneration", "Run Speed", "Traps", "Walk Speed"
    {
      case "Hearing":
        Settings.playerHearing = value;
        break;
      case "Stamina":
        Settings.playerStamina = value * Settings.frameRate;
        break;
      case "Regeneration":
        Settings.playerStaminaRegen = value;
        break;
      case "Run Speed":
        Settings.playerRun = Util.tilesPerSecondToPixelsPerFrame(value);
        break;
      case "Traps":
        Settings.playerTraps = (int) value;
      case "Walk Speed":
        Settings.playerWalk = Util.tilesPerSecondToPixelsPerFrame(value);
        break;
    }
    updatePlayerTextField();
  }

  private void changeZombieValues(float value)
  {
    switch ((String) zombieSettingsBox.getSelectedItem()) // "Decision Rate", "Smell", "Speed", "Turn Angle"
    {
      case "Decision Rate":
        Settings.zombieDecisionRate = value;
        break;
      case "Smell":
        Settings.zombieSmellRadius = value;
        break;
      case "Speed":
        Settings.zombieSpeed = value;
        break;
      case "Turn Angle":
        Settings.minAngle = (value % 1) * Math.PI;
        break;
    }
    updateZombieTextField();
  }

  private void initContentPanel()
  {
    content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
    content.add(gameSettingsPanel);
    content.add(playerSettingsPanel);
    content.add(zombieSettingsPanel);
    content.add(controlPanel);
    add(content);
  }

  private void initControlPanel()
  {
    controlPanel = new JPanel(new FlowLayout());
    launchButton = new JButton("Launch");
    closeButton = new JButton("Close");
    launchButton.addActionListener(this);
    closeButton.addActionListener(this);
    controlPanel.add(launchButton);
    controlPanel.add(closeButton);
  }

  private void initFrame()
  {
    this.setTitle("Settings");
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  private void initGameSetting()
  {
    gameSettingsPanel = new JPanel();
    gameSettingsPanel.setLayout(new BoxLayout(gameSettingsPanel, BoxLayout.LINE_AXIS));
    gameSettingsLabel = new JLabel("Game Settings    ");
    gameSettingsPanel.add(gameSettingsLabel);
    String[] optionStrings = {"Exit Distance", "Frame Rate", "Sight Radius", "Tile Size", "Trap Spawn", "Zombie Spawn"};
    gameSettingsBox = new JComboBox<>(optionStrings);
    gameSettingsBox.setPreferredSize(comboBoxDimensions);
    gameSettingsBox.setName(gameSettingsLabel.getText());
    gameSettingsBox.addActionListener(comboBoxListener);
    gameSettingsPanel.add(gameSettingsBox);
    gameSettingsText = new JTextField();
    gameSettingsText.setPreferredSize(textFieldDimensions);
    gameSettingsText.addActionListener(textFieldListener);
    gameSettingsText.addKeyListener(textFieldListener);
    gameSettingsPanel.add(gameSettingsText);
  }

  private void initPlayerSettings()
  {
    playerSettingsPanel = new JPanel();
    playerSettingsPanel.setLayout(new BoxLayout(playerSettingsPanel, BoxLayout.LINE_AXIS));
    playerSettingsLabel = new JLabel("Player Settings   ");
    playerSettingsPanel.add(playerSettingsLabel);
    String[] optionStrings = {"Hearing", "Stamina", "Regeneration", "Run Speed", "Traps", "Walk Speed"};
    playerSettingsBox = new JComboBox<>(optionStrings);
    playerSettingsBox.setPreferredSize(comboBoxDimensions);
    playerSettingsBox.setName(playerSettingsLabel.getText());
    playerSettingsBox.addActionListener(comboBoxListener);
    playerSettingsPanel.add(playerSettingsBox);
    playerSettingsText = new JTextField();
    playerSettingsText.setPreferredSize(textFieldDimensions);
    playerSettingsText.addActionListener(textFieldListener);
    playerSettingsText.addKeyListener(textFieldListener);
    playerSettingsPanel.add(playerSettingsText);
  }

  private void initWindow()
  {
    initFrame();
    initGameSetting();
    initPlayerSettings();
    initZombieSetting();
    initControlPanel();
    initContentPanel();
  }

  private void initZombieSetting()
  {
    zombieSettingsPanel = new JPanel();
    zombieSettingsPanel.setLayout(new BoxLayout(zombieSettingsPanel, BoxLayout.LINE_AXIS));
    zombieSettingsLabel = new JLabel("Zombie Settings ");
    zombieSettingsPanel.add(zombieSettingsLabel);
    String[] optionStrings = {"Decision Rate", "Smell", "Speed", "Turn Angle"};
    zombieSettingsBox = new JComboBox<>(optionStrings);
    zombieSettingsBox.setPreferredSize(comboBoxDimensions);
    zombieSettingsBox.setName(zombieSettingsLabel.getText());
    zombieSettingsBox.addActionListener(comboBoxListener);
    zombieSettingsPanel.add(zombieSettingsBox);
    zombieSettingsText = new JTextField();
    zombieSettingsText.setPreferredSize(textFieldDimensions);
    zombieSettingsText.addActionListener(textFieldListener);
    zombieSettingsText.addKeyListener(textFieldListener);
    zombieSettingsPanel.add(zombieSettingsText);
  }

  private void showWindows()
  {
    updateGameTextField();
    updatePlayerTextField();
    updateZombieTextField();
    this.pack();
    this.setVisible(true);
  }

  private void updateGameTextField()
  {
    switch ((String) gameSettingsBox
        .getSelectedItem()) // "Exit Distance", "Frame Rate", "Sight Radius", "Tile Size", "Trap Spawn", "Zombie Spawn"
    {
      case "Exit Distance":
        gameSettingsText.setText(String.format("%d", Settings.distanceAwayExit));
        break;
      case "Frame Rate":
        gameSettingsText.setText(String.format("%d", Settings.frameRate));
        break;
      case "Sight Radius":
        gameSettingsText.setText(String.format("%.2f", (float) Settings.playerSightRadius / Settings.tileSize));
        break;
      case "Tile Size":
        gameSettingsText.setText(String.format("%d", Settings.tileSize));
        break;
      case "Trap Spawn":
        gameSettingsText.setText(String.format("%.2f", Settings.trapSpawnRate));
        break;
      case "Zombie Spawn":
        gameSettingsText.setText(String.format("%.2f", Settings.zombieSpawnRate));
        break;
    }
  }

  private void updatePlayerTextField()
  {
    switch ((String) playerSettingsBox
        .getSelectedItem())//"Hearing", "Stamina", "Regeneration", "Run Speed", "Traps", "Walk Speed"
    {
      case "Hearing":
        playerSettingsText.setText(String.format("%.2f", Settings.playerHearing));
        break;
      case "Stamina":
        playerSettingsText.setText(String.format("%.2f", Settings.playerStamina / Settings.frameRate));
        break;
      case "Regeneration":
        playerSettingsText.setText(String.format("%.2f", Settings.playerStaminaRegen));
        break;
      case "Run Speed":
        playerSettingsText.setText(String.format("%.2f", Settings.playerRun * Settings.frameRate / Settings.tileSize));
        break;
      case "Traps":
        playerSettingsText.setText(String.format("%d", Settings.playerTraps));
        break;
      case "Walk Speed":
        playerSettingsText.setText(String.format("%.2f", Settings.playerWalk * Settings.frameRate / Settings.tileSize));
        break;
    }
  }

  private void updateZombieTextField()
  {
    switch ((String) zombieSettingsBox.getSelectedItem()) // "Decision Rate", "Smell", "Speed", "Turn Angle"
    {
      case "Decision Rate":
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieDecisionRate));
        break;
      case "Smell":
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieSmellRadius));
        break;
      case "Speed":
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieSpeed));
        break;
      case "Turn Angle":
        zombieSettingsText.setText(String.format("%.2f", Settings.minAngle / Math.PI));
        break;
    }
  }

  private class BoxListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == gameSettingsBox) updateGameTextField();
      else if (e.getSource() == playerSettingsBox) updatePlayerTextField();
      else if (e.getSource() == zombieSettingsBox) updateZombieTextField();
    }
  }

  private class FieldListener implements ActionListener, KeyListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      float value = 0;
      JTextField field = (JTextField) e.getSource();
      try
      {
        value = Float.parseFloat(field.getText());
        if (field == gameSettingsText)
        {
          changeGameValues(value);
        }
        else if (field == playerSettingsText)
        {
          changePlayerValues(value);
        }
        else if (field == zombieSettingsText)
        {
          changeZombieValues(value);
        }
      }
      catch (NullPointerException | NumberFormatException exp)
      {
        JOptionPane.showMessageDialog(field.getParent(), "Bad Input. Try Again!");
        if (field == gameSettingsText) updateGameTextField();
        else if (field == playerSettingsText) updatePlayerTextField();
        else if (field == zombieSettingsText) updateZombieTextField();
      }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
      if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != KeyEvent.VK_PERIOD &&
          e.getKeyCode() == KeyEvent.VK_MINUS)
      {
        e.consume();
      }
    }
  }
}
