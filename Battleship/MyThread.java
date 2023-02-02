import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class MyThread extends Thread 
{
    boolean playAgain;
    
    MyThread(String fileName, boolean p)
    {
        super(fileName);
        playAgain = p;
    }
    @Override
    public void run() { 
            
        try{
            
            AudioInputStream music = AudioSystem.getAudioInputStream(new File(this.getName()));
            Clip clip = AudioSystem.getClip();
            clip.open(music);

            // create FloatControl object to adjust volume
            FloatControl volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-10.0f);
            clip.setFramePosition(0);
            clip.start();
            if(playAgain)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        
        catch(Exception e)
        {
            System.out.println("Error detected");
            e.printStackTrace();
        }
    }
    
}
