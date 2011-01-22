package wordOfTheDay.client;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class DragAndDrop {
	public static void test() {
		// ensure the document BODY has dimensions in standards mode
		RootPanel.get().setPixelSize(600, 600);

		// workaround for GWT issue 1813
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=1813
		RootPanel.get().getElement().getStyle().setProperty("position",
				"relative");

		// create a DragController to manage drag-n-drop actions
		// note: This creates an implicit DropController for the boundary panel
		final PickupDragController dragController = new PickupDragController(
				RootPanel.get(), true);

		// add a new image to the boundary panel and make it draggable
		final Label label = new Label("aaaaaaa");
		RootPanel.get().add(label, 40, 30);
		dragController.makeDraggable(label);
		dragController.addDragHandler(new DragHandler() {

			public void onPreviewDragStart(DragStartEvent event)
					throws VetoDragException {

			}

			public void onPreviewDragEnd(DragEndEvent event)
					throws VetoDragException {

			}

			public void onDragStart(DragStartEvent event) {
				Label newLabel = new Label(label.getText());
				RootPanel.get().add(newLabel, event.getContext().mouseX,
						event.getContext().mouseY);
				dragController.makeDraggable(newLabel);
				label.setText(event.toString());
			}

			public void onDragEnd(DragEndEvent event) {
				label.setText(event.getContext().mouseY + " "
						+ event.getContext().mouseX);
			}
		});
	}
}
