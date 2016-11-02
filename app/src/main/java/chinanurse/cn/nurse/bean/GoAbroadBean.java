package chinanurse.cn.nurse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class GoAbroadBean implements Serializable {


    /**
     * status : success
     * data : [{"object_id":"188","term_id":"33","term_name":"护理头条","post_title":"人民日报：病人越治越多，说明医学已入误区！","post_excerpt":"人民日报：病人越治越多，说明医学已入误区！","post_date":"2016-08-04 17:52:25","post_modified":"2016-08-04 17:48:16","post_source":"人民日报","post_like":"0","post_hits":"473","recommended":"0","smeta":"{\"thumb\":\"20160804\\/57a31054792c2.jpg\",\"photo\":[{\"url\":\"20160818\\/57b56707a6fa0.jpg\",\"alt\":\"\\u75c5\\u4eba\\u8d8a\\u6cbb\\u8d8a\\u591a1\"},{\"url\":\"20160818\\/57b567103893f.jpg\",\"alt\":\"\\u75c5\\u4eba\\u8d8a\\u6cbb\\u8d8a\\u591a2\"}]}","thumb":[{"url":"20160818/57b56707a6fa0.jpg","alt":"病人越治越多1"},{"url":"20160818/57b567103893f.jpg","alt":"病人越治越多2"},{"url":"20160804/57a31054792c2.jpg","alt":"缩略图"}],"likes":[{"userid":"619","avatar":"avatar20160818131237619.png","username":"中国护士网"},{"userid":"601","avatar":"avatar20160825112125601.png","username":"咩"}],"favorites":[{"userid":"601"}]}]
     */

    private String status;
    /**
     * object_id : 188
     * term_id : 33
     * term_name : 护理头条
     * post_title : 人民日报：病人越治越多，说明医学已入误区！
     * post_excerpt : 人民日报：病人越治越多，说明医学已入误区！
     * post_date : 2016-08-04 17:52:25
     * post_modified : 2016-08-04 17:48:16
     * post_source : 人民日报
     * post_like : 0
     * post_hits : 473
     * recommended : 0
     * smeta : {"thumb":"20160804\/57a31054792c2.jpg","photo":[{"url":"20160818\/57b56707a6fa0.jpg","alt":"\u75c5\u4eba\u8d8a\u6cbb\u8d8a\u591a1"},{"url":"20160818\/57b567103893f.jpg","alt":"\u75c5\u4eba\u8d8a\u6cbb\u8d8a\u591a2"}]}
     * thumb : [{"url":"20160818/57b56707a6fa0.jpg","alt":"病人越治越多1"},{"url":"20160818/57b567103893f.jpg","alt":"病人越治越多2"},{"url":"20160804/57a31054792c2.jpg","alt":"缩略图"}]
     * likes : [{"userid":"619","avatar":"avatar20160818131237619.png","username":"中国护士网"},{"userid":"601","avatar":"avatar20160825112125601.png","username":"咩"}]
     * favorites : [{"userid":"601"}]
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
        private String object_id;
        private String term_id;
        private String term_name;
        private String post_title;
        private String post_excerpt;
        private String post_date;
        private String post_modified;
        private String post_source;
        private String post_like;
        private String post_hits;
        private String recommended;
        private String smeta;
        /**
         * url : 20160818/57b56707a6fa0.jpg
         * alt : 病人越治越多1
         */

        private List<ThumbBean> thumb;
        /**
         * userid : 619
         * avatar : avatar20160818131237619.png
         * username : 中国护士网
         */

        private List<LikesBean> likes;
        /**
         * userid : 601
         */

        private List<FavoritesBean> favorites;

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

        public String getPost_title() {
            return post_title;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
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

        public String getPost_modified() {
            return post_modified;
        }

        public void setPost_modified(String post_modified) {
            this.post_modified = post_modified;
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
    }
}
