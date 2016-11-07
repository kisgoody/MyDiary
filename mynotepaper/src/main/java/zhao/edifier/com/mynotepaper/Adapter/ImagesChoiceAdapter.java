package zhao.edifier.com.mynotepaper.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zhao.edifier.com.mynotepaper.ImagePageActivity;
import zhao.edifier.com.mynotepaper.ImagesListActivity;
import zhao.edifier.com.mynotepaper.R;

/**
 * Created by tech57 on 2016/9/26.
 */
public class ImagesChoiceAdapter extends RecyclerView.Adapter<ImagesChoiceAdapter.MyViewHolder>{

    private Context context;

    private List<String> list,list_check;
    private List<Integer> list_position;


    public ImagesChoiceAdapter(List<String> list, List<String> list_check, List<Integer> list_position) {
        this.list = list;
        this.list_check = list_check;
        this.list_position = list_position;
    }

    public ImagesChoiceAdapter(List list) {
        if(list==null){
            this.list = new ArrayList();
        }else{
            this.list = list;
        }
        this.list_check = new ArrayList();
        this.list_position = new ArrayList();
    }

    public List getList_check() {
        return list_check;
    }

    public void setList_check(List list_check) {
        this.list_check = list_check;
    }

    public List getList_position() {
        return list_position;
    }

    public void setList_position(List list_position) {
        this.list_position = list_position;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.images_deal_layout,null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        scaleBitmap(holder.image, (String) list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePageActivity.class);
                intent.putIntegerArrayListExtra("positions", (ArrayList) list_position);
                intent.putStringArrayListExtra("allImagesPath", (ArrayList) list);
                intent.putStringArrayListExtra("checkedImagesPath", (ArrayList) list_check);
                intent.putExtra("currentPosition", position);

                Bundle bundle;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Pair<View, String> p1 =  Pair.create((View) holder.image, context.getString(R.string.main_transition_name_image));
//                    bundle = ActivityOptions.makeSceneTransitionAnimation((Activity)context, p1).toBundle();
                    bundle = new Bundle();
                } else {
                    bundle = new Bundle();
                }
                ImagesListActivity ac = (ImagesListActivity) context;
                ac.startActivityForResult(intent, 30, bundle);

            }
        });
        holder.iv_check.setTag(position);
//        holder.iv_check.setImageResource(list_position.contains(position)?R.drawable.ic_check_box:R.drawable.ic_check_box_outline);
//        holder.iv_check.setImageResource(list_check.contains(list.get(position)) ? R.drawable.ic_check_box : R.drawable.ic_check_box_outline);

        if(list_check.contains(list.get(position))){
//        if(contains(list_check,list.get(position))){
            holder.iv_check.setImageResource(R.drawable.ic_check_box);
            if(!list_position.contains(position)){
                list_position.add(position);
            }
        }else{
            holder.iv_check.setImageResource(R.drawable.ic_check_box_outline);
        }

        holder.iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = list.get(position);
                ImageView iv= (ImageView) v;
//                list_position.contains(position)
                if(list_position.contains(position)){
                    list_position.remove((Object)position);
                    list_check.remove(path);
                    iv.setImageResource(R.drawable.ic_check_box_outline);
                }else{
                    list_position.add(position);
                    list_check.add(path);
                    iv.setImageResource(R.drawable.ic_check_box);
                }

            }
        });
    }

    public boolean contains(List<String> check,String path){

        boolean flag = false;

        for(String str:check){
            if(str.equals(path)){
                flag = true;
                break;
            }
        }

        return flag;
    }

    private void scaleBitmap(ImageView iv_big_img,String path){

        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        .setUseMemCache(true)
//                .setLoadingDrawable(scaleBitmap(path))
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        x.image().bind(iv_big_img, path, imageOptions);
    }

    public Drawable scaleBitmap(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap map = BitmapFactory.decodeFile(path,options);
        return new BitmapDrawable(context.getResources(),map);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        ImageView iv_check;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);

        }
    }

}
