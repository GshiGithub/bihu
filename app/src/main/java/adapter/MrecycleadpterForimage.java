package adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.bihu.R;

/**
 * Created by ASUS on 2018/2/27.
 */

public class MrecycleadpterForimage extends RecyclerView.Adapter<MrecycleadpterForimage.ViewHolder> {
    private ViewHolder viewHolder;
    private Bitmap[] bitmaps;

    public MrecycleadpterForimage(Bitmap[] bitmaps){
        this.bitmaps = bitmaps;
    }

    @Override
    public MrecycleadpterForimage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_image,parent,false);
        this.viewHolder = new MrecycleadpterForimage.ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MrecycleadpterForimage.ViewHolder holder, int position) {

        holder.imageView.setImageBitmap(bitmaps[position]);

    }

    @Override
    public int getItemCount() {
        return bitmaps.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView = null;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
