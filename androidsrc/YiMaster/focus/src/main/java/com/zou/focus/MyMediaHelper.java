package com.zou.focus;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by zougy on 03.25.025
 */
public class MyMediaHelper {

    private static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    private static AudioManager audioManager;

    public static int CLICK_RIGHT;
    public static int CLICK_ERROR;

    public static void playGameBg(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_bg);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void destory() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (soundPool != null) {
            soundPool.stop(CLICK_RIGHT);
            soundPool.stop(CLICK_ERROR);
            soundPool.release();
            soundPool = null;
        }
    }

    public static void loadClick(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (soundPool == null) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .build();
            CLICK_RIGHT = soundPool.load(context, R.raw.click, 1);
            CLICK_ERROR = soundPool.load(context, R.raw.error, 1);
        }
    }

    public static void playClick(int key) {
        soundPool.play(key, 1f, 1f, 1, 0, 1);
    }

}
