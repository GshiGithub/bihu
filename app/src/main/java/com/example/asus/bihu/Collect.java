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

import adapter.MViewpagerForRecycle;
import httpconnect.Mhttpconnect;
import tool.GetBundleFromString;
import tool.GetSfromU;

public class Collect extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Bundle person;
    private Bundle collect;
    private String[] title;
    private String[] content;


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
        setContentView(R.layout.activity_collect);

        final Intent intent = getIntent();
        person = intent.getBundleExtra("person");
        collect = intent.getBundleExtra("data");



        swipeRefreshLayout = findViewById(R.id.c_swip_faswip);
        recyclerView = swipeRefreshLayout.findViewById(R.id.c_recyc_clist);
        llm = new LinearLayoutManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_answer_5);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.advanced_back);
        }


        Bundle[] bundle2 = new Bundle[20];
        int i;
        for (i = 0; i < 20; i++) {
            try {


                bundle2[i] = collect.getBundle("questions").getBundle(String.valueOf(i));
                int b = bundle2[i].getInt("id");
            } catch (Exception e) {
                break;
            }


        }


        Bundle[] recycle = new Bundle[i];

        for (int j = 0; j < i; j++) {

            recycle[j] = bundle2[j];
        }
        //创建RecycleView


        MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, collect);
        mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
            @Override
            public void Click(int position) {
                Intent intent1 = new Intent(Collect.this,QuestionDetail.class);
                intent1.putExtra("1",collect.getBundle("questions").getBundle(String.valueOf(position)));
                intent1.putExtra("person",person);
                startActivity(intent1);
            }
        });
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mViewpagerForRecycle);

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mhttpconnect.Post("http://bihu.jay86.com/getFavoriteList.php", "page=0&count=20&token=" + person.getString("token"), new Mhttpconnect.Getstring() {
                    @Override
                    public void feedback(String response) {
                        Bundle bundle = new Bundle();

                        GetBundleFromString.Trans(bundle,response);

                        collect = bundle.getBundle("data");
                        Bundle[] bundle2 = new Bundle[20];
                        int i;
                        for (i = 0; i < 20; i++) {
                            try {


                                bundle2[i] = collect.getBundle("questions").getBundle(String.valueOf(i));
                                int b = bundle2[i].getInt("id");
                            } catch (Exception e) {
                                break;
                            }


                        }


                        Bundle[] recycle = new Bundle[i];

                        for (int j = 0; j < i; j++) {

                            recycle[j] = bundle2[j];
                        }
                        //创建RecycleView


                        MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, collect);
                        mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
                            @Override
                            public void Click(int position) {
                                Intent intent1 = new Intent(Collect.this,QuestionDetail.class);
                                intent1.putExtra("1",collect.getBundle("questions").getBundle(String.valueOf(position)));
                                intent1.putExtra("person",person);
                                startActivity(intent1);
                            }
                        });
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(mViewpagerForRecycle);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });
    }
}
