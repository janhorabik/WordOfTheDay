package wordOfTheDay.client.login;

import wordOfTheDay.client.LoginResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GetTodaysService</code>.
 */
public interface LoginServiceAsync {
	public void newUser(String email, AsyncCallback<String> callback);

	public void changePassword(String email, String password,
			AsyncCallback<String> callback);

	public void login(String email, String password,
			AsyncCallback<LoginResult> callback);

	public void isUserLogedIn(AsyncCallback<String> callback);
	
	public void logout(AsyncCallback<Boolean> callback);
}
