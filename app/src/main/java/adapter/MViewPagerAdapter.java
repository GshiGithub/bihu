package adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.asus.bihu.PersonalPage;
import com.example.asus.bihu.R;

import Tab.MFragment;
import httpconnect.Mhttpconnect;
import person.Person;
import tool.GetBundleFromString;
/**
 * Created by ASUS on 2018/2/21.
 */

public class MViewPagerAdapter extends FragmentPagerAdapter {

    final private int countofpage = 2;
    final private String[] mTitle = new String[]{"个人主页","热门问题"};
    private Context context;
    final private String token;
    private int mpage;

    public MViewPagerAdapter(FragmentManager fm,Context context,String token,int page) {
        super(fm);
        this.context = context;
        this.token = token;
        this.mpage = page;
    }

    @Override
    public Fragment getItem(int position) {
        return  MFragment.getFragment(position + 1,token,mpage);
    }

    @Override
    public int getCount() {
        return countofpage;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
