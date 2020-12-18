package com.example.musicplayer;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

/**
 * Created by Administrator on 2017/6/21.
 */

public class MyIntentService extends IntentService {
    private  final  String TAG="MyIntentService";
    private String url ;//文件的网络地址
    private String path ;//文件在本地的目录地址
    private String name;//文件的名字
    private String key;//文件路径存储在SP中的key

    /**
     * 构造方法 必须要
     */
    public MyIntentService() {
        //表示这个线程的名字 可随意
        super("MyIntentService");
    }
    //会自动开启线程下载的
    @Override
    protected void onHandleIntent(Intent intent) {
        android.os.Bundle extras = intent.getExtras();
        url = extras.getString("url");
        path = extras.getString("path");
        name=extras.getString("name");
        key=extras.getString("key");
        if (TextUtils.isEmpty(url)||TextUtils.isEmpty(path)||TextUtils.isEmpty(name)||TextUtils.isEmpty(key)){
            android.util.Log.i(TAG,"url,path,name,key不能为空");
            return;
        }
        //如果文件夹目录不存在则创建
        File file = new File(path );
        if (!file.exists())
            file.mkdirs();
        downloadFile(url,new File(path+name));
    }

    /**
     * 下载文件到本地文件的放法
     * @param downloadUrl    网络地址
     * @param file  本地文件目录
     */
    private void downloadFile(String downloadUrl, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        java.io.InputStream ips = null;
        try {
            java.net.URL url = new java.net.URL(downloadUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setReadTimeout(10000);
            huc.setConnectTimeout(3000);
            ips = huc.getInputStream();
            // 拿到服务器返回的响应码
            int hand = huc.getResponseCode();
            if (hand == 200) {

                // 建立一个byte数组作为缓冲区，等下把读取到的数据储存在这个数组
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = ips.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                saveFileMsg(file);
            } else {
                android.util.Log.i(TAG,"网络连接失败");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (ips != null) {
                    ips.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存文件路劲到sp中
     * @param file
     */
    private void saveFileMsg(File file){
        if (file==null||!file.exists())
            return;
        SharedPreferences sharedPreferences=getSharedPreferences(TAG,0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,file.getAbsolutePath());
        edit.commit();
    }
}