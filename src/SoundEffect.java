/**
 * Created by arirappaport on 9/15/15.
 * Provides sound for the game entities.
 */

import javafx.scene.media.AudioClip;

import java.net.URL;

public class SoundEffect
{
  private AudioClip audioClip; // Each sound effect has its own clip, loaded with its own sound file.

  /**
   * SoundEffect is a wrapper class for AudioCLip.
   * It allows multiple clips to be played at once and
   * automatically mixes them.
   *
   * @param soundFileName The name of file. Can be loaded from
   *                      jar.
   */
  SoundEffect(String soundFileName)
  {
    URL url = this.getClass().getClassLoader().getResource(soundFileName);
    String urlString = url.toString();
    audioClip = new AudioClip(urlString);
  }

  // Play or Re-play the sound effect from the beginning, by rewinding.

  /**
   * Plays the clip
   *
   * @param balance Changes the left/right balance of the clip
   * @param volume  changes the volume level of the clip.
   */
  public void play(double balance, double volume)
  {
    audioClip.setBalance(balance);
    audioClip.setVolume(volume);
    audioClip.play();
  }

  public void stop()
  {
    audioClip.stop();
  }

  public void loop()
  {
    audioClip.setCycleCount(AudioClip.INDEFINITE);
  }

  public boolean isPlaying()
  {
    return audioClip.isPlaying();
  }
}
