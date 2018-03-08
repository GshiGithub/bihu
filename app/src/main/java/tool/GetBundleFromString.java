package tool;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by ASUS on 2018/2/21.
 */

public class GetBundleFromString {
    public static int flag3 = 0;

    public static void Trans(Bundle bundle,String a){
        flag3 = 0;
        String key = null;
        int begin = 0;
        int flag = 1;
        int flag2 = 1;

        for(int[] i = new int[]{0};i[0] < a.length();i[0] ++){
           if(i[0] == 0){
               continue;
           }
           if(a.charAt(i[0]) == '\"'){
               if(begin != 0 && flag == 1){
                   key = a.substring(begin,i[0]);
                   begin = 0;
               }else if(begin != 0 && flag == -1&&flag2 == -1){
                   bundle.putString(key,a.substring(begin,i[0]));
                   begin = 0;
                   flag = 1;
                   flag2 = 0;
               }else if (begin != 0 && flag == -1){
                   flag2 = -1;
                   begin = i[0] + 1;
               }else {

                   begin = i[0] + 1;
               }

           }else if(a.charAt(i[0]) == ':'&&begin == 0){
               flag = -1;
               begin = i[0] + 1;
           }else if(a.charAt(i[0]) == ','&&flag2 == 1){
               String c = a.substring(begin,i[0]);
               if(c.equalsIgnoreCase("null")||c.equals("false")||c.equals("true") )
                   bundle.putString(key,c);
               else {
                   bundle.putInt(key,Integer.parseInt(a.substring(begin,i[0])));
               }
               begin = 0;
               flag = 1;
           }else if(a.charAt(i[0]) == ',' && flag2 != -1){
               flag2 = 1;
           } else if((a.charAt(i[0]) == '{' || a.charAt(i[0]) == '[')&&a.charAt(i[0] - 1) == ':'){
               bundle.putBundle(key,Trans2(a,i));
               i[0] ++;
               begin = 0;
               flag = 1;
           }else if(a.charAt(i[0]) == '{' || a.charAt(i[0]) == '['){
               bundle.putBundle(String.valueOf('0'+ flag3),Trans2(a,i));
               flag3 ++;
               i[0] ++;
               begin = 0;
               flag = 1;
           }else if(a.charAt(i[0]) == ']' || a.charAt(i[0]) == '}' && a.charAt(i[0] - 1) != '\"'){
               String c = a.substring(begin,i[0]);
               if(c.equalsIgnoreCase("null")||c.equals("false")||c.equals("true") )
                   bundle.putString(key,c);
               else {
                   bundle.putInt(key,Integer.parseInt(a.substring(begin,i[0])));
               }
               begin = 0;
               flag = 1;
           }
        }
    }

    public static Bundle Trans2(String a,int[] i){
        i[0] ++;
        String key = null;
        int begin = 0;
        int flag = 1;
        int flag2 = 1;
        Bundle bundle = new Bundle();

        while (a.charAt(i[0]) != ']'&& a.charAt(i[0]) != '}'){
            if(a.charAt(i[0]) == '\"'){
                if(begin != 0 && flag == 1){
                    key = a.substring(begin,i[0]);
                    begin = 0;
                }else if(begin != 0 && flag == -1&&flag2 == -1){
                    bundle.putString(key,a.substring(begin,i[0]));
                    begin = 0;
                    flag = 1;
                    flag2 = 0;
                }else if (begin != 0 && flag == -1){
                    flag2 = -1;
                    begin = i[0] + 1;
                }else {

                    begin = i[0] + 1;
                }

            }else if(a.charAt(i[0]) == ':'&&begin == 0){
                flag = -1;
                begin = i[0] + 1;
            }else if(a.charAt(i[0]) == ','&&flag2 == 1){
                String c = a.substring(begin,i[0]);
                if(c.equalsIgnoreCase("null")||c.equals("false")||c.equals("true") )
                    bundle.putString(key,c);
                else {
                    bundle.putInt(key,Integer.parseInt(a.substring(begin,i[0])));
                }
                begin = 0;
                flag = 1;
            }else if(a.charAt(i[0]) == ',' && flag2 != -1){
                flag2 = 1;
            } else if((a.charAt(i[0]) == '{' || a.charAt(i[0]) == '[')&&a.charAt(i[0] - 1) == ':'){
                bundle.putBundle(key,Trans2(a,i));
                if(i[0] + 1 < a.length() && a.charAt(i[0] + 1) == ',')
                    i[0] ++;
                begin = 0;
                flag = 1;
            }else if(a.charAt(i[0]) == '{' || a.charAt(i[0]) == '['){
                bundle.putBundle(String.valueOf(flag3),Trans2(a,i));
                flag3 ++;
                if(i[0] + 1 < a.length() && a.charAt(i[0] + 1) == ',')
                    i[0] ++;
                begin = 0;
                flag = 1;
            }else if(a.charAt(i[0] + 1) == ']' || a.charAt(i[0] + 1) == '}' && a.charAt(i[0]) != '\"'&&(a.charAt(i[0]) != '}'||a.charAt(i[0]) != ']')){
                String c = a.substring(begin,i[0] + 1);
                if(c.equalsIgnoreCase("null")||c.equals("false")||c.equals("true") )
                    bundle.putString(key,c);
                else {
                    bundle.putInt(key,Integer.parseInt(a.substring(begin,i[0] + 1)));
                }
                begin = 0;
                flag = 1;
            }
            i[0] ++;
        }
        return bundle;
    }
}
