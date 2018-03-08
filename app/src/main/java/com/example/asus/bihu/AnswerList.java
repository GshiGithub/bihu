package com.example.asus.bihu;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import adapter.AdpterRecycleAnswer;
import httpconnect.Mhttpconnect;
import tool.Cache;
import tool.GetBundleFromString;
import tool.GetSfromU;

public class AnswerList extends AppCompatActivity {

    private RecyclerView answerc;

    private int page;
    private Bundle answer1;
    private Bundle person1;
    private Bundle question1;


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
        setContentView(R.layout.activity_answer_list);

        page = 0;

        Intent answersIntent = getIntent();
        answer1 = answersIntent.getBundleExtra("answers");
        person1 = answersIntent.getBundleExtra("person");
        question1 = answersIntent.getBundleExtra("question");



        Toolbar toolbar = findViewById(R.id.toolbar_answer_6);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.advanced_back);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Bundle[] bundles = new Bundle[20];
//                                String[] title = new String[20];
//                                String[] content = new String[20];
//                                String[] date = new String[20];
        int i;
        for (i = 0; i < 20; i++) {
            try{
                bundles[i] = answer1.getBundle("data").getBundle("answers").getBundle(String.valueOf(i));
                int b = bundles[i].getInt("id");
            }catch (Exception e){
                break;

            }

        }
        String[] name = new String[i];
        String[] cont = new String[i];
        String[] da = new String[i];
        Bundle[] bundle =new Bundle[i];
        for (int j = 0; j < i; j++) {
            bundle[j] = bundles[j];

        }
        AdpterRecycleAnswer adpterRecycleAnswer = new AdpterRecycleAnswer(bundle);
        adpterRecycleAnswer.setItemClickListener(new AdpterRecycleAnswer.Action() {
            @Override
            public void Click(int position) {
                Intent intent = new Intent(AnswerList.this, Answer_detail.class);
                intent.putExtra("person", person1);
                intent.putExtra("question",question1);
                intent.putExtra("answer", answer1.getBundle("data").getBundle("answers").getBundle(String.valueOf(position)));
                startActivity(intent);
            }
        });


        final SwipeRefreshLayout view = findViewById(R.id.rec_swip_ans);
        answerc = view.findViewById(R.id.recycle_answers);
                view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String expose = null;
                String data;
                page++;
                if (page+1 >= answer1.getBundle("data").getInt("totalPage")) {
                    page = 0;
                }
                if (Cache.Makecache("http://bihu.jay86.com/getAnswerList.php", 2, new Cache.Action() {
                    @Override
                    public void deal(File file) {
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            byte[] bytes = new byte[fis.available()];
                            fis.read(bytes);
                            fis.close();
                            String data = new String(bytes);
                            Bundle answers = new Bundle();
                            GetBundleFromString.Trans(answers,data);
                            if (answers.getInt("status") == 200) {
                                answer1 = answers;
                                Bundle[] bundles = new Bundle[20];

                                int i;
                                for (i = 0; i < 20; i++) {
                                    try{
                                        bundles[i] = answer1.getBundle("data").getBundle("answers").getBundle(String.valueOf(i));
                                        int b = bundles[i].getInt("id");
                                    }catch (Exception e){
                                        break;

                                    }

                                }
                                String[] name = new String[i];
                                String[] cont = new String[i];
                                String[] da = new String[i];
                                Bundle[] bundle =new Bundle[i];
                                for (int j = 0; j < i; j++) {
                                    bundle[j] = bundles[j];

                                }
                                AdpterRecycleAnswer adpterRecycleAnswer1 = new AdpterRecycleAnswer(bundle);
                                adpterRecycleAnswer1.setItemClickListener(new AdpterRecycleAnswer.Action() {
                                    @Override
                                    public void Click(int position) {
                                        Intent intent = new Intent(AnswerList.this, Answer_detail.class);
                                        intent.putExtra("person", person1);
                                        intent.putExtra("question",question1);
                                        intent.putExtra("answer", answer1.getBundle("data").getBundle("answers").getBundle(String.valueOf(position)));
                                        startActivity(intent);
                                    }
                                });
                                answerc.setAdapter(adpterRecycleAnswer1);
                                view.setRefreshing(false);
                            } else {
                                Toast.makeText(AnswerList.this, "未知错误", Toast.LENGTH_SHORT).show();
                                view.setRefreshing(false);
                            }
                        }catch (Exception e){
                            Toast.makeText(AnswerList.this, "未知错误", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },"page=" + String.valueOf(page) + "&count=" + "20" + "&qid=" + String.valueOf(question1.getInt("id")) + "&token=" + person1.getString("token")));else {
                    Cache.CreatData("http://bihu.jay86.com/getAnswerList.php",
                            "page=" + String.valueOf(page) + "&count=" + "20" + "&qid=" + String.valueOf(question1.getInt("id")) + "&token=" + person1.getString("token"),
                            new Cache.Action() {
                                @Override
                                public void deal(File file) {
                                    try {
                                        FileInputStream fis = new FileInputStream(file);
                                        byte[] bytes = new byte[fis.available()];
                                        fis.read(bytes);
                                        fis.close();
                                        String data = new String(bytes);
                                        Bundle answers = new Bundle();
                                        GetBundleFromString.Trans(answers,data);
                                        if (answers.getInt("status") == 200) {
                                            answer1 = answers;
                                            Bundle[] bundles = new Bundle[20];

                                            int i;
                                            for (i = 0; i < 20; i++) {
                                                try{
                                                    bundles[i] = answer1.getBundle("data").getBundle("answers").getBundle(String.valueOf(i));
                                                    int b = bundles[i].getInt("id");
                                                }catch (Exception e){
                                                    break;

                                                }

                                            }
                                            String[] name = new String[i];
                                            String[] cont = new String[i];
                                            String[] da = new String[i];
                                            Bundle[] bundle =new Bundle[i];
                                            for (int j = 0; j < i; j++) {
                                                bundle[j] = bundles[j];

                                            }
                                            AdpterRecycleAnswer adpterRecycleAnswer1 = new AdpterRecycleAnswer(bundle);
                                            adpterRecycleAnswer1.setItemClickListener(new AdpterRecycleAnswer.Action() {
                                                @Override
                                                public void Click(int position) {
                                                    Intent intent = new Intent(AnswerList.this, Answer_detail.class);
                                                    intent.putExtra("person", person1);
                                                    intent.putExtra("question",question1);
                                                    intent.putExtra("answer", answer1.getBundle("data").getBundle("answers").getBundle(String.valueOf(position)));
                                                    startActivity(intent);
                                                }
                                            });
                                            answerc.setAdapter(adpterRecycleAnswer1);
                                            view.setRefreshing(false);
                                        } else {
                                            Toast.makeText(AnswerList.this, "未知错误", Toast.LENGTH_SHORT).show();
                                            view.setRefreshing(false);
                                        }
                                    }catch (Exception e){
                                        Toast.makeText(AnswerList.this, "未知错误", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                }

            }
        });


        answerc.setLayoutManager(linearLayoutManager);
        answerc.setAdapter(adpterRecycleAnswer);
    }
}
