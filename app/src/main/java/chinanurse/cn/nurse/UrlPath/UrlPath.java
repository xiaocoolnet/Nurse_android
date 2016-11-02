package chinanurse.cn.nurse.UrlPath;

/**
 * Created by wzh on 2016/5/7.
 */
public interface UrlPath extends NetBaseConstant {
    String news_firstpage = NetBaseConstant.NET_BASE_PREFIX + "&a=getChannellist&";
    //获取资讯文章列表
    String news_list = NetBaseConstant.NET_BASE_PREFIX + "&a=getNewslist&";
    //获取新闻界面相关文章列表
    String news_list_about = NetBaseConstant.NET_BASE_PREFIX + "&a=getRelatedNewslist";
    //获取首页幻灯片列表
    String news_title_list = NetBaseConstant.NET_BASE_PREFIX + "&a=getslidelist";
    //获取每日一练类型名称
    String STUDY_TITLE = NetBaseConstant.NET_BASE_PREFIX + "&a=getDaliyExamTypeList";
    //登陆
    String isLogin = NetBaseConstant.NET_BASE_PREFIX + "&a=applogin";
    //注册
    String isRegister = NetBaseConstant.NET_BASE_PREFIX + "&a=AppRegister";
    //判断是否已经注册
    String isCanRegister = NetBaseConstant.NET_BASE_PREFIX + "&a=checkphone";
    //发送验证码
    String sendMobileCode = NetBaseConstant.NET_BASE_PREFIX + "&a=SendMobileCode";
    //点赞
    String setLike = NetBaseConstant.NET_BASE_PREFIX + "&a=SetLike";
    //取消点赞
    String resetLike = NetBaseConstant.NET_BASE_PREFIX + "&a=ResetLike";
    //修改个人资料[姓名]
    String updateusername = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserName";
    //修改个人资料[头像]
    String updateUserAvatar = NetBaseConstant.NET_BASE_PREFIX + "a=UpdateUserAvatar";
    //修改个人资料[性别]
    String updateUserSex = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserSex";
    //修改个人资料[手机]
    String updateUserPhone = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserPhone";
    //修改个人资料[邮箱]
    String updateUserEmail = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserEmail";
    //修改个人资料[生日]
    String updateUserBirthday = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserBirthday";
    //修改个人资料[地址]
    String updateUserAddress = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserAddress";
    //修改个人资料[学校]
    String updateUserSchool = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserSchool";
     //修改个人资料[真实姓名]
    String UpdateUserRealName = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserRealName";
    //修改个人资料[专业]
    String updateUserMajor = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserMajor";
    //修改个人资料[学历]
    String updateUserEducation = NetBaseConstant.NET_BASE_PREFIX + "&a=UpdateUserEducation";
    //获取个人资料
    String getuserinfo = NetBaseConstant.NET_BASE_PREFIX + "&a=getuserinfo";
    //上传个人头像
    String uploadavatar = NetBaseConstant.NET_BASE_PREFIX + "a=uploadavatar";
    //获取招聘信息列表
    String EMPLOY_LIST = NetBaseConstant.NET_BASE_PREFIX + "a=getjoblist_android&userid=1";
    //获取找人才建立列表
    String TALENT_LIST = NetBaseConstant.NET_BASE_PREFIX + "a=getResumeList_android&companyid=1";
    //获取每日一练考试题列表
    String TEXTLIST = NetBaseConstant.NET_BASE_PREFIX + "a=getDaliyExamList";
    //点击收藏
    String COLLEXT = NetBaseConstant.NET_BASE_PREFIX + "a=addfavorite";
    //点击取消收藏
    String DELLCOLLEXT = NetBaseConstant.NET_BASE_PREFIX + "a=cancelfavorite";
    //判断是否收藏
    String ISCOLLECT = NetBaseConstant.NET_BASE_PREFIX + "a=CheckHadFavorite";
    //获取粉丝列表
    String GETFANSLIST = NetBaseConstant.NET_BASE_PREFIX + "a=getMyFansList";
    //获取关注列表
    String GETATTIONLIST = NetBaseConstant.NET_BASE_PREFIX + "a=getMyFollowList";
    //发布招聘岗位
    String PUBLISHJOB = NetBaseConstant.NET_BASE_PREFIX + "a=publishjob";
    //发布简历
    String PUBLISHRESUME = NetBaseConstant.NET_BASE_PREFIX + "a=PublishResume";
    //编辑简历
    String UpdataMyResume = NetBaseConstant.NET_BASE_PREFIX + "a=UpdataMyResume";
    //投递简历
    String APPLYJOB = NetBaseConstant.NET_BASE_PREFIX + "a=ApplyJob";
    //获取字典列表
    String DICTIONARYLIST = NetBaseConstant.NET_BASE_PREFIX + "a=getDictionaryList";
    //获取论坛头部列表
    String GETBBSTYPE = NetBaseConstant.NET_BASE_PREFIX + "a=getbbstype";
    //获取论坛数据列表
    String GETCOMMUNITYLIST = NetBaseConstant.NET_BASE_PREFIX + "a=getbbspostlist";
    //获取论坛帖子详情
    String GETCOMMUNITYDETAILS = NetBaseConstant.NET_BASE_PREFIX + "a=showpostinfo";
    //发布帖子
    String POSTCOMMUNITY = NetBaseConstant.NET_BASE_PREFIX + "a=addbbsposts";
    //提交答案
    String SUBMITANSWER = NetBaseConstant.NET_BASE_PREFIX + "a=SubmitAnswers";
    //点击获取收藏列表
    String SETCOLLEXT = NetBaseConstant.NET_BASE_PREFIX + "a=getfavoritelist";
    //获取本公司收到简历列表
//    String GETMYRECIVERESUMELIST = NetBaseConstant.NET_BASE_PREFIX + "a=getMyReciveResumeList";
    String GETMYRECIVERESUMELIST = NetBaseConstant.NET_BASE_PREFIX + "a=getMyReciveResumeList_android";
    //添加评论
    String ADDCOMMENT = NetBaseConstant.NET_BASE_PREFIX + "a=SetComment";
    //获取我做过的考试题
    String TEXTLIST_OVER = NetBaseConstant.NET_BASE_PREFIX + "a=GetExampaper";
    //获取错题考试题
    String TEXTLIST_OERROE = NetBaseConstant.NET_BASE_PREFIX + "a=GetMyErrorExampaper";
    //获取我的今天是否签到
    String GETMYSIGNLOG = NetBaseConstant.NET_BASE_PREFIX + "a=GetMySignLog";
    //获取我的今天是否签到
    String SIGNDAY = NetBaseConstant.NET_BASE_PREFIX + "a=SignDay";
    //获取获取我的消息列表
    String GETSYSTEMMESSAGE = NetBaseConstant.NET_BASE_PREFIX + "a=getsystemmessage_new";
    //检测是否收藏
    String CheckHadFavorite = NetBaseConstant.NET_BASE_PREFIX + "a=CheckHadFavorite";
    //检测是否点赞
    String CheckHadLike = NetBaseConstant.NET_BASE_PREFIX + "a=CheckHadLike";
    //邀请面试
    String InviteJob = NetBaseConstant.NET_BASE_PREFIX + "a=InviteJob";
    //发布企业认证
    String UpdataCompanyCertify = NetBaseConstant.NET_BASE_PREFIX + "a=UpdataCompanyCertify";
    //获取企业状态
    String getCompanyCertify = NetBaseConstant.NET_BASE_PREFIX + "a=getCompanyCertify";
    //获取我的简历
    String getResumeInfo = NetBaseConstant.NET_BASE_PREFIX + "a=getResumeInfo";
    //获取判断是否投递简历
    String ApplyJob_judge = NetBaseConstant.NET_BASE_PREFIX + "a=ApplyJob_judge";
    //忘记密码
    String forgetpwd = NetBaseConstant.NET_BASE_PREFIX + "a=forgetpwd";
    //判断是否邀请面试
    String InviteJob_judge = NetBaseConstant.NET_BASE_PREFIX + "a=InviteJob_judge";
    //企业获取本公司发布的招聘岗位列表
    String getMyPublishJobList = NetBaseConstant.NET_BASE_PREFIX + "a=getMyPublishJobList_android";
    //获取个人积分详情
    String getRanking_User = NetBaseConstant.NET_BASE_PREFIX + "a=getRanking_User";
    //获取积分排行榜
    String getRankingList = NetBaseConstant.NET_BASE_PREFIX + "a=getRankingList";
    //邀请页面
    String scorepengyou = NetBaseConstant.NET_BASE_PREFIX + "a=scorepengyou";
    //分享积分
    String addscore = NetBaseConstant.NET_BASE_PREFIX + "a=addScore_fenxiang";
    //阅读积分
    String Addscore = NetBaseConstant.NET_BASE_PREFIX + "a=addScore_ReadingInformation";
    //阅读积分
    String GETrefcomments = NetBaseConstant.NET_BASE_PREFIX + "a=getRefComments";
    //个人获取我收到的邀请面试列表
    String UserGetInvite = NetBaseConstant.NET_BASE_PREFIX + "a=UserGetInvite_android";
    //个人获取我收到的邀请面试列表
    String getSynAccuracy = NetBaseConstant.NET_BASE_PREFIX + "a=getSynAccuracy";
    //获取我做过的考试题
    String GetExampaper = NetBaseConstant.NET_BASE_PREFIX + "a=GetExampaper";
    //获取折线图数据
    String GetLineChartData = NetBaseConstant.NET_BASE_PREFIX + "a=GetLineChartData";
    //设置已读
    String GETREAD = NetBaseConstant.NET_BASE_PREFIX + "a=setMessageread";
    //判断已读
    String Messageread_judge = NetBaseConstant.NET_BASE_PREFIX + "a=Messageread_judge";
    //反馈信息
    String addfeedback = NetBaseConstant.NET_BASE_PREFIX + "a=addfeedback";
    //获取获取我的消息已读列表
    String getMessagereadlist = NetBaseConstant.NET_BASE_PREFIX + "a=getMessagereadlist";
    //删除我的消息
    String delMySystemMessag = NetBaseConstant.NET_BASE_PREFIX + "a=delMySystemMessag";
    //护理部数据
    String getNewslist_count = NetBaseConstant.NET_BASE_PREFIX + "a=getNewslist_count";
}
