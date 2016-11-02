package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20 0020.
 */

public class News_bean {

    /**
     * status : success
     * data : [{"id":"90","userid":"609","refid":"689","create_time":"1476953294","status":"1"}]
     */

    private String status;
    /**
     * id : 90
     * userid : 609
     * refid : 689
     * create_time : 1476953294
     * status : 1
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
        private String userid;
        private String refid;
        private String create_time;
        private String status;

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

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
