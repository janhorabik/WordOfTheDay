package wordOfTheDay.server;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentUser7 {
//	@PrimaryKey
//	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
//	private Key key;

	@PrimaryKey
	@Persistent
	private String email;

	@Persistent
	private String passwordHash;

	public PersistentUser7(String email, String passwordHash) {
		this.email = email;
		this.passwordHash = passwordHash;
	}

//	public Key getKey() {
//		return key;
//	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
}
