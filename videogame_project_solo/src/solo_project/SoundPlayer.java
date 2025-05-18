package solo_project;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
	

    private static Clip audioClip;


    public static void playSound(String soundPath) { //for sfx
        try {
        	
        	
            File soundFile = new File(soundPath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void playMusic(String soundPath) { //for music
        try {
        	
            if (audioClip != null && audioClip.isRunning()) {
                return; // Prevent overlapping sound
            }

            
            File soundFile = new File(soundPath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            audioClip = AudioSystem.getClip();   // Assign to static field
            audioClip.open(audioIn);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

