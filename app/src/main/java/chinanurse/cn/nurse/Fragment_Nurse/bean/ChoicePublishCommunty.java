package chinanurse.cn.nurse.Fragment_Nurse.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuchongkun on 2016/12/20.
 */

public class ChoicePublishCommunty implements Serializable {
    //term_id(分类id),name(分类名称),community[id(圈子id),community_name(圈子名称)]

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;

    public List<ChoicePublishData> getData() {
        return data;
    }

    public void setData(List<ChoicePublishData> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<ChoicePublishData> data;

    public class ChoicePublishData implements Serializable {
        public String getTermId() {
            return termId;
        }

        public void setTermId(String termId) {
            this.termId = termId;
        }

        @SerializedName("term_id")
        private String termId;

        public String getTermName() {
            return termName;
        }

        public void setTermName(String termName) {
            this.termName = termName;
        }

        @SerializedName("name")
        private String termName;

        public List<CommunityData> getCommunityData() {
            return CommunityData;
        }

        public void setCommunityData(List<CommunityData> communityData) {
            CommunityData = communityData;
        }

        @SerializedName("community")
        private List<CommunityData> CommunityData;

        public class CommunityData implements Serializable {
            public String getCommunityId() {
                return communityId;
            }

            public void setCommunityId(String communityId) {
                this.communityId = communityId;
            }

            @SerializedName("id")
            private String communityId;

            public String getCommunityName() {
                return communityName;
            }

            public void setCommunityName(String communityName) {
                this.communityName = communityName;
            }

            @SerializedName("community_name")
            private String communityName;

            @Override
            public String toString() {
                return "CommunityBean{" +
                        "communityId='" + communityId + '\'' +
                        ", communityName='" + communityName + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ChoicePublishData{" +
                    "termId='" + termId + '\'' +
                    ", termName='" + termName + '\'' +
                    ", CommunityBean=" + CommunityData +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChoicePublishCommunty{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
