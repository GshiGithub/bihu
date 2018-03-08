package com.example.asus.bihu;

import android.content.Intent;
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

public class ChangePass extends AppCompatActivity {

    private EditText pasf;
    private EditText pass;
    private Button change;

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
        setContentView(R.layout.activity_change_pass);

        person = getIntent().getBundleExtra("person");

        pasf = findViewById(R.id.cp_et_f);
        pass = findViewById(R.id.cp_et_s);
        change = findViewById(R.id.cp_bu_change);


        Toolbar toolbar = findViewById(R.id.toolbar_answer_4);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.advanced_back);
        }


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = pasf.getText().toString();
                String second = pass.getText().toString();
                if (first.equals(second) ){
                    Mhttpconnect.Post("http://bihu.jay86.com/changePassword.php", "password=" + pasf.getText().toString() + "&token=" + person.getString("token"),
                            new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle,response);

                                    if (bundle.getInt("status") == 200){
                                        Toast.makeText(ChangePass.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("token",pasf.getText().toString());
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }else {
                                        Toast.makeText(ChangePass.this,"未知错误",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(ChangePass.this,"两次密码不同",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
