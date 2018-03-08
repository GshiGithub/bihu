package com.example.asus.bihu;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import httpconnect.Mhttpconnect;
import tool.GetBundleFromString;

public class AskQuestion extends AppCompatActivity {

    private EditText content;
    private EditText image;
    private EditText title;
    private TextView sign;
    private Button hi;
    private View hold1;
    private View hold2;
    private View hold3;

    private Bundle question;
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
        setContentView(R.layout.activity_answer);

        Intent intent = getIntent();
        final Bundle question1 = intent.getBundleExtra("question");
        final Bundle person1 = intent.getBundleExtra("person");
        question = question1;
        person = person1;


        hold1 = findViewById(R.id.aa_layout_hold1);
        hold2 = findViewById(R.id.aa_layout_hold2);
        hold3 = findViewById(R.id.aa_layout_hold3);
        hold1 = hold1.findViewById(R.id.aa_layout_hold11);
        hold2 = hold2.findViewById(R.id.aa_layout_hold21);
        hold3 = hold3.findViewById(R.id.aa_layout_hold31);
        hold1.getBackground().setAlpha(100);
        hold2.getBackground().setAlpha(100);
        hold3.getBackground().setAlpha(100);

        content = hold2.findViewById(R.id.ans_et_content);
        image = hold3.findViewById(R.id.ans_et_image);
        title = hold1.findViewById(R.id.answer_et_title);
        hi = findViewById(R.id.ans_bu_handin);


        hi.setText("提交问题");
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        image.setMovementMethod(ScrollingMovementMethod.getInstance());
        title.setMovementMethod(ScrollingMovementMethod.getInstance());

        Toolbar toolbar = findViewById(R.id.toolbar_answer_2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.advanced_back);
        }

        //提交
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mhttpconnect.Post("http://bihu.jay86.com/question.php", "title="+title.getText()+"&content=" +
                        content.getText() + "&images=" + image.getText() + "&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                    @Override
                    public void feedback(String response) {
                        Bundle resp = new Bundle();
                        GetBundleFromString.Trans(resp,response);
                        if(resp.getInt("status") == 200){
                            Toast.makeText(AskQuestion.this,"发布成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AskQuestion.this,"未知错误\n提交失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
