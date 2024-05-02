package com.example.itube;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class HomeActivity extends AppCompatActivity {

    private EditText editTextYouTubeUrl;
    private Button buttonPlay, buttonAddToPlaylist, buttonMyPlaylist;
    private YouTubePlayerView youTubePlayerView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_home);
        dbHelper = new DatabaseHelper(this);

        editTextYouTubeUrl = findViewById(R.id.editTextYouTubeUrl);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonAddToPlaylist = findViewById(R.id.buttonAddToPlaylist);
        buttonMyPlaylist = findViewById(R.id.buttonMyPlaylist);

        buttonPlay.setOnClickListener(view -> {
            String url = editTextYouTubeUrl.getText().toString().trim();
            if (!TextUtils.isEmpty(url)) {
                String videoId = extractVideoIdFromUrl(url);
                if (videoId != null) {
                    Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoId", videoId);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddToPlaylist.setOnClickListener(view -> {
            String url = editTextYouTubeUrl.getText().toString().trim();
            if (!TextUtils.isEmpty(url)) {
                String userEmail = getUserEmail();
                dbHelper.addUrl(userEmail, url);
                Toast.makeText(this, "Added to playlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "URL is empty", Toast.LENGTH_SHORT).show();
            }
        });

        buttonMyPlaylist.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlaylistActivity.class);
            String userEmail = getUserEmail();
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        });

    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getBoolean("LoggedIn", false);
    }

    private String extractVideoIdFromUrl(String url) {
        Uri uri = Uri.parse(url);
        String videoId = uri.getQueryParameter("v");
        if (videoId == null) {
            videoId = uri.getLastPathSegment();
        }
        return videoId;
    }

    private String getUserEmail() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getString("userEmail", "");
    }
}
