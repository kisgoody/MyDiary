package zhao.edifier.com.mynotepaper;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import zhao.edifier.com.mylibrary.ZUtile.LUtile;
import zhao.edifier.com.mynotepaper.Adapter.ImagesAdapter;
import zhao.edifier.com.mynotepaper.Adapter.Images_Pager_Adapter;
import zhao.edifier.com.mynotepaper.Mode.Obj.ContentObject;
import zhao.edifier.com.mynotepaper.Mode.Obj.DBManager;
import zhao.edifier.com.mynotepaper.Mode.Obj.ImageObj;
import zhao.edifier.com.mynotepaper.Mode.Obj.NoteImageObj;
import zhao.edifier.com.mynotepaper.Mode.Obj.NotePaperObj;
import zhao.edifier.com.mynotepaper.Utile.SpacesItemDecoration;
import zhao.edifier.com.mynotepaper.Utile.U;
import zhao.edifier.com.mynotepaper.View.ContentView;

public class PageActivity extends BaseActivity implements ContentView,View.OnClickListener,Images_Pager_Adapter.ItemClickListener {

    private NotePaperObj contentObject;
    private TextView tv_title;
    private ViewStub stub;
    private RecyclerView recyclerView;
    private Images_Pager_Adapter adapter;
//    private TextInputEditText mTextInputEditText;
    private EditText mTextInputEditText;
    private ImageView ib_back,ib_maps,iv_more;
    private DBManager dbManager;
    private boolean isChange;
    private static final String TAG = "PageActivity";
    private long time;
    private List<NoteImageObj> startList;
    private boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme);
//        setTheme(R.style.Theme_Three);
        setContentView(R.layout.activity_page);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mTextInputEditText = (EditText) findViewById(R.id.et_info);
        stub = (ViewStub) findViewById(R.id.re_maps);
        ib_back = (ImageView) findViewById(R.id.ib_back);
        iv_more = $(R.id.iv_more);
        iv_more.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ib_maps = (ImageView) findViewById(R.id.ib_maps);
        ib_maps.setOnClickListener(this);
        startAnimation();
        readTodayData();

//        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        time = System.currentTimeMillis();
        if (adapter == null) {
            adapter = new Images_Pager_Adapter(null);
            adapter.setOnItemClickListener(PageActivity.this);
            calculateSize(adapter);

        }

        if(contentObject==null){
            isNew = true;
            tv_title.setText(timeFormat(time));
            contentObject = new NotePaperObj();
            contentObject.setUserId(BmobUser.getCurrentUser().getObjectId());
        }else{
            isNew = false;
            timeDeal(contentObject,tv_title);
//            tv_title.setText(timeFormat(contentObject.getCreateDate()));
            mTextInputEditText.setText(contentObject.getContent());
            flashImages();
        }
        mTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChange = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    void themeCurrent(int theme) {

        switch (theme){

            case 1:
                ib_back.setImageResource(R.drawable.ic_arrow_yellow);
                ib_maps.setImageResource(R.drawable.ic_pic_yellow);
                iv_more.setImageResource(R.drawable.ic_more_yellow);
                break;
            case 2:
                ib_back.setImageResource(R.drawable.ic_arrow_blue);
                ib_maps.setImageResource(R.drawable.ic_pic_blue);
                iv_more.setImageResource(R.drawable.ic_more_blue);
                break;
            case 3:
                ib_back.setImageResource(R.drawable.ic_arrow_green);
                ib_maps.setImageResource(R.drawable.ic_pic_green);
                iv_more.setImageResource(R.drawable.ic_more_green);

                break;
            case 4:
                ib_back.setImageResource(R.drawable.ic_arrow_red);
                ib_maps.setImageResource(R.drawable.ic_pic_red);
                iv_more.setImageResource(R.drawable.ic_more_red);

                break;
            case 5:
                ib_back.setImageResource(R.drawable.ic_arrow_black);
                ib_maps.setImageResource(R.drawable.ic_pic_black);
                iv_more.setImageResource(R.drawable.ic_more_black);

                break;
        }

    }

    public void startAnimation(){

        ActivityCompat.setEnterSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                ib_back.setScaleX(0f);
                ib_back.setScaleY(0f);
                ib_maps.setScaleX(0f);
                ib_maps.setScaleY(0f);
                $(R.id.iv_more).setScaleX(0f);
                $(R.id.iv_more).setScaleY(0f);
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);

                ViewCompat.animate(ib_back).setStartDelay(350).scaleX(1f).scaleY(1f).alpha(1f);
                ViewCompat.animate(ib_maps).setStartDelay(350).scaleX(1f).scaleY(1f).alpha(1f);
                ViewCompat.animate($(R.id.iv_more)).setStartDelay(350).scaleX(1f).scaleY(1f).alpha(1f);
            }
        });


    }
    public void calculateSize(Images_Pager_Adapter adapter){

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int w = mDisplayMetrics.widthPixels;
        int h = mDisplayMetrics.heightPixels;
        adapter.setImageSize(w / 3, (w / 3) * h / w);
    }
    public void flashImages(){
        BmobQuery<NoteImageObj> query = new BmobQuery<>();
        query.addWhereEqualTo("notePaperObj", contentObject);
        query.findObjects(new FindListener<NoteImageObj>() {
            @Override
            public void done(List<NoteImageObj> list, BmobException e) {

                if (e != null) {
                    Log.d(TAG, e.toString());
                } else {
                    startList = list;
                    notifyRecyclerView(changeObjects(list));
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

    public void  notifyRecyclerView(List<BmobObject> list){

        if (recyclerView == null) {
            recyclerView = (RecyclerView) stub.inflate();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PageActivity.this);
            linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
            recyclerView.addItemDecoration(new SpacesItemDecoration(8));
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }

        adapter.setImages(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20&&resultCode==21){
            isChange =true;
            List<String> list_check = data.getStringArrayListExtra("checkedImagesPath");
            newImages(list_check);
//            int size = list_check.size();
//            String images[] = new String[list_check.size()];
//            list_check.toArray(images);
//            choiceImagesResult(images);
        }

    }

    public void newImages(List<String> list_check ){
        List<BmobObject> list = new ArrayList<>();
        for(String path:list_check){
            list.add(new NoteImageObj(path,0,contentObject));
        }
        notifyRecyclerView(list);
    }

    public void timeDeal(NotePaperObj contentObject,TextView textView){

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
        textView.setText(showTime);

    }

    public String timeFormat(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        String timeStr = format.format(time);
        return timeStr;
    }

    @Override
    public void choiceImages(String[] choicedImages) {

    }

    @Override
    public void choiceImagesResult(String[] resultImages) {
        flashImages();
    }

    @Override
    public void readTodayData() {
        dbManager = DBManager.newInstance();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        contentObject = (NotePaperObj) bundle.getSerializable("ContentObject");
    }

    @Override
    public void saveData(NotePaperObj contentObject) {
//        contentObject.setUserId(BmobUser.getCurrentUser().getObjectId());
        if(isNew) {
            contentObject.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        getUnUploadImagePath();
                        Log.d("save", "ok");

                    } else {
                        Log.d("save", e.toString() + " s:" + s);
                    }


                }
            });
        }else{
            contentObject.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        getUnUploadImagePath();
                        Log.d("update", "ok");

                    } else {
                        Log.d("update", e.toString());
                    }
                }
            });

        }
//        dbManager.saveObj(contentObject);
    }

    @Override
    public void onBackPressed() {

        if(isChange) {
            contentObject.setCreateDate(time);
            contentObject.setContent(mTextInputEditText.getText().toString());
//            if(adapter!=null){
//                contentObject.setImages(U.array2String(adapter.getImages()));
//            }
            saveData(contentObject);
            Log.d(TAG, "saveData");

        }else{
            super.onBackPressed();
        }
    }

    public void getUnUploadImagePath(){

        if(startList==null||startList.size()==0){
            insertData();
            return;
        }
        //删除数据库图片路径
        for(NoteImageObj bm: startList){
            Log.d("getUnUploadImagePath", bm.getObjectId());
            NoteImageObj nn = new NoteImageObj();
            nn.setObjectId(bm.getObjectId());
            nn.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.d("delete","ok");
                    } else {
                        Log.d("delete",e.toString());
                    }
                }
            });
        }
        insertData();
    }

    public void insertData(){
        List<BmobObject> imageObjs = adapter.getImages();
        if(imageObjs==null||imageObjs.size()==0){
            setResult(11);
            finish();
        return;
        }
        //添加图片路径
        for(BmobObject bmobObject:imageObjs){
            bmobObject.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Log.d("save","ok");

                    }else{
                        Log.d("save",e.toString());
                    }
                }
            });

        }
        setResult(11);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.iv_more:
                popLayout();
                break;
            case R.id.ib_maps:
                Intent intent = new Intent(this,ImagesListActivity.class);
                if(adapter!=null&&adapter.getImages()!=null&&adapter.getImages().size()>0){
                    intent.putStringArrayListExtra("maps", (ArrayList) getPaths());
                }
                startActivityForResult(intent, 20);
                break;
        }
    }

    public List<String> strings2List(String[] strings){
        List<String> list = new ArrayList();
        for(String string:strings){
            list.add(string);
        }
        return list;
    }

    @Override
    public void onItemClickListener(View v, int position) {
//        list = intent.getStringArrayListExtra("allImagesPath");
//        list_check = intent.getStringArrayListExtra("checkedImagesPath");
//        list_position = intent.getIntegerArrayListExtra("positions");
//        final int  currentPosition  = intent.getIntExtra("currentPosition", 0);
        if(adapter.isEditImages()){
            adapter.setEditImages(false);
            adapter.notifyDataSetChanged();
            return;
        }

        Intent intent = new Intent(this,ImagePageActivity.class);
        intent.putStringArrayListExtra("allImagesPath", (ArrayList<String>) getPaths());
        intent.putExtra("tag", "look");
        intent.putExtra("currentPosition",position);
        startActivity(intent);

    }

    public List<String> getPaths(){
        List<BmobObject> list = adapter.getImages();
        List<String> list_path = new ArrayList<>();

        for(BmobObject note:list){

            NoteImageObj n = (NoteImageObj)note;

            list_path.add(n.getUrl());
        }
        return  list_path;
    }

    @Override
    public void onItemDeleteClickListener(View v, int position) {
        BmobObject bmobObject = adapter.getImages().get(position);
        bmobObject.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    flashImages();
                    isChange = true;
                }else {
                    Log.d("onItemDeleteClickListener",e.toString());
                }
            }
        });
//        String path ="{"+ adapter.getImages().get(position).getUrl()+"}";
//        String newStr = contentObject.getImages().replace(path,"");
//        contentObject.setImages(newStr);
//        dbManager.saveObj(contentObject);



    }


    @Override
    public boolean onItemLongClickListener(View v, int position) {
        adapter.setEditImages(!adapter.isEditImages());
        adapter.notifyDataSetChanged();
        return false;
    }
}
