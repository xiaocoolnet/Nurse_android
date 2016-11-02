package chinanurse.cn.nurse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class News_list_type implements Serializable{
    /**
     * status : success
     * data : [{"object_id":"480","term_id":"35","term_name":"热门图片","post_title":"男性液体进入女人体内的全过程\u201c生命\u201d太震撼了\u2026","post_excerpt":"男性液体进入女人体内的全过程\u201c生命\u201d太震撼了\u2026","post_date":"2016-09-02 15:08:05","post_modified":"2016-09-02 14:56:18","post_source":"护理界","post_like":"0","post_hits":"753","recommended":"0","smeta":"{\"thumb\":\"\",\"photo\":[{\"url\":\"20160902\\/57c9253c510cc.jpg\",\"alt\":\"2\"},{\"url\":\"20160902\\/57c92547b23a9.jpg\",\"alt\":\"7_\\u526f\\u672c\"},{\"url\":\"20160902\\/57c9255274ee9.jpg\",\"alt\":\"9_\\u526f\\u672c\"}]}","thumb":[{"url":"20160902/57c9253c510cc.jpg","alt":"2"},{"url":"20160902/57c92547b23a9.jpg","alt":"7_副本"},{"url":"20160902/57c9255274ee9.jpg","alt":"9_副本"}],"likes":[{"userid":"619","avatar":"avatar20160818131237619.png","username":"中国护士网"},{"userid":"608","avatar":"avatar20160831160520608.png","username":"张伟利"},{"userid":"609","avatar":"avatar6091470217808610.jpg","username":"22222"}],"favorites":[{"userid":"608"}],"comments":[{"userid":"608","username":"张伟利","content":"写的很好","photo":"avatar20160831160520608.png","add_time":"1472821457","type":"1","userlevel":"1","major":"心理学"}]}]
     */

    private String status;
    /**
     * object_id : 480
     * term_id : 35
     * term_name : 热门图片
     * post_title : 男性液体进入女人体内的全过程“生命”太震撼了…
     * post_excerpt : 男性液体进入女人体内的全过程“生命”太震撼了…
     * post_date : 2016-09-02 15:08:05
     * post_modified : 2016-09-02 14:56:18
     * post_source : 护理界
     * post_like : 0
     * post_hits : 753
     * recommended : 0
     * smeta : {"thumb":"","photo":[{"url":"20160902\/57c9253c510cc.jpg","alt":"2"},{"url":"20160902\/57c92547b23a9.jpg","alt":"7_\u526f\u672c"},{"url":"20160902\/57c9255274ee9.jpg","alt":"9_\u526f\u672c"}]}
     * thumb : [{"url":"20160902/57c9253c510cc.jpg","alt":"2"},{"url":"20160902/57c92547b23a9.jpg","alt":"7_副本"},{"url":"20160902/57c9255274ee9.jpg","alt":"9_副本"}]
     * likes : [{"userid":"619","avatar":"avatar20160818131237619.png","username":"中国护士网"},{"userid":"608","avatar":"avatar20160831160520608.png","username":"张伟利"},{"userid":"609","avatar":"avatar6091470217808610.jpg","username":"22222"}]
     * favorites : [{"userid":"608"}]
     * comments : [{"userid":"608","username":"张伟利","content":"写的很好","photo":"avatar20160831160520608.png","add_time":"1472821457","type":"1","userlevel":"1","major":"心理学"}]
     */

    private List<DataBean> data;

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

    public static class DataBean implements Serializable{
        private String istop;
        private String post_hits;
        private String object_id;
        private String post_like;
        private String term_id;
        private String recommended;
        private String post_title;
        private String term_name;
        private String post_source;
        private String post_excerpt;
        private String smeta;
        private String post_date;
        private String message_id;
        private String post_modified;
        private String isread;

        public String getPost_modified() {
            return post_modified;
        }

        public void setPost_modified(String post_modified) {
            this.post_modified = post_modified;
        }

        public String getIsread() {
            return isread;
        }

        public void setIsread(String isread) {
            this.isread = isread;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }
        /**
         * url : 20160902/57c9253c510cc.jpg
         * alt : 2
         */

        private List<ThumbBean> thumb;
        /**
         * userid : 619
         * avatar : avatar20160818131237619.png
         * username : 中国护士网
         */

        private List<LikesBean> likes;
        /**
         * userid : 608
         */

        private List<FavoritesBean> favorites;
        /**
         * userid : 608
         * username : 张伟利
         * content : 写的很好
         * photo : avatar20160831160520608.png
         * add_time : 1472821457
         * type : 1
         * userlevel : 1
         * major : 心理学
         */

        private List<CommentsBean> comments;

        public String getPost_title() {
            return post_title;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getTerm_id() {
            return term_id;
        }

        public void setTerm_id(String term_id) {
            this.term_id = term_id;
        }

        public String getTerm_name() {
            return term_name;
        }

        public void setTerm_name(String term_name) {
            this.term_name = term_name;
        }

        public String getIstop() {
            return istop;
        }

        public void setIstop(String istop) {
            this.istop = istop;
        }

        public String getPost_excerpt() {
            return post_excerpt;
        }

        public void setPost_excerpt(String post_excerpt) {
            this.post_excerpt = post_excerpt;
        }

        public String getPost_date() {
            return post_date;
        }

        public void setPost_date(String post_date) {
            this.post_date = post_date;
        }

        public String getPost_source() {
            return post_source;
        }

        public void setPost_source(String post_source) {
            this.post_source = post_source;
        }

        public String getPost_like() {
            return post_like;
        }

        public void setPost_like(String post_like) {
            this.post_like = post_like;
        }

        public String getPost_hits() {
            return post_hits;
        }

        public void setPost_hits(String post_hits) {
            this.post_hits = post_hits;
        }

        public String getRecommended() {
            return recommended;
        }

        public void setRecommended(String recommended) {
            this.recommended = recommended;
        }

        public String getSmeta() {
            return smeta;
        }

        public void setSmeta(String smeta) {
            this.smeta = smeta;
        }

        public List<ThumbBean> getThumb() {
            return thumb;
        }

        public void setThumb(List<ThumbBean> thumb) {
            this.thumb = thumb;
        }

        public List<LikesBean> getLikes() {
            return likes;
        }

        public void setLikes(List<LikesBean> likes) {
            this.likes = likes;
        }

        public List<FavoritesBean> getFavorites() {
            return favorites;
        }

        public void setFavorites(List<FavoritesBean> favorites) {
            this.favorites = favorites;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class ThumbBean implements Serializable{
            private String url;
            private String alt;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getAlt() {
                return alt;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }
        }

        public static class LikesBean implements Serializable{
            private String userid;
            private String avatar;
            private String username;

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class FavoritesBean implements Serializable{
            private String userid;

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }
        }

        public static class CommentsBean implements Serializable{
            private String userid;
            private String username;
            private String content;
            private String photo;
            private String add_time;
            private String type;
            private String userlevel;
            private String major;

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUserlevel() {
                return userlevel;
            }

            public void setUserlevel(String userlevel) {
                this.userlevel = userlevel;
            }

            public String getMajor() {
                return major;
            }

            public void setMajor(String major) {
                this.major = major;
            }
        }
    }
}
