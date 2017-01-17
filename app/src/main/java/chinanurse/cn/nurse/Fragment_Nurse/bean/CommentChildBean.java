package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhuchongkun on 2017/1/1.
 */

public class CommentChildBean implements Serializable {
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    @SerializedName("pid")
    private String pid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("userid")
    private String userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("username")
    private String userName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @SerializedName("content")
    private String content;

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @SerializedName("photo")
    private String userPhoto;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @SerializedName("add_time")
    private String addTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    private String type;

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    @SerializedName("userlevel")
    private String userLevel;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @SerializedName("major")
    private String major;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("cid")
    private String id;

    @Override
    public String toString() {
        return "CommentChildBean{" +
                "pid='" + pid + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", addTime='" + addTime + '\'' +
                ", type='" + type + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", major='" + major + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
