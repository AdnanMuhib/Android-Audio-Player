package com.example.hinakhalid.servicesexample;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class playerService2 extends IntentService {
    private MediaPlayer player;

    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.

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
         //   return START_STICKY;
        }

    public playerService2() {
        super("serviceI");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone

        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }
}
