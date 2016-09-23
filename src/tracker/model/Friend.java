package tracker.model;

public class Friend {
	
	int id;
	String email;
	int totKm;
	String name;
	int active;
	
	public Friend(int id, String email, int totKm) {
		super();
		this.id = id;
		this.email = email;
		this.totKm = totKm;
	}
	public Friend(int id, String email, int totKm, String name,int active) {
		super();
		this.id = id;
		this.email = email;
		this.totKm = totKm;
		this.name = name;
		this.active = active;
	}
	
	public Friend(int id, String email, int totKm, String name) {
		super();
		this.id = id;
		this.email = email;
		this.totKm = totKm;
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTotKm() {
		return totKm;
	}

	public void setTotKm(int totKm) {
		this.totKm = totKm;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	
	
	
}
