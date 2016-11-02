package chinanurse.cn.nurse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzh on 2016/6/26.
 */
public class NurseEmployTalentBean implements Serializable{


    /**
     * status : success
     * data : [{"id":"119","userid":"609","name":"李梅青","sex":"0","avatar":"","experience":"3-5","birthday":"1991-07-23","education":"大专","certificate":"中级主管","wantposition":"护理部主任","title":"","address":"山东省-烟台市-芝罘区","currentsalary":"5000-8000","jobstate":"离职","welfare":"","count":"","description":"这个挺好","email":"lliuy@163.com","phone":"15762218382","hiredate":"立即到岗","wantcity":"山东省-烟台市","wantsalary":"8000-10000","create_time":"1476842480","state":"0"}]
     */

    private String status;
    /**
     * id : 119
     * userid : 609
     * name : 李梅青
     * sex : 0
     * avatar :
     * experience : 3-5
     * birthday : 1991-07-23
     * education : 大专
     * certificate : 中级主管
     * wantposition : 护理部主任
     * title :
     * address : 山东省-烟台市-芝罘区
     * currentsalary : 5000-8000
     * jobstate : 离职
     * welfare :
     * count :
     * description : 这个挺好
     * email : lliuy@163.com
     * phone : 15762218382
     * hiredate : 立即到岗
     * wantcity : 山东省-烟台市
     * wantsalary : 8000-10000
     * create_time : 1476842480
     * state : 0
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
        private String experience;
        private String birthday;
        private String education;
        private String certificate;
        private String wantposition;
        private String title;
        private String address;
        private String currentsalary;
        private String jobstate;
        private String welfare;
        private String count;
        private String description;
        private String email;
        private String phone;
        private String hiredate;
        private String wantcity;
        private String wantsalary;
        private String create_time;
        private String state;

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

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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
