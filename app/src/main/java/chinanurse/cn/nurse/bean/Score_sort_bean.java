package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class Score_sort_bean {

    /**
     * status : success
     * data : [{"id":"639","name":"我的昵称","score":"146","photo":"avatar20160827160443639.png","time":"1471762340"}]
     */

    private String status;
    /**
     * id : 639
     * name : 我的昵称
     * score : 146
     * photo : avatar20160827160443639.png
     * time : 1471762340
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

    public static class DataBean {
        private String id;
        private String name;
        private String score;
        private String photo;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
