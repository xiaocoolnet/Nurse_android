package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class Scroe_bean {

    /**
     * status : success
     * data : [{"userid":"609","event":"第一次点赞","score":"20","create_time":"1472279217"}]
     */

    private String status;
    /**
     * userid : 609
     * event : 第一次点赞
     * score : 20
     * create_time : 1472279217
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
        private String userid;
        private String event;
        private String score;
        private String create_time;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
