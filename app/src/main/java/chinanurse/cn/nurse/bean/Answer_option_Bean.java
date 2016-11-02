package chinanurse.cn.nurse.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Answer_option_Bean implements Parcelable {

    /**
     * status : success
     * data : [{"id":"234","post_title":"下列属于语言性交流的是 ","post_description":"","post_difficulty":"1","answerlist":[{"answer_title":"手势","isanswer":"0"},{"answer_title":"沉默","isanswer":"0"},{"answer_title":"倾诉","isanswer":"1"},{"answer_title":"倾听","isanswer":"0"},{"answer_title":"眼神","isanswer":"0"}]},{"id":"233","post_title":"下列哪项不属于护理技术操作前解释的内容 ","post_description":"","post_difficulty":"1","answerlist":[{"answer_title":"操作的目的 ","isanswer":"0"},{"answer_title":"病人需配合的内容 ","isanswer":"0"},{"answer_title":"操作过程 ","isanswer":"0"},{"answer_title":"给予心理上的安慰 ","isanswer":"0"},{"answer_title":"感谢病人的合作 ","isanswer":"1"}]},{"id":"232","post_title":"护士为长期卧床的病人做床上擦浴，此时护士的角色是 ","post_description":"","post_difficulty":"1","answerlist":[{"answer_title":"健康照顾者 ","isanswer":"1"},{"answer_title":"健康教育者 ","isanswer":"0"},{"answer_title":"健康咨询者 ","isanswer":"0"},{"answer_title":"护理管理者 ","isanswer":"0"},{"answer_title":"护理研究者 ","isanswer":"0"}]},{"id":"26","post_title":"患者，男性，35岁。在全麻下行胆总管切开取石、T管引流术，腹腔放置引流管。术毕返回病房，神志清醒。体检示：脉搏95次/分钟，血压125/70mmHg，腹腔引流液100ml。回病房1小时候腹腔引流液为210ml，呈血性；脉搏110次/分钟，血压105/65mmHg，唇稍干燥。","post_description":"","post_difficulty":"1","answerlist":[{"answer_title":"呼吸频率","isanswer":"0"},{"answer_title":"腹腔引流量和色","isanswer":"1"},{"answer_title":"补液速度","isanswer":"0"},{"answer_title":"患者体温","isanswer":"0"},{"answer_title":"T管引流量","isanswer":"0"}]},{"id":"27","post_title":"患者，男性，35岁。在全麻下行胆总管切开取石、T管引流术，腹腔放置引流管。术毕返回病房，神志清醒。体检示：脉搏95次/分钟，血压125/70mmHg，腹腔引流液100ml。回病房1小时候腹腔引流液为210ml，呈血性；脉搏110次/分钟，血压105/65mmHg，唇稍干燥。\r\n根据该患者的病情，应疑为","post_description":"","post_difficulty":"1","answerlist":[{"answer_title":"胆道出血","isanswer":"1"},{"answer_title":"腹腔内出血","isanswer":"0"},{"answer_title":"胆瘘","isanswer":"1"},{"answer_title":"呼吸困难","isanswer":"0"},{"answer_title":"消化道出血","isanswer":"0"}]},{"id":"231","post_title":"以工作为导向，按工作内容分配护理工作的护理工作方式是","post_description":"","post_difficulty":"1","answerlist":[{"answer_title":"个体护理 ","isanswer":"0"},{"answer_title":"功能制护理 ","isanswer":"1"},{"answer_title":"小组制护理 ","isanswer":"0"},{"answer_title":"责任制护理 ","isanswer":"0"},{"answer_title":"综合护理 ","isanswer":"0"}]},{"id":"31","post_title":"心绞痛最有效的急救药物是","post_description":"硝酸酯类药物是最有效、作用最快终止心绞痛发作的药物，可扩张冠脉，增加冠脉血流量，同时扩张外周血管，减轻心脏负担而缓解心绞痛。","post_difficulty":"1","answerlist":[{"answer_title":"硝酸甘油 ","isanswer":"1"},{"answer_title":"速效救心丸 ","isanswer":"0"},{"answer_title":"地西泮 ","isanswer":"0"},{"answer_title":"卡托普利 ","isanswer":"0"},{"answer_title":"洋地黄类 ","isanswer":"0"}]},{"id":"32","post_title":"下面属于急性心肌梗死24小时内禁用的药物是 ","post_description":"急性心肌梗死24小时以内禁止使用洋地黄制剂。","post_difficulty":"1","answerlist":[{"answer_title":"呋塞米（速尿） ","isanswer":"0"},{"answer_title":"硝酸异山梨酯 ","isanswer":"0"},{"answer_title":"链激酶 ","isanswer":"0"},{"answer_title":"利多卡因 ","isanswer":"0"},{"answer_title":"洋地黄 ","isanswer":"1"}]},{"id":"33","post_title":"最有效治疗顽固性腹水的方法是","post_description":"腹水浓缩回输是治疗难治性腹水的较好办法。","post_difficulty":"1","answerlist":[{"answer_title":"应用利尿药 ","isanswer":"0"},{"answer_title":"定期放腹水 ","isanswer":"0"},{"answer_title":"腹水浓缩回输 ","isanswer":"1"},{"answer_title":"透析疗法 ","isanswer":"0"},{"answer_title":"定期输新鲜血 ","isanswer":"0"}]}]
     */

    private String status;
    /**
     * id : 234
     * post_title : 下列属于语言性交流的是
     * post_description :
     * post_difficulty : 1
     * answerlist : [{"answer_title":"手势","isanswer":"0"},{"answer_title":"沉默","isanswer":"0"},{"answer_title":"倾诉","isanswer":"1"},{"answer_title":"倾听","isanswer":"0"},{"answer_title":"眼神","isanswer":"0"}]
     */

    private List<DataBean> data;

    protected Answer_option_Bean() {

    }

    protected Answer_option_Bean(Parcel in) {
        status = in.readString();
    }

    public static final Creator<Answer_option_Bean> CREATOR = new Creator<Answer_option_Bean>() {
        @Override
        public Answer_option_Bean createFromParcel(Parcel in) {
            return new Answer_option_Bean(in);
        }

        @Override
        public Answer_option_Bean[] newArray(int size) {
            return new Answer_option_Bean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
    }

    public static class DataBean implements Parcelable{
        private String id;
        private String post_title;
        private String post_description;
        private String post_difficulty;
        /**
         * answer_title : 手势
         * isanswer : 0
         */

        private List<AnswerlistBean> answerlist;


        protected DataBean() {}

        protected DataBean(Parcel in) {
            id = in.readString();
            post_title = in.readString();
            post_description = in.readString();
            post_difficulty = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<AnswerlistBean> getAnswerlist() {
            return answerlist;
        }

        public void setAnswerlist(List<AnswerlistBean> answerlist) {
            this.answerlist = answerlist;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(post_title);
            dest.writeString(post_description);
            dest.writeString(post_difficulty);
        }

        public static class AnswerlistBean {
            private String answer_title;
            private String isanswer;

            public String getAnswer_title() {
                return answer_title;
            }

            public void setAnswer_title(String answer_title) {
                this.answer_title = answer_title;
            }

            public String getIsanswer() {
                return isanswer;
            }

            public void setIsanswer(String isanswer) {
                this.isanswer = isanswer;
            }
        }
    }
}
