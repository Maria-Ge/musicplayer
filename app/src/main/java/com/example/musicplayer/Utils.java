package com.example.musicplayer;

import android.content.Context;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import java.util.ArrayList;

class Utils {
    //定义一个集合，存放从本地读取到的内容
    public static ArrayList<String> list=new ArrayList<>();
    public static java.util.List<SongInfo> listsong;
    public static SongInfo songInfo;

    public static java.util.List<SongInfo> getmusic(Context context) {

        listsong = new ArrayList<>();

        android.database.Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.IS_MUSIC,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.DURATION
                },
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                songInfo = new SongInfo();
                songInfo.sid = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                songInfo.songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                songInfo.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                songInfo.songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                songInfo.songTime = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                android.util.Log.d("TAG", "IDIS--------------> "+songInfo.sid);
                android.util.Log.d("TAG", "tIME--------------> "+songInfo.songTime);
                if (songInfo.songName.contains("-")) {
                    String[] str = songInfo.songName.split("-");
                    songInfo.singer = str[1];
                    songInfo.songName = str[0];
                    android.util.Log.d("TAG", "STR0 IS--------------> "+str[1]);
                    android.util.Log.d("TAG", "STR1 IS--------------> "+str[0]);
                }
                listsong.add(songInfo);
            }
        }
        cursor.close();
        return listsong;
    }


}
