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
   * Indicated whether the audio is playing.
   *
   * @return Returns true if audio is playing.
   */
  public boolean isPlaying()
  {
    return audioClip.isPlaying();
  }

  /**
   * Causes the audio to loop upon play until stop method is called.
   */
  public void loop()
  {
    audioClip.setCycleCount(AudioClip.INDEFINITE);
  }

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

  /**
   * Stops playback.
   */
  public void stop()
  {
    audioClip.stop();
  }
}
