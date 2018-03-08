package httpconnect;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ASUS on 2018/2/20.
 */

public class Mhttpconnect {


    public interface Getstring{
         void feedback(String response);
    }

     public static void Post(final String url, final String content, final Getstring getstring){

         final Handler handler = new Handler();
         new Thread(new Runnable() {
             @Override
             public void run() {
                 Log.e("666",url + content);
                 String data = connect(url,content);
                 if (data == null);
                 else {
                     final String response = data;
                             handler.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     getstring.feedback(response);
                                 }
                             });
                 }

             }
         }).start();
     }


     public interface GetImage{
         void feedback(Bitmap bitmap);
     }

     public  static void GetBitMap(final String url,final GetImage getImage){
         final Handler handler = new Handler();
         new Thread(new Runnable() {

             @Override
             public void run() {
                 Bitmap bitmap1 = connect2(url);
                 if (bitmap1 == null);
                 else {
                     final Bitmap bitmap = bitmap1;
                             handler.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     getImage.feedback(bitmap);
                                 }
                             });
                 }

             }
         }).start();
     }


     public static String connect(String url,String content){
         HttpURLConnection httpsURLConnection = null;



         try{

             URL url1 = new URL(url);

             httpsURLConnection = (HttpURLConnection)url1.openConnection();

             httpsURLConnection.setRequestMethod("POST");
             httpsURLConnection.setReadTimeout(5000);
             httpsURLConnection.setConnectTimeout(10000);
             httpsURLConnection.setDoOutput(true);

             String data = content;
             OutputStream outputStream = httpsURLConnection.getOutputStream();
             outputStream.write(data.getBytes());
             outputStream.flush();
             outputStream.close();

             int responsecode = httpsURLConnection.getResponseCode();
             if(responsecode == 200){
                 InputStream inputStream = httpsURLConnection.getInputStream();

                 String response = getreturn(inputStream);
                 Log.e("666",response);
                 return response;
             }
             else{
                  throw new NetworkErrorException("response is"+responsecode);
             }
         }catch (Exception e) {
             e.printStackTrace();
         }finally {
             if(httpsURLConnection != null)
             {
                 httpsURLConnection.disconnect();
             }
         }
         return null;
     }

    public static Bitmap connect2(String url) {
        HttpURLConnection httpsURLConnection = null;


        try {

            URL url1 = new URL(url);

            httpsURLConnection = (HttpURLConnection) url1.openConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setReadTimeout(5000);
            httpsURLConnection.setConnectTimeout(10000);
            httpsURLConnection.connect();


            int responsecode = httpsURLConnection.getResponseCode();
            if (responsecode == 200) {
                InputStream inputStream = httpsURLConnection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                return bitmap;
            } else {
                throw new NetworkErrorException("response is" + responsecode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return null;

    }
    public static String getreturn(InputStream inputStream)
     throws Exception{
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         byte[] input = new byte[1024];
         int len = -1;
         while ((len = inputStream.read(input)) != -1){
             byteArrayOutputStream.write(input,0,len);
         }
         String data = byteArrayOutputStream.toString();
         inputStream.close();
         byteArrayOutputStream.close();
         return data;
     }
}
