package zhao.edifier.com.mynotepaper.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import zhao.edifier.com.mynotepaper.R;
/**
 * Created by tech57 on 2016/9/28.
 */
public class ImagesViewPagerAdapter extends PagerAdapter{


    private List<String> list,list_check;
    private List<Integer> list_position;

    public ImagesViewPagerAdapter(List<String> list, List<String> list_check, List<Integer> list_position) {
        this.list = list;
        this.list_check = list_check;
        this.list_position = list_position;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList_check() {
        return list_check;
    }

    public void setList_check(List<String> list_check) {
        this.list_check = list_check;
    }

    public List<Integer> getList_position() {
        return list_position;
    }

    public void setList_position(List<Integer> list_position) {
        this.list_position = list_position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item_layout,null);
        PhotoView iv_image = (PhotoView) view.findViewById(R.id.iv_image);
//        iv_image.setMaxZoom(4f);
        scaleBitmap(iv_image, list.get(position));
        container.addView(view);

        new PhotoViewAttacher(iv_image).setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

            }
        });
        return view;
    }

    private void scaleBitmap(ImageView iv_big_img,String path){
//        ImageLoader.getInstance().displayImage(path,iv_big_img);

        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
//                .setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                        // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                        .setUseMemCache(true)
//                .setLoadingDrawable(scaleBitmap(path))
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER).build();
        x.image().bind(iv_big_img, path, imageOptions);
    }
}
