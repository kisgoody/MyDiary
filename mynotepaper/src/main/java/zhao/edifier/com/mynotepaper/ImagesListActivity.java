package zhao.edifier.com.mynotepaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mynotepaper.Adapter.ImagesChoiceAdapter;
import zhao.edifier.com.mynotepaper.Mode.Obj.ImagesSearch;
import zhao.edifier.com.mynotepaper.Utile.SpacesItemDecoration;

public class ImagesListActivity extends AppCompatActivity implements View.OnClickListener{


    private RecyclerView recyclerView;
    private TextView tv_title;
    private ImageButton ib_back;
    private boolean isCheck;
    private ImagesChoiceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);

        final List list = getIntent().getStringArrayListExtra("maps");

        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
        adapter = new ImagesChoiceAdapter(null);
        if(list!=null&&list.size()>0){
            adapter.setList_check(list);
        }
        recyclerView.setAdapter(adapter);

        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);

        new ImagesSearch(this,new ImagesSearch.ImagesSearchListener() {
            @Override
            public void OnImagesSearchListener(String path) {
                adapter.getList().add(path);
//                adapter.notifyItemInserted(adapter.getList().size()-1);
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        List<String> list = intent.getStringArrayListExtra("allImagesPath");
        List<String> list_check = intent.getStringArrayListExtra("checkedImagesPath");
        List<Integer> list_position = intent.getIntegerArrayListExtra("positions");
        if(requestCode==30&&resultCode==31){
            adapter.setList(list);
            adapter.setList_check(list_check);
            adapter.setList_position(list_position);
            adapter.notifyDataSetChanged();
        }else if(requestCode==30&&resultCode==32){
//            setResult(21,intent);
            onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                    onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        intent.putStringArrayListExtra("checkedImagesPath", (ArrayList) adapter.getList_check());
        setResult(21,intent);
        super.onBackPressed();


    }
}
