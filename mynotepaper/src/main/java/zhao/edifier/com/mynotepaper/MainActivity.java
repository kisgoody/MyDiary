package zhao.edifier.com.mynotepaper;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobTableSchema;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import zhao.edifier.com.mylibrary.ZUtile.StatusBarCompat;
import zhao.edifier.com.mynotepaper.Adapter.MainAdapter;
import zhao.edifier.com.mynotepaper.Mode.Obj.ContentObject;
import zhao.edifier.com.mynotepaper.Mode.Obj.DBManager;
import zhao.edifier.com.mynotepaper.Mode.Obj.NotePaperObj;
import zhao.edifier.com.mynotepaper.Utile.SpacesItemDecoration;
import zhao.edifier.com.mynotepaper.Utile.U;
import zhao.edifier.com.mynotepaper.View.HomeView;

import static zhao.edifier.com.mynotepaper.R.layout.pop_main_layout;

public class  MainActivity extends BaseActivity implements HomeView,SwipeRefreshLayout.OnRefreshListener{


    private RecyclerView recyclerView;
    private DBManager dbManager;
    private MainAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static  final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdate();
        dbManager = DBManager.newInstance();

        recyclerView = $(R.id.rv_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        swipeRefreshLayout = $(R.id.swipe_flash);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        onRefresh();
        $(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
        $(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                popLayout();
            }
        });

    }

    public void checkUpdate(){
        Bmob.getTableSchema("AppVersion", new QueryListener<BmobTableSchema>() {
            @Override
            public void done(BmobTableSchema schema, BmobException ex) {
                if (ex == null) {
                    if (!schema.getClassName().equals("AppVersion")) {
                        BmobUpdateAgent.initAppVersion();
                        Log.i("bmob", "创建表：" + schema.getClassName() + "-" + schema.getFields().toString());
                    }else{
                        BmobUpdateAgent.update(MainActivity.this);
                        Log.i("bmob", "update：" + schema.getClassName() + "-" + schema.getFields().toString());
                    }
                } else {
                    BmobUpdateAgent.initAppVersion();
                    Log.i("bmob", "获取指定表的表结构信息失败:" + ex.getLocalizedMessage() + "(" + ex.getErrorCode() + ")");
                }
            }
        });


    }


    public void searchData(){

        //读取网络数据
        BmobUser user = BmobUser.getCurrentUser();
        BmobQuery<NotePaperObj> query= new BmobQuery<>();

        query.addWhereEqualTo("userId", user.getObjectId());
        query.order("-createDate");
        query.findObjects(new FindListener<NotePaperObj>() {
            @Override
            public void done(List<NotePaperObj> list, BmobException e) {
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (e == null) {
                    if(adapter==null) {
                        adapter = new MainAdapter(list, new MainAdapter.ClickListener() {

                            @Override
                            public void onItemClick(Bundle bundle, NotePaperObj contentObject) {
                                itemClick(bundle, contentObject);
                            }

                            @Override
                            public void onItemLongClick() {

                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }else{
                        adapter.setDatas(list);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Log.d(TAG,e.toString());
                }
            }
        });
    }

    public void changeStyle(){
        StatusBarCompat.compat(this, 0x000000);
        getWindow().setBackgroundDrawable(new ColorDrawable(0xffffff));
        getTheme().applyStyle(R.style.Theme_First, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10&&resultCode==11){
            searchData();
//            adapter.setDatas(readData());
//            adapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<Object> readData() {
       List<Object> list =  dbManager.findAllESC(ContentObject.class, "date");
        return list;
    }

    @Override
    public void deleteDate(int position) {
        dbManager.deleteObj(adapter.getDatas().get(position));
    }

    @Override
    public void addData() {
        U.ThemeName = U.PageTheme;
        Intent intent = new Intent(MainActivity.this,PageActivity.class);
        startActivityForResult(intent, 10);
    }

    @Override
    public void itemClick(Bundle bundle,NotePaperObj contentObject) {
        U.ThemeName = U.PageTheme;
        Intent intent = new Intent(MainActivity.this,PageActivity.class);
        intent.putExtra("ContentObject", contentObject);
        startActivityForResult(intent, 10, bundle);

    }

    @Override
    protected void onResume() {
        super.onResume();
        U.ThemeName=U.APPTHEME;
    }

    @Override
    void themeCurrent(int theme) {
        ImageView iv_more = $(R.id.iv_more);
        switch (theme){
            case 6:
                iv_more.setImageResource(R.drawable.ic_more_black);
                break;
            default:
                iv_more.setImageResource(R.drawable.ic_more_withe);
                break;
        }
    }


    @Override
    public void onRefresh() {
        searchData();
    }
}
