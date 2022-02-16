package com.example.video_player;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.halilibo.bvpkotlin.BetterVideoPlayer;

public class PlayerActivity extends AppCompatActivity {
     BetterVideoPlayer player;
     String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        player=findViewById(R.id.player);
        filePath=getIntent().getStringExtra("VIDEO");
        Uri videoUri = Uri.parse(filePath);
        player.setSource(videoUri);

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }
}