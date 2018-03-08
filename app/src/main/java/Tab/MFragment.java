package Tab;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asus.bihu.Answer_detail;
import com.example.asus.bihu.AskQuestion;
import com.example.asus.bihu.ChangeAvator;
import com.example.asus.bihu.ChangePass;
import com.example.asus.bihu.Collect;
import com.example.asus.bihu.QuestionDetail;
import com.example.asus.bihu.Question_detail;
import com.example.asus.bihu.R;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import adapter.MViewPagerAdapter;
import adapter.MViewpagerForRecycle;
import de.hdodenhof.circleimageview.CircleImageView;
import httpconnect.Mhttpconnect;
import tool.Cache;
import tool.GetBundleFromString;
import tool.GetSfromU;
import tool.GetStringFromIS;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ASUS on 2018/2/21.
 */

public class MFragment extends Fragment {
    private int nowpage;
    static String token;
    private Bundle person;
    private Bundle questions;
    private int mpage;
    ImageView authorava;
    private SwipeRefreshLayout srl;

    public static MFragment getFragment(int position, String token, int page) {
        Bundle agrs = new Bundle();
        MFragment.token = token;
        agrs.putInt("page", page);
        agrs.putInt("x", position);
        MFragment mFragment = new MFragment();
        mFragment.setArguments(agrs);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        person = getActivity().getIntent().getBundleExtra("1");
        nowpage = getArguments().getInt("x");
        mpage = getArguments().getInt("page");
    }


    //菜单点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.frafme_item_changeavator: {

                Intent intent = new Intent(getContext(), ChangeAvator.class);
                intent.putExtra("person", person);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.frafme_item_changepass: {
                Intent intent = new Intent(getContext(), ChangePass.class);
                intent.putExtra("person", person);
                startActivityForResult(intent, 2);
                break;
            }
            case R.id.frafme_item_collect: {
                Mhttpconnect.Post("http://bihu.jay86.com/getFavoriteList.php", "page=0&count=20&token=" + person.getString("token"),
                        new Mhttpconnect.Getstring() {
                            @Override
                            public void feedback(String response) {
                                Bundle bundle = new Bundle();
                                GetBundleFromString.Trans(bundle, response);

                                if (bundle.getInt("status") == 200) {
                                    if (bundle.getBundle("data").getBundle("questions").isEmpty()) {
                                        Toast.makeText(getContext(), "暂无收藏", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(getContext(), Collect.class);
                                        intent.putExtra("person", person);
                                        intent.putExtra("data", bundle.getBundle("data"));
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            }
            case R.id.frafme_item_askques: {
                Intent intent = new Intent(getContext(), AskQuestion.class);
                intent.putExtra("person", person);
                startActivity(intent);
                break;
            }
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            this.person.putString("avatar", data.getStringExtra("avatar"));
            if (person.getString("avatar") == null || person.getString("avatar").equalsIgnoreCase("null"))
                ;
            else {
                if (Cache.Makecache(person.getString("avatar"), 1, new Cache.Action() {
                    @Override
                    public void deal(File file) {
                        try{
                            FileInputStream fis = new FileInputStream(file);
                            Bitmap bm = BitmapFactory.decodeStream(fis);
                            fis.close();
                            authorava.setImageBitmap(bm);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },""));else {
                    Cache.CreatBitmap(person.getString("avatar"), new Cache.Action() {
                        @Override
                        public void deal(File file) {
                            try{
                                FileInputStream fis = new FileInputStream(file);
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                                fis.close();
                                authorava.setImageBitmap(bm);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            this.person.putString("token", data.getStringExtra("token"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (nowpage == 1) {
            final View view = inflater.inflate(R.layout.fragment_first, container, false);
            authorava = view.findViewById(R.id.fraf_image_authorava);
            Bundle person = this.person;



            TextView name = view.findViewById(R.id.fraf_text_name);
            FloatingActionButton home = view.findViewById(R.id.fraf_image_home);
            home.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    getActivity().getMenuInflater().inflate(R.menu.fra_menu_person, menu);
                }
            });


//            id.setText("id:" + String.valueOf(person.getInt("id")));
            name.setText("name:" + GetSfromU.decodeUnicode(person.getString("username")));
            if (person.getString("avatar") == null || person.getString("avatar").equalsIgnoreCase("null"))
                ;
            else {
                if (Cache.Makecache(person.getString("avatar"), 1, new Cache.Action() {
                    @Override
                    public void deal(File file) {
                        try{
                            FileInputStream fis = new FileInputStream(file);
                            Bitmap bm = BitmapFactory.decodeStream(fis);
                            fis.close();
                            authorava.setImageBitmap(bm);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },""));else {
                    Cache.CreatBitmap(person.getString("avatar"), new Cache.Action() {
                        @Override
                        public void deal(File file) {
                            try{
                                FileInputStream fis = new FileInputStream(file);
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                                fis.close();
                                authorava.setImageBitmap(bm);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }

            return view;
        } else if (nowpage == 2) {
            final View view = inflater.inflate(R.layout.recycle_question, container, false);

            final SwipeRefreshLayout view1 = view.findViewById(R.id.rec_swip_fat);
            final String[] title = new String[20];


            mpage = 0;


            final String[] content = new String[20];
            final Bundle[] bundle1 = {new Bundle()};
            final Bundle person = this.person;
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            final RecyclerView recyclerView = view1.findViewById(R.id.recycle_question);


            view1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mpage++;
                    if (mpage >= questions.getBundle("data").getInt("totalPage"))
                        mpage = 0;
                    if (Cache.Makecache("http://bihu.jay86.com/getQuestionList.php", 2, new Cache.Action() {
                        @Override
                        public void deal(File file) {
                              try {
                                  FileInputStream fis = new FileInputStream(file);
                                  byte[] bytes = new byte[fis.available()];
                                  fis.read(bytes);
                                  fis.close();


                                  String data = new String(bytes);
                                  final Bundle bundle = new Bundle();
                                  GetBundleFromString.Trans(bundle1[0], data);
                                  Bundle[] bundle2 = new Bundle[20];
                                  int i;
                                  for (i = 0; i < 20; i++) {
                                      try {


                                          bundle2[i] = bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(i));
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


                                  MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, bundle1[0]);
                                  recyclerView.setLayoutManager(linearLayoutManager);
                                  recyclerView.setAdapter(mViewpagerForRecycle);


                                  mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
                                      @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                      @Override
                                      public void Click(int position) {

                                          Intent intent = new Intent(getContext(), QuestionDetail.class);
                                          intent.putExtra("1", bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(position)));
                                          Bundle bundle2 = person;
                                          intent.putExtra("person", person);
                                          startActivity(intent);

                                      }
                                  });
                                  view1.setRefreshing(false);
                              }catch (Exception e){
                                  e.printStackTrace();
                              }
                        }
                    },"page=" + String.valueOf(mpage) + "&count=20&token=" + MFragment.token));
                    else {
                        Cache.CreatData("http://bihu.jay86.com/getQuestionList.php", "page=" + String.valueOf(mpage) + "&count=20&token=" + MFragment.token,
                                new Cache.Action() {
                                    @Override
                                    public void deal(File file) {
                                        try {
                                            FileInputStream fis = new FileInputStream(file);
                                            byte[] bytes = new byte[fis.available()];
                                            fis.read(bytes);
                                            fis.close();
                                            String data = new String(bytes);
                                            final Bundle bundle = new Bundle();
                                            GetBundleFromString.Trans(bundle1[0], data);
                                            Bundle[] bundle2 = new Bundle[20];
                                            int i;
                                            for (i = 0; i < 20; i++) {
                                                try {


                                                    bundle2[i] = bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(i));
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


                                            MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, bundle1[0]);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                            recyclerView.setAdapter(mViewpagerForRecycle);


                                            mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
                                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void Click(int position) {

                                                    Intent intent = new Intent(getContext(), QuestionDetail.class);
                                                    intent.putExtra("1", bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(position)));
                                                    Bundle bundle2 = person;
                                                    intent.putExtra("person", person);
                                                    startActivity(intent);

                                                }
                                            });
                                            view1.setRefreshing(false);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }

                }
            });
            //获取问题

            if (Cache.Makecache("http://bihu.jay86.com/getQuestionList.php", 2, new Cache.Action() {
                @Override
                public void deal(File file) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        byte[] bytes = new byte[fis.available()];
                        fis.read(bytes);
                        fis.close();
                        String data = new String(bytes);
                        final Bundle bundle = new Bundle();
                        GetBundleFromString.Trans(bundle1[0], data);
                        Bundle[] bundle2 = new Bundle[20];
                        int i;
                        for (i = 0; i < 20; i++) {
                            try {


                                bundle2[i] = bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(i));
                            } catch (Exception e) {
                                break;
                            }


                        }


                        Bundle[] recycle = new Bundle[i];

                        for (int j = 0; j < i; j++) {

                            recycle[j] = bundle2[j];
                        }
                        //创建RecycleView


                        MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, bundle1[0]);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(mViewpagerForRecycle);


                        mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void Click(int position) {

                                Intent intent = new Intent(getContext(), QuestionDetail.class);
                                intent.putExtra("1", bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(position)));
                                Bundle bundle2 = person;
                                intent.putExtra("person", person);
                                startActivity(intent);

                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },"page=" + String.valueOf(mpage) + "&count=20&token=" + MFragment.token));
            else {
                Cache.CreatData("http://bihu.jay86.com/getQuestionList.php", "page=" + String.valueOf(mpage) + "&count=20&token=" + MFragment.token, new Cache.Action() {
                    @Override
                    public void deal(File file) {
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            byte[] bytes = new byte[fis.available()];
                            fis.read(bytes);
                            fis.close();
                            String data = new String(bytes);
                            final Bundle bundle = new Bundle();
                            GetBundleFromString.Trans(bundle1[0], data);
                            Bundle[] bundle2 = new Bundle[20];
                            int i;
                            for (i = 0; i < 20; i++) {
                                try {


                                    bundle2[i] = bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(i));
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


                            MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, bundle1[0]);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(mViewpagerForRecycle);


                            mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void Click(int position) {

                                    Intent intent = new Intent(getContext(), QuestionDetail.class);
                                    intent.putExtra("1", bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(position)));
                                    Bundle bundle2 = person;
                                    intent.putExtra("person", person);
                                    startActivity(intent);

                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            Mhttpconnect.Post("http://bihu.jay86.com/getQuestionList.php", "page=" + String.valueOf(mpage) + "&count=20&token=" + MFragment.token, new Mhttpconnect.Getstring() {
                @Override
                public void feedback(String response) {
                    final Bundle bundle = new Bundle();
                    GetBundleFromString.Trans(bundle1[0], response);
                    questions = bundle1[0];
                    Bundle[] bundle2 = new Bundle[20];
                    int i;
                    for (i = 0; i < 20; i++) {
                        try {


                            bundle2[i] = bundle1[0].getBundle("data").getBundle("questions").getBundle(String.valueOf(i));
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


                    MViewpagerForRecycle mViewpagerForRecycle = new MViewpagerForRecycle(recycle, bundle1[0]);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(mViewpagerForRecycle);


                    mViewpagerForRecycle.setItemClickListener(new MViewpagerForRecycle.Action() {
                        @Override
                        public void Click(int position) {

                            Intent intent = new Intent(getContext(), QuestionDetail.class);
                            intent.putExtra("1", questions.getBundle("data").getBundle("questions").getBundle(String.valueOf(position)));
                            Bundle bundle2 = person;
                            intent.putExtra("person", person);
                            startActivity(intent);
                        }
                    });
                }
            });

            return view;
        }
        return null;
    }


}
