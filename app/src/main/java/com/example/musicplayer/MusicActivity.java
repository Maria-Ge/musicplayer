package com.example.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicActivity extends AppCompatActivity {
    java.util.List<SongInfo> listsong;
    ArrayList<String> songNameList = new ArrayList<String>();
    private CircleImageView circleImageView;//圆形组件
    private android.widget.TextView tv_end;
    private android.widget.TextView tv_current;
    private android.widget.TextView tv_song;
    private android.widget.TextView tv_singer;
    private int listID;
    private boolean PlayModel;
    String path;
    String songname;
    String singer;
    String songtime;
    private boolean isSeekBarChanging;//防止与计时器冲突
    private android.widget.SeekBar seekBar;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ImageView file = findViewById(R.id.file);
        circleImageView = findViewById(R.id.circle_image);
        ImageView stop = findViewById(R.id.stop);
        ImageView prev = findViewById(R.id.prev);
        ImageView next = findViewById(R.id.next);
        ImageView model = findViewById(R.id.model);
        ImageView random = findViewById(R.id.random);
        Utils.getmusic(MusicActivity.this);
        listsong = new ArrayList<>();
        listsong = Utils.getmusic(this);
        tv_end = findViewById(R.id.tv_end);
        tv_current = findViewById(R.id.tv_current);
        tv_song = findViewById(R.id.song);
        tv_singer = findViewById(R.id.singer);
        seekBar = findViewById(R.id.bar);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());

        Intent intent = getIntent();
        listID = intent.getIntExtra("listID",1);
        android.util.Log.d("TAG", "onCreate: ID IS ---------------> " + listID);
        initPlayer(listID);
        mediaPlayer.pause();

        play(listID);

        random.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                playrandom();
                Toast.makeText(MusicActivity.this,"随机播放",Toast.LENGTH_SHORT).show();
            }
        });
        model.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                playloop();
                Toast.makeText(MusicActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
            }
        });
        file.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent1 = new Intent(MusicActivity.this, PlayActivity.class);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                startActivity(intent1);

            }
        });
        stop.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        });//暂停
        prev.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(listID==0){
                    listID=listsong.size()-1;
                    initPlayer(listID);
                    play(listID);
                }
                else {
                    listID = listID - 1;
                    initPlayer(listID);
                    play(listID);
                }

            }//上一首
        });
        next.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(listID==(listsong.size()-1)){
                    listID=0;
                    initPlayer(listID);
                    play(listID);
                }
                else {
                    listID = listID + 1;
                    initPlayer(listID);
                    play(listID);
                }
            }
        });//下一首
    }


    public void initPlayer(int listID) {
        android.util.Log.d("TAG", "initPlayer:listID is "+listID);
        songtime = listsong.get(listID).songTime;
        singer = listsong.get(listID).singer;
        songname = listsong.get(listID).songName;
        seekBar.setMax(Integer.parseInt(songtime));
        songtime = ShowTime(Integer.parseInt(songtime));
        android.util.Log.d("TAG", "initPlayer:TIME I---------------> " + songtime);
        tv_end.setText(songtime);
        tv_song.setText(singer);
        tv_singer.setText(songname);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isSeekBarChanging && mediaPlayer.isPlaying()) {//如果进度条未改变，并且当前正在播放
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tv_current.setText(ShowTime(mediaPlayer.getCurrentPosition()));
                    Message msg = new Message();
                    msg.what = 1;
                }
            }
        }, 0, 1000);
    }

    private void playloop() {
        if(listID==(listsong.size()-1)){
            listID=0;
            initPlayer(listID);
            play(listID);
        }
        else {
            listID = listID + 1;
            initPlayer(listID);
            play(listID);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mediaPlayer.pause();

            }
          return super.onKeyDown(keyCode, event);
    }

    public String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        android.util.Log.d("TAG", "Minute---------------> " + minute);
        android.util.Log.d("TAG", "Second---------------> " + second);
        return String.format("%02d:%02d", minute, second);
    }//格式化时间



    private void playrandom(){
        listID = (int) (1 + Math.random() * (listsong.size() - 1 ));
        initPlayer(listID);
        play(listID);
    }


    public class MySeekBar implements android.widget.SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {}
        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
            isSeekBarChanging = true;
        }
        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    public void play (int i) {
        try {
            mediaPlayer.reset();
            path = listsong.get(i).songPath;
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaP) {
                    mediaP.start();
                }
            });
        } catch (Exception e) {
            android.util.Log.v("MusicService", e.getMessage());
        }
    }
}

