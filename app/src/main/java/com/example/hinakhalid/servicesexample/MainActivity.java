package com.example.hinakhalid.servicesexample;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ArrayList<Uri> playList;
    int current_song_index;
    int last_song_index;
    TextView songName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songName = (TextView)findViewById(R.id.textViewSongName);
        current_song_index = -1;
        last_song_index = -1;
    }

    public  void OnBtnFolderSelect_Clicked(View view) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/mpeg");
        startActivityForResult(intent, 1);
    }
    public  void OnBtnCreatePlaylist_Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/mpeg");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 2);
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
                        songName.setText(getFileNameFromUri(uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case 2:{
                if (resultCode == RESULT_OK){
                    //Uri uri = null;
                    if (data != null) {
                        playList = new ArrayList<>();
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                Uri uri = item.getUri();
                                playList.add(uri);
                                //In case you need image's absolute path
                                //String path= getRealPathFromURI(getActivity(), uri)
                            }
                        }
                        //playList = (ArrayList) data.getData();
                        //Log.i("PATH", "Uri: " + uri.toString());
                    }
                    Log.i("PATH","Address");
                    try {
                        if(playList.size() > 0){
                            current_song_index = 0;
                            last_song_index = playList.size() -1;
                            Uri firstSong = playList.get(0);
                            this.playMusic(firstSong);
                            songName.setText(getFileNameFromUri(firstSong));
                        }

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
                        try {
                            playNextSong();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        mediaPlayer.start();
    }

    public void PlayPause(View view){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else if(mediaPlayer != null) {
            mediaPlayer.start();
        }

    }


    public void btnPlayNext_Clicked(View view) throws IOException {
        playNextSong();
    }

    public void playNextSong() throws IOException {
        if (current_song_index < last_song_index){
            playMusic(playList.get(current_song_index+1));
            songName.setText(getFileNameFromUri(playList.get(current_song_index+1)));
            current_song_index = current_song_index + 1;
        }
    }

    public void btnPlayPrevious_Clicked(View view) throws IOException {
        playPreviousSong();
    }

    public void playPreviousSong() throws IOException {
        if (current_song_index > 0){
            playMusic(playList.get(current_song_index-1));
            songName.setText(getFileNameFromUri(playList.get(current_song_index-1)));
            current_song_index = current_song_index - 1;
        }
    }
    public  void showPlayList(View view){

        if(playList == null){
            return;
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select Audio File to Play");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
        for (Uri u: playList) {
            arrayAdapter.add(getFileNameFromUri(u));
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String strName = arrayAdapter.getItem(which);
                try {
                    playMusic(playList.get(which));
                    songName.setText(getFileNameFromUri(playList.get(which)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builderSingle.show();
    }
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
