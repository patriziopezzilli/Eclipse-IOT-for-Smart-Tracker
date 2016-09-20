package tracker.model;

public class Path {
	
	int id;
	String city;
	int km;
	int time;        //express in hours
	String user;
	
	public Path(int id, String city, int km, int time, String user) {
		super();
		this.id = id;
		this.city = city;
		this.km = km;
		this.time = time;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
	
}
