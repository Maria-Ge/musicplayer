package com.example.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn001;
    ImageView imageView001;
    android.widget.ListView lv;
    ArrayList<String> url_list=new ArrayList<String>();
    ArrayList<String> as_list=new ArrayList<String>();
    ArrayList<Integer> id_list=new ArrayList<Integer>();

    private String url1,as1;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn001= (Button) findViewById(R.id.btn001);
        imageView001= (ImageView) findViewById(R.id.image001);
        btn001.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent=new Intent(MainActivity.this,PlayActivity.class);
                startActivity(intent);
            }
        });



        parseHTMLwithJSOUP();//将html数据解析出来并传到界面上
    }
    public void parseHTMLwithJSOUP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://freemusicarchive.org/genre/Classical/").timeout(60000).get();
                    Elements urls = doc.select("a.icn-arrow");
                    Elements artists = doc.select("span.ptxt-artist");
                    Elements songs = doc.select("span.ptxt-track");
                    for (int i = 0; i < urls.size(); i++) {
                        String url0 = urls.get(i).attr("data-url");
                        String url = url0.substring(0,url0.length() -7);
                        String artist = artists.get(i + 1).text();
                        String song = songs.get(i + 1).text();
                        android.util.Log.e("URL:", url);
                        android.util.Log.e("ARTIST:", artist);
                        android.util.Log.e("SONG:", song);
                        id_list.add(i);
                        url_list.add(url);
                        as_list.add(song + "\n" + artist);
                    }
                    showResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //显示在界面上
    private  void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv=(android.widget.ListView)findViewById(R.id.lv);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                        MainActivity.this,android.R.layout.simple_list_item_1,as_list);
                lv.setAdapter(adapter);

                //点击item事件
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {

                        //得到当前歌曲的相关信息
                        url1=url_list.get(position);//得到歌曲下载链接
                        String name=url1.substring(35,url1.length() -9);
                        int id1=id_list.get(position);
                        as1=as_list.get(position);//得到song和artist
                        android.util.Log.d("MainActivity:","url is " +url1);
                        android.util.Log.d("MainActivity:","as is " +as1);


                        File file=new File(getFilesDir().getAbsolutePath()+"/Music/",""+name+".mp3");

                        if (!file.exists()) {
                            Intent intent=new Intent(MainActivity.this,MyIntentService.class);
                            android.os.Bundle bundle=new android.os.Bundle();
                            bundle.putString("url"," https://freemusicarchive.org/track/"+name+"/download");
                            bundle.putString("path",getFilesDir().getAbsolutePath()+"/Music/");
                            bundle.putString("name",""+name+".mp3");
                            bundle.putString("key","path");
                            intent.putExtras(bundle);
                            startService(intent);
                        }


                    }
                });
            }
        });
    }



}