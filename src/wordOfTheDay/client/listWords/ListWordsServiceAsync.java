package wordOfTheDay.client.listWords;

import java.util.Vector;

import wordOfTheDay.client.Word7;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ListWordsService</code>.
 */
public interface ListWordsServiceAsync {
	public void listWords(AsyncCallback<Vector<Word7> > callback);
}
