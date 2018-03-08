package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ASUS on 2018/2/27.
 */

public class GetStringFromIS {
    public static String getString(InputStream is){
        InputStreamReader isr = null;
        try{
            isr = new InputStreamReader(is,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
