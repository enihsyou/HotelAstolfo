
public class Papers {
	private String p_id;
	private int year;
	private int month;
	private int day;
	public Papers(String p_id, int year, int month, int day) {
		this.p_id = p_id;
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public String getP_id() {
		return p_id;
	}
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	
}
