package wordOfTheDay.client.listWords;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wordOfTheDay.client.Note;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.notesTable.DataFilter;
import wordOfTheDay.client.listWords.notesTable.LabelBeginFilter;
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
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

class LabelElement extends TreeNode {
	private String shortName;
	private String longName;

	public LabelElement(String shortName, String longName, String parentName) {
		this.shortName = shortName;
		this.longName = longName;
		setAttribute("Name", shortName);
		setAttribute("LongName", longName);
		setAttribute("Parent", parentName);
	}

	public String getShortName() {
		return this.shortName;
	}

	public String getLongName() {
		return this.longName;
	}
}

class LabelCommand implements Command {

	private MessagesPanel messagesPanel;

	public LabelCommand(String nameArg, boolean rename,
			MessagesPanel messagesPanel) {
		this.name = nameArg;
		this.isRename = rename;
		this.messagesPanel = messagesPanel;
	}

	public void execute() {
		class Reply implements ServerResponse {
			public void error(String error) {
				messagesPanel.showMessage(error);
			}

			public void serverReplied(String reply) {
				messagesPanel.showMessage(reply);
			}

			public void askedServer(String messageAtTheBeginning) {
				messagesPanel.showMessage(messageAtTheBeginning);
			}
		}
		String newName = isRename ? newName = Window.prompt("New name", name)
				: "";
		if (newName != null) {
			LabelsTree.askServerToChangeLabel.setLabel(this.name, newName,
					!isRename);
			Reply reply = new Reply();
			LabelsTree.askServerToChangeLabel.askServer(reply);
		}
	}

	private boolean isRename;
	private String name;
};

public class LabelsTree {

	private HorizontalPanel panel;
	private MessagesPanel messagesPanel;

	public LabelsTree(DatabaseOnClient database, 
			HorizontalPanel panel, MessagesPanel messagesPanel) {
		this.database = database;
		this.panel = panel;
		this.messagesPanel = messagesPanel;
		askServerToChangeLabel = new AskServerToChangeLabel(database);
	}

	static int MAX_LABEL_LEN = 13;
	private DatabaseOnClient database;
	private TreeGrid treeGrid;
	static AskServerToChangeLabel askServerToChangeLabel;
	protected static PopupPanel popup;

	public void update() {
		this.panel.clear();
		this.panel.setSpacing(30);
		this.panel.add(createSmartTree());
	}

	private TreeGrid createSmartTree() {
		treeGrid = new TreeGrid();
		treeGrid.setWidth(100);
		treeGrid.setHeight(400);
		treeGrid.setNodeIcon(null);
		TreeGridField field = new TreeGridField("Name", "Labels");
		field.setCanSort(true);
		treeGrid.setFields(field);

		final com.smartgwt.client.widgets.tree.Tree tree = new com.smartgwt.client.widgets.tree.Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("Name");
		tree.setIdField("LongName");
		tree.setParentIdField("Parent");

		LabelElement[] elements = createLabelElements();
		tree.setData(elements);

		treeGrid.setData(tree);

		Menu menu = createMenu();
		treeGrid.setContextMenu(menu);
		treeGrid.setShowHover(true);
		treeGrid.setCanHover(true);
		treeGrid.setShowEmptyMessage(false);
		treeGrid.setHoverCustomizer(new HoverCustomizer() {
			public String hoverHTML(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				return record.getAttribute("LongName");
			}
		});
		treeGrid.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			public void onClick(
					com.smartgwt.client.widgets.events.ClickEvent event) {
				LabelElement labelElement = (LabelElement) treeGrid
						.getSelectedRecord();
				if (labelElement != null) {
					database.setCurrentLabel(labelElement.getLongName());
				}
			}
		});

		treeGrid.draw();
		return treeGrid;
	}

	private Menu createMenu() {
		Menu menu = new Menu();
		menu.setWidth(150);
		menu.setShowHeaderContextMenu(true);

		com.smartgwt.client.widgets.menu.MenuItem renameItem = new com.smartgwt.client.widgets.menu.MenuItem(
				"Rename");
		renameItem
				.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					public void onClick(MenuItemClickEvent event) {
						LabelCommand renameCommand = new LabelCommand(
								((LabelElement) treeGrid.getSelectedRecord())
										.getLongName(), true, messagesPanel);
						renameCommand.execute();
					}
				});
		com.smartgwt.client.widgets.menu.MenuItem removeItem = new com.smartgwt.client.widgets.menu.MenuItem(
				"Remove");
		removeItem
				.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					public void onClick(MenuItemClickEvent event) {
						LabelCommand removeCommand = new LabelCommand(
								((LabelElement) treeGrid.getSelectedRecord())
										.getLongName(), false, messagesPanel);
						removeCommand.execute();
					}
				});
		menu.addItem(renameItem);
		menu.addItem(removeItem);
		return menu;
	}

	private LabelElement[] createLabelElements() {
		Set<String> labels = database.getLabelsOfCurrentDataModel();
		Set<String> realLabels = new HashSet<String>();
		for (String label : labels) {
			String[] splited = label.split(":");
			String longName = "";
			for (int i = 0; i < splited.length; ++i) {
				String shortName = splited[i];
				longName += i == 0 ? shortName : (":" + shortName);
				realLabels.add(new String(longName));
			}
		}
		Window.alert("Creating labels: " + realLabels);
		Set<LabelElement> labelElements = new HashSet<LabelElement>();
		for (String label : realLabels) {
			String[] splited = label.split(":");
			String shortName = splited[splited.length - 1];
			String longName = label;
			String parent = splited.length == 1 ? "" : label.substring(0,
					label.length() - shortName.length() - 1);
			labelElements.add(new LabelElement(shortName, longName, parent));
		}
		LabelElement[] a = new LabelElement[0];
		return labelElements.toArray(a);
	}

	public void newNoteWasAdded(Note note) {
		update();
	}

	public void notesWereRemoved(Set<Note> set) {
		update();
	}

}
