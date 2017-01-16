package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2016/12/16.
 * 圈子
 */

public class CommunityNetBean implements Serializable {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;

    public List<CommunityBean> getData() {
        return data;
    }

    public void setData(List<CommunityBean> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<CommunityBean> data;

    @Override
    public String toString() {
        return "CommunityNetBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
