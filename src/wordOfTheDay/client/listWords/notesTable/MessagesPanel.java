package wordOfTheDay.client.listWords.notesTable;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

class TimerToClearText extends Timer {
	private Label label;

	public TimerToClearText(Label label) {
		this.label = label;
	}

	public void run() {
		label.setText("");
	}
}

public class MessagesPanel {
	private HorizontalPanel panel;
	private Label label;
	private TimerToClearText timer;
	private static int delay = 5000;

	public MessagesPanel(HorizontalPanel panelForMessages) {
		this.label = new Label();
		this.label.setStyleName("messageBackground");
		this.timer = new TimerToClearText(label);
		timer.schedule(delay);
		setPanel(panelForMessages);
	}
	
	public void setPanel(HorizontalPanel panel)
	{
		this.panel = panel;
		this.panel.setHeight("30px");
		this.panel.add(label);
	}

	public void showMessage(String message) {
		this.label.setText(message);
		timer.schedule(delay);
	}
}
