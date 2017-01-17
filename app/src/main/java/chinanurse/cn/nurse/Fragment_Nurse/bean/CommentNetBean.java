package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2017/1/1.
 */

public class CommentNetBean implements Serializable {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<CommentBean> data;

    @Override
    public String toString() {
        return "CommentNetBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
