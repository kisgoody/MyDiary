package zhao.edifier.com.mydiary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mydiary.mode.database.DBManager;
import zhao.edifier.com.mydiary.view.SwipeBackLayout;
import zhao.edifier.com.mylibrary.ZUtile.StatusBarCompat;

import static zhao.edifier.com.mydiary.R.layout.exit_dialog_21blow_layout;

public abstract  class ModeActivity extends AppCompatActivity {
    private List<Activity> activityList = new ArrayList<Activity>();
    private FragmentTransaction transaction;
    public DBManager dbManager;
    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //滑动删除
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);

        activityList.add(this);
        x.view().inject(this);
        dbManager = DBManager.newInstance();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    public void replaceFragment(int layout,Fragment fragment){
        if(transaction==null){
            FragmentManager manager =getSupportFragmentManager();
            transaction = manager.beginTransaction();
        }
        transaction.add(layout, fragment);
        transaction.commit();
    }


    public void changeStyle(Toolbar toolbar){
        toolbar.setBackground(new ColorDrawable(0xffa5bfda));
        setSupportActionBar(toolbar);

    }

    public void changeTitle(Toolbar toolbar,String title){
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);
    }

    PopupWindow pop;
    public void exitApp(View rootView){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name).setMessage(R.string.mode_exitApp).setPositiveButton(R.string.mode_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                exit();
            }
        }).setNegativeButton(R.string.mode_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        }else{

        if(pop==null) {
                pop = new PopupWindow();
                pop.setWidth(-1);
                pop.setHeight(-1);
                pop.setTouchable(true);
                View v = LayoutInflater.from(this).inflate(R.layout.exit_dialog_21blow_layout, null);
                pop.setContentView(v);
            v.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                    }
                });
                v.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                    }
                });

                pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
                pop.showAsDropDown(rootView,0,0);


        }
    }

    private void exit(){
        for(Activity activity:activityList){
            activity.finish();
        }
        System.exit(0);
    }

    public void showToast(String string){

        Toast.makeText(getApplicationContext(), string,Toast.LENGTH_SHORT).show();
    }
    public void showToast(int stringId){

        Toast.makeText(getApplicationContext(), getString(stringId),Toast.LENGTH_SHORT).show();
    }
    public void Log(Object object){
        System.out.println(object.toString());

    }
    public void callPermission(){
        if (Build.VERSION.SDK_INT >= 23) {

            List<String> permission = new ArrayList();
            int READ_PHONE_STATE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
            int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if(READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.READ_PHONE_STATE);
            } if(ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            } if(ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            } if(READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            } if(WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED){
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if(permission.size()==0){
//                new MyLocation(getApplicationContext());
            }else{
                String[] string = new String[permission.size()];
                permission.toArray(string);
//                    ActivityCompat.requestPermissions(this, string, 100);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
            }

        } else {
//            new MyLocation(getApplicationContext());
        }


    }


}
