import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager 
{
    private static Clip lastClip;
    private static Clip musicClip;

    public static void playSound(String fileName) 
    {
        try 
        {
            URL soundURL = SoundManager.class.getResource("/SoundEffects/" + fileName);
            if (soundURL == null) {
                System.out.println("Sound file not found: " + fileName);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            lastClip = clip; // Keep reference
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static void playBackgroundMusic(String fileName, float volume) 
    {
        try 
        {
            if (musicClip != null && musicClip.isRunning()) 
            {
                return; // Already playing
            }
            URL soundURL = SoundManager.class.getResource("/SoundEffects/" + fileName);
            if (soundURL == null) 
            {
                System.out.println("Music file not found: " + fileName);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);

            // Set volume
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // e.g., -10.0f for quieter

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() 
    {
        if (musicClip != null) 
        {
            musicClip.stop();
            musicClip.close();
        }
    }

    public static void setBackgroundMusicVolume(float volume) 
    {
        try 
        {
            if (musicClip != null && musicClip.isOpen()) 
            {
                FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}