package zhao.edifier.com.mydiary;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import java.util.Collections;

import zhao.edifier.com.mydiary.adapter.MusicAdapter;
import zhao.edifier.com.mydiary.adapter.OnItemTouchListener;
import zhao.edifier.com.mydiary.adapter.SpacesItemDecoration;
import zhao.edifier.com.mydiary.mode.music.MediaUtils;

public class MusicSelectActivity extends ModeActivity {

    private RecyclerView rv_music;
    private MusicAdapter adapter;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_select);
        rv_music = (RecyclerView) findViewById(R.id.rv_music);
        rv_music.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MusicAdapter(MediaUtils.getAudioList(this));
        rv_music.setAdapter(adapter);
        itemClickDeal();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void itemClickDeal(){
        rv_music.addItemDecoration(new SpacesItemDecoration(16));
        final ItemTouchHelper helper = new ItemTouchHelper(new MyCallBack());
        helper.attachToRecyclerView(rv_music);
        rv_music.addOnItemTouchListener(new OnItemTouchListener(rv_music) {
            @Override
            public void onItemClick(final RecyclerView.ViewHolder vh) {
//                vh.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mainActivity.intent2DiaryDetail((DiaryObj) adapter.getList().get(vh.getAdapterPosition()));
//                    }
//                });

            }

            @Override
            public void onItemLongPressClick(RecyclerView.ViewHolder vh) {
                if (vh.getAdapterPosition() >= 0) {
                    helper.startDrag(vh);
                }
            }
        });

    }

    public class MyCallBack extends ItemTouchHelper.Callback {

        /**
         * 拖动标识
         */
        private int dragFlags;
        /**
         * 删除滑动标识
         */
        private int swipeFlags;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            dragFlags = 0;
            swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager
                    || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                if (viewHolder.getAdapterPosition() >= 0)
                    swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (toPosition >= 0) {
                if (fromPosition < toPosition)
                    //向下拖动
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(adapter.getList(), i, i + 1);
                    }
                else {
                    //向上拖动
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(adapter.getList(), i, i - 1);
                    }
                }
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            }
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            rv_music.getAdapter().notifyItemRemoved(position);
            adapter.getList().remove(position);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

}
