import javafx.scene.media.MediaPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
//import javafx.scene.media.AudioClip;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

/**
 * Created by arirappaport on 9/15/15.
 */
public class SoundEffect
{
    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip audioClip;


    // Constructor to construct each element of the enum with its own sound file.
    SoundEffect(String soundFileName)
    {
      //URL url = this.getClass().getClassLoader().getResource(soundFileName);
      //String urlString = url.toString();
      //audioClip = Applet.newAudioClip(url);
      //audioClip.play();
      try{
        URL url = this.getClass().getClassLoader().getResource(soundFileName);
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        audioClip = AudioSystem.getClip();
        audioClip.open(ais);
        audioClip.setFramePosition(0);
      }
      catch(UnsupportedAudioFileException ex){}
      catch(LineUnavailableException ex){}
      catch (IOException ex) {}

    }

    // Play or Re-play the sound effect from the beginning, by rewinding.
    public void play(double balance, double volume)
    {
      //audioClip.setBalance(balance);
      //audioClip.setVolume(volume);
      audioClip.start();
    }
    public void stop()
    {
      audioClip.stop();
    }
  }
