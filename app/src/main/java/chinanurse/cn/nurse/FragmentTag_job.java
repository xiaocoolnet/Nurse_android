package chinanurse.cn.nurse;

/**
 * FragmentManager存储Fragment用的tag的枚举
 * tag表示Fragment的完整类名
 */
public enum FragmentTag_job {
	TAG_WORK(" chinanurse.cn.nurse.Fragment_Nurse_job.EmployWorkFragment"),
	TAG_TALENT("chinanurse.cn.nurse.Fragment_Nurse_job.EmployTalentFragment"),
	TAG_BOOK("chinanurse.cn.nurse.Fragment_Nurse_job.EmployBookFragment"),
	TAG_DETAILWORK("chinanurse.cn.nurse.NursePage.EmployDetailsActivity");



	String tag;

	private FragmentTag_job(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return tag;
	}
}
