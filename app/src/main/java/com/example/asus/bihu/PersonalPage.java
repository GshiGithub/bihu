package com.example.asus.bihu;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;

import java.io.InputStream;

import Tab.MFragment;
import adapter.MViewPagerAdapter;
import adapter.MViewpagerForRecycle;
import httpconnect.Mhttpconnect;
import person.Person;
import tool.GetBundleFromString;

public class PersonalPage extends FragmentActivity {


    private int mpage;
    private MViewPagerAdapter mViewPagerAdapter;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout srl;
//    private Handler handler;

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_personal_page);

        Intent intent = getIntent();
        final String temp = intent.getBundleExtra("1").getString("token");










        mpage = 0;
        mViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(),this,temp,mpage);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(mViewPagerAdapter);
        tableLayout = (TabLayout)findViewById(R.id.tab);
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        tableLayout.setupWithViewPager(viewPager);


    }
}
