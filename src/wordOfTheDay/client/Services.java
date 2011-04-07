package wordOfTheDay.client;

import com.google.gwt.core.client.GWT;

import wordOfTheDay.client.addWord.AddWordService;
import wordOfTheDay.client.listWords.NoteService;
import wordOfTheDay.client.listWords.NoteServiceAsync;

public class Services {
	public static final NoteServiceAsync noteService = GWT
			.create(NoteService.class);
}
