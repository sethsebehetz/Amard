package com.etz.gh.amard.utilities;
//
// Java program to play an Audio
// file using Clip Object

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AmardSoundPlayer {
    // to store current position

    static Long currentFrame;
    static Clip clip;

    // current status of clip
    static String status;

    static AudioInputStream audioInputStream;
    static String filePath = Config.getProperty("SOUND.FILE");

    static {
        try {
            // create AudioInputStream object
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            // create clip reference
            clip = AudioSystem.getClip();
            // open audioInputStream to the clip
            clip.open(audioInputStream);
            //Thread.sleep(10000);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AmardSoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AmardSoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(AmardSoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public static void main(String[] args) {
        try {
            AmardSoundPlayer.play();            
            AmardSoundPlayer.stop();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    // Method to play the audio
    private static void play() {
        try {
            //start the clip
            clip.start();
            clip.loop(Integer.parseInt(Config.getProperty("SOUND.LOOP.COUNT")));
            status = "play";
            Thread.sleep(Integer.parseInt(Config.getProperty("SOUND.LOAD.TIME"))); //without a sleep sound does not play
        } catch (InterruptedException ex) {
            Logger.getLogger(AmardSoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Method to pause the audio
    private static void pause() {
        if (status.equals("paused")) {
            System.out.println("audio is already paused");
            return;
        }
        currentFrame = clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        if (status.equals("play")) {
            System.out.println("Audio is already being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    // Method to restart the audio
    private static void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        play();
    }

    // Method to stop the audio
    private static void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    // Method to jump over a specific part
    private static void jump(long c) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (c > 0 && c < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            play();
        }
    }

    // Method to reset audio stream
    private static void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
