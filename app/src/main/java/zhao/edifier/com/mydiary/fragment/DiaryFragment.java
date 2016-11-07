package zhao.edifier.com.mydiary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.util.Collections;
import java.util.List;

import zhao.edifier.com.mydiary.MainActivity;
import zhao.edifier.com.mydiary.R;
import zhao.edifier.com.mydiary.adapter.DiaryAdapter;
import zhao.edifier.com.mydiary.adapter.OnItemTouchListener;
import zhao.edifier.com.mydiary.adapter.SpacesItemDecoration;
import zhao.edifier.com.mydiary.mode.DiaryObj;

/**
 * Created by tech57 on 2016/8/29.
 */
public class DiaryFragment extends Fragment {

    private RecyclerView rv_diarys;
    public DiaryAdapter adapter;
    private MainActivity mainActivity;
    private List diarys;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_blank, container,false);
        rv_diarys = (RecyclerView) v.findViewById(R.id.rv_diarys);
        rv_diarys.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mainActivity = (MainActivity) getActivity();

        diarys = mainActivity.searchAllDiary();
        adapter = new DiaryAdapter(diarys);
        rv_diarys.setAdapter(adapter);

        itemClickDeal();
        return v;
    }

    public void flashList(){
        diarys = mainActivity.searchAllDiary();
        adapter.setList(diarys);
        adapter.notifyDataSetChanged();
    }


    public void itemClickDeal(){
        rv_diarys.addItemDecoration(new SpacesItemDecoration(16));
        final ItemTouchHelper helper = new ItemTouchHelper(new MyCallBack());
        helper.attachToRecyclerView(rv_diarys);
        rv_diarys.addOnItemTouchListener(new OnItemTouchListener(rv_diarys) {
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
            int positon = viewHolder.getAdapterPosition();
            mainActivity.dbManager.deleteObj(adapter.getList().get(positon));
            rv_diarys.getAdapter().notifyItemRemoved(positon);
            adapter.getList().remove(positon);
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


