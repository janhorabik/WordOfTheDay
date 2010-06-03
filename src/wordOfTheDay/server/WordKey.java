package wordOfTheDay.server;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WordKey implements Serializable, IsSerializable {

	private int date;
	private String email;

	public WordKey() {
	}

	public WordKey(int date, String email) {
		super();
		this.date = date;
		this.email = email;
	}

	public int getDate() {
		return date;
	}

	public String getEmail() {
		return email;
	}

	public String toString() {
		return date + ", " + email;
	}
}
