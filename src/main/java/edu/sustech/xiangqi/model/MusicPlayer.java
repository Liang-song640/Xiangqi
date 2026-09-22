package edu.sustech.xiangqi.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MusicPlayer {
    private Clip currentClip;
    private final Map<String, Clip> musicClips = new HashMap<>();
    private final Map<String, Clip> soundEffects = new HashMap<>();
    private float volume = 1.0f;

    //加载背景音乐
    public void loadMusic(String key, String path) {
        try {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            musicClips.put(key, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("加载音乐失败: " + path);
            e.printStackTrace();
        }
    }

    public void loadSoundEffect(String key, String path) {
        try {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundEffects.put(key, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("加载音效失败: " + path);
            e.printStackTrace();
        }
    }

    //播放背景音乐
    public void playMusic(String key) {
        stopCurrent(); //先停止当前音乐
        Clip clip = musicClips.get(key);
        if (clip != null) {
            currentClip = clip;
            setVolume((int)(volume * 100));
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY); //播放时再设置循环
            clip.start();
        }
    }

    public void playSoundEffect(String key) {
        Clip clip = soundEffects.get(key);
        if (clip != null) {
            clip.setFramePosition(0);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
            clip.start();
        }
    }

    //停止后清空currentClip
    public void stopCurrent() {
        if (currentClip != null) {
            if (currentClip.isRunning()) {
                currentClip.stop();
            }
            currentClip.setFramePosition(0); //重置到开头
            currentClip = null;
        }
    }

    public void setVolume(int volume) {
        this.volume = volume / 100f;
        if (currentClip != null) {
            FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = 20f * (float) Math.log10(this.volume);
            gainControl.setValue(dB);
        }
    }

    public void release() {
        stopCurrent();
        musicClips.values().forEach(Clip::close);
        soundEffects.values().forEach(Clip::close);
    }
}