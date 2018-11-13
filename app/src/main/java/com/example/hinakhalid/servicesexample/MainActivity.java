package com.example.hinakhalid.servicesexample;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ArrayList<HashMap<String, String>> songsPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File extStore = Environment.getExternalStorageDirectory();

        String sdpath, sd0path;
        sdpath="/storage/extSdCard/";
        if(new File("/storage/extSdCard/").exists())
        {
            sdpath="/storage/extSdCard/";
            Log.i("Sd Cardext Path",sdpath);
        }
        if(new File("/storage/sdcard1/").exists())
        {
            sdpath="/storage/sdcard1/";
            Log.i("Sd Card1 Path",sdpath);
        }
        if(new File("/storage/usbcard1/").exists())
        {
            sdpath="/storage/usbcard1/";
            Log.i("USB Path",sdpath);
        }
        if(new File("/storage/sdcard0/").exists())
        {
            sd0path="/storage/sdcard0/";
            Log.i("Sd Card0 Path",sd0path);
        }
       songsPlaylist = new ArrayList<HashMap<String, String>>();
      // getPlayList(sdpath);
        if(songsPlaylist != null){
            for(int i=0;i<songsPlaylist.size();i++){
                String fileName=songsPlaylist.get(i).get("file_name");
                String filePath=songsPlaylist.get(i).get("file_path");
                //here you will get list of file name and file path that present in your device
                Log.e("Songs"," name ="+fileName +" path = "+filePath);
            }
        }

    }
    ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
            try {
                File rootFolder = new File(rootPath);
                File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
                for (File file : files) {
                    String filname = file.getName();
                    if (file.getName().endsWith(".mp3")){
                        HashMap<String, String> song = new HashMap<>();
                        song.put("file_path", file.getAbsolutePath());
                        song.put("file_name", file.getName());
                        songsPlaylist.add(song);
                    }
                    else if (file.isDirectory()) {
                        if (getPlayList(file.getAbsolutePath()) != null) {
                            //fileList.addAll(getPlayList(file.getAbsolutePath()));
                            getPlayList(file.getAbsolutePath());
                        } else {
                            break;
                        }
                    }
                }
                return songsPlaylist;
            } catch (Exception e) {
                return null;
            }
    }
    public  void OnBtnFolderSelect_Clicked(View view) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/mpeg");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:{
                if (resultCode == RESULT_OK){
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        Log.i("PATH", "Uri: " + uri.toString());
                    }
                    Log.i("PATH","Address");
                    try {
                        this.playMusic(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
    public  void playMusic(Uri path) throws IOException {
        if(mediaPlayer!= null){
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(getApplicationContext(), path);
        mediaPlayer.prepare();
        mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playNextSong();
                    }
                }
        );
        mediaPlayer.start();
    }

    public void PlayPause(View view){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.start();
        }
    }


    public void btnPlayNext_Clicked(View view){
        playNextSong();
    }

    public void playNextSong(){

    }

    public void btnPlayPrevious_Clicked(View view){
        playPreviousSong();
    }

    public void playPreviousSong(){

    }
}
