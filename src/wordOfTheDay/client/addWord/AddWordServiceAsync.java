package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word9;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AddWordService</code>.
 */
public interface AddWordServiceAsync {
	public void addWord(Word9 word, boolean add, AsyncCallback<String> callback);

	void addLabel(int date, String label, AsyncCallback<Void> callback);
}
