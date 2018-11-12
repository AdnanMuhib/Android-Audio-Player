package com.example.hinakhalid.servicesexample;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

public class playerService extends Service {
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone
        player = new MediaPlayer();
      try {
          AssetFileDescriptor descriptor = getAssets().openFd("song.mp3");
          player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
          descriptor.close();
          player.prepare();
      }catch(Exception e)
      {
          Log.i("app", "onStartCommand: "+e.toString());
          e.printStackTrace();
      }


      //  player = MediaPlayer.create(this,Settings.System.DEFAULT_RINGTONE_URI);
        //setting loop play to true
        //this will make the ringtone continuously playing

        player.setLooping(true);

        //staring the player
        player.start();

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }
}
