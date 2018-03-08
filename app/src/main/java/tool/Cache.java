package tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import httpconnect.Mhttpconnect;

/**
 * Created by ASUS on 2018/3/5.
 */

public class Cache {
    public interface Action{
        void deal(File file);
    }

    public static boolean Makecache(String url,int describe,Action action,String content){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(url == null){
                return true;
            }
            String data = null;
            File file;
            if (describe == 1) {
                data = url;
                data = data.replace("/","");
                data = data.replace("\\","");
                file = new File(Environment.getExternalStorageDirectory(), data + ".png");
            }
            else{
                data = url;
                data = data.replace("/","");
                data = data.replace("\\","");
                data = data + content;
                file = new File(Environment.getExternalStorageDirectory(),url + "txt");
            }
            if(file.exists()){
                action.deal(file);
                return true;
            }else {
                return false;
            }
        }else {
                return false;
        }
    }

    public static void CreatBitmap(final String url, final Action action){
        Mhttpconnect.GetBitMap(url, new Mhttpconnect.GetImage() {
            @Override
            public void feedback(Bitmap bitmap) {
                String data = null;
                ByteArrayOutputStream bm = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,bm);
                data = url;
                data = data.replace("/","");
                data = data.replace("\\","");
                File file = new File(Environment.getExternalStorageDirectory(),data+".png");

                try{
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bm.toByteArray());
                    fos.close();
                    action.deal(file);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public static void CreatData(final String url, final String content, final Action action){
        Mhttpconnect.Post(url, content, new Mhttpconnect.Getstring() {
            @Override
            public void feedback(String response) {
                File file;
                if (response == null);
                else {
                    Bundle bundle = new Bundle();
                    GetBundleFromString.Trans(bundle,response);
                    if(bundle.getInt("status") == 200){
                        String data = url;
                        data = data.replace("/","");
                        data = data.replace("\\","");
                        data = data + content;

                        file = new File(Environment.getExternalStorageDirectory(),data + ".txt");
                        try {
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(response.getBytes());
                            fos.close();
                            action.deal(file);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
    }
}
