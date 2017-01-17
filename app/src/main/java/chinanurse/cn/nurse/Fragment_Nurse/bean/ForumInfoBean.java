package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhuchongkun on 2016/12/16.
 * 帖子
 */

public class ForumInfoBean implements Serializable {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @SerializedName("data")
    private DataBean data;

    public class DataBean implements Serializable {


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @SerializedName("tid")
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @SerializedName("status")
        private String status;//帖子状态 0审核拒绝，1通过

        public String getReviewTime() {
            return reviewTime;
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }

        @SerializedName("review_time")
        private String reviewTime;//审核时间


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
        private String isReward;//(是否打赏 1显示，0不)


        public String getAddLike() {
            return addLike;
        }

        public void setAddLike(String addLike) {
            this.addLike = addLike;
        }

        @SerializedName("add_like")
        private String addLike;//判断我是否点赞

        public String getCollect() {
            return collect;
        }

        public void setCollect(String collect) {
            this.collect = collect;
        }

        @SerializedName("favorites_add")
        private String collect;//收藏数量

        public String getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(String collect_num) {
            this.collect_num = collect_num;
        }

        @SerializedName("favorites")
        private String collect_num;//判断我是否收藏

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", communityId='" + communityId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", title='" + title + '\'' +
                    ", creatTime='" + creatTime + '\'' +
                    ", content='" + content + '\'' +
                    ", description='" + description + '\'' +
                    ", status='" + status + '\'' +
                    ", reviewTime='" + reviewTime + '\'' +
                    ", photo=" + Arrays.toString(photo) +
                    ", hits='" + hits + '\'' +
                    ", like='" + like + '\'' +
                    ", isTop='" + isTop + '\'' +
                    ", isBest='" + isBest + '\'' +
                    ", isReward='" + isReward + '\'' +
                    ", addLike='" + addLike + '\'' +
                    ", collect='" + collect + '\'' +
                    ", collect_num='" + collect_num + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ForumInfoBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
