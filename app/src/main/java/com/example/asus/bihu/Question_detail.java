package com.example.asus.bihu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import adapter.MrecycleadpterForimage;
import httpconnect.Mhttpconnect;
import tool.Cache;
import tool.GetBundleFromString;
import tool.GetSfromU;

public class Question_detail extends AppCompatActivity {

    private ImageView imageView;
    private TextView cheer_text;
    private TextView cheercount;
    private TextView navie_text;
    private TextView naviecount;
    private TextView cheer;
    private TextView navie;
    private ImageView test;
    private LinearLayoutManager linearLayoutManager;

    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecyclerView recyclerView = findViewById(R.id.detail_recycle);
            Bitmap[] bitmaps = (Bitmap[]) msg.obj;

            MrecycleadpterForimage mrecycleadpterForimage = new MrecycleadpterForimage(bitmaps);

            recyclerView.setAdapter(mrecycleadpterForimage);
            recyclerView.setLayoutManager(linearLayoutManager);

        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Drawable drawable = (Drawable) msg.obj;
            imageView.setImageDrawable(drawable);
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("1");

        TextView obtainanswer = findViewById(R.id.obtain_answer);
        ImageView back = findViewById(R.id.detail_back);
        cheer = findViewById(R.id.cheer);
        navie = findViewById(R.id.navie);
        cheer_text = findViewById(R.id.cheer_text);
        naviecount = findViewById(R.id.navie_count);
        naviecount.setText(String.valueOf(bundle.getInt("naive")));
        cheercount = findViewById(R.id.cheer_count);
        cheercount.setText(String.valueOf(bundle.getInt("exciting")));


        linearLayoutManager = new LinearLayoutManager(this);

        final Bitmap[] bitmap2 = new Bitmap[1];
        String data = bundle.getString("authorAvatar");


        TextView tv = (TextView) findViewById(R.id.avator);
        tv.setText(GetSfromU.decodeUnicode(bundle.getString("content")));

        if (bundle.getString("is_exciting").equalsIgnoreCase("true")) {
            cheer.setText("已点赞");
            cheer.setTextColor(R.color.black);
        }


        if (bundle.getString("is_naive").equalsIgnoreCase("true")) {
            navie.setText("踩过");
            navie.setTextColor(R.color.black);
        }


        //获取个人信息
        final Bundle person = intent.getBundleExtra("person");

        String name = person.getString("username");

        //作者名字
        TextView author = findViewById(R.id.authorName);
        author.setText("作者" + GetSfromU.decodeUnicode(bundle.getString("authorName")));


        //作者id
        TextView authorid = findViewById(R.id.authorid);
        authorid.setText("id:" + String.valueOf(bundle.getInt("authorId")));

        //近期回复时间
        TextView recent = findViewById(R.id.recent);
        recent.setText("近期回复" + bundle.getString("recent"));

        //已回答数
        TextView answercount = findViewById(R.id.answercount);
        answercount.setText("已回答数" + String.valueOf(bundle.getInt("answerCount")));

        //收藏
        final TextView collect = findViewById(R.id.collect);
        if (bundle.getString("is_favorite").equalsIgnoreCase("true")) {
            collect.setText("已收藏");
            collect.setTextColor(R.color.black);
        }

        //点赞

        cheer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle.getString("is_exciting").equalsIgnoreCase("true")) {
                    String data = "id=" + String.valueOf(bundle.getInt("id")) + "&type=1&token=" + person.getString("token");
                    Mhttpconnect.Post("http://bihu.jay86.com/cancelExciting.php", data, new Mhttpconnect.Getstring() {
                        @Override
                        public void feedback(String response) {
                            Bundle feedback = new Bundle();
                            GetBundleFromString.Trans(feedback, response);
                            if (feedback.getInt("status") == 200) {
                                Toast.makeText(Question_detail.this, "已取消点赞", Toast.LENGTH_SHORT).show();
                                bundle.putString("is_exciting", "false");
                                cheer.setText("点赞");
                                cheer.setHighlightColor(R.color.red);
                                cheercount.setText(String.valueOf(bundle.getInt("exciting") - 1));
                                bundle.putInt("exciting", bundle.getInt("exciting") - 1);
                            } else {
                                Toast.makeText(Question_detail.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String data = "id=" + String.valueOf(bundle.getInt("id")) + "&type=1&token=" + person.getString("token");
                    Mhttpconnect.Post("http://bihu.jay86.com/exciting.php", data, new Mhttpconnect.Getstring() {
                        @Override
                        public void feedback(String response) {
                            Bundle feedback = new Bundle();
                            GetBundleFromString.Trans(feedback, response);
                            if (feedback.getInt("status") == 200) {
                                Toast.makeText(Question_detail.this, "点赞成功", Toast.LENGTH_SHORT).show();
                                bundle.putString("is_exciting", "true");
                                cheer.setText("已点赞");
                                cheer.setTextColor(R.color.black);
                                cheercount.setText(String.valueOf(bundle.getInt("exciting") + 1));
                                bundle.putInt("exciting", bundle.getInt("exciting") + 1);
                            } else {
                                Toast.makeText(Question_detail.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        //踩

        navie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle.getString("is_naive").equalsIgnoreCase("true")) {
                    String data = "id=" + String.valueOf(bundle.getInt("id")) + "&type=1&token=" + person.getString("token");
                    Mhttpconnect.Post("http://bihu.jay86.com/cancelNaive.php", data, new Mhttpconnect.Getstring() {
                        @Override
                        public void feedback(String response) {
                            Bundle feedback = new Bundle();
                            GetBundleFromString.Trans(feedback, response);
                            if (feedback.getInt("status") == 200) {
                                Toast.makeText(Question_detail.this, "已取消踩", Toast.LENGTH_SHORT).show();
                                bundle.putString("is_naive", "false");
                                navie.setText("踩");
                                navie.setTextColor(R.color.red);
                                naviecount.setText(String.valueOf(bundle.getInt("naive") - 1));
                                bundle.putInt("naive", bundle.getInt("naive") - 1);
                            } else {
                                Toast.makeText(Question_detail.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String data = "id=" + String.valueOf(bundle.getInt("id")) + "&type=1&token=" + person.getString("token");
                    Mhttpconnect.Post("http://bihu.jay86.com/naive.php", data, new Mhttpconnect.Getstring() {
                        @Override
                        public void feedback(String response) {
                            Bundle feedback = new Bundle();
                            GetBundleFromString.Trans(feedback, response);
                            if (feedback.getInt("status") == 200) {
                                Toast.makeText(Question_detail.this, "踩成功", Toast.LENGTH_SHORT).show();
                                bundle.putString("is_naive", "true");
                                navie.setText("已踩过");
                                navie.setTextColor(R.color.black);
                                naviecount.setText(String.valueOf(bundle.getInt("naive") + 1));
                                bundle.putInt("naive", bundle.getInt("naive") + 1);
                            } else {
                                Toast.makeText(Question_detail.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        //收藏
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle.getString("is_favorite").equalsIgnoreCase("true")) {
                    String data = "qid=" + String.valueOf(bundle.getInt("id")) + "&token=" + person.getString("token");
                    Mhttpconnect.Post("http://bihu.jay86.com/cancelFavorite.php", data, new Mhttpconnect.Getstring() {
                        @Override
                        public void feedback(String response) {
                            Bundle feedback = new Bundle();
                            GetBundleFromString.Trans(feedback, response);
                            if (feedback.getInt("status") == 200) {
                                Toast.makeText(Question_detail.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                                bundle.putString("is_favorite", "false");
                                collect.setText("收藏");
                                collect.setTextColor(R.color.red);

                            } else {
                                Toast.makeText(Question_detail.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String data = "qid=" + String.valueOf(bundle.getInt("id")) + "&token=" + person.getString("token");
                    Mhttpconnect.Post("http://bihu.jay86.com/favorite.php", data, new Mhttpconnect.Getstring() {
                        @Override
                        public void feedback(String response) {
                            Bundle feedback = new Bundle();
                            GetBundleFromString.Trans(feedback, response);
                            if (feedback.getInt("status") == 200) {
                                Toast.makeText(Question_detail.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                bundle.putString("is_favorite", "true");
                                collect.setText("已收藏");
                                collect.setTextColor(R.color.black);

                            } else {
                                Toast.makeText(Question_detail.this, "错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //查看回答
        obtainanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle.getInt("answerCount") == 0) {
                    Toast.makeText(Question_detail.this, "无回答", Toast.LENGTH_SHORT);
                } else {
                    String expose = "page=0&count="+bundle.getInt("answerCount") +"&qid=" + String.valueOf(bundle.getInt("id")) + "&token=" + person.getString("token");
                    String data = expose;
                    if (Cache.Makecache(data, 2, new Cache.Action() {
                        @Override
                        public void deal(File file) {
                            try{
                                FileInputStream fis = new FileInputStream(file);
                                byte[] bytes = new byte[fis.available()];
                                fis.read(bytes);
                                 String data = new String(bytes);
                                 fis.close();
                                 Bundle answers = new Bundle();
                                 GetBundleFromString.Trans(answers,data);
                                if (answers.getInt("status") == 200) {
                                    Intent intent1 = new Intent(Question_detail.this, AnswerList.class);
                                    intent1.putExtra("person", person);
                                    intent1.putExtra("question", bundle);
                                    intent1.putExtra("answers", answers);
                                    startActivity(intent1);

                                }
                            }catch (Exception e){
                                Toast.makeText(Question_detail.this, "打开缓存文件失败", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },""));else {
                        Cache.CreatData("http://bihu.jay86.com/getAnswerList.php", expose, new Cache.Action() {
                            @Override
                            public void deal(File file) {
                                try{
                                    FileInputStream fis = new FileInputStream(file);
                                    byte[] bytes = new byte[fis.available()];
                                    fis.read(bytes);
                                    fis.close();
                                    String data = new String(bytes);

                                    Bundle answers = new Bundle();
                                    GetBundleFromString.Trans(answers,data);
                                    if (answers.getInt("status") == 200) {
                                        Intent intent1 = new Intent(Question_detail.this, AnswerList.class);
                                        intent1.putExtra("person", person);
                                        intent1.putExtra("question", bundle);
                                        intent1.putExtra("answers", answers);
                                        startActivity(intent1);

                                    }
                                }catch (Exception e){
                                    Toast.makeText(Question_detail.this, "打开缓存文件失败", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
//                    Mhttpconnect.Post("http://bihu.jay86.com/getAnswerList.php",expose
//                            ,
//                            new Mhttpconnect.Getstring() {
//                                @Override
//                                public void feedback(String response) {
//                                    Bundle answers = new Bundle();
//                                    GetBundleFromString.Trans(answers, response);
//                                    if (answers.getInt("status") == 200) {
//                                        Intent intent1 = new Intent(Question_detail.this, AnswerList.class);
//                                        intent1.putExtra("person", person);
//                                        intent1.putExtra("question", bundle);
//                                        intent1.putExtra("answers", answers);
//                                        startActivity(intent1);
//
//                                    }
//                                }
//                            });
                }
            }
        });


        tv.setMovementMethod(ScrollingMovementMethod.getInstance());        //超长文本上下滑动

        imageView = findViewById(R.id.image_author);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
//                bundle.getString("authorAvatar")
                Bitmap bitmap = Mhttpconnect.connect2(bundle.getString("authorAvatar"));
                Drawable drawable = new BitmapDrawable(Question_detail.this.getResources(),bitmap);
                message.obj = drawable;
                handler.sendMessage(message);
            }
        }).start();


        //加载图片
        String b = bundle.getString("images");
        if (b.equalsIgnoreCase("null") || b.equalsIgnoreCase("")) ;
        else
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String htp = bundle.getString("images");


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


}

