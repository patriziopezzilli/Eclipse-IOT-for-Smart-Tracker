package tracker.model;

public class Module {
	
	int id;
	String name;
	String iframe;
	int id_device;
	
	public Module(int id, String name, String iframe, int id_device) {
		super();
		this.id = id;
		this.name = name;
		this.iframe = iframe;
		this.id_device = id_device;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIframe() {
		return iframe;
	}

	public void setIframe(String iframe) {
		this.iframe = iframe;
	}

	public int getId_device() {
		return id_device;
	}

	public void setId_device(int id_device) {
		this.id_device = id_device;
	}
	
	
}
