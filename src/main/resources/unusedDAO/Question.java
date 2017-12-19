
public class Question {
	private int q_id;
	private String content;
	public Question(int q_id, String content) {
		this.q_id = q_id;
		this.content = content;
	}
	public Question(String content) {
		q_id=0;
		this.content=content;
	}
	public int getQ_id() {
		return q_id;
	}
	public String getContent() {
		return content;
	}
}
