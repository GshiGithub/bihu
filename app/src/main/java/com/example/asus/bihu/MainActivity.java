package com.example.asus.bihu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import httpconnect.Mhttpconnect;
import person.Person;
import tool.ForSDPermission;
import tool.GetBundleFromString;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android6.0以后sd权限必须动态申请
        ForSDPermission.verifyStoragePermissions(this);
//        Log.e("666",this.getPackageResourcePath());
        //跳转到注册界面


//        SQLiteDatabase data = SQLiteDatabase.openOrCreateDatabase("D:\\bihubysg\\app\\src\\main\\java\\cache\\data.db",null);



//        ImageView imageView1 = findViewById(R.id.main_image_holder);
//        imageView1.getBackground().setAlpha(100);
        ImageView imageView = findViewById(R.id.main_image_back);
//        imageView.getBackground().setAlpha(220);
        TextView signup = (TextView)findViewById(R.id.tosignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Sign_upActivity.class);
                startActivity(intent);
                finish();
            }
        });




        //登录
        final EditText name = (EditText)findViewById(R.id.login_name);
        final EditText mima = (EditText)findViewById(R.id.mima_etext);

        Button login = (Button)findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Mhttpconnect.Post("http://bihu.jay86.com/login.php","username="+name.getText().toString()+"&password="+mima.getText().toString(),
                        new Mhttpconnect.Getstring() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void feedback(String response) {
                        Bundle bundle = new Bundle();
                        GetBundleFromString.Trans(bundle,response);
                        if(bundle.getInt("status") == 400){
                              Toast.makeText(MainActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();


                        }else if(bundle.getInt("status") != 200){
                            Toast.makeText(MainActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Person person = new Person(bundle.getString("username"),bundle.getInt("password"),bundle.getString("token"));
                            Intent intent = new Intent(MainActivity.this,PersonalPage.class);
                            intent.putExtra("1",bundle.getBundle("data"));
                            startActivity(intent, ActivityOptions.makeScaleUpAnimation(name,name.getWidth(),name.getHeight(),0,0).toBundle());

                            finish();
                        }
                    }
                });
            }
        });
    }
}
