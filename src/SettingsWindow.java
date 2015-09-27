import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Mohammad R. Yousefi on 9/26/2015.
 * View and Edit settings for the program.
 */
public class SettingsWindow extends JFrame implements ActionListener
{
  private final Dimension COMBO_BOX_DIMENSIONS = new Dimension(200, 30);
  private final BoxListener COMBO_BOX_LISTENER = new BoxListener();
  private final Dimension TEXT_FIELD_DIMENSIONS = new Dimension(75, 30);
  private final FieldListener TEXT_FIELD_LISTENER = new FieldListener();
  private JButton closeButton;
  private JPanel content;
  private JPanel controlPanel;
  private JComboBox<String> gameSettingsBox;
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

  public SettingsWindow()
  {
    initWindow();
    showWindows();
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(SettingsWindow::new);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    dispose();
  }

  private void changeGameValues(int value)
  {
    switch (gameSettingsBox.getSelectedIndex()) // "Frame Rate", "Tile Size"
    {
      case 0:
        Settings.updateFrameRate(value);
        break;
      case 1:
        Settings.updateTileSize(value);
        break;
    }
  }

  private void changePlayerValues(float value)
  {
    switch (playerSettingsBox.getSelectedIndex()) // "Hearing", "Stamina", "Regeneration", "Run Speed", "Walk Speed"
    {
      case 0:
        Settings.playerHearing = value;
        break;
      case 1:
        Settings.playerStamina = value * Settings.frameRate;
        break;
      case 2:
        Settings.playerStaminaRegen = value;
        break;
      case 3:
        Settings.playerRun = Util.tilesPerSecondToPixelsPerFrame(value);
        break;
      case 4:
        Settings.playerWalk = Util.tilesPerSecondToPixelsPerFrame(value);
        break;
    }
  }

  private void changeZombieValues(float value)
  {
    switch (zombieSettingsBox.getSelectedIndex()) // "Decision Rate", "Smell", "Speed", "Turn Angle"
    {
      case 0:
        Settings.zombieDecisionRate = value;
        break;
      case 1:
        Settings.zombieSmellRadius = value;
        break;
      case 2:
        Settings.zombieSpeed = value;
        break;
      case 3:
        Settings.minAngle = (value % 1) * Math.PI;
        break;
    }
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
    closeButton = new JButton("Close");
    closeButton.addActionListener(this);
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
    String[] optionStrings = {"Frame Rate", "Tile Size"};
    gameSettingsBox = new JComboBox<>(optionStrings);
    gameSettingsBox.setPreferredSize(COMBO_BOX_DIMENSIONS);
    gameSettingsBox.setName(gameSettingsLabel.getText());
    gameSettingsBox.addActionListener(COMBO_BOX_LISTENER);
    gameSettingsPanel.add(gameSettingsBox);
    gameSettingsText = new JTextField();
    gameSettingsText.setPreferredSize(TEXT_FIELD_DIMENSIONS);
    gameSettingsText.addActionListener(TEXT_FIELD_LISTENER);
    gameSettingsText.addKeyListener(TEXT_FIELD_LISTENER);
    gameSettingsPanel.add(gameSettingsText);
  }

  private void initPlayerSettings()
  {
    playerSettingsPanel = new JPanel();
    playerSettingsPanel.setLayout(new BoxLayout(playerSettingsPanel, BoxLayout.LINE_AXIS));
    playerSettingsLabel = new JLabel("Player Settings   ");
    playerSettingsPanel.add(playerSettingsLabel);
    String[] optionStrings = {"Hearing", "Stamina", "Regeneration", "Run Speed", "Walk Speed"};
    playerSettingsBox = new JComboBox<>(optionStrings);
    playerSettingsBox.setPreferredSize(COMBO_BOX_DIMENSIONS);
    playerSettingsBox.setName(playerSettingsLabel.getText());
    playerSettingsBox.addActionListener(COMBO_BOX_LISTENER);
    playerSettingsPanel.add(playerSettingsBox);
    playerSettingsText = new JTextField();
    playerSettingsText.setPreferredSize(TEXT_FIELD_DIMENSIONS);
    playerSettingsText.addActionListener(TEXT_FIELD_LISTENER);
    playerSettingsText.addKeyListener(TEXT_FIELD_LISTENER);
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
    zombieSettingsBox.setPreferredSize(COMBO_BOX_DIMENSIONS);
    zombieSettingsBox.setName(zombieSettingsLabel.getText());
    zombieSettingsBox.addActionListener(COMBO_BOX_LISTENER);
    zombieSettingsPanel.add(zombieSettingsBox);
    zombieSettingsText = new JTextField();
    zombieSettingsText.setPreferredSize(TEXT_FIELD_DIMENSIONS);
    zombieSettingsText.addActionListener(TEXT_FIELD_LISTENER);
    zombieSettingsText.addKeyListener(TEXT_FIELD_LISTENER);
    zombieSettingsPanel.add(zombieSettingsText);
  }

  private void parseValue(JTextField field)
  {
    if (field == gameSettingsText)
    {
      int value = Integer.parseInt(field.getText());
      changeGameValues(value);
    }
    else if (field == playerSettingsText)
    {
      float value = Float.parseFloat(field.getText());
      changePlayerValues(value);
    }
    else if (field == zombieSettingsText)
    {
      float value = Float.parseFloat(field.getText());
      changeZombieValues(value);
    }
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
    switch (gameSettingsBox.getSelectedIndex()) // "Frame Rate", "Tile Size"
    {
      case 0:
        gameSettingsText.setText(String.format("%d", Settings.frameRate));
        break;
      case 1:
        gameSettingsText.setText(String.format("%d", Settings.tileSize));
        break;
    }
  }

  private void updatePlayerTextField()
  {
    switch (playerSettingsBox.getSelectedIndex()) // "Hearing", "Stamina", "Regeneration", "Run Speed", "Walk Speed"
    {
      case 0:
        playerSettingsText.setText(String.format("%.2f", Settings.playerHearing));
        break;
      case 1:
        playerSettingsText.setText(String.format("%.2f", Settings.playerStamina / Settings.frameRate));
        break;
      case 2:
        playerSettingsText.setText(String.format("%.2f", Settings.playerStaminaRegen));
        break;
      case 3:
        playerSettingsText.setText(String.format("%.2f", Settings.playerRun * Settings.frameRate / Settings.tileSize));
        break;
      case 4:
        playerSettingsText.setText(String.format("%.2f", Settings.playerWalk * Settings.frameRate / Settings.tileSize));
        break;
    }
  }

  private void updateZombieTextField()
  {
    switch (zombieSettingsBox.getSelectedIndex()) // "Decision Rate", "Smell", "Speed", "Turn Angle"
    {
      case 0:
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieDecisionRate));
        break;
      case 1:
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieSmellRadius));
        break;
      case 2:
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieSpeed));
        break;
      case 3:
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
      parseValue((JTextField) e.getSource());
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
