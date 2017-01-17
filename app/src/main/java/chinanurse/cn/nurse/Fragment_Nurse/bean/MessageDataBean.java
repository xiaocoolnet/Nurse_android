package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhuchongkun on 2017/1/9.
 */

public class MessageDataBean implements Serializable {
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @SerializedName("id")
    private String messageId;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    @SerializedName("from_userid")
    private String fromUserId;

    public String getMessageCentent() {
        return messageCentent;
    }

    public void setMessageCentent(String messageCentent) {
        this.messageCentent = messageCentent;
    }

    @SerializedName("centent")
    private String messageCentent;

    public String getMessagetType() {
        return messagetType;
    }

    public void setMessagetType(String messagetType) {
        this.messagetType = messagetType;
    }

    @SerializedName("type")
    private String messagetType;

    public String getMessageTid() {
        return messageTid;
    }

    public void setMessageTid(String messageTid) {
        this.messageTid = messageTid;
    }

    @SerializedName("tid")
    private String messageTid;

    public String getMessageCreateTime() {
        return messageCreateTime;
    }

    public void setMessageCreateTime(String messageCreateTime) {
        this.messageCreateTime = messageCreateTime;
    }

    @SerializedName("create_time")
    private String messageCreateTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("fu_name")
    private String userName;

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    @SerializedName("fu_level")
    private String userLevel;

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @SerializedName("fu_photo")
    private String userPhoto;

    public String getCommunityAdmin() {
        return communityAdmin;
    }

    public void setCommunityAdmin(String communityAdmin) {
        this.communityAdmin = communityAdmin;
    }

    @SerializedName("fu_community_admin")
    private String communityAdmin;

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    @SerializedName("fu_sex")
    private String userSex;

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    @SerializedName("forum_title")
    private String forumTitle;

    @Override
    public String toString() {
        return "MessageDataBean{" +
                "messageId='" + messageId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", messageCentent='" + messageCentent + '\'' +
                ", messagetType='" + messagetType + '\'' +
                ", messageTid='" + messageTid + '\'' +
                ", messageCreateTime='" + messageCreateTime + '\'' +
                ", userName='" + userName + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", communityAdmin='" + communityAdmin + '\'' +
                ", userSex='" + userSex + '\'' +
                ", forumTitle='" + forumTitle + '\'' +
                '}';
    }
}
