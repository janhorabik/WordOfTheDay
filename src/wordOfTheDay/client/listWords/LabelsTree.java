package wordOfTheDay.client.listWords;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.advancedTable.DataFilter;
import wordOfTheDay.client.listWords.advancedTable.LabelBeginFilter;
import wordOfTheDay.client.listWords.notesTable.MessagesPanel;
import wordOfTheDay.client.listWords.notesTable.NotesTable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class LabelsTree {

	private HorizontalPanel panel;
	private MessagesPanel messagesPanel;

	public LabelsTree(DatabaseOnClient database, NotesTable table,
			HorizontalPanel panel, MessagesPanel messagesPanel) {
		this.database = database;
		this.table = table;
		this.panel = panel;
		this.messagesPanel = messagesPanel;
	}

	static int MAX_LABEL_LEN = 13;
	private DatabaseOnClient database;
	private NotesTable table;
	private List<Anchor> anchors = new LinkedList<Anchor>();
	private static AskServerToChangeLabel askServerToChangeLabel;
	protected static PopupPanel popup;

	public void draw() {
		this.panel.clear();
		this.panel.add(createTree());
	}

	ScrollPanel createTree() {
		askServerToChangeLabel = new AskServerToChangeLabel(database);
		Tree tree = new Tree();
		tree.setStyleName("wordOfTheDayLeft");
		for (final String treeLabel : database.getLabelsOfCurrentDataModel()) {
			TreeItem parentItem = null;
			String[] labels = treeLabel.split(":");
			String labelSoFar = "";
			for (int i = 0; i < labels.length; ++i) {
				String label = labels[i];
				labelSoFar += label;
				if (parentItem == null) {
					// look at top:
					parentItem = findElement(tree, label);
					if (parentItem == null) {
						// create new
						parentItem = createItem(tree, label, labelSoFar);
					}
				} else {
					TreeItem currentItem = findElement(parentItem, label);
					if (currentItem == null) {
						// create new
						currentItem = createItem(parentItem, label, labelSoFar);
					}
					parentItem = currentItem;
				}
				labelSoFar += ":";
			}
		}
		ScrollPanel panel = new ScrollPanel(tree);
		panel.setHeight("500px");
		panel.setWidth("200px");
		return panel;
	}

	private TreeItem createItem(TreeItem parentItem, final String label,
			String labelSoFar) {
		HorizontalPanel panel = createPanelElement(label, labelSoFar);
		TreeItem currentItem = parentItem.addItem(panel);
		currentItem.setTitle(labelSoFar);
		currentItem.setUserObject(label);
		return currentItem;
	}

	private HorizontalPanel createPanelElement(String label,
			final String labelSoFar) {
		HorizontalPanel panel = new HorizontalPanel();
		final Anchor arrow = Arrow.createArrow();
		new Anchor("&nbsp;&nbsp;&nbsp;&nbsp;", true);
		class ArrowMouseOverHandler implements MouseOverHandler {
			public void onMouseOver(MouseOverEvent event) {
				arrow.setText(AdvancedTable.SORT_DESC_SYMBOL);
			}
		}
		;
		class ArrowMouseOutHandler implements MouseOutHandler {
			public void onMouseOut(MouseOutEvent event) {
				arrow.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;");
			}
		}
		;
		arrow.addMouseOverHandler(new ArrowMouseOverHandler());
		arrow.addMouseOutHandler(new ArrowMouseOutHandler());
		arrow.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MenuBar menu = createMenu(labelSoFar);
				LabelsTree.popup = new PopupPanel();
				popup.add(menu);
				popup.setAutoHideEnabled(true);
				popup.setPopupPosition(arrow.getAbsoluteLeft(),
						arrow.getAbsoluteTop() + 25);
				popup.show();
			}
		});
		panel.add(arrow);

		// panel.add(menu);

		Anchor anchor = createAnchor(label, labelSoFar);
		this.anchors.add(anchor);
		anchor.addMouseOverHandler(new ArrowMouseOverHandler());
		anchor.addMouseOutHandler(new ArrowMouseOutHandler());
		panel.add(anchor);
		return panel;
	}

	private MenuBar createMenu(String labelSoFar) {
		class LabelCommand implements Command {

			public LabelCommand(String labelArg, boolean rename) {
				this.label = labelArg;
				this.isRename = rename;
			}

			public void execute() {
				class Reply implements ServerResponse {
					public void error(String error) {
						messagesPanel.showMessage(error);
					}

					public void serverReplied(String reply) {
						messagesPanel.showMessage(reply);
					}
				}
				String newLabel = isRename ? newLabel = Window.prompt(
						"New label", label) : "";
				if (newLabel != null) {
					askServerToChangeLabel.setLabel(label, newLabel, !isRename);
					Reply reply = new Reply();
					askServerToChangeLabel.askServer(reply);
				}
				LabelsTree.popup.hide();
			}

			private boolean isRename;
			private String label;

		}
		;

		LabelCommand renameCommand = new LabelCommand(labelSoFar, true);
		LabelCommand removeCommand = new LabelCommand(labelSoFar, false);
		MenuBar menu = new MenuBar(false);
		menu.setAnimationEnabled(true);
		MenuBar possibilities = new MenuBar(true);
		MenuItem rename = new MenuItem("Rename", renameCommand);
		MenuItem remove = new MenuItem("Remove", removeCommand);
		possibilities.addItem(rename);
		possibilities.addItem(remove);
		menu.addItem(new MenuItem(AdvancedTable.SORT_DESC_SYMBOL, possibilities));
		menu.setWidth("15px");
		return possibilities;
		// return menu;
	}

	private TreeItem createItem(Tree tree, final String label,
			final String labelSoFar) {
		HorizontalPanel panel = createPanelElement(label, labelSoFar);
		TreeItem parentItem = tree.addItem(panel);
		parentItem.setTitle(labelSoFar);
		parentItem.setUserObject(label);
		return parentItem;
	}

	private TreeItem findElement(TreeItem parentItem, String label) {
		int numElements = parentItem.getChildCount();
		for (int i = 0; i < numElements; ++i) {
			if (parentItem.getChild(i).getUserObject().equals(label))
				return parentItem.getChild(i);
		}
		return null;
	}

	private TreeItem findElement(Tree tree, String label) {
		int numElements = tree.getItemCount();
		for (int i = 0; i < numElements; ++i)
			if (tree.getItem(i).getUserObject().equals(label)) {
				return tree.getItem(i);
			}
		return null;
	}

	private Anchor createAnchor(final String label, final String labelSoFar) {
		String shortLabel = label.length() > MAX_LABEL_LEN ? label.substring(0,
				MAX_LABEL_LEN) : label;
		final Anchor anchor = new Anchor(shortLabel);
		anchor.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
//				Dashboard.tooltipListener.labelWasPressed();
//				Dashboard.tooltip.getContainer().setText(labelSoFar);
			}
		});
		anchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				Dashboard.tooltip.hide();
				DataFilter filter = new LabelBeginFilter(labelSoFar);
				table.applyFilter(filter);
				refreshAnchors();
				anchor.setStyleName("anchorClicked");
			}
		});
		return anchor;
	}
	
	private void refreshAnchors()
	{
		for (Anchor anchor : anchors) {
			anchor.setStyleName("gwt-Anchor");
		}
	}
}
