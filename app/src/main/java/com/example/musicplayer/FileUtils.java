package com.example.musicplayer;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/21.
 */

public class FileUtils {
    /**
     * 获取文件的MD5值
     * @param file
     * @return
     */
    public  static String getMD5(File file){
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取目录下的所有文件
     * @param dir
     * @return
     */
    public static java.util.List<File> listFilesInDir(File dir) {
        if (!dir.isDirectory()||!dir.exists()) return null;
        java.util.List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    java.util.List<File> fileList = listFilesInDir(file);
                    if (fileList != null) {
                        list.addAll(fileList);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 删除除file以外的同目录下的其他文件
     * @param dir
     * @param file
     */
    public  static  void deleteFile(File dir,File file){
        if (!file.exists()) return;
        java.util.List<File> files = listFilesInDir(dir);
        for (File file1 : files) {
            String absolutePath = file1.getAbsolutePath();
            android.util.Log.i("sss","delete"+absolutePath);
            if (!file.getAbsolutePath().equals(absolutePath)){
                file1.delete();
            }
        }
    }
}