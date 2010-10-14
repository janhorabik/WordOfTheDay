package wordOfTheDay.client;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;

public class LinkMouseOutChangeStyleHandler implements MouseOutHandler {

	private Anchor anchor;

	public LinkMouseOutChangeStyleHandler(Anchor anchor) {
		this.anchor = anchor;
	}

	public void onMouseOut(MouseOutEvent event) {
		anchor.setStyleName("previousNextLink");
	}
}