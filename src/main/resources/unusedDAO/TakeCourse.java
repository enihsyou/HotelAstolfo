
public class TakeCourse {
	private String s_id;
	private String t_id;
	private double u_grade;
	private double m_grade;
	private double f_score;
	public TakeCourse(String s_id, String t_id, double u_grade, double m_grade, double f_score) {
		this.s_id = s_id;
		this.t_id = t_id;
		this.u_grade = u_grade;
		this.m_grade = m_grade;
		this.f_score = f_score;
	}
	public String getS_id() {
		return s_id;
	}
	public String getT_id() {
		return t_id;
	}
	public double getU_grade() {
		return u_grade;
	}
	public double getM_grade() {
		return m_grade;
	}
	public double getF_score() {
		return f_score;
	}
}
