
public class Student {
	private String s_id;
	private String s_mail;
	private String s_name;
	private String s_passwd;
	public Student (String s_id,String s_mail,String s_name,String s_passwd){
		this.s_id=s_id;
		this.s_mail=s_mail;
		this.s_name=s_name;
		this.s_passwd=s_passwd;
	}
	public String getS_id() {
		return s_id;
	}
	public String getS_mail() {
		return s_mail;
	}
	public String getS_Name() {
		return s_name;
	}
	public String getS_passwd() {
		return s_passwd;
	}
}
