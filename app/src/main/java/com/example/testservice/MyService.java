package com.example.testservice;





import static com.example.testservice.MyApp.channel_id;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service{
    private MediaPlayer mediaPlayer;
    private Boolean isPlaying;
    private Song mSong;
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME =2;
    public static final int ACTION_CLEAR = 3;

    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                break;
        }
    }

    private void resumeMusic() {
        if(mediaPlayer != null && !isPlaying){
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(mSong);
        }
    }

    private void pauseMusic() {
        if(mediaPlayer != null && isPlaying){
            mediaPlayer.pause();
            isPlaying= false;
            sendNotification(mSong);
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Song song = (Song)bundle.get("data");
            if(song != null){
                mSong = song;
                startMusic(song);
                sendNotification(song);
            }
        }
        int intentService = intent.getIntExtra("action_service",0);
        handleActionMusic(intentService);
        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), song.getResource());
        }
        mediaPlayer.start();
        isPlaying = true;

    }

    private void sendNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent
                .FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),song.getResource());
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.layout_cus);
        remoteViews.setTextViewText(R.id.tv_titleSOng,song.getTitle());
        remoteViews.setTextViewText(R.id.tv_singleSOng,song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song,bitmap);
        remoteViews.setImageViewResource(R.id.img_playOrPause,R.drawable.baseline_pause);

        if(isPlaying){
            remoteViews.setOnClickPendingIntent(R.id.img_playOrPause,getPendingIntent(this,ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.img_playOrPause,R.drawable.baseline_pause);
        }else {
            remoteViews.setOnClickPendingIntent(R.id.img_playOrPause,getPendingIntent(this,ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.img_playOrPause,R.drawable.baseline_play);
        }
        remoteViews.setOnClickPendingIntent(R.id.img_clear,getPendingIntent(this,ACTION_CLEAR));

        Notification notification = new Notification.Builder(this,channel_id)
                .setSmallIcon(R.drawable.baseline_music)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .build();
        startForeground(1,notification);
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action",action);
        return PendingIntent.getBroadcast(context.getApplicationContext(),action,intent,PendingIntent
                .FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}