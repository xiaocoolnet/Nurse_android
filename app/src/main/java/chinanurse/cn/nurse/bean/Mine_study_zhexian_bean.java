package chinanurse.cn.nurse.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class Mine_study_zhexian_bean implements Serializable{


    /**
     * status : success
     * data : {"rate_1":0,"create_time_1":1477065600,"rate_2":0,"create_time_2":1477152000,"rate_3":0,"create_time_3":1477238400,"rate_4":0,"create_time_4":1477324800,"rate_5":0,"create_time_5":1477411200,"rate_6":0,"create_time_6":1477497600,"rate_7":13.333333333333,"create_time_7":1477584000}
     */

    private String status;
    /**
     * rate_1 : 0
     * create_time_1 : 1477065600
     * rate_2 : 0
     * create_time_2 : 1477152000
     * rate_3 : 0
     * create_time_3 : 1477238400
     * rate_4 : 0
     * create_time_4 : 1477324800
     * rate_5 : 0
     * create_time_5 : 1477411200
     * rate_6 : 0
     * create_time_6 : 1477497600
     * rate_7 : 13.333333333333
     * create_time_7 : 1477584000
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int rate_1;
        private int create_time_1;
        private int rate_2;
        private int create_time_2;
        private int rate_3;
        private int create_time_3;
        private int rate_4;
        private int create_time_4;
        private int rate_5;
        private int create_time_5;
        private int rate_6;
        private int create_time_6;
        private int rate_7;
        private int create_time_7;

        public int getRate_1() {
            return rate_1;
        }

        public void setRate_1(int rate_1) {
            this.rate_1 = rate_1;
        }

        public int getCreate_time_1() {
            return create_time_1;
        }

        public void setCreate_time_1(int create_time_1) {
            this.create_time_1 = create_time_1;
        }

        public int getRate_2() {
            return rate_2;
        }

        public void setRate_2(int rate_2) {
            this.rate_2 = rate_2;
        }

        public int getCreate_time_2() {
            return create_time_2;
        }

        public void setCreate_time_2(int create_time_2) {
            this.create_time_2 = create_time_2;
        }

        public int getRate_3() {
            return rate_3;
        }

        public void setRate_3(int rate_3) {
            this.rate_3 = rate_3;
        }

        public int getCreate_time_3() {
            return create_time_3;
        }

        public void setCreate_time_3(int create_time_3) {
            this.create_time_3 = create_time_3;
        }

        public int getRate_4() {
            return rate_4;
        }

        public void setRate_4(int rate_4) {
            this.rate_4 = rate_4;
        }

        public int getCreate_time_4() {
            return create_time_4;
        }

        public void setCreate_time_4(int create_time_4) {
            this.create_time_4 = create_time_4;
        }

        public int getRate_5() {
            return rate_5;
        }

        public void setRate_5(int rate_5) {
            this.rate_5 = rate_5;
        }

        public int getCreate_time_5() {
            return create_time_5;
        }

        public void setCreate_time_5(int create_time_5) {
            this.create_time_5 = create_time_5;
        }

        public int getRate_6() {
            return rate_6;
        }

        public void setRate_6(int rate_6) {
            this.rate_6 = rate_6;
        }

        public int getCreate_time_6() {
            return create_time_6;
        }

        public void setCreate_time_6(int create_time_6) {
            this.create_time_6 = create_time_6;
        }

        public int getRate_7() {
            return rate_7;
        }

        public void setRate_7(int rate_7) {
            this.rate_7 = rate_7;
        }

        public int getCreate_time_7() {
            return create_time_7;
        }

        public void setCreate_time_7(int create_time_7) {
            this.create_time_7 = create_time_7;
        }
    }
}
