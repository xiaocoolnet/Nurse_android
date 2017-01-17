package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2017/1/1.
 */

public class CommentBean implements Serializable {
    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @SerializedName("refid")
    private String refId;

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

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @SerializedName("auth_type")
    private String authType;

    public String getCommentsLikes() {
        return commentsLikes;
    }

    public void setCommentsLikes(String commentsLikes) {
        this.commentsLikes = commentsLikes;
    }

    @SerializedName("comments_likes")
    private String commentsLikes;

    public String getAddLike() {
        return addLike;
    }

    public void setAddLike(String addLike) {
        this.addLike = addLike;
    }

    @SerializedName("add_like")
    private String addLike;

    public List<CommentChildBean> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<CommentChildBean> childComments) {
        this.childComments = childComments;
    }

    @SerializedName("child_comments")
    private List<CommentChildBean> childComments;

    @Override
    public String toString() {
        return "CommentBean{" +
                "refId='" + refId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", addTime='" + addTime + '\'' +
                ", type='" + type + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", major='" + major + '\'' +
                ", id='" + id + '\'' +
                ", authType='" + authType + '\'' +
                ", commentsLikes='" + commentsLikes + '\'' +
                ", addLike='" + addLike + '\'' +
                ", childComments=" + childComments +
                '}';
    }
}
