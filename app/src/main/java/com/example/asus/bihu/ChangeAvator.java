package com.example.asus.bihu;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import httpconnect.Mhttpconnect;
import tool.GetBundleFromString;

public class ChangeAvator extends AppCompatActivity {
    private EditText avator;
    private FloatingActionButton change;

   private Bundle person;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
            }
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avator);

        person = getIntent().getBundleExtra("person");

        avator = findViewById(R.id.ca_et_avator);
        change = findViewById(R.id.ca_bu_change);


        Toolbar toolbar = findViewById(R.id.toolbar_answer_3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.advanced_back);
        }

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mhttpconnect.Post("http://bihu.jay86.com/modifyAvatar.php", "token=" + person.getString("token") + "&avatar=" + avator.getText().toString(),
                        new Mhttpconnect.Getstring() {
                            @Override
                            public void feedback(String response) {
                                Bundle bundle = new Bundle();
                                GetBundleFromString.Trans(bundle,response);

                                if (bundle.getInt("status") == 200){
                                    Toast.makeText(ChangeAvator.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("avatar",avator.getText().toString());
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }else {
                                    Toast.makeText(ChangeAvator.this,"未知错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
