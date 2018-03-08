package adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.bihu.R;

import java.io.File;
import java.io.FileInputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import tool.Cache;
import tool.GetSfromU;

/**
 * Created by ASUS on 2018/2/28.
 */

public class AdpterRecycleAnswer extends  RecyclerView.Adapter<AdpterRecycleAnswer.ViewHolder> implements View.OnClickListener {
    private String[] authorname = null;
    private String[] content = null;
    private String[] date = null;
    private Bundle[] answer;

    AdpterRecycleAnswer.ViewHolder viewHolder;
    AdpterRecycleAnswer.Action action;

    public static interface Action{
        void Click(int position);
    }

    public AdpterRecycleAnswer(Bundle[] answer){
        this.answer = answer;
    }

    @Override
    public AdpterRecycleAnswer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer,parent,false);
        this.viewHolder = new AdpterRecycleAnswer.ViewHolder(view);

        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdpterRecycleAnswer.ViewHolder holder, int position) {
        String data = answer[position].getString("content");
        holder.content.setText(GetSfromU.decodeUnicode(answer[position].getString("content")));
        holder.date.setText(GetSfromU.decodeUnicode(answer[position].getString("date")));
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

    public  void setItemClickListener(AdpterRecycleAnswer.Action action ){
        this.action = action;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        private TextView content;
        private TextView date;
        private CircleImageView author;
        private TextView cheer;
        private TextView navie;
        public ViewHolder(View itemView) {
            super(itemView);

            View view = itemView.findViewById(R.id.ia_layout_hold);
            view.getBackground().setAlpha(100);
            content = view.findViewById(R.id.item_answer_content);
            date = view.findViewById(R.id.item_answer_date);
            author = view.findViewById(R.id.ia_cirimag_authorava);
            cheer = view.findViewById(R.id.ia_txt_cheercount);
            navie = view.findViewById(R.id.ia_txt_naviecount);
        }
    }
}
