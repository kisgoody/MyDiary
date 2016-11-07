package zhao.edifier.com.mynotepaper.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import zhao.edifier.com.mylibrary.ZUtile.LUtile;
import zhao.edifier.com.mynotepaper.Mode.Obj.ContentObject;
import zhao.edifier.com.mynotepaper.Mode.Obj.ImageObj;
import zhao.edifier.com.mynotepaper.Mode.Obj.NoteImageObj;
import zhao.edifier.com.mynotepaper.Mode.Obj.NotePaperObj;
import zhao.edifier.com.mynotepaper.R;
import zhao.edifier.com.mynotepaper.Utile.U;

/**
 * Created by tech57 on 2016/9/26.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>{

    private static final String TAg = "MainAdapter";
    private List<NotePaperObj> datas;
    private Activity activity;
    private ClickListener mClickListener;
    private Map<Integer,RecyclerView> map;
    private boolean isDelete;

    public void setOnItemClikListener(ClickListener mClickListener){
        this.mClickListener = mClickListener;

    }
    public interface ClickListener{
        void onItemClick(Bundle bundle,NotePaperObj contentObject);
        void onItemLongClick();

    }

    public MainAdapter(List<NotePaperObj> datas,ClickListener mClickListener) {
        if(datas==null){
            this.datas = new ArrayList<>();
        }
        else {
            this.datas = datas;
        }
        this.mClickListener = mClickListener;
        map = new HashMap<>();
    }

    public List<NotePaperObj> getDatas() {
        return datas;
    }

    public void setDatas(List<NotePaperObj> datas) {
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        activity = (Activity) parent.getContext();
        View v = View.inflate(parent.getContext(), R.layout.content_item_layout, null);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NotePaperObj contentObject = datas.get(position);
        holder.tv_content.setText(contentObject.getContent());
        queryItemImages(contentObject, holder);
        timeDeal(contentObject, holder);
        if(isDelete){
            holder.iv_check.setVisibility(View.VISIBLE);
        }else{
            holder.iv_check.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(isDelete){
                    isDelete = !isDelete;
                    notifyDataSetChanged();
                    return;
                }
                Bundle bundle;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Pair<View, String> p1 = Pair.create((View) holder.stub, activity.getString(R.string.main_transition_name_image));
                    Pair<View, String> p2 = Pair.create((View) holder.tv_date, activity.getString(R.string.main_transition_name_textView));
                    bundle = ActivityOptions.makeSceneTransitionAnimation(activity, p1, p2).toBundle();
                } else {
                    bundle = new Bundle();
                }
                mClickListener.onItemClick(bundle, contentObject);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isDelete = !isDelete;
                notifyDataSetChanged();
                return true;
            }
        });
        holder.iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotePaperObj contentObject1 = getDatas().get(position);

                contentObject1.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            getDatas().remove(position);
                            notifyDataSetChanged();
                            if(holder.recyclerView!=null){
                                Images_Pager_Adapter adapter = (Images_Pager_Adapter) holder.recyclerView.getAdapter();
                                List<BmobObject> objects =adapter.getImages();
                                   if(objects!=null&&objects.size()>0) for (BmobObject bmobObject:objects){
                                        bmobObject.delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                            if(e==null){
//                                                Log.d("deleteImages","delete ok");
                                            }else {
                                                Log.d("deleteImages",e.toString());
                                            }
                                            }
                                        });

                                    }
                            }
                        } else {
                            Log.d("delete", e.toString());
                        }
                    }
                });
            }
        });


    }

    public void timeDeal(NotePaperObj contentObject,MyViewHolder holder){

        String today = LUtile.millis2Data(System.currentTimeMillis(), "dd/MM/yyyy");
        String createDay = LUtile.millis2Data(contentObject.getCreateDate(), "dd/MM/yyyy");

        String nowMoth = LUtile.millis2Data(System.currentTimeMillis(), "MM/yyyy");
        String createMoth = LUtile.millis2Data(contentObject.getCreateDate(), "MM/yyyy");

        String nowYear = LUtile.millis2Data(System.currentTimeMillis(),"yyyy");
        String createYear = LUtile.millis2Data(contentObject.getCreateDate(),"yyyy");

        String showTime = "";
        if(nowYear.equals(createYear)){
            if(nowMoth.equals(createMoth)) {
                if (today.equals(createDay)) {
                    showTime = LUtile.millis2Data(contentObject.getCreateDate(), "今天HH:mm");

                } else {
                    int dd = Integer.parseInt(LUtile.millis2Data(System.currentTimeMillis(), "dd"));
                    int createDD = Integer.parseInt(LUtile.millis2Data(contentObject.getCreateDate(), "dd"));
                    if(dd-createDD==1){
                        showTime = LUtile.millis2Data(contentObject.getCreateDate(), "昨天HH:mm");

                    }else if(dd-createDD==2){
                        showTime = LUtile.millis2Data(contentObject.getCreateDate(), "前天HH:mm");

                    }else{
                        showTime = LUtile.millis2Data(contentObject.getCreateDate(), "MM月dd日 HH:mm");
                    }
                }
            }else{
                showTime = LUtile.millis2Data(contentObject.getCreateDate(), "MM月dd日 HH:mm");
            }

        }else{
            showTime = LUtile.millis2Data(contentObject.getCreateDate(), "yyyy/MM/dd HH:mm");
        }
        holder.tv_date.setText(showTime);

    }

    public void queryItemImages(final NotePaperObj notePaperObj, final MyViewHolder holder){
        BmobQuery<NoteImageObj> query= new BmobQuery<>();
        query.addWhereEqualTo("notePaperObj", notePaperObj);
        query.findObjects(new FindListener<NoteImageObj>() {
            @Override
            public void done(List<NoteImageObj> list, BmobException e) {

                if (e != null) {
                    Log.d("findObjects", e.toString());
                    if (holder.recyclerView != null) {
                        Images_Pager_Adapter adapter = (Images_Pager_Adapter) holder.recyclerView.getAdapter();
                        adapter.setImages(null);
                        adapter.notifyDataSetChanged();
                        holder.recyclerView.setVisibility(View.GONE);
                    }
                    return;
                }
                if (e == null) {
                    if (list != null&&list.size() > 0) {
                        initRecyclerView(holder, changeObjects(list), notePaperObj);
                    } else {
                        if (holder.recyclerView != null) {
                            Images_Pager_Adapter adapter = (Images_Pager_Adapter) holder.recyclerView.getAdapter();
                            adapter.setImages(null);
                            adapter.notifyDataSetChanged();
                            holder.recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

    }

    public List<BmobObject> changeObjects(List<NoteImageObj> list){
        List<BmobObject> list_bmob = new ArrayList();
        for(NoteImageObj noteImageObj:list)
        list_bmob.add((BmobObject)noteImageObj);

        return list_bmob;
    }

    public String timeFormat(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        String timeStr = format.format(time);

        return timeStr;

    }

    public void initRecyclerView(final MyViewHolder holder,List<BmobObject> images, final NotePaperObj contentObject){
        if(holder.recyclerView==null){
            holder.recyclerView = (RecyclerView) holder.stub.inflate();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
            linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
//            holder.recyclerView.addItemDecoration(new SpacesItemDecoration(8));
            Images_Pager_Adapter adapter = new Images_Pager_Adapter(images);
            calculateSize(adapter);
            holder.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new Images_Pager_Adapter.ItemClickListener() {
                @Override
                public void onItemClickListener(View v, int position) {
                    Bundle bundle ;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        Pair<View, String> p1 =  Pair.create((View) holder.stub, activity.getString(R.string.main_transition_name_image));
                        Pair<View, String> p2 =  Pair.create((View) holder.tv_date, activity.getString(R.string.main_transition_name_textView));
                        bundle = ActivityOptions.makeSceneTransitionAnimation(activity, p1, p2).toBundle();
                    }else{
                        bundle = new Bundle();
                    }
                    mClickListener.onItemClick(bundle,contentObject);
                }

                @Override
                public void onItemDeleteClickListener(View v, int position) {

                }

                @Override
                public boolean onItemLongClickListener(View v, int position) {
                    return false;
                }
            });

        }else{
            if(holder.recyclerView.getVisibility()==View.GONE)holder.recyclerView.setVisibility(View.VISIBLE);
            Images_Pager_Adapter adapter = (Images_Pager_Adapter) holder.recyclerView.getAdapter();
            adapter.setImages(images);
            adapter.notifyDataSetChanged();
        }

    }

    public void calculateSize(Images_Pager_Adapter adapter){

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int w = mDisplayMetrics.widthPixels;
        int h = mDisplayMetrics.heightPixels;
        adapter.setImageSize(-1,h/3);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ViewStub stub;
        TextView tv_content;
        TextView tv_date;
        RecyclerView recyclerView;
        ImageView iv_check;

        public MyViewHolder(View itemView) {
            super(itemView);
            stub = (ViewStub) itemView.findViewById(R.id.vs_recycler);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);

        }
    }
}
