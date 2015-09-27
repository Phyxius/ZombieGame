import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

/**
 * Created by Mohammad R. Yousefi on 9/26/2015.
 * View and Edit settings for the program.
 */
public class SettingsWindow extends JFrame implements ActionListener
{
  private Dimension boxSize = new Dimension(200, 30);
  private JButton closeButton;
  private BoxListener comboListener = new BoxListener();
  private JPanel content;
  private JPanel controlPanel;
  private Dimension fieldSize = new Dimension(75, 30);
  private JComboBox gameSettingsBox;
  private JLabel gameSettingsLabel;
  private JPanel gameSettingsPanel;
  private JFormattedTextField gameSettingsText;
  private JComboBox playerSettingsBox;
  private JLabel playerSettingsLabel;
  private JPanel playerSettingsPanel;
  private JFormattedTextField playerSettingsText;
  private FieldListener textFieldListener = new FieldListener();
  private JComboBox zombieSettingsBox;
  private JLabel zombieSettingsLabel;
  private JPanel zombieSettingsPanel;
  private JFormattedTextField zombieSettingsText;

  public SettingsWindow()
  {
    initWindow();
    showWindows();
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(() -> {
      new SettingsWindow();
    });
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
        return;
      case 1:
        Settings.updateTileSize(value);
        return;
    }
  }

  private void changePlayerValues(float value)
  {
    switch (playerSettingsBox.getSelectedIndex()) // "Hearing", "Stamina", "Regeneration", "Run Speed", "Walk Speed"
    {
      case 0:
        Settings.playerHearing = value;
        return;
      case 1:
        Settings.playerStamina = value * Settings.frameRate;
        return;
      case 2:
        Settings.playerStaminaRegen = value * Settings.frameRate;
        return;
      case 3:
        Settings.playerRun = Util.tilesPerSecondToPixelsPerFrame(value);
        return;
      case 4:
        Settings.playerWalk = Util.tilesPerSecondToPixelsPerFrame(value);
        return;
    }
  }

  private void changeZombieValues(float value)
  {
    switch (zombieSettingsBox.getSelectedIndex()) // "Decision Rate", "Smell", "Speed", "Turn Angle"
    {
      case 0:
        Settings.zombieDecisionRate = value;
        return;
      case 1:
        Settings.zombieSmellRadius = value;
        return;
      case 2:
        Settings.zombieSpeed = value;
        return;
      case 3:
        Settings.minAngle = (value % 1) * Math.PI;
        return;
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
    gameSettingsBox = new JComboBox(optionStrings);
    gameSettingsBox.setPreferredSize(boxSize);
    gameSettingsBox.setName(gameSettingsLabel.getText());
    gameSettingsBox.addActionListener(comboListener);
    gameSettingsPanel.add(gameSettingsBox);
    gameSettingsText = new JFormattedTextField(NumberFormat.getNumberInstance());
    gameSettingsText.setPreferredSize(fieldSize);
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
    String[] optionStrings = {"Hearing", "Stamina", "Regeneration", "Run Speed", "Walk Speed"};
    playerSettingsBox = new JComboBox(optionStrings);
    playerSettingsBox.setPreferredSize(boxSize);
    playerSettingsBox.setName(playerSettingsLabel.getText());
    playerSettingsBox.addActionListener(comboListener);
    playerSettingsPanel.add(playerSettingsBox);
    playerSettingsText = new JFormattedTextField(NumberFormat.getCurrencyInstance());
    playerSettingsText.setPreferredSize(fieldSize);
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
    zombieSettingsBox = new JComboBox(optionStrings);
    zombieSettingsBox.setPreferredSize(boxSize);
    zombieSettingsBox.setName(zombieSettingsLabel.getText());
    zombieSettingsBox.addActionListener(comboListener);
    zombieSettingsPanel.add(zombieSettingsBox);
    zombieSettingsText = new JFormattedTextField(NumberFormat.getCurrencyInstance());
    zombieSettingsText.setPreferredSize(fieldSize);
    zombieSettingsText.addActionListener(textFieldListener);
    zombieSettingsText.addKeyListener(textFieldListener);
    zombieSettingsPanel.add(zombieSettingsText);
  }

  private void parseValue(JFormattedTextField field)
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
        return;
      case 1:
        gameSettingsText.setText(String.format("%d", Settings.tileSize));
        return;
    }
  }

  private void updatePlayerTextField()
  {
    switch (playerSettingsBox.getSelectedIndex()) // "Hearing", "Stamina", "Regeneration", "Run Speed", "Walk Speed"
    {
      case 0:
        playerSettingsText.setText(String.format("%.2f", Settings.playerHearing));
        return;
      case 1:
        playerSettingsText.setText(String.format("%.2f", Settings.playerStamina / Settings.frameRate));
        return;
      case 2:
        playerSettingsText.setText(String.format("%.2f", Settings.playerStaminaRegen / Settings.frameRate));
        return;
      case 3:
        playerSettingsText.setText(String.format("%.2f", Settings.playerRun * Settings.frameRate / Settings.tileSize));
        return;
      case 4:
        playerSettingsText.setText(String.format("%.2f", Settings.playerWalk * Settings.frameRate / Settings.tileSize));
        return;
    }
  }

  private void updateZombieTextField()
  {
    switch (zombieSettingsBox.getSelectedIndex()) // "Decision Rate", "Smell", "Speed", "Turn Angle"
    {
      case 0:
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieDecisionRate));
        return;
      case 1:
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieSmellRadius));
        return;
      case 2:
        zombieSettingsText.setText(String.format("%.2f", Settings.zombieSpeed));
        return;
      case 3:
        zombieSettingsText.setText(String.format("%.2f", Settings.minAngle / Math.PI));
        return;
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
      parseValue((JFormattedTextField) e.getSource());
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
