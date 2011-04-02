package wordOfTheDay.client.MyPopup;

public interface ServerResponse {
	void error(String error);

	void serverReplied(String reply);
	
	void askedServer(String messageAtTheBeginning);
}
