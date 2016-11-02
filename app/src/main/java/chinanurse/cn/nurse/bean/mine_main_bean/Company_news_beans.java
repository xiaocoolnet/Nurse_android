package chinanurse.cn.nurse.bean.mine_main_bean;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class Company_news_beans {

    /**
     * status : success
     * data : {"id":"1","userid":"603","create_time":"1470121000","companyname":"北京校酷","companyinfo":"北京校酷info","linkman":"朱总","phone":"123456","email":"123456@163.com","license":"证书","status":"1"}
     */

    private String status;
    /**
     * id : 1
     * userid : 603
     * create_time : 1470121000
     * companyname : 北京校酷
     * companyinfo : 北京校酷info
     * linkman : 朱总
     * phone : 123456
     * email : 123456@163.com
     * license : 证书
     * status : 1
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String userid;
        private String create_time;
        private String companyname;
        private String companyinfo;
        private String linkman;
        private String phone;
        private String email;
        private String license;
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public String getCompanyinfo() {
            return companyinfo;
        }

        public void setCompanyinfo(String companyinfo) {
            this.companyinfo = companyinfo;
        }

        public String getLinkman() {
            return linkman;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
