package com.example.itube;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    private ListView listViewPlaylist;
    private ArrayList<String> playlist = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        databaseHelper = new DatabaseHelper(this);
        listViewPlaylist = findViewById(R.id.listViewPlaylist);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playlist);
        listViewPlaylist.setAdapter(adapter);

        // Get user email from intent or from shared preferences
        userEmail = getIntent().getStringExtra("userEmail");
        if (userEmail != null) {
            loadPlaylistFromDatabase();
        } else {
            Toast.makeText(this, "User not identified, cannot load playlist.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPlaylistFromDatabase() {
        ArrayList<String> loadedPlaylist = databaseHelper.getPlaylist(userEmail);
        if (loadedPlaylist != null) {
            playlist.clear();
            playlist.addAll(loadedPlaylist);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to load playlist", Toast.LENGTH_SHORT).show();
        }
    }
}
