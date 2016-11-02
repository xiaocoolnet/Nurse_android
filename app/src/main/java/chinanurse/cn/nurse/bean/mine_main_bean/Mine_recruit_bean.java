package chinanurse.cn.nurse.bean.mine_main_bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/16 0016.
 */


//用 招聘中 简历的接口生成的bean容器，后期要改
public class Mine_recruit_bean {


    private String status;
    /**
     * id : 4
     * userid : 1
     * name :
     * sex : 0
     * avatar :
     * birthday : 0000-00-00
     * experience : 0
     * education :
     * certificate :
     * wantposition : 0
     * title :
     * address :
     * currentsalary :
     * jobstate :
     * description :
     * email :
     * phone :
     * hiredate : 0
     * wantcity :
     * wantsalary :
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
        private String name;
        private String sex;
        private String avatar;
        private String birthday;
        private String experience;
        private String education;
        private String certificate;
        private String wantposition;
        private String title;
        private String address;
        private String currentsalary;
        private String jobstate;
        private String description;
        private String email;
        private String phone;
        private String hiredate;
        private String wantcity;
        private String wantsalary;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getWantposition() {
            return wantposition;
        }

        public void setWantposition(String wantposition) {
            this.wantposition = wantposition;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCurrentsalary() {
            return currentsalary;
        }

        public void setCurrentsalary(String currentsalary) {
            this.currentsalary = currentsalary;
        }

        public String getJobstate() {
            return jobstate;
        }

        public void setJobstate(String jobstate) {
            this.jobstate = jobstate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHiredate() {
            return hiredate;
        }

        public void setHiredate(String hiredate) {
            this.hiredate = hiredate;
        }

        public String getWantcity() {
            return wantcity;
        }

        public void setWantcity(String wantcity) {
            this.wantcity = wantcity;
        }

        public String getWantsalary() {
            return wantsalary;
        }

        public void setWantsalary(String wantsalary) {
            this.wantsalary = wantsalary;
        }
    }
}
