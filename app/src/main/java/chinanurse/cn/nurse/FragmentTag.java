package chinanurse.cn.nurse;

/**
 * FragmentManager存储Fragment用的tag的枚举
 * tag表示Fragment的完整类名
 */
public enum FragmentTag {
	/**
	 * MainActivity 下的Fragment
	 */
	TAG_NEWS("chinanurse.cn.nurse.Fragment_Main.NewsFragment"),
	TAG_STUDY("chinanurse.cn.nurse.Fragment_Main.StudyFragment"),
	TAG_ABROAD("chinanurse.cn.nurse.Fragment_Main.AbroadFragment"),
	TAG_NURSE("chinanurse.cn.nurse.Fragment_Main.NurseFragment"),
	TAG_MINE("chinanurse.cn.nurse.Fragment_Main.MineFragment"),
	/**
	 * NurseFragment下的Fragment
	 */
	TAG_COMMUNITY("chinanurse.cn.nurse.Fragment_Nurse.CommunityFragment"),
	TAG_EMPLOY("chinanurse.cn.nurse.Fragment_Nurse.EmployFragment"),
	/**
	 * CommunityFragment下的Fragment
	 *
	 */
	TAG_FIND("chinanurse.cn.nurse.Fragment_Nurse.FindFragment"),
	TAG_FOLLOW_FORUM_LIST("chinanurse.cn.nurse.Fragment_Nurse.FollowForumListFragment"),
	TAG_ME("chinanurse.cn.nurse.Fragment_Nurse.MeFragment"),
	TAG_MY_FORUM_CONTENT("chinanurse.cn.nurse.Fragment_Nurse.MyForumCommentFragment"),
	TAG_MY_FORUM_LIST("chinanurse.cn.nurse.Fragment_Nurse.MyForumListFragment"),
	/**
	 * EmployFragment下的Fragment
	 */
	TAG_WORK(" chinanurse.cn.nurse.Fragment_Nurse_job.EmployWorkFragment"),
	TAG_TALENT("chinanurse.cn.nurse.Fragment_Nurse_job.EmployTalentFragment"),
	TAG_BOOK("chinanurse.cn.nurse.Fragment_Nurse_job.EmployBookFragment"),
	TAG_DETAILWORK("chinanurse.cn.nurse.Fragment_Nurse_job.EmployDetailsfragment"),
	TAG_DETAILTALENT("chinanurse.cn.nurse.Fragment_Nurse_job.EmployResumeDetailsfragment");





	String tag;

	private FragmentTag(String tag){
		this.tag = tag;
	}

	public String getTag(){
		return tag;
	}
}
