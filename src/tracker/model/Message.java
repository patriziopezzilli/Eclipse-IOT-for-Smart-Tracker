package tracker.model;

public class Message {
	
	int id;
	String mitt;
	String content;
	
	public Message(int id,String mitt, String content) {
		super();
		this.id = id;
		this.mitt = mitt;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMitt() {
		return mitt;
	}
	public void setMitt(String mitt) {
		this.mitt = mitt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	
	
}
