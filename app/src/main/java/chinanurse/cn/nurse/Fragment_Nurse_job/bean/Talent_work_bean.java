package chinanurse.cn.nurse.Fragment_Nurse_job.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class Talent_work_bean implements Serializable{

    /**
     * status : success
     * data : [{"id":"77","userid":"639","companyname":"高扬企业认证测试","companyinfo":"企业简介","photo":"","email":"458819795@qq.com","title":"222","jobtype":"护士长","experience":"","education":"中专","certificate":"","address":"北京市-北京市-朝阳区 222","salary":"面议","welfare":"五险一金","count":"5人","description":"222","linkman":"高扬","phone":"18363867129","state":"0","jobtypename":""}]
     */

    private String status;
    /**
     * id : 77
     * userid : 639
     * companyname : 高扬企业认证测试
     * companyinfo : 企业简介
     * photo :
     * email : 458819795@qq.com
     * title : 222
     * jobtype : 护士长
     * experience :
     * education : 中专
     * certificate :
     * address : 北京市-北京市-朝阳区 222
     * salary : 面议
     * welfare : 五险一金
     * count : 5人
     * description : 222
     * linkman : 高扬
     * phone : 18363867129
     * state : 0
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

    public static class DataBean implements Serializable{
        private String id;
        private String userid;
        private String companyname;
        private String companyinfo;
        private String photo;
        private String email;
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
        private String create_time;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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
