package wordOfTheDay.server.service;

import java.util.logging.Logger;

import wordOfTheDay.client.LoginResult;
import wordOfTheDay.client.login.LoginService;
import wordOfTheDay.server.MessageSender;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.Random;
import wordOfTheDay.server.ValidationManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	private static final Logger log = Logger
	.getLogger(LoginServiceImpl.class.getName());
	
	public String newUser(String email) {
		if (!ValidationManager.emailIsValid(email))
			return "This is not a valid email address";
		if (PMF.hasUser(email))
			return "User with this email address is already in database";
		String password = Random.getRandomString();
		String hashedPassword = Hasher.getHash(password);
		PMF.storeUser(email, hashedPassword);
		MessageSender.sendMessage("janhorabik@gmail.com",
				"Registration at Word Of The Day",
				"You have successfully registered to Word Of The Day. Your password is: "
						+ hashedPassword, email, "smtp.gmail.com");
		return "User added successfully, email with password has been send to "
				+ email;
	}

	public String changePassword(String email, String password) {
		String hashedPassword = Hasher.getHash(password);
		PMF.changePassword(email, hashedPassword);
		return "Password changed";
	}

	public LoginResult login(String email, String password) {
		log.info("login start");
		String hashedPassword = Hasher.getHash(password);
		String hashedPasswordFromDb = PMF.getHashedPasswordOf(email);
		log.info("login got from db");
		// if (hashedPasswordFromDb == null)
		// return LoginResult.createFailure("Login and password do not match");
		if (true/* hashedPassword.equals(hashedPasswordFromDb) */) {
			System.out.println(this.getThreadLocalRequest().getSession()
					.toString());
			log.info("login setting email session");
			this.getThreadLocalRequest().getSession().setAttribute("email",
					email);
			log.info("session set");
			return LoginResult.createSuccess(email);
		}
		return LoginResult.createFailure("Login and password do not match");
	}

	public String isUserLogedIn() {
		return (String) getThreadLocalRequest().getSession().getAttribute("email");
	}

	public Boolean logout() {
		this.getThreadLocalRequest().getSession().setAttribute("email", null);
		return true;
	}
}
