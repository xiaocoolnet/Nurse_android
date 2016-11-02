package chinanurse.cn.nurse.MinePage.MyStudy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class MineStudt_question_error_Bean implements Serializable{

    private String status;
    /**
     * questionid : 102
     * post_title : 患者，女性，45岁，胸部外伤后出现了张力性气胸的表现，现场抢救首先应
     * post_description : 张力性气胸时，胸腔压力不断升高，肺萎陷，还可将纵隔推向健侧，严重影响呼吸功能，应立即穿刺排气。
     * post_difficulty : 3
     * answer : 2
     * answers : [{"id":"384","questionid":"102","title":"封闭伤口 ","isanswer":"0","listorder":"0"},{"id":"385","questionid":"102","title":"气管切开 ","isanswer":"0","listorder":"0"},{"id":"386","questionid":"102","title":"快速输液、吸氧 ","isanswer":"0","listorder":"0"},{"id":"387","questionid":"102","title":"闭式胸膜腔引流 ","isanswer":"0","listorder":"0"},{"id":"388","questionid":"102","title":"胸腔穿刺排气 ","isanswer":"1","listorder":"0"}]
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

    public static class DataBean implements Serializable{
        private String questionid;
        private String post_title;
        private String post_description;
        private String post_difficulty;
        private String answer;
        /**
         * id : 384
         * questionid : 102
         * title : 封闭伤口
         * isanswer : 0
         * listorder : 0
         */

        private List<AnswersBean> answers;

        public String getQuestionid() {
            return questionid;
        }

        public void setQuestionid(String questionid) {
            this.questionid = questionid;
        }

        public String getPost_title() {
            return post_title;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public String getPost_description() {
            return post_description;
        }

        public void setPost_description(String post_description) {
            this.post_description = post_description;
        }

        public String getPost_difficulty() {
            return post_difficulty;
        }

        public void setPost_difficulty(String post_difficulty) {
            this.post_difficulty = post_difficulty;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public List<AnswersBean> getAnswers() {
            return answers;
        }

        public void setAnswers(List<AnswersBean> answers) {
            this.answers = answers;
        }

        public static class AnswersBean implements Serializable{
            private String id;
            private String questionid;
            private String title;
            private String isanswer;
            private String listorder;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQuestionid() {
                return questionid;
            }

            public void setQuestionid(String questionid) {
                this.questionid = questionid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIsanswer() {
                return isanswer;
            }

            public void setIsanswer(String isanswer) {
                this.isanswer = isanswer;
            }

            public String getListorder() {
                return listorder;
            }

            public void setListorder(String listorder) {
                this.listorder = listorder;
            }
        }
    }
}
