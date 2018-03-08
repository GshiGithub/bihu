package com.example.asus.bihu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import httpconnect.Mhttpconnect;
import person.Person;
import tool.GetBundleFromString;

public class Sign_upActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //注册按钮
        Button button = (Button)findViewById(R.id.sign_up_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.signup_etext_name);
                EditText mima = (EditText)findViewById(R.id.signup_etext_mima);
                final String output = "username=" + name.getText().toString()
                        + "&password=" + mima.getText().toString();
                Mhttpconnect.Post("http://bihu.jay86.com/register.php",output , new Mhttpconnect.Getstring() {
                    @Override
                    public void feedback(String response) {
                        final Bundle bundle = new Bundle();
                        GetBundleFromString.Trans(bundle,response);
                        if(bundle.getInt("status") == 400){
                            Toast.makeText(Sign_upActivity.this,"用户名已被使用",Toast.LENGTH_SHORT).show();
                        }else if(bundle.getInt("status") != 200){
                            Toast.makeText(Sign_upActivity.this,"未知错误",Toast.LENGTH_SHORT);
                        }
                        else {
                            Toast.makeText(Sign_upActivity.this,"注册成功\n即将登录",Toast.LENGTH_SHORT).show();
                            Mhttpconnect.Post("http://bihu.jay86.com/login.php", output, new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    if(response == null){
                                        Toast.makeText(Sign_upActivity.this,"未知错误，登录失败",Toast.LENGTH_SHORT);
                                    }
                                    else {
                                        Bundle person = new Bundle();
                                        GetBundleFromString.Trans(person,response);
                                        Toast.makeText(Sign_upActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Sign_upActivity.this,PersonalPage.class);


                                        intent.putExtra("1",person.getBundle("data"));
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });


        //返回按钮
        ImageView back = (ImageView) findViewById(R.id.sign_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_upActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
