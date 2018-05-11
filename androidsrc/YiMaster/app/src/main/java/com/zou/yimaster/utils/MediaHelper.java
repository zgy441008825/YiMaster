package com.zou.yimaster.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;

import com.zou.yimaster.R;

import org.xutils.x;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 44100 on 05/10 010
 *
 * @author 44100
 */
public class MediaHelper {

    public static final String SOUND_KEY_CLICK = "CLICK";
    public static final String SOUND_KEY_ERROR = "ERROR";

    private SoundPool soundPool;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private Map<String, Integer> soundMap = new HashMap<>();

    private static MediaHelper helper;

    public static MediaHelper getInstance() {
        if (helper == null) {
            synchronized (MediaHelper.class) {
                if (helper == null) {
                    helper = new MediaHelper();
                }
            }
        }
        return helper;
    }

    private MediaHelper() {
        audioManager = (AudioManager) x.app().getSystemService(Context.AUDIO_SERVICE);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        init();
    }

    @SuppressLint("CheckResult")
    private void init() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(x.app(), Uri.parse("android.resource://" + x.app().getPackageName() +
                        "/" + R.raw.game_bg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!soundMap.containsKey(SOUND_KEY_CLICK))
            soundMap.put(SOUND_KEY_CLICK, soundPool.load(x.app(), R.raw.click, 1));
        if (!soundMap.containsKey(SOUND_KEY_ERROR))
            soundMap.put(SOUND_KEY_ERROR, soundPool.load(x.app(), R.raw.error, 1));
    }

    public void playSound(String key) {
        try {
            init();
            int id = soundMap.get(key);
            soundPool.play(id, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), audioManager.getStreamVolume
                    (AudioManager.STREAM_MUSIC), 1, 0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOrStopBackground() {
        init();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public boolean backgroundPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void release() {
        soundPool.release();
        soundPool.unload(soundMap.get(SOUND_KEY_CLICK));
        soundPool.unload(soundMap.get(SOUND_KEY_ERROR));
        soundMap.clear();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
