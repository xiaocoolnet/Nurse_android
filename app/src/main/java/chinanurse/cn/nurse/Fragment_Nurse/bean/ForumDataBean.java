package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by zhuchongkun on 2017/1/9.
 */

public class ForumDataBean implements Serializable {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;// 帖子ID

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    @SerializedName("community_id")
    private String communityId;//圈子ID

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("userid")
    private String userId;//?

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("title")
    private String title;//标题？


    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    @SerializedName("create_time")
    private String creatTime;//创建时间 ？

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @SerializedName("content")
    private String content;//内容

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    private String description;// 介绍


    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    @SerializedName("review_time")
    private String reviewTime;//回复时间


    public String[] getPhoto() {
        return photo;
    }

    public void setPhoto(String[] photo) {
        this.photo = photo;
    }

    @SerializedName("photo")
    private String[] photo;// 帖子图片

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    @SerializedName("hits")
    private String hits;//点击数

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    @SerializedName("like_num")
    private String like;//点赞数

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    @SerializedName("istop")
    private String isTop;//是否置顶(置顶1,不0)

    public String getIsBest() {
        return isBest;
    }

    public void setIsBest(String isBest) {
        this.isBest = isBest;
    }

    @SerializedName("isbest")
    private String isBest;//是否精华(选精1,不0)

    public String getIsReward() {
        return isReward;
    }

    public void setIsReward(String isReward) {
        this.isReward = isReward;
    }

    @SerializedName("isreward")
    private String isReward;//是否打赏

    public String getPublishAddress() {
        return publishAddress;
    }

    public void setPublishAddress(String publishAddress) {
        this.publishAddress = publishAddress;
    }

    @SerializedName("address")
    private String publishAddress;//发帖位置

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @SerializedName("user_photo")
    private String userPhoto;//用户头像

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    @SerializedName("level")
    private String userLevel;//用户的等级

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    @SerializedName("comments_count")
    private String commentsCount;//评论数量

    public String getAddLike() {
        return addLike;
    }

    public void setAddLike(String addLike) {
        this.addLike = addLike;
    }

    @SerializedName("add_like")
    private String addLike;//判断我是否点赞

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @SerializedName("community_name")
    private String communityName;// 圈子名称

    public String getCommunityPhoto() {
        return communityPhoto;
    }

    public void setCommunityPhoto(String communityPhoto) {
        this.communityPhoto = communityPhoto;
    }

    @SerializedName("community_photo")
    private String communityPhoto;// 圈子照片

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    @SerializedName("c_master")
    private String isMaster;//发帖人是否是圈主:1是,0不

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SerializedName("user_name")
    private String userName;

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @SerializedName("auth_type")
    private String authType;

    @Override
    public String toString() {
        return "ForumDataBean{" +
                "id='" + id + '\'' +
                ", communityId='" + communityId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", creatTime='" + creatTime + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                ", photo=" + Arrays.toString(photo) +
                ", hits='" + hits + '\'' +
                ", like='" + like + '\'' +
                ", isTop='" + isTop + '\'' +
                ", isBest='" + isBest + '\'' +
                ", isReward='" + isReward + '\'' +
                ", publishAddress='" + publishAddress + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", commentsCount='" + commentsCount + '\'' +
                ", addLike='" + addLike + '\'' +
                ", communityName='" + communityName + '\'' +
                ", communityPhoto='" + communityPhoto + '\'' +
                ", isMaster='" + isMaster + '\'' +
                ", userName='" + userName + '\'' +
                ", authType='" + authType + '\'' +
                '}';
    }
}
