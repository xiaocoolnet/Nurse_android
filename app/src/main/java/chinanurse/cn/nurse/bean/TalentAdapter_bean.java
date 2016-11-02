package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class TalentAdapter_bean {

    /**
     * status : success
     * data : [{"id":"3","companyname":"北京儿童医院","companyinfo":"北京儿童医院是三甲医院，待遇优厚","photo":"","title":"企业高薪招聘儿科护士主管","jobtype":"7","experience":"1-3","education":"本科及以上","certificate":"护士证","address":"北京市朝阳区","salary":"11000-13000","welfare":"五险一金","count":"10","description":"任职要求：\r\n\r\n1、专科以上学历，计算机相关专业，1年以上php开发经验；\r\n\r\n2、精通PHP+Mysql、Ajax等相关开发，熟悉WINDOWS、LINUX、UNIX等操作系统，熟悉Apache、IIS、ZEND等应用；\r\n\r\n3、精通Linux，熟悉Apache、MySQL、PHP等软件的编译安装和最优化配置；\r\n\r\n4、熟练掌握Mysql数据库，精通Sql，熟练掌握Linux操作系统，了解主流应用（如Adodb、Smarty、ThinkPHP、WebService等）；    \r\n\r\n5、熟练掌握MySQL数据库应用，具备数据库应用系统的规划及设计能力。","linkman":"赵经理","phone":"18653503688","state":"1","jobtypename":""}]
     */

    private String status;
    /**
     * id : 3
     * companyname : 北京儿童医院
     * companyinfo : 北京儿童医院是三甲医院，待遇优厚
     * photo :
     * title : 企业高薪招聘儿科护士主管
     * jobtype : 7
     * experience : 1-3
     * education : 本科及以上
     * certificate : 护士证
     * address : 北京市朝阳区
     * salary : 11000-13000
     * welfare : 五险一金
     * count : 10
     * description : 任职要求：

     1、专科以上学历，计算机相关专业，1年以上php开发经验；

     2、精通PHP+Mysql、Ajax等相关开发，熟悉WINDOWS、LINUX、UNIX等操作系统，熟悉Apache、IIS、ZEND等应用；

     3、精通Linux，熟悉Apache、MySQL、PHP等软件的编译安装和最优化配置；

     4、熟练掌握Mysql数据库，精通Sql，熟练掌握Linux操作系统，了解主流应用（如Adodb、Smarty、ThinkPHP、WebService等）；

     5、熟练掌握MySQL数据库应用，具备数据库应用系统的规划及设计能力。
     * linkman : 赵经理
     * phone : 18653503688
     * state : 1
     * jobtypename :
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
        private String companyname;
        private String companyinfo;
        private String photo;
        private String title;
        private String jobtype;
        private String experience;
        private String education;
        private String certificate;
        private String address;
        private String salary;
        private String welfare;
        private String count;
        private String description;
        private String linkman;
        private String phone;
        private String state;
        private String jobtypename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getJobtype() {
            return jobtype;
        }

        public void setJobtype(String jobtype) {
            this.jobtype = jobtype;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getWelfare() {
            return welfare;
        }

        public void setWelfare(String welfare) {
            this.welfare = welfare;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getJobtypename() {
            return jobtypename;
        }

        public void setJobtypename(String jobtypename) {
            this.jobtypename = jobtypename;
        }
    }
}
