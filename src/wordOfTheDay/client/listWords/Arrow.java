package wordOfTheDay.client.listWords;

import com.google.gwt.user.client.ui.Anchor;

public class Arrow {
	public static Anchor createArrow() {

		final Anchor arrow = new Anchor("&nbsp;&nbsp;&nbsp;&nbsp;", true);
//		class ArrowMouseOverHandler implements MouseOverHandler {
//			public void onMouseOver(MouseOverEvent event) {
//				arrow.setText("^");
//			}
//		}
//		;
//		class ArrowMouseOutHandler implements MouseOutHandler {
//			public void onMouseOut(MouseOutEvent event) {
//				arrow.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;");
//			}
//		}
//		;
//		arrow.addMouseOverHandler(new ArrowMouseOverHandler());
//		arrow.addMouseOutHandler(new ArrowMouseOutHandler());

		return arrow;
	}
}
