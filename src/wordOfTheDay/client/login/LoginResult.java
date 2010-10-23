package wordOfTheDay.client.login;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoginResult implements IsSerializable, Serializable {
	private LoginResult(String message, boolean ok) {
		this.message = message;
		this.ok = ok;
	}

	private LoginResult() {

	}

	public static LoginResult createFailure(String message) {
		return new LoginResult(message, false);
	}

	public static LoginResult createSuccess(String email) {
		return new LoginResult(email, true);
	}

	public String getMessage() {
		return message;
	}

	public boolean isOk() {
		return ok;
	}

	private String message;
	private boolean ok;
}
