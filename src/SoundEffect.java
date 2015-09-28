import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
//import javafx.scene.media.AudioClip;
import java.applet.Applet;
//import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

/**
 * Created by arirappaport on 9/15/15.
 */
public class SoundEffect
{
    // Each sound effect has its own clip, loaded with its own sound file.
    private AudioClip audioClip;
  private boolean playing;
    // Constructor to construct each element of the enum with its own sound file.

    /**
     * SoundEffect is a wrapper class for AudioCLip.
     * It allows multiple clips to be played at once and
     * automatically mixes them.
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
     * @param balance Changes the left/right balance of the clip
     * @param volume changes the volume level of the clip.
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
