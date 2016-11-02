package chinanurse.cn.nurse.MinePage.MyNews;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class Mine_News_Bean {


    /**
     * status : success
     * data : [{"id":"3","title":"感谢来到平台的你","content":"感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你","photo":"./data/product_img/1.JPG","create_time":"0"},{"id":"2","title":"感谢来到平台的你","content":"感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你","photo":"./data/product_img/1.JPG","create_time":"0"},{"id":"1","title":"感谢来到平台的你","content":"感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你","photo":"./data/product_img/1.JPG","create_time":"0"}]
     */

    private String status;
    /**
     * id : 3
     * title : 感谢来到平台的你
     * content : 感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你感谢来到平台的你
     * photo : ./data/product_img/1.JPG
     * create_time : 0
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
        private String title;
        private String content;
        private String photo;
        private String create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
