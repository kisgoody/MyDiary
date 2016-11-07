package zhao.edifier.com.mynotepaper.Adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import zhao.edifier.com.mynotepaper.R;

/**
 * Created by tech57 on 2016/9/26.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder>{

    private String[] images;
    private Context context;
    private int w,h;
    private ItemClickListener itemClickListener;
    private boolean editImages;
    private boolean isUpload;

    public interface ItemClickListener{

        void onItemClickListener(View v,int position);
        void onItemDeleteClickListener(View v,int position);
        boolean onItemLongClickListener(View v,int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public ImagesAdapter(String[] images) {
        this.images = images;
    }
    public void setImageSize(int w,int h){
        this.w = w;
        this.h = h;
    }

    public boolean isEditImages() {
        return editImages;
    }

    public void setEditImages(boolean editImages) {
        this.editImages = editImages;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.images_item,null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        scaleBitmap(holder.image, images[position]);

        if(editImages)holder.iv_check.setVisibility(View.VISIBLE);
        else holder.iv_check.setVisibility(View.GONE);

        if(isUpload)holder.upload_bg.setVisibility(View.VISIBLE);
        else holder.upload_bg.setVisibility(View.GONE);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(itemClickListener!=null) itemClickListener.onItemClickListener(v, position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return itemClickListener != null ? itemClickListener.onItemLongClickListener(v, position) : false;

            }
        });

        holder.iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null) itemClickListener.onItemDeleteClickListener(v,position);
            }
        });
    }


    private void scaleBitmap(ImageView iv_big_img,String path){
        Activity ac = (Activity) context;
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int w = mDisplayMetrics.widthPixels;
        int h = mDisplayMetrics.heightPixels;
        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                 .setSize(w/3,h/3)
                .setIgnoreGif(false)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        x.image().bind(iv_big_img, path, imageOptions);

    }


    @Override
    public int getItemCount() {
        if(images==null)return 0;
        else return images.length;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        ImageView iv_check;
        View upload_bg;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);
            upload_bg =itemView.findViewById(R.id.upload_bg);
            itemView.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
            itemView.setBackgroundResource(R.drawable.recycler_bg);
//            calculateSize(image);
        }



    }

}
