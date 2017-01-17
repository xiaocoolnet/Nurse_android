package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhuchongkun on 2016/12/16.
 * 帖子
 */

public class ForumBean implements Serializable {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;


    public List<ForumDataBean> getData() {
        return data;
    }

    public void setData(List<ForumDataBean> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<ForumDataBean> data;

    @Override
    public String toString() {
        return "ForumBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
