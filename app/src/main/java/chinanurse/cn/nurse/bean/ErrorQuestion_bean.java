package chinanurse.cn.nurse.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class ErrorQuestion_bean {

    /**
     * data : [{"post_title":"胸腔穿刺时，抽液不宜过多过快是为了防止 ","post_difficulty":"3","answerlist":[{"answer_title":"自发性气胸             ","isanswer":"0","id":"785"},{"answer_title":"呼吸频率加快           ","isanswer":"0","id":"786"},{"answer_title":"频繁咳嗽               ","isanswer":"0","id":"787"},{"answer_title":"纵隔复位太快           ","isanswer":"1","id":"788"},{"answer_title":"电解质紊乱             ","isanswer":"0","id":"789"}],"post_isright":"","question_select":"","id":"182","post_description":"纵隔复位太快，可刺激纵隔与肺门部位的神经，并可使大血管扭曲，影响血液流回心脏，引起循环功能严重障碍。"}]
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

    public static class DataEntity {
        /**
         * post_title : 胸腔穿刺时，抽液不宜过多过快是为了防止
         * post_difficulty : 3
         * answerlist : [{"answer_title":"自发性气胸             ","isanswer":"0","id":"785"},{"answer_title":"呼吸频率加快           ","isanswer":"0","id":"786"},{"answer_title":"频繁咳嗽               ","isanswer":"0","id":"787"},{"answer_title":"纵隔复位太快           ","isanswer":"1","id":"788"},{"answer_title":"电解质紊乱             ","isanswer":"0","id":"789"}]
         * post_isright :
         * question_select :
         * id : 182
         * post_description : 纵隔复位太快，可刺激纵隔与肺门部位的神经，并可使大血管扭曲，影响血液流回心脏，引起循环功能严重障碍。
         */
        private String post_title;
        private String post_difficulty;
        private List<AnswerlistEntity> answerlist;
        private String post_isright;
        private String question_select;
        private String id;
        private String post_description;
        private String current_answer;//正确答案

//        private String post_title;//题目
//        private String post_description;//试题描述
//        private String istight;//题是正确还是错误的状态
//        private String questionselect;//当前点击的选项String形式
//        private String isanswer;//选项是正确答案还是错误答案状态
//        private String answer_title;//现象list

        public String getCurrent_answer() {
            return current_answer;
        }

        public void setCurrent_answer(String current_answer) {
            this.current_answer = current_answer;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public void setPost_difficulty(String post_difficulty) {
            this.post_difficulty = post_difficulty;
        }

        public void setAnswerlist(List<AnswerlistEntity> answerlist) {
            this.answerlist = answerlist;
        }

        public void setPost_isright(String post_isright) {
            this.post_isright = post_isright;
        }

        public void setQuestion_select(String question_select) {
            this.question_select = question_select;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setPost_description(String post_description) {
            this.post_description = post_description;
        }

        public String getPost_title() {
            return post_title;
        }

        public String getPost_difficulty() {
            return post_difficulty;
        }

        public List<AnswerlistEntity> getAnswerlist() {
            return answerlist;
        }

        public String getPost_isright() {
            return post_isright;
        }

        public String getQuestion_select() {
            return question_select;
        }

        public String getId() {
            return id;
        }

        public String getPost_description() {
            return post_description;
        }


        public static class AnswerlistEntity {
            /**
             * answer_title : 自发性气胸
             * isanswer : 0
             * id : 785
             */
            private String answer_title;
            private String isanswer;
            private String id;

            public void setAnswer_title(String answer_title) {
                this.answer_title = answer_title;
            }

            public void setIsanswer(String isanswer) {
                this.isanswer = isanswer;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAnswer_title() {
                return answer_title;
            }

            public String getIsanswer() {
                return isanswer;
            }

            public String getId() {
                return id;
            }
        }
    }
}
