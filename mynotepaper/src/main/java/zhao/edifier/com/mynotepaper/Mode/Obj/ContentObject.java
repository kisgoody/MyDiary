package zhao.edifier.com.mynotepaper.Mode.Obj;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by tech57 on 2016/9/26.
 */
@Table(name = "ContentObject")
public class ContentObject extends BmobObject implements Serializable{

    private static final long serialVersionUID = 100L;
    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "images")
    private String images;
    @Column(name = "date")
    private long date;
    @Column(name = "content")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
