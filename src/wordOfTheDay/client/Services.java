package wordOfTheDay.client;

import wordOfTheDay.client.dashboard.NoteService;
import wordOfTheDay.client.dashboard.NoteServiceAsync;

import com.google.gwt.core.client.GWT;

public class Services {
	public static final NoteServiceAsync noteService = GWT
			.create(NoteService.class);
}
