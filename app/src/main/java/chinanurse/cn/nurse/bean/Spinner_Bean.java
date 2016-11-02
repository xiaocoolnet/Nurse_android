package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class Spinner_Bean {
    /**
     * status : success
     * data : [{"id":"10","name":"护士长"},{"id":"11","name":"护士"},{"id":"12","name":"男护士"},{"id":"13","name":"女护士"}]
     */

    private String status;
    /**
     * id : 10
     * name : 护士长
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

    public static class DataBean {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
