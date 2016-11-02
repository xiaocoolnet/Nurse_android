package chinanurse.cn.nurse.bean.mine_main_bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/7/16 0016.
 */

//用 社区中 论坛列表的接口生成的bean容器，后期要改

public class Mine_Post_bean implements Parcelable {


    /**
     * status : success
     * data : [{"mid":"38","best":"0","type":"1","userid":"609","name":"啦啦啦","title":"咔咔咔拉进来啦啦","content":"啦啦来啦力量力量力量力量力","write_time":"1468545276","photo":null,"pic":[{"pictureurl":"{}"}],"like":[],"comment":[]},{"mid":"37","best":"0","type":"1","userid":"609","name":"啦啦啦","title":"啦啦啦啦啦啦","content":"啦啦啦啦啦邋遢","write_time":"1468523144","photo":null,"pic":[{"pictureurl":"{}"}],"like":[],"comment":[]},{"mid":"36","best":"0","type":"1","userid":"609","name":"啦啦啦","title":"啦啦啦啦啦啦啦啦啦啦啦","content":"啦啦啦啦啦啦","write_time":"1468523052","photo":null,"pic":[{"pictureurl":"{}"}],"like":[],"comment":[]},{"mid":"35","best":"0","type":"1","userid":"599","name":"w","title":"提子不错","content":"","write_time":"1468522900","photo":"1234.png","pic":[{"pictureurl":"as"}],"like":[],"comment":[]},{"mid":"34","best":"0","type":"1","userid":"599","name":"w","title":"提子不错","content":"","write_time":"1468522886","photo":"1234.png","pic":[{"pictureurl":"as"}],"like":[],"comment":[]},{"mid":"33","best":"0","type":"1","userid":"599","name":"w","title":"提子不错","content":"","write_time":"1468522883","photo":"1234.png","pic":[{"pictureurl":"as"}],"like":[],"comment":[]},{"mid":"32","best":"0","type":"1","userid":"599","name":"w","title":"提子不错","content":"","write_time":"1468522866","photo":"1234.png","pic":[{"pictureurl":"{1"},{"pictureurl":"2"},{"pictureurl":"3}"}],"like":[],"comment":[]}]
     */

    private String status;
    /**
     * mid : 38
     * best : 0
     * type : 1
     * userid : 609
     * name : 啦啦啦
     * title : 咔咔咔拉进来啦啦
     * content : 啦啦来啦力量力量力量力量力
     * write_time : 1468545276
     * photo : null
     * pic : [{"pictureurl":"{}"}]
     * like : []
     * comment : []
     */

    private List<DataBean> data;

    protected Mine_Post_bean(Parcel in) {
        status = in.readString();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<Mine_Post_bean> CREATOR = new Creator<Mine_Post_bean>() {
        @Override
        public Mine_Post_bean createFromParcel(Parcel in) {
            return new Mine_Post_bean(in);
        }

        @Override
        public Mine_Post_bean[] newArray(int size) {
            return new Mine_Post_bean[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeTypedList(data);
    }

    public static class DataBean implements Parcelable {
        private String mid;
        private String best;
        private String type;
        private String userid;
        private String name;
        private String title;
        private String content;
        private String write_time;
        private Object photo;
        /**
         * pictureurl : {}
         */

        private List<PicBean> pic;
        private List<?> like;
        private List<?> comment;

        protected DataBean(Parcel in) {
            mid = in.readString();
            best = in.readString();
            type = in.readString();
            userid = in.readString();
            name = in.readString();
            title = in.readString();
            content = in.readString();
            write_time = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getBest() {
            return best;
        }

        public void setBest(String best) {
            this.best = best;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getWrite_time() {
            return write_time;
        }

        public void setWrite_time(String write_time) {
            this.write_time = write_time;
        }

        public Object getPhoto() {
            return photo;
        }

        public void setPhoto(Object photo) {
            this.photo = photo;
        }

        public List<PicBean> getPic() {
            return pic;
        }

        public void setPic(List<PicBean> pic) {
            this.pic = pic;
        }

        public List<?> getLike() {
            return like;
        }

        public void setLike(List<?> like) {
            this.like = like;
        }

        public List<?> getComment() {
            return comment;
        }

        public void setComment(List<?> comment) {
            this.comment = comment;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mid);
            dest.writeString(best);
            dest.writeString(type);
            dest.writeString(userid);
            dest.writeString(name);
            dest.writeString(title);
            dest.writeString(content);
            dest.writeString(write_time);
        }

        public static class PicBean {
            private String pictureurl;

            public String getPictureurl() {
                return pictureurl;
            }

            public void setPictureurl(String pictureurl) {
                this.pictureurl = pictureurl;
            }
        }
    }
}
