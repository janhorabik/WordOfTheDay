package wordOfTheDay.client;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;

public class LinkMouseOverChangeStyleHandler implements MouseOverHandler {

	private Anchor anchor;

	public LinkMouseOverChangeStyleHandler(Anchor anchor) {
		this.anchor = anchor;
	}

	public void onMouseOver(MouseOverEvent event) {
		anchor.setStyleName("linkMouseOver");
	}
}