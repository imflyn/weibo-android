package net.weibo.common;

import net.weibo.app.AppContext;
import net.weibo.app.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.SparseIntArray;

public class SoundTool
{
    private static SoundTool      soundTool;
    private static SoundPool      soundPool;

    public static final int       NEWS_REFRESH  = 0;
    public static final int       NEWS_PULLDOWN = 1;
    public static final int       NEW_NOTICE    = 2;
    public static final int       TAKE_PHOTO    = 4;
    public static final int       PULLDOWN      = 5;

    private static SparseIntArray map           = new SparseIntArray();

    private SoundTool()
    {
        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);

    }

    public static SoundTool getInstance()
    {
        if (null == soundTool)
        {
            soundTool = new SoundTool();
            map.put(NEWS_REFRESH, R.raw.refresh_loading);
            map.put(NEWS_PULLDOWN, R.raw.refresh_pulling);
            map.put(NEW_NOTICE, R.raw.ptt_sound_playsound_over);
            map.put(TAKE_PHOTO, R.raw.kaca);
            map.put(PULLDOWN, R.raw.psst);
        }

        return soundTool;
    }

    public void play(int num)
    {
        AudioManager mAudioManager = (AudioManager) AppContext.getInstance().getSystemService(Context.AUDIO_SERVICE);
        final int current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);

        final int sourceid = soundPool.load(AppContext.getInstance(), map.get(num), 0);
        // 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率最低0.5最高为2，1代表正常速度

        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener()
        {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
            {
                soundPool.play(sourceid, current, current, 1, 0, 1);
            }
        });

    }

}
