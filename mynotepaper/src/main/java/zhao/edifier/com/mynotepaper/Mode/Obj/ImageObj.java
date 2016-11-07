package zhao.edifier.com.mynotepaper.Mode.Obj;

/**
 * Created by tech57 on 2016/10/19.
 */
public class ImageObj {
    String url;
    int upload_progress;
    boolean isupload;//true:url,false:filePath

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpload_progress() {
        return upload_progress;
    }

    public void setUpload_progress(int upload_progress) {
        this.upload_progress = upload_progress;
    }

    public boolean isupload() {
        return isupload;
    }

    public void setIsupload(boolean isupload) {
        this.isupload = isupload;
    }
}
