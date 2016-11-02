package chinanurse.cn.nurse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class MyCollect_bean implements Serializable {


    /**
     * status : success
     * data : [{"id":"773","userid":"609","title":"好消息：河南晋职称，再也不用考外语了！","description":"河南晋职称也不用考外语了！","type":"1","object_id":"477","createtime":"1472633319","term_id":"33","term_name":"护理头条","post_title":"好消息：河南晋职称，再也不用考外语了！","post_excerpt":"河南晋职称也不用考外语了！","post_date":"2016-08-31 16:33:28","post_source":"河南日报","post_like":"0","post_hits":"604","recommended":"0","smeta":"{\"thumb\":\"\",\"photo\":[{\"url\":\"20160831\\/57c69652b5b80.jpg\",\"alt\":\"3\"}]}"}]
     */

    private String status;
    /**
     * id : 773
     * userid : 609
     * title : 好消息：河南晋职称，再也不用考外语了！
     * description : 河南晋职称也不用考外语了！
     * type : 1
     * object_id : 477
     * createtime : 1472633319
     * term_id : 33
     * term_name : 护理头条
     * post_title : 好消息：河南晋职称，再也不用考外语了！
     * post_excerpt : 河南晋职称也不用考外语了！
     * post_date : 2016-08-31 16:33:28
     * post_source : 河南日报
     * post_like : 0
     * post_hits : 604
     * recommended : 0
     * smeta : {"thumb":"","photo":[{"url":"20160831\/57c69652b5b80.jpg","alt":"3"}]}
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
        private String id;
        private String userid;
        private String title;
        private String description;
        private String type;
        private String object_id;
        private String createtime;
        private String term_id;
        private String term_name;
        private String post_title;
        private String post_excerpt;
        private String post_date;
        private String post_source;
        private String post_like;
        private String post_hits;
        private String recommended;
        private String smeta;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
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
    }
}
