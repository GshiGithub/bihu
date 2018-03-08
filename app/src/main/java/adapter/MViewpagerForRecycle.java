package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import httpconnect.Mhttpconnect;
import tool.Cache;
import tool.GetSfromU;

import com.example.asus.bihu.BuildConfig;
import com.example.asus.bihu.QuestionDetail;
import com.example.asus.bihu.Question_detail;
import com.example.asus.bihu.R;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by ASUS on 2018/2/21.
 */

public class MViewpagerForRecycle extends RecyclerView.Adapter<MViewpagerForRecycle.ViewHolder> implements View.OnClickListener {
    private Bundle[] answer;
    private Bundle bundle;
    private ViewHolder viewHolder;
    Action action;

    public static interface Action{
        void Click(int position);
    }

    public MViewpagerForRecycle(Bundle[] answer,Bundle bundle){
        this.answer = answer;
        this.bundle = bundle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle,parent,false);
         this.viewHolder = new ViewHolder(view);

         view.setOnClickListener(this);
         return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
         holder.title_txt.setText("title:" + GetSfromU.decodeUnicode(answer[position].getString("title")));
         holder.content_txt.setText("content:" + GetSfromU.decodeUnicode(answer[position].getString("content")));
         holder.date.setText(answer[position].getString("date"));
         holder.cheer.setText(String.valueOf(answer[position].getInt("exciting")));
         holder.navie.setText(String.valueOf(answer[position].getInt("naive")));

        if (answer[position].getString("authorAvatar").equalsIgnoreCase("null") || answer[position].getString("authorAvatar").equalsIgnoreCase(""))
            ;
        else{
            if (Cache.Makecache(answer[position].getString("authorAvatar"), 1, new Cache.Action() {
                @Override
                public void deal(File file) {
                    try{
                        FileInputStream fis = new FileInputStream(file);
                        Bitmap bm = BitmapFactory.decodeStream(fis);
                        fis.close();
                        holder.author.setImageBitmap(bm);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            },""));else {
                Cache.CreatBitmap(answer[position].getString("authorAvatar"), new Cache.Action() {
                    @Override
                    public void deal(File file) {
                        try{
                            FileInputStream fis = new FileInputStream(file);
                            Bitmap bm = BitmapFactory.decodeStream(fis);
                            fis.close();
                            holder.author.setImageBitmap(bm);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }


         holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return answer.length;
    }

    @Override
    public void onClick(View v) {
         action.Click((Integer) v.getTag());
    }

    public  void setItemClickListener(Action action ){
        this.action = action;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title_txt = null;
        TextView content_txt = null;
        CircleImageView author = null;
        TextView cheer = null;
        TextView navie = null;
        TextView date = null;
        public ViewHolder(View itemView) {
            super(itemView);

            View view = itemView.findViewById(R.id.bitem_layout_hold);
            view.getBackground().setAlpha(100);
            title_txt = (TextView)view.findViewById(R.id.text_item_recycle_question_title);
            content_txt = (TextView)view.findViewById(R.id.text_item_recycle_question_content);
            author = view.findViewById(R.id.ir_cimag_authorava);
            cheer = view.findViewById(R.id.ir_txt_cheercount);
            navie = view.findViewById(R.id.ir_txt_naviecount);
            date = view.findViewById(R.id.ir_txt_date);
        }
    }
}
