package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2016/12/16.
 * 圈主
 */

public class CommunityMasterBean implements Serializable {
//c_name(圈子名称),userid,real_name(真实姓名),real_code(身份证号),real_address(联系地址),real_tel(手机号),real_qq(qq),real_content(申请感言),real_photo(个人日志生活照),real_photo_2(手持身份证照片)

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<DataBean> data;

    public class DataBean implements Serializable {
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @SerializedName("userid")
        private String userId;//?

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        @SerializedName("community_id")
        private String communityId;//圈子ID

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        @SerializedName("c_name")
        private String communityName;// 圈子名称

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }

        @SerializedName("power")
        private String power;//?

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long creatTime) {
            this.creatTime = creatTime;
        }

        @SerializedName("create_time")
        private long creatTime;//创建时间 ?

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @SerializedName("user_name")
        private String userName;//圈主姓名

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        @SerializedName("user_photo")
        private String userPhoto;//圈主头像

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        private String userID;//圈主身份证

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        private String userAddress;//圈主地址

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        private String userPhone;//圈主手机号

        public String getUserQQ() {
            return userQQ;
        }

        public void setUserQQ(String userQQ) {
            this.userQQ = userQQ;
        }

        private String userQQ;//圈主qq

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        private String words;// 申请感言

        public String getLivePhoto() {
            return livePhoto;
        }

        public void setLivePhoto(String livePhoto) {
            this.livePhoto = livePhoto;
        }

        private String livePhoto;//生活照

        public String getHandPhoto() {
            return handPhoto;
        }

        public void setHandPhoto(String handPhoto) {
            this.handPhoto = handPhoto;
        }

        private String handPhoto;//手持身份证照

        @Override
        public String toString() {
            return "DataBean{" +
                    "userId='" + userId + '\'' +
                    ", communityId='" + communityId + '\'' +
                    ", communityName='" + communityName + '\'' +
                    ", power='" + power + '\'' +
                    ", creatTime=" + creatTime +
                    ", userName='" + userName + '\'' +
                    ", userPhoto='" + userPhoto + '\'' +
                    ", userID='" + userID + '\'' +
                    ", userAddress='" + userAddress + '\'' +
                    ", userPhone='" + userPhone + '\'' +
                    ", userQQ='" + userQQ + '\'' +
                    ", words='" + words + '\'' +
                    ", livePhoto='" + livePhoto + '\'' +
                    ", handPhoto='" + handPhoto + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CommunityMasterBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
