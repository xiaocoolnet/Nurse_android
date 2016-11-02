package chinanurse.cn.nurse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzh on 2016/6/25.
 */
public class NurseEmployBean implements Serializable{


    /**
     * status : success
     * data : [{"id":"3","userid":"1","photo":"","companyname":"北京儿童医院","companyinfo":"北京儿童医院是三甲医院，待遇优厚","title":"企业高薪招聘儿科护士主管","experience":"1-3","jobtype":"7","education":"本科及以上","certificate":"护士证","address":"北京市朝阳区","salary":"11000-13000","description":"任职要求：\r\n\r\n1、专科以上学历，计算机相关专业，1年以上php开发经验；\r\n\r\n2、精通PHP+Mysql、Ajax等相关开发，熟悉WINDOWS、LINUX、UNIX等操作系统，熟悉Apache、IIS、ZEND等应用；\r\n\r\n3、精通Linux，熟悉Apache、MySQL、PHP等软件的编译安装和最优化配置；\r\n\r\n4、熟练掌握Mysql数据库，精通Sql，熟练掌握Linux操作系统，了解主流应用（如Adodb、Smarty、ThinkPHP、WebService等）；    \r\n\r\n5、熟练掌握MySQL数据库应用，具备数据库应用系统的规划及设计能力。","welfare":"五险一金","count":"10","linkman":"赵经理","phone":"18653503688","email":"","listorder":"0","create_time":"1466146169","state":"1"}]
     */

    private String status;
    /**
     * id : 3
     * userid : 1
     * photo :
     * companyname : 北京儿童医院
     * companyinfo : 北京儿童医院是三甲医院，待遇优厚
     * title : 企业高薪招聘儿科护士主管
     * experience : 1-3
     * jobtype : 7
     * education : 本科及以上
     * certificate : 护士证
     * address : 北京市朝阳区
     * salary : 11000-13000
     * description : 任职要求：

     1、专科以上学历，计算机相关专业，1年以上php开发经验；

     2、精通PHP+Mysql、Ajax等相关开发，熟悉WINDOWS、LINUX、UNIX等操作系统，熟悉Apache、IIS、ZEND等应用；

     3、精通Linux，熟悉Apache、MySQL、PHP等软件的编译安装和最优化配置；

     4、熟练掌握Mysql数据库，精通Sql，熟练掌握Linux操作系统，了解主流应用（如Adodb、Smarty、ThinkPHP、WebService等）；

     5、熟练掌握MySQL数据库应用，具备数据库应用系统的规划及设计能力。
     * welfare : 五险一金
     * count : 10
     * linkman : 赵经理
     * phone : 18653503688
     * email :
     * listorder : 0
     * create_time : 1466146169
     * state : 1
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

    public static class DataBean  implements Serializable{
        private String id;
        private String companyid;
        private String userid;
        private String photo;
        private String companyname;
        private String companyinfo;
        private String title;
        private String experience;
        private String jobtype;
        private String education;
        private String certificate;
        private String address;
        private String salary;
        private String description;
        private String welfare;
        private String count;
        private String linkman;
        private String phone;
        private String email;
        private String listorder;
        private String create_time;
        private String state;
        private int hits;

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public String getCompanyid() {
            return companyid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getJobtype() {
            return jobtype;
        }

        public void setJobtype(String jobtype) {
            this.jobtype = jobtype;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getListorder() {
            return listorder;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
