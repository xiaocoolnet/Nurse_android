package chinanurse.cn.nurse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的收藏--试题收藏 容器
 * Created by dell-2618 on 2016/8/12.
 */
public class MyCollectQuestion_Bean implements Serializable{

    /**
     * status : success
     * data : [{"id":"652","userid":"609","title":"5岁小儿的正常血压为","description":"2岁后可按公式计算，收缩压 (mmHg)=年龄X2 80，舒张压为收缩压的 2/3。","type":"2","object_id":"301","createtime":"1470987034","answerslist":[{"id":"1327","questionid":"301","title":"110/70mmHg","isanswer":"0","listorder":"0"},{"id":"1328","questionid":"301","title":"100/65mmHg","isanswer":"0","listorder":"0"},{"id":"1329","questionid":"301","title":"90/60mmHg","isanswer":"1","listorder":"0"},{"id":"1330","questionid":"301","title":"90/55mmHg","isanswer":"0","listorder":"0"},{"id":"1331","questionid":"301","title":"80/50mmHg","isanswer":"0","listorder":"0"}],"questionid":null,"post_title":null,"post_description":null,"post_difficulty":null,"answer":null},{"id":"352","userid":"609","title":"2个月赴美国做注册护士RN 学习、工作+全家移民！","description":"护士短缺在美国不是新现象了，这些年美国的医疗保健业界一直在持续关注这个现象。据美国劳工部统计，美国护士年均缺口达12万。随着\u201c婴儿潮\u201d 时代也就是上世纪四十年代中到六十年代中出生的7800万人逐渐进入老龄， 老人护理任务日益繁重，预计到2020年，美国护士短缺量将超过50万人，（每年需求量22%递增）","type":"2","object_id":"22","createtime":"1469342751","answerslist":[{"id":"1","questionid":"22","title":"12312312312312","isanswer":"1","listorder":"0"}],"questionid":null,"post_title":null,"post_description":null,"post_difficulty":null,"answer":null},{"id":"337","userid":"609","title":"香港玛丽医院护士拔透析管出错，致病人身亡","description":"据香港大公报报道，香港玛丽医院发生护士拔喉程序出错致病人死亡事故。一名83岁老翁上周五(27日)因病情稳定，可拔喉并由深切治疗部转送至普通病房，但一名男护士疑于老翁坐着时拔喉，老翁于十分钟内病情恶化，30日凌晨离世，初步调查怀疑病人于拔喉时有空气入血管，导致血管栓塞后死亡。\r\n","type":"2","object_id":"4","createtime":"1469327179","answerslist":[],"questionid":null,"post_title":null,"post_description":null,"post_difficulty":null,"answer":null},{"id":"218","userid":"609","title":"香港玛丽医院护士拔透析管出错，致病人身亡","description":"据香港大公报报道，香港玛丽医院发生护士拔喉程序出错致病人死亡事故。一名83岁老翁上周五(27日)因病情稳定，可拔喉并由深切治疗部转送至普通病房，但一名男护士疑于老翁坐着时拔喉，老翁于十分钟内病情恶化，30日凌晨离世，初步调查怀疑病人于拔喉时有空气入血管，导致血管栓塞后死亡。\r\n","type":"2","object_id":"4","createtime":"1468751625","answerslist":[],"questionid":null,"post_title":null,"post_description":null,"post_difficulty":null,"answer":null},{"id":"204","userid":"609","title":"中华护理学会李秀华理事长会见国际护士主席","description":"中华护理学会理事长李秀华、副理事长刘华平、孙红、学术部宋江莉在韩国首尔参加第三届中日韩护理学术交流期间,会见了国际护士会主席Rosemary Bryant女士. 双方在友好气氛中就中华护理学会加入国际护士会具体事宜进行了会谈.","type":"2","object_id":"4","createtime":"1468738072","answerslist":[],"questionid":null,"post_title":null,"post_description":null,"post_difficulty":null,"answer":null},{"id":"202","userid":"609","title":"香港玛丽医院护士拔透析管出错，致病人身亡","description":"据香港大公报报道，香港玛丽医院发生护士拔喉程序出错致病人死亡事故。一名83岁老翁上周五(27日)因病情稳定，可拔喉并由深切治疗部转送至普通病房，但一名男护士疑于老翁坐着时拔喉，老翁于十分钟内病情恶化，30日凌晨离世，初步调查怀疑病人于拔喉时有空气入血管，导致血管栓塞后死亡。\r\n","type":"2","object_id":"4","createtime":"1468737651","answerslist":[],"questionid":null,"post_title":null,"post_description":null,"post_difficulty":null,"answer":null}]
     */

    private String status;
    /**
     * id : 652
     * userid : 609
     * title : 5岁小儿的正常血压为
     * description : 2岁后可按公式计算，收缩压 (mmHg)=年龄X2 80，舒张压为收缩压的 2/3。
     * type : 2
     * object_id : 301
     * createtime : 1470987034
     * answerslist : [{"id":"1327","questionid":"301","title":"110/70mmHg","isanswer":"0","listorder":"0"},{"id":"1328","questionid":"301","title":"100/65mmHg","isanswer":"0","listorder":"0"},{"id":"1329","questionid":"301","title":"90/60mmHg","isanswer":"1","listorder":"0"},{"id":"1330","questionid":"301","title":"90/55mmHg","isanswer":"0","listorder":"0"},{"id":"1331","questionid":"301","title":"80/50mmHg","isanswer":"0","listorder":"0"}]
     * questionid : null
     * post_title : null
     * post_description : null
     * post_difficulty : null
     * answer : null
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

    public static class DataBean implements Serializable {
        private String id;
        private String userid;
        private String title;
        private String description;
        private String type;
        private String object_id;
        private String createtime;
        private Object questionid;
        private Object post_title;
        private Object post_description;
        private Object post_difficulty;
        private Object answer;
        /**
         * id : 1327
         * questionid : 301
         * title : 110/70mmHg
         * isanswer : 0
         * listorder : 0
         */

        private List<AnswerslistBean> answerslist;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public Object getQuestionid() {
            return questionid;
        }

        public void setQuestionid(Object questionid) {
            this.questionid = questionid;
        }

        public Object getPost_title() {
            return post_title;
        }

        public void setPost_title(Object post_title) {
            this.post_title = post_title;
        }

        public Object getPost_description() {
            return post_description;
        }

        public void setPost_description(Object post_description) {
            this.post_description = post_description;
        }

        public Object getPost_difficulty() {
            return post_difficulty;
        }

        public void setPost_difficulty(Object post_difficulty) {
            this.post_difficulty = post_difficulty;
        }

        public Object getAnswer() {
            return answer;
        }

        public void setAnswer(Object answer) {
            this.answer = answer;
        }

        public List<AnswerslistBean> getAnswerslist() {
            return answerslist;
        }

        public void setAnswerslist(List<AnswerslistBean> answerslist) {
            this.answerslist = answerslist;
        }

        public static class AnswerslistBean implements Serializable {
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
