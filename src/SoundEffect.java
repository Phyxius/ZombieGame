import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import javafx.scene.media.AudioClip;
import java.io.IOException;
import java.net.URL;

/**
 * Created by arirappaport on 9/15/15.
 */
public class SoundEffect
{
    // Each sound effect has its own clip, loaded with its own sound file.
    private AudioClip audioClip;


    // Constructor to construct each element of the enum with its own sound file.
    SoundEffect(String soundFileName)
    {
      URL url = this.getClass().getClassLoader().getResource(soundFileName);
      String urlString = url.toString();
      audioClip = new AudioClip(urlString);

    }

    // Play or Re-play the sound effect from the beginning, by rewinding.
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
  }
