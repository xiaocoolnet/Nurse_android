package chinanurse.cn.nurse.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Nuser_community implements Parcelable {

    /**
     * data : [{"like":[{}],"name":"Qqqqqqqqqqqqqqqqq","write_time":"0","mid":"5","photo":null,"best":"0","comment":[{}],"pic":[{}],"type":"1","title":"666","userid":"607","content":"777"}]
     * status : success
     */
    private List<DataEntity> data;
    private String status;

    protected Nuser_community(Parcel in) {
        data = in.createTypedArrayList(DataEntity.CREATOR);
        status = in.readString();
    }

    public static final Creator<Nuser_community> CREATOR = new Creator<Nuser_community>() {
        @Override
        public Nuser_community createFromParcel(Parcel in) {
            return new Nuser_community(in);
        }

        @Override
        public Nuser_community[] newArray(int size) {
            return new Nuser_community[size];
        }
    };

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeString(status);
    }

    public static class DataEntity implements Parcelable {
        /**
         * like : [{}]
         * name : Qqqqqqqqqqqqqqqqq
         * write_time : 0
         * mid : 5
         * photo : null
         * best : 0
         * comment : [{}]
         * pic : [{}]
         * type : 1
         * title : 666
         * userid : 607
         * content : 777
         */
        private List<LikeEntity> like;
        private String name;
        private String write_time;
        private String mid;
        private String photo;
        private String best;
        private List<CommentEntity> comment;
        private List<PicEntity> pic;
        private String type;
        private String title;
        private String userid;
        private String content;
        private String titlelist;

        protected DataEntity(Parcel in) {
            name = in.readString();
            write_time = in.readString();
            mid = in.readString();
            photo = in.readString();
            best = in.readString();
            type = in.readString();
            title = in.readString();
            userid = in.readString();
            content = in.readString();
            titlelist = in.readString();
        }

        public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
            @Override
            public DataEntity createFromParcel(Parcel in) {
                return new DataEntity(in);
            }

            @Override
            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };

        public String getTitlelist() {
            return titlelist;
        }

        public void setTitlelist(String titlelist) {
            this.titlelist = titlelist;
        }

        public void setLike(List<LikeEntity> like) {
            this.like = like;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setWrite_time(String write_time) {
            this.write_time = write_time;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setBest(String best) {
            this.best = best;
        }

        public void setComment(List<CommentEntity> comment) {
            this.comment = comment;
        }

        public void setPic(List<PicEntity> pic) {
            this.pic = pic;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<LikeEntity> getLike() {
            return like;
        }

        public String getName() {
            return name;
        }

        public String getWrite_time() {
            return write_time;
        }

        public String getMid() {
            return mid;
        }

        public String getPhoto() {
            return photo;
        }

        public String getBest() {
            return best;
        }

        public List<CommentEntity> getComment() {
            return comment;
        }

        public List<PicEntity> getPic() {
            return pic;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getUserid() {
            return userid;
        }

        public String getContent() {
            return content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(write_time);
            dest.writeString(mid);
            dest.writeString(photo);
            dest.writeString(best);
            dest.writeString(type);
            dest.writeString(title);
            dest.writeString(userid);
            dest.writeString(content);
            dest.writeString(titlelist);
        }

        public class LikeEntity {
        }

        public class CommentEntity {
        }

        public class PicEntity {
        }
    }
}
