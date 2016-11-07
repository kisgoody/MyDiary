package zhao.edifier.com.mydiary.mode;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by tech57 on 2016/8/29.
 */
@Table(name = "DiaryObj")
public class DiaryObj implements Serializable{

    private static final long serialVersionUID = 9L;
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "createTime")
    private long createTime;
    @Column(name = "modifyTime")
    private long modifyTime;
    @Column(name = "mapsURL")
    private String[] mapsURL;
    @Column(name = "musicURL")
    private String musicURL;
    @Column(name = "location")
    private String location;
    @Column(name = "weatherInfo")
    private String weatherInfo;
    @Column(name = "temp")
    private String temp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String[] getMapsURL() {
        return mapsURL;
    }

    public void setMapsURL(String[] mapsURL) {
        this.mapsURL = mapsURL;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
