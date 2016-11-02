package chinanurse.cn.nurse;

/**
 * FragmentManager存储Fragment用的tag的枚举
 * tag表示Fragment的完整类名
 */
public enum FragmentTag {
	TAG_NEWS("chinanurse.cn.nurse.Fragment_Main.NewsFragment"),
	TAG_STUDY("chinanurse.cn.nurse.Fragment_Main.StudyFragment"),
	TAG_ABROAD("chinanurse.cn.nurse.Fragment_Main.AbroadFragment"),
	TAG_NURSE("chinanurse.cn.nurse.Fragment_Main.SecondFragment"),
	TAG_ME("chinanurse.cn.nurse.Fragment_Main.MineFragment"),

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
