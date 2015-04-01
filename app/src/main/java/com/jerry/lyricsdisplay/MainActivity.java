package com.jerry.lyricsdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beaglebuddy.mp3.MP3;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {
    public static final String NO_LYRICS_FOUND = "No lyrics found in tag UNSYNCEDLYRICS";
    private static int READ_REQUEST_CODE = 102;

    private ObservableScrollView observableScrollView;
    private Toolbar toolbar;
    private TextView textView1;
    private TextView songTitle;
    //private Button audioChooser;
    private FloatingActionButton audioChooserFab;
    private Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        observableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        textView1 = (TextView) findViewById(R.id.lyric_cardview);
        songTitle = (TextView) findViewById(R.id.song_title_card);
        mainContext = this.getApplicationContext();

        audioChooserFab = (FloatingActionButton) findViewById(R.id.fab);
        audioChooserFab.attachToScrollView(observableScrollView);

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");

        registerReceiver(mReceiver, intentFilter);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        String path = null;
        String title = null;
        String lyric = null;


        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
            }

            path = URIGetter.getPath(mainContext, uri);

            try {
                MP3 mp3 = new MP3(path);
                title = mp3.getTitle();
                lyric = mp3.getLyrics();
                if(lyric!=null && title!=null){
                    songTitle.setText(title);
                    textView1.setText(lyric);
                }else {
                    songTitle.setText(title);
                    textView1.setText(NO_LYRICS_FOUND);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void chooseAudio(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    /*    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("track");
            textView1.setText(title);
        }
    };*/
}
