package chinanurse.cn.nurse.NursePage.sheng_shi_qu;


import java.util.*;

public class Provence {
	private String id;
	private String name;
	private List<City> citys;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<City> getCitys() {
		return citys;
	}
	public void setCitys(List<City> citys) {
		this.citys = citys;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
