package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhuchongkun on 2017/1/2.
 */

public class CommunityBean implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;// 圈子ID

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("community_name")
    private String name;// 圈子名称

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @SerializedName("photo")
    private String photo;// 圈子头像

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    private String description;// 圈子介绍

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }

    @SerializedName("best")
    private String best;//是否精选(选精1,不0)

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    @SerializedName("create_time")
    private long creatTime;//创建时间

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    @SerializedName("hot")
    private String hot;// 是否热门(热门1,不0)

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    @SerializedName("term_id")
    private String term_id;// 圈子类型id

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    @SerializedName("term_name")
    private String term_name;// 圈子类型名称

    public String getForum_count() {
        return forum_count;
    }

    public void setForum_count(String forum_count) {
        this.forum_count = forum_count;
    }

    @SerializedName("f_count")
    private String forum_count;//帖子数

    public String getPerson_count() {
        return person_count;
    }

    public void setPerson_count(String person_count) {
        this.person_count = person_count;
    }

    @SerializedName("person_num")
    private String person_count;// 人数

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    @SerializedName("join")
    private String join;//(是否加入该圈子 1已加入，0未加入)

}
