package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2017/1/9.
 */

public class MessageBean implements Serializable {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;

    public List<MessageDataBean> getData() {
        return data;
    }

    public void setData(List<MessageDataBean> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<MessageDataBean> data;

    @Override
    public String toString() {
        return "MessageBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
