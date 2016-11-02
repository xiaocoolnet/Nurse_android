package chinanurse.cn.nurse.WebView.webview_comments_bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class Webview_comments_bean {

    /**
     * status : success
     * data : [{"refid":"305","userid":"600","username":"q","content":"你好2232323243445345","photo":"1234.png","add_time":"1474359307","type":"1","userlevel":"1","major":"","cid":"134","child_comments":[{"pid":"134","userid":"600","username":"q","content":"我很好777788999","photo":"1234.png","add_time":"1474359404","type":"3","userlevel":"1","major":""}]}]
     */

    private String status;
    /**
     * refid : 305
     * userid : 600
     * username : q
     * content : 你好2232323243445345
     * photo : 1234.png
     * add_time : 1474359307
     * type : 1
     * userlevel : 1
     * major :
     * cid : 134
     * child_comments : [{"pid":"134","userid":"600","username":"q","content":"我很好777788999","photo":"1234.png","add_time":"1474359404","type":"3","userlevel":"1","major":""}]
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
        private String refid;
        private String userid;
        private String username;
        private String content;
        private String photo;
        private String add_time;
        private String type;
        private String userlevel;
        private String major;
        private String cid;
        /**
         * pid : 134
         * userid : 600
         * username : q
         * content : 我很好777788999
         * photo : 1234.png
         * add_time : 1474359404
         * type : 3
         * userlevel : 1
         * major :
         */

        private List<ChildCommentsBean> child_comments;

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

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

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public List<ChildCommentsBean> getChild_comments() {
            return child_comments;
        }

        public void setChild_comments(List<ChildCommentsBean> child_comments) {
            this.child_comments = child_comments;
        }

        public static class ChildCommentsBean {
            private String pid;
            private String userid;
            private String username;
            private String content;
            private String photo;
            private String add_time;
            private String type;
            private String userlevel;
            private String major;

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

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
