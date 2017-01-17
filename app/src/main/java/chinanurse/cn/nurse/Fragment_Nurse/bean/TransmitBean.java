package chinanurse.cn.nurse.Fragment_Nurse.bean;

import android.content.Intent;

import java.io.Serializable;

import chinanurse.cn.nurse.FragmentTag;

/**
 * Created by zhuchongkun on 2016/12/19.
 */

public class TransmitBean implements Serializable {
    public CommunityBean getCommunityData() {
        return communityData;
    }

    public void setCommunityData(CommunityBean communityData) {
        this.communityData = communityData;
    }

    private CommunityBean communityData;

    public CommunityMasterBean.DataBean getCommunityMasterData() {
        return communityMasterData;
    }

    public void setCommunityMasterData(CommunityMasterBean.DataBean communityMasterData) {
        this.communityMasterData = communityMasterData;
    }

    private CommunityMasterBean.DataBean communityMasterData;


    public ForumDataBean getForumData() {
        return forumData;
    }

    public void setForumData(ForumDataBean forumData) {
        this.forumData = forumData;
    }

    private ForumDataBean forumData;

    public PersonAuthenticationBean.DataBean getPersonAuthenticationData() {
        return personAuthenticationData;
    }

    public void setPersonAuthenticationData(PersonAuthenticationBean.DataBean personAuthenticationData) {
        this.personAuthenticationData = personAuthenticationData;
    }

    private PersonAuthenticationBean.DataBean personAuthenticationData;

    public FragmentTag getFrom() {
        return from;
    }

    public void setFrom(FragmentTag from) {
        this.from = from;
    }

    private FragmentTag from;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    private Intent intent;

    @Override
    public String toString() {
        return "TransmitBean{" +
                "communityData=" + communityData +
                ", communityMasterData=" + communityMasterData +
                ", forumData=" + forumData +
                ", personAuthenticationData=" + personAuthenticationData +
                ", from=" + from +
                ", intent=" + intent +
                '}';
    }
}
