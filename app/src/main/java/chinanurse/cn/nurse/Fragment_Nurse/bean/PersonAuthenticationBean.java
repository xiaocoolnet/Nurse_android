package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2016/12/16.
 * 个人认证
 */

public class PersonAuthenticationBean implements Serializable {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<DataBean> data;

    public class DataBean implements Serializable {

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        private String userId;// ID ?

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;//认证类型

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        private String company;//认证单位

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        private String department;//工作科室

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        private String photo;// 证件照

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String status;//认证状态 1通过，0拒绝，2认证中

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long creatTime) {
            this.creatTime = creatTime;
        }

        private long creatTime;//创建时间 ?

        @Override
        public String toString() {
            return "DataBean{" +
                    "userId='" + userId + '\'' +
                    ", type='" + type + '\'' +
                    ", company='" + company + '\'' +
                    ", department='" + department + '\'' +
                    ", photo='" + photo + '\'' +
                    ", status='" + status + '\'' +
                    ", creatTime=" + creatTime +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PersonAuthenticationBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
