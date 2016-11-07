package zhao.edifier.com.mynotepaper.Mode.Obj;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by tech57 on 2016/10/19.
 */
public class NotePaperObj extends BmobObject implements Serializable {

    private static final long serialVersionUID = 100L;
    private String userId;
    private String content;
    private long createDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
