package zhao.edifier.com.mydiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mydiary.MainActivity;
import zhao.edifier.com.mydiary.R;
import zhao.edifier.com.mydiary.mode.DiaryObj;
import zhao.edifier.com.mylibrary.ZUtile.LUtile;

/**
 * Created by tech57 on 2016/8/29.
 */
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder>{


    private List<Object> list =null;
    private Context con;


    public DiaryAdapter(List<Object> list) {
        if(list==null)list = new ArrayList();
        else this.list = list;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        con = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DiaryObj diaryObj = (DiaryObj) list.get(position);
        holder.tv_content.setText(diaryObj.getContent());
        String today = LUtile.millis2Data(System.currentTimeMillis(),"dd/MM/yyyy");
        String createDay = LUtile.millis2Data(diaryObj.getCreateTime(), "dd/MM/yyyy");
        String nowYear = LUtile.millis2Data(System.currentTimeMillis(),"yyyy");
        String createYear = LUtile.millis2Data(diaryObj.getCreateTime(),"yyyy");
        String showTime = "";
        if(nowYear.equals(createYear)){
            if(today.equals(createDay)){
                showTime = LUtile.millis2Data(diaryObj.getCreateTime(), "HH:mm");

            }else{
                showTime = LUtile.millis2Data(diaryObj.getCreateTime(), "dd/MM HH:mm");
            }

        }else{
            showTime = LUtile.millis2Data(diaryObj.getCreateTime(), "dd/MM/yyyy HH:mm");
        }

        holder.tv_data.setText(showTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) con;
                mainActivity.intent2DiaryDetail((DiaryObj) getList().get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_data;
        TextView tv_content;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_data = (TextView) itemView.findViewById(R.id.tv_data);
        }
    }

}
