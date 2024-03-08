package io.codeforall.bootcamp;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    public static String[] BOUNCE_ENEMY = {"jocaNaoDoi.wav", "tenisOUT.wav", "juro.wav", "tenis.wav"};
    public static String[] JUMP_ENEMY = {"fuisht.wav"};
    public static String[] DEATH = {"ohJoca.wav", "ohJoca2.wav"};
    public static String[] LUCIANO = {"duvida.wav", "eitacaralho.wav", "fdscaralho.wav"};

    public void playSound(String audioPath) {
        String fullPath = ".resources/audio/" + audioPath;
        Thread playbackThread = new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fullPath));

                AudioFormat format = audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
                audioLine.open(format);

                audioLine.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                    audioLine.write(buffer, 0, bytesRead);
                }

                audioLine.drain();

                audioLine.close();
                audioInputStream.close();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        });

        playbackThread.start();

    }

    public void playBgMusic() {
        String audioPath = ".resources/audio/bgmusic.wav";
        Thread playbackThread = new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(audioPath));

                AudioFormat format = audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
                audioLine.open(format);

                audioLine.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                    audioLine.write(buffer, 0, bytesRead);
                }

                audioLine.drain();

                audioLine.close();
                audioInputStream.close();
                playBgMusic();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        });

        playbackThread.start();

    }


}
