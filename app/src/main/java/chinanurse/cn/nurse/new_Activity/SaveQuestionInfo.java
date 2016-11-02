package chinanurse.cn.nurse.new_Activity;

public class SaveQuestionInfo {

    private String questionId;//题目id
    private String realAnswer;//题目答案
    private String is_correct;//是否正确
    private String score;//分值
    private String presentanswer;//我的答案

    public String getPresentanswer() {
        return presentanswer;
    }

    public void setPresentanswer(String presentanswer) {
        this.presentanswer = presentanswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getRealAnswer() {
        return realAnswer;
    }

    public void setRealAnswer(String realAnswer) {
        this.realAnswer = realAnswer;
    }

    public String getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(String is_correct) {
        this.is_correct = is_correct;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String toString() {
        return "{'question_id':'" + getQuestionId() + "','realAnswer':'" + getRealAnswer() + "','is_correct':'" + getIs_correct() + "','score':'" + getScore() + "'}";
    }

}
