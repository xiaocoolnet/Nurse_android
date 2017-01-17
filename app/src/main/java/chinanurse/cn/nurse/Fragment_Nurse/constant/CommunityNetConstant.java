package chinanurse.cn.nurse.Fragment_Nurse.constant;

import chinanurse.cn.nurse.UrlPath.NetBaseConstant;

/**
 * Created by zhuchongkun on 2016/12/16.
 */

public interface CommunityNetConstant extends NetBaseConstant {
    /**
     * 上传个人头像 a=uploadavatar
     * 入参：POST： upfile
     * 出参：头像的完整地址http://nurse.xiaocool.net/uploads/microblog/文件名.jpg
     * Demo:POST提交
     */
    String UPLOAD_AVATAR = NET_BASE_HOST + "a=uploadavatar";
    /**
     * 获取 全部圈子 下的子分类用 getChannellist
     */
    String CHANNEL_LIST = NET_BASE_HOST + "a=getChannellist";
    /**
     * 获取圈子列表 a=getCommunityList
     */
    String COMMUNITY_LIST = NET_BASE_HOST + "a=getCommunityList";
    /**
     * 获取帖子列表 a=getForumList
     */
    String FORUM_LIST = NET_BASE_HOST + "a=getForumList";
    /**
     * 获取圈主列表 a=getMasterList
     */
    String MASTER_LIST = NET_BASE_HOST + "a=getMasterList";
    /**
     * 申请圈主 a=apply_community
     */
    String APPLY_COMMUNITY = NET_BASE_HOST + "a=apply_community";
    /**
     * 判断是否为圈主 a=judge_apply_community
     */
    String JUDGE_APPLY_COMMUNITY = NET_BASE_HOST + "a=judge_apply_community";
    /**
     * 发布个人认证 a=authentication_person
     */
    String AUTHENTICATION_PERSON = NET_BASE_HOST + "a=authentication_person";
    /**
     * 获取个人认证状态 a=getPersonAuth
     */
    String GET_PERSON_AUTHENTICATION = NET_BASE_HOST + "a=getPersonAuth";
    /**
     * 判断是否加入圈子 a=judge_Community
     */
    String JUDGE_COMMUNITY = NET_BASE_HOST + "a=judge_Community";
    /**
     * 加入圈子 a=addCommunity
     */
    String ADD_COMMUNITY = NET_BASE_HOST + "a=addCommunity";
    /**
     * 获取帖子详情 a=getForumInfo
     */
    String GET_FORUM_INFO = NET_BASE_HOST + "a=getForumInfo";
    /**
     * 发布帖子 a=PublishForum
     */
    String PUBLISH_FORUM = NET_BASE_HOST + "a=PublishForum";
    /**
     * 选择发布圈子 a=getPublishCommunity
     */
    String GET_PUBLISH_COMMUNITY = NET_BASE_HOST + "a=getPublishCommunity";
    /**
     * 获取圈子详情 a=getCommunityInfo
     */
    String GET_COMMUNITY_INFO = NET_BASE_HOST + "a=getCommunityInfo";
    /**
     * 删除帖子 a=DeleteForum
     */
    String DELETE_FORUM = NET_BASE_HOST + "a=DeleteForum";
    /**
     * 添加 圈子 关注粉丝 a=addfollow_fans
     */
    String ADD_FOLLOW_FANS = NET_BASE_HOST + "a=addfollow_fans";
    /**
     * 删除 圈子 关注 a=delFollow_fans
     */
    String DEL_FOLLOW_FANS = NET_BASE_HOST + "a=delFollow_fans";
    /**
     * 获取圈子关注列表(我关注了谁，我是关注人) a=getFollowList
     */
    String GET_FOLLOW_LIST = NET_BASE_HOST + "a=getFollowList";
    /**
     * 获取圈子粉丝列表（谁关注了我，我是被关注人）a=getFansList
     */
    String GET_FAN_LIST = NET_BASE_HOST + "a=getFansList";
    /**
     * 获取 关注 和 粉丝 的数量 a=getFollowFans_num
     */
    String GET_FOLLOW_FANS_NUM = NET_BASE_HOST + "a=getFollowFans_num";
    /**
     * 添加举报 a=addReport
     */
    String ADD_REPORT = NET_BASE_HOST + "a=addReport";
    /**
     * 添加打赏 a=addReward
     */
    String ADD_REWARD = NET_BASE_HOST + "a=addReward";
    /**
     * 取消 加入圈子 a=delJoinCommunity
     */
    String DEL_JOIN_COMMUNITY = NET_BASE_HOST + "a=delJoinCommunity";
    /**
     * 判断是否添加关注 a=judgeFollowFans
     */
    String JUDGE_FOLLOW_FANS = NET_BASE_HOST + "a=judgeFollowFans";
    /**
     * 获取我加入的圈子 a=getMyCommunityList
     */
    String GET_MY_COMMUNITY_LIST = NET_BASE_HOST + "a=getMyCommunityList";
    /**
     * 获取 我发的帖子 a=getMyForumList
     */
    String GET_MY_FORUM_LIST = NET_BASE_HOST + "a=getMyForumList";
    /**
     * 帖子加精 a=forumSetBest
     */
    String FORUM_SET_BEST = NET_BASE_HOST + "a=forumSetBest";
    /**
     * 帖子置顶 a=forumSetTop
     */
    String FORUM_SET_TOP = NET_BASE_HOST + "a=forumSetTop";
    /**
     * 点赞 a=SetLike
     */
    String SET_LIKE = NET_BASE_HOST + "a=SetLike";
    /**
     * 取消赞 a=ResetLike
     */
    String RESET_LIKE = NET_BASE_HOST + "a=ResetLike";
    /**
     * 检测是否点赞 a=CheckHadLike
     */
    String CHECK_HAD_LIKE = NET_BASE_HOST + "a=CheckHadLike";
    /**
     * 添加评论 a=SetComment
     */
    String SET_COMMENT = NET_BASE_HOST + "a=SetComment";
    /**
     * 获取圈子评论 a=getForumComments
     */
    String GET_FORUM_COMMENTS = NET_BASE_HOST + "a=getForumComments";
    /**
     * 删除帖子评论以及子评论 a=DelForumComments
     */
    String DEL_FORUM_COMMENTS = NET_BASE_HOST + "a=DelForumComments";
    /**
     * 收藏 a=addfavorite
     */
    String ADD_FAVORITE = NET_BASE_HOST + "a=addfavorite";
    /**
     * 取消收藏  a=cancelfavorite
     */
    String CANCEL_FAVORITE = NET_BASE_HOST + "a=cancelfavorite";
    /**
     * 判断是否是圈子管理员 a=judge_community_admin
     */
    String JUDGE_COMMUNITY_ADMIN = NET_BASE_HOST + "a=judge_community_admin";
    /**
     * 获取申请圈主状态 ：a=getA_C_Status
     */
    String GET_A_C_STATUS = NET_BASE_HOST + "a=getA_C_Status";
    /**
     * 获取护士币 a=getNurse_score
     */
    String GET_NURSE_SCORE = NET_BASE_HOST + "a=getNurse_score";
    /**
     * 首页图片 a=getHomePage
     */
    String GET_HOME_PAGE = NET_BASE_HOST + "a=getHomePage";
    /**
     * 获取我评价的帖子 a=getMyJudgeCommunity
     */
    String GET_MY_JUDGE_COMMUNITY = NET_BASE_HOST + "a=getMyJudgeCommunity";
    /**
     * 获取我加入的圈子中的帖子列表 以及 我关注的人发的帖子 a=getFollowForumList
     */
    String GET_FOLLOW_FORUMLIST = NET_BASE_HOST + "a=getFollowForumList";
    /**
     * 获取个人信息 a=getuserinfo
     */
    String GET_USER_INFO = NET_BASE_HOST + "a=getuserinfo";
    /**
     * 获取我的消息（被加精置顶、被评论、被打赏等消息） a=getMyMessageList
     */
    String GET_MY_MESSAGELIST = NET_BASE_HOST + "a=getMyMessageList";
    /**
     * 获取评论数量  a=getComments_count
     */
    String GET_COMMENTS_COUNT = NET_BASE_HOST + "a=getComments_count";
    /**
     * 帖子推荐 a=forumSetRecommend
     */
    String FORUM_SET_RECOMMEND = NET_BASE_HOST + "a=forumSetRecommend";
    /**
     * 获取他管理的圈子接口  a=get_community_id
     */
    String GET_COMMUNITY_ID = NET_BASE_HOST + "a=get_community_id";

    /**
     * 获取点赞数
     */
    String GET_LIKE_COUNT = NET_BASE_HOST + "a=GetLikeCount";

}
