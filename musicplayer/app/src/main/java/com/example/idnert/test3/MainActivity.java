package com.example.idnert.test3;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Next(View v){

        String CMDNEXT = "next";
        String CMDNAME = "command";
        String SERVICECMD = "com.android.music.musicservicecommand";
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDNEXT  );
            MainActivity.this.sendBroadcast(i);
        }
    }

    public void Previous(View v){
        String CMDPREVIOUS = "previous";
        String CMDNAME = "command";
        String SERVICECMD = "com.android.music.musicservicecommand";
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDPREVIOUS  );
            MainActivity.this.sendBroadcast(i);
        }
    }

   public void Up(View v){
        KeyEvent kdown = new KeyEvent(KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_BACK);
        this.dispatchKeyEvent(kdown);
    }

    public void Down(View v){
        KeyEvent kdown = new KeyEvent(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_BACK);
        this.dispatchKeyEvent(kdown);
    }
}
