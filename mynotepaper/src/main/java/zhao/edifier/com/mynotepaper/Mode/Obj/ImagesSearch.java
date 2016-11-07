package zhao.edifier.com.mynotepaper.Mode.Obj;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tech57 on 2016/9/27.
 */
public class ImagesSearch {


    private static List<File> fileList = new ArrayList<File>();
    private static String[] img = new String[]{".jpg", ".png", ".gif", ".bmp"};

    private ImagesSearchListener mImagesSearchListener;
    private Context context;
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();

    public interface ImagesSearchListener{

        void OnImagesSearchListener(String path);

    }

    public void setOnImagesSearchListener(ImagesSearchListener mImagesSearchListener){
        this.mImagesSearchListener = mImagesSearchListener;
    }


    public ImagesSearch(Context context ,ImagesSearchListener mImagesSearchListener){
        this.context = context;
        this.mImagesSearchListener = mImagesSearchListener;
        new Thread(new Runnable() {
            @Override
            public void run() {

//            checkFile(Environment.getExternalStorageDirectory());
                search();
            }
        }).start();


    }


    public void search(){

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver =context.getContentResolver();

        //只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED+" desc");

        if(mCursor == null){
            return;
        }

        while (mCursor.moveToNext()) {
            //获取图片的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));



            Message msg = handler.obtainMessage();
            msg.obj = path;
            msg.sendToTarget();

            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();
//            Log.d("checkFile","file.getAbsolutePath():"+file.getAbsolutePath());
            if (!mGruopMap.containsKey(parentName)) {
                List<String> chileList = new ArrayList<String>();
                chileList.add(path);
                mGruopMap.put(parentName, chileList);
            } else {
                mGruopMap.get(parentName).add(path);
            }
        }



        //通知Handler扫描图片完成
        mCursor.close();
    }

    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param mGruopMap
     * @return
//     */
//    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap){
//        if(mGruopMap.size() == 0){
//            return null;
//        }
//        List<ImageBean> list = new ArrayList<ImageBean>();
//
//        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, List<String>> entry = it.next();
//            ImageBean mImageBean = new ImageBean();
//            String key = entry.getKey();
//            List<String> value = entry.getValue();
//
//            mImageBean.setFolderName(key);
//            mImageBean.setImageCounts(value.size());
//            mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片
//
//            list.add(mImageBean);
//        }
//
//        return list;
//
//    }
//
//
//}



    /**
     * 遍历sdcard 找到某找类型的file放到list中。
     * 比较耗时 建议放在线程中做
     * @param file
     */
    private void checkFile(File file) {// 遍历文件，在这里是遍历sdcard

        if (file.isDirectory()) {// 判断是否是文件夹

            File[] files = file.listFiles();// 以该文件夹的子文件或文件夹生成一个数组

            if (files != null) {// 如果文件夹不为空

                for (int i = 0; i < files.length; i++) {

                    File f = files[i];

                    checkFile(f);// 递归调用

                }

            }

        } else if (file.isFile()) {// 判断是否是文件

            int dot = file.getName().lastIndexOf(".");

            if (dot > -1 && dot < file.getName().length()) {

                String extriName = file.getName().substring(dot,

                        file.getName().length());// 得到文件的扩展名

                if (extriName.equals(img[0]) || extriName.equals(img[1])

                        || extriName.equals(img[2]) || extriName.equals(img[3])) {// 判断是否是图片文件 www.it165.net
                    Message msg = handler.obtainMessage();
                    msg.obj = file.getAbsolutePath();
                    msg.sendToTarget();
                    Log.d("checkFile","file.getAbsolutePath():"+file.getAbsolutePath());
//                    fileList.add(file);
//                    adapter.notifyItemRangeInserted();
                }

            }

        }


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String path = (String) msg.obj;
            mImagesSearchListener.OnImagesSearchListener(path);
        }
    };

}
