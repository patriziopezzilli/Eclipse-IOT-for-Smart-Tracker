package tracker.model;

import java.util.Date;

public class Event {
	
		String description;
		Date timestamp;
		
		public Event(String description, Date timestamp) {
			super();
			this.description = description;
			this.timestamp = timestamp;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Date getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}
		
		
}
