package com.example.musicplayer;


public class SongInfo {
    String songName;//歌曲名
    String singer;//歌手
    String sid;
    String songPath;//歌曲地址
    String songTime;

    public String getSongTime(){ return songTime; }
    public void setSongTime(String songTime){this.songTime=songTime;}
    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    public String getSinger() {
        return singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    public String getSongPath() {
        return songPath;
    }
    public String getmaxsid(){
        String max = null;
        return max;
    };
    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }
}


