package wordOfTheDay.client.login;

import wordOfTheDay.client.LoginResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("loginServlet")
public interface LoginService extends RemoteService {
	String newUser(String email);

	String changePassword(String email, String password);

	LoginResult login(String email, String password);

	String isUserLogedIn();

	Boolean logout();
}
