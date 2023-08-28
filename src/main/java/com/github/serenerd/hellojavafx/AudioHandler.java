package com.github.serenerd.hellojavafx;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioHandler {

    private static final URL file = Thread.currentThread().getContextClassLoader().getResource("pong.wav");
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void playAudio() {
        executor.execute(() -> {
            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();  // Start playing
                // Wait for the clip to finish playing
                while (!clip.isRunning()) {
                    Thread.sleep(10);
                }
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }
                clip.close();  // Close the clip when done
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}