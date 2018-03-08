package com.example.asus.bihu;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import adapter.MrecycleadpterForimage;
import de.hdodenhof.circleimageview.CircleImageView;
import httpconnect.Mhttpconnect;
import tool.Cache;
import tool.ForSDPermission;
import tool.GetBundleFromString;
import tool.GetSfromU;

/**
 * Created by ASUS on 2018/3/1.
 */

public class QuestionDetail extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView content;
    private Button image;
    private CircleImageView author;
    private CircleImageView drawerauthor;
    private TextView authorid;
    private TextView authorname;
    private MenuItem answerid;
    private MenuItem answerdate;
    private MenuItem cheercount;
    private MenuItem naviecount;
    private MenuItem cheer;
    private MenuItem navie;
    private MenuItem best;
    private RecyclerView recyclerView;
    private NavigationView navigationView;
    private TextView answerques;
    private TextView toanswer;
    private View itemLayout;
    private View iitem;
    private View header;
    private View above;
    private View below;

    private int cheerornot;
    private int navieornot;
    private int faornor;
    private Bundle answer1;
    private Bundle person1;


    Handler handler2 = new Handler() {

        public void handleMessage(Message msg) {
            MrecycleadpterForimage mrecycleadpterForimage = new MrecycleadpterForimage((Bitmap[]) msg.obj);
            recyclerView.setAdapter(mrecycleadpterForimage);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
            case R.id.forimage:{
                if (answer1.getString("images").equalsIgnoreCase("") || answer1.getString("images").equalsIgnoreCase("null"))
                    Toast.makeText(QuestionDetail.this, "无图片", Toast.LENGTH_SHORT).show();
                else
                    drawerLayout.openDrawer(GravityCompat.END);
                break;
            }
        }
        return true;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition fade = TransitionInflater.from(this).inflateTransition(Android.R.transition.fade);
//        getWindow().setEnterTransition(fade);
        setContentView(R.layout.activity_answer_detail);


        Intent intent = getIntent();
        final Bundle answer = intent.getBundleExtra("1");
        final Bundle person = intent.getBundleExtra("person");
        answer1 = answer;
        person1 = person;
        int a = answer1.getInt("answerCount");
        String t = person.getString("token");

        itemLayout = findViewById(R.id.answerdetail_itmlayout_fra);
        above = itemLayout.findViewById(R.id.ad_relate_hold);
        below = above.findViewById(R.id.below);
        iitem = above.findViewById(R.id.ansdetail_iitm_layout);
        navigationView = findViewById(R.id.answer_nav);
        Menu navmenu = navigationView.getMenu();
        header = navigationView.inflateHeaderView(R.layout.header_nav);


        answerques = below.findViewById(R.id.quesdt_text_answer);
        toanswer = below.findViewById(R.id.quesdt_text_toanswer);


        drawerLayout = findViewById(R.id.drawer);
//        image = iitem.findViewById(R.id.point_image_button);
        author = iitem.findViewById(R.id.author_image_list);
        content = iitem.findViewById(R.id.content_answer);
        drawerauthor = header.findViewById(R.id.author_image_headernav);
        authorid = header.findViewById(R.id.authorid_text_headnav);
        authorname = header.findViewById(R.id.authorname_text_headnav);
        answerid = (MenuItem) navmenu.findItem(R.id.answerid_menuitem_menu);
        answerdate = (MenuItem) navmenu.findItem(R.id.answerdate_menuitem_menu);
        cheercount = (MenuItem) navmenu.findItem(R.id.answercheercount_menuitem_menu);
        naviecount = (MenuItem) navmenu.findItem(R.id.answermenu_itm_answernaviecount);
        cheer = (MenuItem) navmenu.findItem(R.id.ansme_itm_answercheer);
        navie = (MenuItem) navmenu.findItem(R.id.ansme_itm_answernavie);
        best = (MenuItem) navmenu.findItem(R.id.ansme_itm_answerbest);

        content.getBackground().setAlpha(200);





        //加载抽屉的回答者图片及主界面
        if (answer.getString("authorAvatar").equalsIgnoreCase("null") || answer.getString("authorAvatar").equalsIgnoreCase(""))
            ;
        else{
            if (Cache.Makecache(answer.getString("authorAvatar"), 1, new Cache.Action() {
                @Override
                public void deal(File file) {
                    try{
                        FileInputStream fis = new FileInputStream(file);
                        Bitmap bm = BitmapFactory.decodeStream(fis);
                        fis.close();
                        author.setImageBitmap(bm);
                        Drawable drawable = new BitmapDrawable(QuestionDetail.this.getResources(), bm);
//                        header.setBackground(drawable);
                        drawerauthor.setImageBitmap(bm);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            },""));else {
                  Cache.CreatBitmap(answer.getString("authorAvatar"), new Cache.Action() {
                      @Override
                      public void deal(File file) {
                          try{
                              FileInputStream fis = new FileInputStream(file);
                              Bitmap bm = BitmapFactory.decodeStream(fis);
                              fis.close();
                              author.setImageBitmap(bm);
                              Drawable drawable = new BitmapDrawable(QuestionDetail.this.getResources(), bm);
                              drawerauthor.setImageBitmap(bm);
                          }catch (Exception e){
                              e.printStackTrace();
                          }
                      }
                  });
            }
        }


        //内容
        content.setText(GetSfromU.decodeUnicode(answer1.getString("content")));
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        //作者id
        authorid.setText("id:" + GetSfromU.decodeUnicode(String.valueOf(answer.getInt("authorId"))));
        //作者姓名
        authorname.setText("name:" + GetSfromU.decodeUnicode(answer.getString("authorName")));
        //回答的id
        answerid.setTitle("问题的id:" + String.valueOf(answer.getInt("id")));
        //回答的日期
        answerdate.setTitle("日期：" + answer.getString("date"));
        //点赞数
        cheercount.setTitle("点赞:" + String.valueOf(answer.getInt("exciting")));
        //踩
        naviecount.setTitle("踩:" + String.valueOf(answer.getInt("naive")));
        //是否点赞
        try {
            if (answer.getString("is_exciting").equalsIgnoreCase("false")) {
                cheer.setTitle("未点赞");
                cheerornot = 1;
            } else {
                cheer.setTitle("已点赞");
                cheerornot = 0;
            }
        }catch (Exception e){
            cheer.setTitle("已点赞");
            cheerornot = 0;
        }

        //是否踩
        try {
            if (answer.getString("is_naive").equalsIgnoreCase("false")) {
                navie.setTitle("未踩");
                navieornot = 1;
            } else {
                navie.setTitle("已踩");
                navieornot = 0;
            }
        }catch (Exception e){
            navie.setTitle("已踩");
            navieornot = 0;
        }

        //best
        try{
            if(answer.getString("is_favorite").equalsIgnoreCase("false")){
                best.setTitle("未收藏");
                best.setIcon(R.drawable.collect);
                faornor = 1;
            }else {
                best.setTitle("已收藏");
                faornor = 0;
            }
        }catch (Exception e){
            best.setTitle("已收藏");
            faornor = 0;
        }



        //设置recycleview
        if (answer.getString("images").equalsIgnoreCase("") || answer.getString("images").equalsIgnoreCase("null"))
            ;
        else {

            recyclerView = findViewById(R.id.recycle_answers_image);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String htp = answer.getString("images");

                    int num = 1;
                    for (int i = 0; i < htp.length(); i++) {
                        if (htp.charAt(i) == ',')
                            num++;
                    }
                    Bitmap[] bitmaps = new Bitmap[num];
                    int flag = 0;
                    int begin = 0;
                    for (int i = 0; i < htp.length(); i++) {
                        if (htp.charAt(i) == ',' || i == htp.length() - 1) {
                            if (i == htp.length() - 1)
                                i = htp.length();
                            String a = htp.substring(begin, i);


                            begin = i + 1;
                            bitmaps[flag] = Mhttpconnect.connect2(a);
                            flag++;
                        }
                    }
                    Message message = Message.obtain();
                    message.obj = bitmaps;
                    handler2.sendMessage(message);

                }
            }).start();

        }
        //toolbar的设置
        Toolbar toolbar = findViewById(R.id.toolbar_answer);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.advanced_back);
        }


        //点击事件
        //主界面的回答者图片
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //呼出图片

        //查看回答
        toanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer1.getInt("answerCount") == 0) {
                    Toast.makeText(QuestionDetail.this, "暂无回答", Toast.LENGTH_SHORT).show();
                } else {
                    String expose = "page=0&count=" + answer1.getInt("answerCount") + "&qid=" + String.valueOf(answer1.getInt("id")) + "&token=" + person1.getString("token");
                    String data = expose;
                    Mhttpconnect.Post("http://bihu.jay86.com/getAnswerList.php", expose
                            ,
                            new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle answers = new Bundle();
                                    GetBundleFromString.Trans(answers, response);
                                    if (answers.getInt("status") == 200) {
                                        Intent intent1 = new Intent(QuestionDetail.this, AnswerList.class);
                                        intent1.putExtra("person", person1);
                                        intent1.putExtra("question", answer1);
                                        intent1.putExtra("answers", answers);
                                        startActivity(intent1);

                                    }
                                }
                            });
                }
            }
        });

        //点赞或踩
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ansme_itm_answercheer: {
                        if (cheerornot == 0) {
                            Mhttpconnect.Post("http://bihu.jay86.com/cancelExciting.php", "id=" + String.valueOf(answer1.getInt("id")) + "&type=1&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle, response);
                                    if (bundle.getInt("status") == 200) {
                                        Toast.makeText(getBaseContext(), "已取消点赞", Toast.LENGTH_SHORT).show();
                                        cheer.setTitle("未点赞");
                                        cheercount.setTitle("点赞:" + String.valueOf(answer1.getInt("exciting") - 1));
                                        answer1.putInt("exciting", answer1.getInt("exciting") - 1);
                                        answer1.putString("is_exciting", "false");
                                        cheerornot = 1;
                                    } else {
                                        Toast.makeText(QuestionDetail.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Mhttpconnect.Post("http://bihu.jay86.com/exciting.php", "id=" + String.valueOf(answer1.getInt("id")) + "&type=1&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle, response);
                                    if (bundle.getInt("status") == 200) {
                                        Toast.makeText(getBaseContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                                        cheer.setTitle("已点赞");
                                        cheercount.setTitle("点赞:" + String.valueOf(answer1.getInt("exciting") + 1));
                                        answer1.putInt("exciting", answer1.getInt("exciting") + 1);
                                        answer1.putString("is_exciting", "true");
                                        cheerornot = 0;
                                    } else {
                                        Toast.makeText(QuestionDetail.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        break;
                    }
                    case R.id.ansme_itm_answernavie: {
                        if (navieornot == 0) {
                            Mhttpconnect.Post("http://bihu.jay86.com/cancelNaive.php", "id=" + String.valueOf(answer1.getInt("id")) + "&type=1&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle, response);
                                    if (bundle.getInt("status") == 200) {
                                        Toast.makeText(getBaseContext(), "已取消踩", Toast.LENGTH_SHORT).show();
                                        navie.setTitle("未踩");
                                        naviecount.setTitle("踩:" + String.valueOf(answer1.getInt("naive") - 1));
                                        answer1.putInt("naive", answer1.getInt("naive") - 1);
                                        answer1.putString("is_naive", "false");
                                        navieornot = 1;
                                    } else {
                                        Toast.makeText(QuestionDetail.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Mhttpconnect.Post("http://bihu.jay86.com/naive.php", "id=" + String.valueOf(answer1.getInt("id")) + "&type=1&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle, response);
                                    if (bundle.getInt("status") == 200) {
                                        Toast.makeText(getBaseContext(), "踩成功", Toast.LENGTH_SHORT).show();
                                        navie.setTitle("已踩");
                                        naviecount.setTitle("踩:" + String.valueOf(answer1.getInt("naive") + 1));
                                        answer1.putInt("naive", answer1.getInt("naive") + 1);
                                        answer1.putString("is_naive", "true");
                                        navieornot = 0;
                                    } else {
                                        Toast.makeText(QuestionDetail.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        break;
                    }
                    case R.id.ansme_itm_answerbest:{
                        if (faornor == 0) {
                            Mhttpconnect.Post("http://bihu.jay86.com/cancelFavorite.php", "qid=" + String.valueOf(answer1.getInt("id")) + "&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle, response);
                                    if (bundle.getInt("status") == 200) {
                                        Toast.makeText(getBaseContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                                        best.setTitle("未收藏");


                                        answer1.putString("is_favorite", "false");
                                        faornor = 1;
                                    } else {
                                        Toast.makeText(QuestionDetail.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Mhttpconnect.Post("http://bihu.jay86.com/favorite.php", "qid=" + String.valueOf(answer1.getInt("id")) + "&token=" + person1.getString("token"), new Mhttpconnect.Getstring() {
                                @Override
                                public void feedback(String response) {
                                    Bundle bundle = new Bundle();
                                    GetBundleFromString.Trans(bundle, response);
                                    if (bundle.getInt("status") == 200) {
                                        Toast.makeText(getBaseContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                                        best.setTitle("已收藏");


                                        answer1.putString("is_favorite", "true");
                                        faornor = 0;
                                    } else {
                                        Toast.makeText(QuestionDetail.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        break;
                    }

                }
                return true;
            }
        });

        //我来回答
        answerques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(QuestionDetail.this, Answer.class);
                intent1.putExtra("person", person1);
                intent1.putExtra("question", answer1);
                startActivity(intent1);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image,menu);
        return true;
    }

}
