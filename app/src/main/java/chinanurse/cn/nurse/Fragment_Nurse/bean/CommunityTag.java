package chinanurse.cn.nurse.Fragment_Nurse.bean;

import java.io.Serializable;

/**
 * Created by zhuchongkun on 2016/12/30.
 */

public class CommunityTag implements Serializable {

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    private String tagName;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    private String tagId;

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    private String isSelected;

    @Override
    public String toString() {
        return "CommunityTag{" +
                "tagName='" + tagName + '\'' +
                ", tagId='" + tagId + '\'' +
                ", isSelected='" + isSelected + '\'' +
                '}';
    }
}
