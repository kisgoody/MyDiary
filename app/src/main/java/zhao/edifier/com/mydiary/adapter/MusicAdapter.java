package zhao.edifier.com.mydiary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mydiary.R;
import zhao.edifier.com.mydiary.mode.music.Audio;

/**
 * Created by tech57 on 2016/8/29.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{


    private List<Audio> list =null;

    private int checkPositon = -1;
    private ImageView imageView;


    public MusicAdapter(List<Audio> list) {
        if(list==null)list = new ArrayList();
        else this.list = list;
    }

    public List<Audio> getList() {
        return list;
    }

    public void setList(List<Audio> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Audio song = (Audio) list.get(position);
        holder.tv_content.setText(song.getTitle());
        holder.tv_data.setText(song.getArtist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView!=null){
                    imageView.setVisibility(View.INVISIBLE);
                }
                checkPositon = position;
                imageView = (ImageView) v.findViewById(R.id.iv_check);
                imageView.setVisibility(View.VISIBLE);
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

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_data = (TextView) itemView.findViewById(R.id.tv_data);
        }
    }

}
