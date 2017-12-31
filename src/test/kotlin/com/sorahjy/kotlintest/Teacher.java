
public class Teacher {
	private String t_id;
	private String t_mail;
	private String t_name;
	private String t_passwd;
	public Teacher(String t_id, String t_mail, String t_name,String t_passwd) {
		this.t_id = t_id;
		this.t_mail = t_mail;
		this.t_name = t_name;
		this.t_passwd=t_passwd;
	}
	public String getT_id() {
		return t_id;
	}
	public String getT_mail() {
		return t_mail;
	}
	public String getT_Name() {
		return t_name;
	}
	public String getT_passwd() {
		return t_passwd;
	}
	
}
