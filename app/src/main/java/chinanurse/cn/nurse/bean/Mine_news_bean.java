package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class Mine_news_bean {

    /**
     * status : success
     * data : [{"id":"170","userid":"609","content":"咔咔咔","create_time":"1479347398","status":"2","devicestate":"0","reply":[{"id":"15","userid":"609","title":"系统回复","fid":"170","content":"喀喀喀","create_time":"1479347416","status":"1"}]}]
     */

    private String status;
    /**
     * id : 170
     * userid : 609
     * content : 咔咔咔
     * create_time : 1479347398
     * status : 2
     * devicestate : 0
     * reply : [{"id":"15","userid":"609","title":"系统回复","fid":"170","content":"喀喀喀","create_time":"1479347416","status":"1"}]
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
        private String content;
        private String create_time;
        private String status;
        private String devicestate;
        /**
         * id : 15
         * userid : 609
         * title : 系统回复
         * fid : 170
         * content : 喀喀喀
         * create_time : 1479347416
         * status : 1
         */

        private List<ReplyBean> reply;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getDevicestate() {
            return devicestate;
        }

        public void setDevicestate(String devicestate) {
            this.devicestate = devicestate;
        }

        public List<ReplyBean> getReply() {
            return reply;
        }

        public void setReply(List<ReplyBean> reply) {
            this.reply = reply;
        }

        public static class ReplyBean {
            private String id;
            private String userid;
            private String title;
            private String fid;
            private String content;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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
}
