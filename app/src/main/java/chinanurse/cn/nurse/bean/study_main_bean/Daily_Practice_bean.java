package chinanurse.cn.nurse.bean.study_main_bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/23.
 */
public class Daily_Practice_bean {

    /**
     * data : [{"childlist":[{"name":"初级","count":"100","term_id":"41"},{"name":"专业","count":"100","term_id":"40"}],"haschild":1,"name":"主管护师","count":"100","term_id":"25"}]
     * status : success
     */
    private List<DataEntity> data;
    private String status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public static class DataEntity implements Serializable{
        /**
         * childlist : [{"name":"初级","count":"100","term_id":"41"},{"name":"专业","count":"100","term_id":"40"}]
         * haschild : 1
         * name : 主管护师
         * count : 100
         * term_id : 25
         */
        private List<ChildlistEntity> childlist;
        private int haschild;
        private String name;
        private String count;
        private String term_id;
        private String exam_time;

        public String getExam_time() {
            return exam_time;
        }

        public void setExam_time(String exam_time) {
            this.exam_time = exam_time;
        }

        public void setChildlist(List<ChildlistEntity> childlist) {
            this.childlist = childlist;
        }

        public void setHaschild(int haschild) {
            this.haschild = haschild;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public void setTerm_id(String term_id) {
            this.term_id = term_id;
        }

        public List<ChildlistEntity> getChildlist() {
            return childlist;
        }

        public int getHaschild() {
            return haschild;
        }

        public String getName() {
            return name;
        }

        public String getCount() {
            return count;
        }

        public String getTerm_id() {
            return term_id;
        }

        public static class ChildlistEntity implements Serializable{
            /**
             * name : 初级
             * count : 100
             * term_id : 41
             */
            private String name;
            private String count;
            private String term_id;
            private String exam_time;

            public String getExam_time() {
                return exam_time;
            }

            public void setExam_time(String exam_time) {
                this.exam_time = exam_time;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public void setTerm_id(String term_id) {
                this.term_id = term_id;
            }

            public String getName() {
                return name;
            }

            public String getCount() {
                return count;
            }

            public String getTerm_id() {
                return term_id;
            }
        }
    }
}
