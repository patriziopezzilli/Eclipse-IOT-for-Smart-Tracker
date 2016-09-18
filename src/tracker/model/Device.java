package tracker.model;

public class Device {
	
	int id;
	String serial;
	
	public Device(int id, String serial) {
		super();
		this.id = id;
		this.serial = serial;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	
}
