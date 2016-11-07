package zhao.edifier.com.mynotepaper.Mode.Obj;

import cn.bmob.v3.BmobObject;

/**
 * Created by tech57 on 2016/10/19.
 */
public class NoteImageObj extends BmobObject{

    private String url;
    private int uploadProgress=100;
    private NotePaperObj notePaperObj;

    public NoteImageObj(String url,int uploadProgress,NotePaperObj notePaperObj){
        this.url = url;
        this.uploadProgress = uploadProgress;
        this.notePaperObj = notePaperObj;
    }
    public NoteImageObj(){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public NotePaperObj getNotePaperObj() {
        return notePaperObj;
    }

    public void setNotePaperObj(NotePaperObj notePaperObj) {
        this.notePaperObj = notePaperObj;
    }
}
