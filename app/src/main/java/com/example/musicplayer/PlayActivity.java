package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;


public class PlayActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 0;
    MediaPlayer mediaPlayer = new MediaPlayer();
    android.widget.ListView listView;
    ArrayList<String> songNameList = new ArrayList<String>();
    java.util.List<com.example.musicplayer.SongInfo> listsong;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            java.util.List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!permissions.isEmpty()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION);
            }
        }

        listsong = new ArrayList<>();
        listsong = Utils.getmusic(this);
        for (int i = 0; i < listsong.size(); i++) {
            songNameList.add(listsong.get(i).singer);
        }
        android.util.Log.d("TAG", "onCreate:listsong size is "+listsong.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlayActivity.this, android.R.layout.simple_list_item_1, songNameList);
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int position, long id) {
                int songid = (int) id;
                android.util.Log.d("TAG", " LIST ID IS " + songid);
                String a = Integer.toString(songid);
                Intent intent = new Intent(PlayActivity.this, MusicActivity.class);
                intent.putExtra("listID", songid);
                startActivity(intent);
            }
        });
    }

    //权限请求许可
    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}