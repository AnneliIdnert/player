package com.example.idnert.test3;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private AudioManager mAudioManager;
    private String TAG = "TEST";

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Button volumeUp = (Button) findViewById(R.id.volume_button_up);
        Button volumeDown = (Button) findViewById(R.id.volume_button_down);

        volumeUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Up(v);
            }
        });

        volumeDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Down(v);
            }
        });
    }

    public void Next(View v){
        //TODO: PREVENT GOOGLE PLAY TO ALWAYS HOGG MEDIA BUTTON INTENTION.
        //String CMDNEXT = "next";
        //String CMDNAME = "command";
        //String SERVICECMD = "com.android.music.musicservicecommand";

        Intent mediaEvent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
        mediaEvent.putExtra(Intent.EXTRA_KEY_EVENT, event);
        context.sendBroadcast(mediaEvent);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent mediaEvent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT);
                mediaEvent.putExtra(Intent.EXTRA_KEY_EVENT, event);
                context.sendBroadcast(mediaEvent);
            }
        }, 100);

        /**if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDNEXT  );
            MainActivity.this.sendBroadcast(i);
        } */
    }

    public void Previous(View v){
        String CMDPREVIOUS = "previous";
        String CMDNAME = "command";
        String SERVICECMD = "com.android.music.musicservicecommand";

        if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDPREVIOUS  );
            MainActivity.this.sendBroadcast(i);
        }
    }

   public void Up(View v){
       mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
               AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    public void Down(View v){
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    //TODO: UGLY TEST HACK TO SIMULATE HARDWARE MEDIA BUTTON
    public void handleMediaKeyEvent(KeyEvent keyEvent) {
    /*
     * Attempt to execute the following with reflection.
     *
     * [Code]
     * IAudioService audioService = IAudioService.Stub.asInterface(b);
     * audioService.dispatchMediaKeyEvent(keyEvent);
     */
        try {
            // Get binder from ServiceManager.checkService(String)
            IBinder iBinder  = (IBinder) Class.forName("android.os.ServiceManager")
                    .getDeclaredMethod("checkService",String.class)
                    .invoke(null, Context.AUDIO_SERVICE);

            Log.d(TAG, "STEG 1");

            // get audioService from IAudioService.Stub.asInterface(IBinder)
            Object audioService  = Class.forName("android.media.IAudioService$Stub")
                    .getDeclaredMethod("asInterface",IBinder.class)
                    .invoke(null,iBinder);

            Log.d(TAG, "STEG 2");
            Log.d(TAG, keyEvent.toString());

            // Dispatch keyEvent using IAudioService.dispatchMediaKeyEvent(KeyEvent)
            Class.forName("android.media.IAudioService")
                    .getDeclaredMethod("dispatchMediaKeyEvent",KeyEvent.class)
                    .invoke(audioService, keyEvent);

            Log.d(TAG, "STEG 3");

        }  catch (Exception e1) {
            e1.printStackTrace();
            Log.d(TAG, "NOPE INTE RÄTT ARGUMENT");
        }
    }
}
