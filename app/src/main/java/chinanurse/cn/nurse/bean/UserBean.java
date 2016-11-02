package chinanurse.cn.nurse.bean;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by Administrator on 2016/6/27.
 */
public class UserBean{

    /**
     * status : success
     * data : {"userid":"578","usertype":"0","phone":"18653503680","devicestate":"0","password":"###977f0978d33e16cf20d2eac1b4f5168f"}
     */
    /**
     * userid : 578
     * usertype : 0
     * phone : 18653503680
     * devicestate : 0
     * password : ###977f0978d33e16cf20d2eac1b4f5168f
     */
    private String userid;
    private String usertype;
    private String phone;
    private String devicestate;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private String name;
    private String city;
    private String email;
    private String qq;
    private String weixin;
    private String photo;
    private String school;
    private String major;
    private String education;
    private String level;
    private String time;
    private String score;
    private String fanscount;
    private String money;
    private String sex;
    private String realname;
    private String birthday;
    private String address;
    private String followcount;

    public String getFollowcount() {
        if("".equals(mySharedPreferences.getString("Followcount","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Followcount","").toString();
    }

    public void setFollowcount(String followcount) {
        editor.putString("Followcount",followcount);
        editor.commit();
        this.followcount = followcount;
    }

    public UserBean(Context context) {
        this.context = context;
        mySharedPreferences= context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
    }
    public String getUserid() {
        if("".equals(mySharedPreferences.getString("Userid", "").toString())&&mySharedPreferences.getString("Userid","").toString() != null){
            return "";
        }
        return mySharedPreferences.getString("Userid","").toString();
    }
    public void setUserid(String userid) {
        editor.putString("Userid",userid);
        editor.commit();
        this.userid = userid;
    }

    public void removeUserid(){
        editor.remove("Userid");
        editor.remove("Usertype");
        editor.commit();
    }
    public String getAddress() {
        if("".equals(mySharedPreferences.getString("Address","").toString())&&mySharedPreferences.getString("Address","").toString() != null){
            return "";
        }
        return mySharedPreferences.getString("Address", "");
    }

    public void setAddress(String address) {
        editor.putString("Address",address);
        editor.commit();
        this.address = address;
    }
    public String getUsertype() {
        if("".equals(mySharedPreferences.getString("Usertype","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Usertype", "");
    }

    public void setUsertype(String usertype) {
        editor.putString("Usertype",usertype);
        editor.commit();
        this.usertype = usertype;
    }
    public String getName() {
        if("".equals(mySharedPreferences.getString("Name","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Name","");
    }

    public void setName(String name) {
        editor.putString("Name",name);
        editor.commit();
        this.name = name;
    }

    public String getDevicestate() {
        if("".equals(mySharedPreferences.getString("Devicestate","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Devicestate","");
    }

    public void setDevicestate(String devicestate) {
        editor.putString("Devicestate",devicestate);
        editor.commit();
        this.devicestate = devicestate;
    }
    public String getPhone() {
        if("".equals(mySharedPreferences.getString("Phone","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Phone","");
    }

    public void setPhone(String phone) {
        editor.putString("Phone",phone);
        editor.commit();
        this.phone = phone;
    }
    public String getCity() {
        if("".equals(mySharedPreferences.getString("City","").toString())){
            return "";
        }
        return mySharedPreferences.getString("City","");
    }

    public void setCity(String city) {
        editor.putString("City",city);
        editor.commit();
        this.city = city;
    }
    public String getEmail() {
        if("".equals(mySharedPreferences.getString("Email","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Email","");
    }

    public void setEmail(String email) {
        editor.putString("Email",email);
        editor.commit();
        this.email = email;
    }
    public String getQq() {
        if("".equals(mySharedPreferences.getString("Qq","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Qq","");
    }

    public void setQq(String qq) {
        editor.putString("Qq",qq);
        editor.commit();
        this.qq = qq;
    }
    public String getWeixin() {
        if("".equals(mySharedPreferences.getString("Werixin","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Werixin","");
    }

    public void setWeixin(String weixin) {
        editor.putString("Werixin",weixin);
        editor.commit();
        this.weixin = weixin;
    }
    public String getPhoto() {
        if("".equals(mySharedPreferences.getString("Photo","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Photo","");
    }

    public void setPhoto(String phone) {
        editor.putString("Photo",phone);
        editor.commit();
        this.phone = phone;
    }
    public String getSchool() {
        if("".equals(mySharedPreferences.getString("School","").toString())){
            return "";
        }
        return mySharedPreferences.getString("School","");
    }

    public void setSchool(String school) {
        editor.putString("School",school);
        editor.commit();
        this.school = school;
    }
    public String getMajor() {
        if("".equals(mySharedPreferences.getString("Major","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Major","");
    }

    public void setMajor(String major) {
        editor.putString("Major",major);
        editor.commit();
        this.major = major;
    }
    public String getEducation() {
        if("".equals(mySharedPreferences.getString("Education","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Education","");
    }

    public void setEducation(String education) {
        editor.putString("Education",education);
        editor.commit();
        this.education = education;
    }public String getLevel() {
        if("".equals(mySharedPreferences.getString("Level","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Level","");
    }

    public void setLevel(String level) {
        editor.putString("Level",level);
        editor.commit();
        this.level = level;
    }public String getTime() {
        if("".equals(mySharedPreferences.getString("Time","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Time","");
    }

    public void setTime(String time) {
        editor.putString("Time",time);
        editor.commit();
        this.time = time;
    }
    public String getScore() {
        if("".equals(mySharedPreferences.getString("Score","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Score","");
    }

    public void setScore(String score) {
        editor.putString("Score",score);
        editor.commit();
        this.score = score;
    }
    public String getFanscount() {
        if("".equals(mySharedPreferences.getString("Fanscount","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Fanscount","");
    }

    public void setFanscount(String fanscount) {
        editor.putString("Fanscount",fanscount);
        editor.commit();
        this.fanscount = fanscount;
    }
    public String getMoney() {
        if("".equals(mySharedPreferences.getString("Money","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Money","");
    }

    public void setMoney(String money) {
        editor.putString("Money",money);
        editor.commit();
        this.money = money;
    }
    public String getSex() {
        if("".equals(mySharedPreferences.getString("Sex","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Sex","");
    }

    public void setSex(String sex) {
        editor.putString("Sex",sex);
        editor.commit();
        this.sex = sex;
    }
    public String getRealname() {
        if("".equals(mySharedPreferences.getString("Realname","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Realname","");
    }

    public void setRealname(String realname) {
        editor.putString("Realname",realname);
        editor.commit();
        this.realname = realname;
    }
    public String getBirthday() {
        if("".equals(mySharedPreferences.getString("Birthday","").toString())){
            return "";
        }
        return mySharedPreferences.getString("Birthday","");
    }

    public void setBirthday(String birthday) {
        editor.putString("Birthday",birthday);
        editor.commit();
        this.birthday = birthday;
    }
    /**
     * 第二次自动登录
     *
     * @return
     */
    public boolean isLogined() {
        if ("".equals(mySharedPreferences.getString("Userid",""))) {
            return false;
        }
        return true;
    }
}
