package com.jerry.lyricsdisplay.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beaglebuddy.mp3.MP3;
import com.jerry.lyricsdisplay.Mp3Singleton;
import com.jerry.lyricsdisplay.R;
import com.jerry.lyricsdisplay.URIGetter;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    public static final String NO_LYRICS_FOUND = "No lyrics found in tag UNSYNCEDLYRICS";
    private static int READ_REQUEST_CODE = 102;

    private ObservableScrollView observableScrollView;
    private TextView textView1;
    private TextView songTitle;
    private FloatingActionButton audioChooserFab;
    private Context mainContext;

    private Mp3Singleton mp3 = Mp3Singleton.getInstance();

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        observableScrollView = (ObservableScrollView) v.findViewById(R.id.scroll_view);
        textView1 = (TextView) v.findViewById(R.id.lyric_cardview);
        songTitle = (TextView) v.findViewById(R.id.song_title_card);
        mainContext = v.getContext();

        audioChooserFab = (FloatingActionButton) v.findViewById(R.id.fab);
        audioChooserFab.attachToScrollView(observableScrollView);
        audioChooserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAudio(v);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        String path = null;
        String title = null;
        String lyric = null;

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
            }

            path = URIGetter.getPath(mainContext, uri);
            mp3.setMp3(path);

            title = mp3.getTitle();
            lyric = mp3.getLyric();
            if (lyric != null && title != null) {
                songTitle.setText(title);
                textView1.setText(lyric);
            } else {
                songTitle.setText(title);
                textView1.setText(NO_LYRICS_FOUND);
            }


        }
    }

    public void chooseAudio(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


}
