package wordOfTheDay.client.listWords;

import java.util.Vector;

import wordOfTheDay.client.Word8;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ListWordsService</code>.
 */
public interface ListWordsServiceAsync {
	public void listWords(AsyncCallback<Vector<Word8> > callback);
}
