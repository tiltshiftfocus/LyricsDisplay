package com.jerry.lyricsdisplay.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jerry.lyricsdisplay.Mp3Singleton;
import com.jerry.lyricsdisplay.R;
import com.jerry.lyricsdisplay.URIGetter;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetLyricFragment extends Fragment {


    private static int READ_REQUEST_CODE = 102;

    private ObservableScrollView observableScrollView;
    private EditText editText1;
    private TextView songTitle;
    private FloatingActionButton audioChooserFab;
    private Context mainContext;

    private Mp3Singleton mp3Singleton = Mp3Singleton.getInstance();

    public SetLyricFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_lyric, container, false);
        setHasOptionsMenu(true);

        observableScrollView = (ObservableScrollView) v.findViewById(R.id.edit_scroll_view);
        editText1 = (EditText) v.findViewById(R.id.set_lyric);
        songTitle = (TextView) v.findViewById(R.id.edit_song_title_card);
        mainContext = v.getContext();

        audioChooserFab = (FloatingActionButton) v.findViewById(R.id.fab);
        audioChooserFab.attachToScrollView(observableScrollView);
        audioChooserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAudio(v);
            }
        });

        if (mp3Singleton.getMp3() != null) {
            songTitle.setText(mp3Singleton.getTitle());
            editText1.setText(mp3Singleton.getLyric());
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setlyric, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.saveBtn:
                saveLyrics();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveLyrics() {
        try {
            if (mp3Singleton.getMp3() == null) {
                Toast.makeText(getActivity(), "No Music/Song Loaded!", Toast.LENGTH_SHORT).show();
            } else {
                mp3Singleton.getMp3().setLyrics(String.valueOf(editText1.getText()));
                mp3Singleton.getMp3().save();
                mp3Singleton.reloadMp3();
                Toast.makeText(getActivity(), "Lyrics Saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getActivity(),"Lyrics Save Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        String title = null;
        String lyric = null;
        String path = null;

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
            }

            path = URIGetter.getPath(mainContext, uri);
            mp3Singleton.setMp3(path);

            title = mp3Singleton.getTitle();
            lyric = mp3Singleton.getLyric();
            if (lyric != null && title != null) {
                songTitle.setText(title);
                editText1.setText(lyric);
            } else {
                songTitle.setText(title);
                editText1.setText(getResources().getString(R.string.no_lyric_found));
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
