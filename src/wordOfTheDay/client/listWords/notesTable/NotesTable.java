package wordOfTheDay.client.listWords.notesTable;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Note;
import wordOfTheDay.client.Services;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.multiFieldsPanels.MultiFieldsPanelWithSuggest;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

interface CellTableResource extends CellTable.Resources {
	public interface CellTableStyle extends CellTable.Style {

		String DEFAULT_CSS = "WordOfTheDay.css";
		// @source("wordoftheday.css")
		// CellTableStyle cellTableStyle();
	}
};

public class NotesTable {
	private DatabaseOnClient db;
	private HorizontalPanel panel;
	private CellTable<Note> table;
	private ListDataProvider<Note> dataProvider;
	private SimplePager pager;
	private CheckBoxesListener listener;
	private MultiSelectionModel<Note> selectionModel;
	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<Note> KEY_PROVIDER = new ProvidesKey<Note>() {
		public Object getKey(Note item) {
			return item == null ? null : item.getSeqNum();
		}
	};

	// mouseup
	class MyCellWithMouseUp extends AbstractSafeHtmlCell<String> {

		public MyCellWithMouseUp() {
			this(SimpleSafeHtmlRenderer.getInstance());
		}

		public MyCellWithMouseUp(SafeHtmlRenderer<String> renderer) {
			super(renderer, "mouseup");
		}

		public void onBrowserEvent(Context context, Element parent,
				String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if ("mouseup".equals(event.getType())) {
				if (valueUpdater != null) {
					valueUpdater.update(value);
				}
			}
		}

		protected void render(Context context, SafeHtml value,
				SafeHtmlBuilder sb) {
			if (value != null) {
				sb.append(value);
			}
		}
	}

	class MyMouseUpEditableCell extends EditTextCell {

		public void onBrowserEvent(Context context, Element parent,
				String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if ("mouseup".equals(event.getType())) {
				if (valueUpdater != null) {
					valueUpdater.update(value);
				}
			}
		}
	}

	class LabelsClickableTextCell extends ClickableTextCell {
		protected void render(Context context, SafeHtml value,
				SafeHtmlBuilder sb) {
			if (value != null) {
				String[] values = value.asString().split(",");
				for (String string : values) {
					sb.append(SafeHtmlUtils.fromString(string));
					sb.appendHtmlConstant("<br>");
				}
			}
		}
	}

	class FieldClickableTextCell extends ClickableTextCell {
		protected void render(Context context, SafeHtml value,
				SafeHtmlBuilder sb) {
			if (value != null) {
				String[] values = value.asString().split("\n");
				for (String string : values) {
					sb.append(SafeHtmlUtils.fromString(string));
					sb.appendHtmlConstant("<br>");
				}
			}
		}
	}

	public NotesTable(DatabaseOnClient database,
			HorizontalPanel notesTablePanel,
			CheckBoxesListener checkBoxesListener) {
		this.db = database;
		this.panel = notesTablePanel;
		this.panel.setWidth("1000px");
		this.listener = checkBoxesListener;
	}

	public void drawTable() {
		panel.clear();
		int dataModelSeqNum = db.getCurrentDataModelSeqNum();
		if ((dataModelSeqNum != -1)
				/*&& (db.getNotes(db.getCurrentDataModelSeqNum()).size() > 0)*/) {
			VerticalPanel tablePanel = new VerticalPanel();
			tablePanel.setStyleName("whiteBackground");
			// scrollPanel.setHeight("500px");
			ScrollPanel scrollPanel = new ScrollPanel();
			scrollPanel.setWidth("1000px");
			scrollPanel.add(createNotesTable(dataModelSeqNum));
			tablePanel.add(scrollPanel);
			tablePanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
			tablePanel.add(pager);

			panel.add(tablePanel);
		}
	}

	private CellTable<Note> createNotesTable(int dataModelSeqNum) {
		DataModel model = db.getModel(dataModelSeqNum);
		// Create a CellTable.

		CellTableResource resource = GWT.create(CellTableResource.class);

		table = new CellTable<Note>(10, resource);
		// table.getsetStyleName("advancedTableRow");

		// Create a data provider.
		dataProvider = new ListDataProvider<Note>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(table);

		// Add the data to the data provider, which automatically pushes it to
		// the widget.
		List<Note> list = dataProvider.getList();
		list.addAll(db.getNotes(dataModelSeqNum));

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		ListHandler<Note> sortHandler = new ListHandler<Note>(
				dataProvider.getList());
		table.addColumnSortHandler(sortHandler);

		// Create a Pager to control the table.
		addPager();
		// pager.setPageSize(3);
		// pager.setStyleName("advancedTableRow");

		// Add a selection model so we can select cells.
		addSelectionModel();

		// Checkbox column. This table will uses a checkbox column for
		// selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to
		// enable
		// mouse selection.
		addCheckBoxColumn();

		addFieldsColumns(model, sortHandler);

		addLabelsColumn();
		// table.setColumnWidth(labelsColumn, 100, Unit.PCT);

		// Set the total row count. This isn't strictly necessary, but it
		// affects
		// paging calculations, so its good habit to keep the row count up to
		// date.
		table.setRowCount(dataProvider.getList().size(), true);

		// Push the data into the widget.
		table.setWidth("800px");
		return table;
	}

	private void addSelectionModel() {
		selectionModel = new MultiSelectionModel<Note>(KEY_PROVIDER);
		table.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<Note> createCheckboxManager());
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						listener.checkBoxesChanged(!selectionModel
								.getSelectedSet().isEmpty());
					}
				});
	}

	private void addPager() {
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(table);
	}

	private void addCheckBoxColumn() {
		Column<Note, Boolean> checkColumn = new Column<Note, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(Note object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};

		table.addColumn(checkColumn);
	}

	private void addFieldsColumns(DataModel model, ListHandler<Note> sortHandler) {
		// Create fields columns.
		for (int i = 0; i < model.getFields().size(); ++i) {
			final int j = i;
			final String fieldName = model.getFields().get(j);
			Column<Note, String> column = new Column<Note, String>(
					new FieldClickableTextCell()) {
				@Override
				public String getValue(Note object) {
					return object.getField(j);
				}
			};

			column.setSortable(true);

			column.setFieldUpdater(new FieldUpdater<Note, String>() {
				public void update(int index, Note object, String value) {
					showDialogBox(j, fieldName, object);
				}

			});

			table.addColumn(column, model.getFields().get(i));
			// table.setColumnWidth(column, 100, Unit.PX);
			sortHandler.setComparator(column, new Comparator<Note>() {
				public int compare(Note o1, Note o2) {
					return o1.getField(j).compareTo(o2.getField(j));
				}
			});
		}
	}

	private void showDialogBox(final int j, final String fieldName, Note object) {
		final DialogBox dialogBox = new DialogBox(true);
		dialogBox.setText("Change field");
		dialogBox.setAnimationEnabled(true);
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.add(new Label(fieldName + ":"));
		final TextArea text = new TextArea();
		text.addStyleName("textArea");
		text.setText(object.getField(j));
		hpanel.add(text);
		panel.add(hpanel);
		HorizontalPanel closePanel = new HorizontalPanel();
		closePanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		closePanel.setSpacing(5);
		Button closeButton = new Button("Close");
		closePanel.add(closeButton);
		Button cancelButton = new Button("Cancel");
		closePanel.add(cancelButton);
		panel.add(closePanel);
		dialogBox.setWidget(panel);

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		final Note note = object;
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				note.setField(j, text.getText());
				dataProvider.refresh();
				Services.noteService.changeNote(note,
						new AsyncCallback<Void>() {
							public void onSuccess(Void result) {
								// db.update();
								db.noteWasChanged(note);
							}

							public void onFailure(Throwable caught) {
							}
						});
			}
		});
		dialogBox.center();
	}

	private void addLabelsColumn() {
		// Create label column

		Column<Note, String> labelsColumn = new Column<Note, String>(
				new LabelsClickableTextCell()) {
			public String getValue(Note object) {
				return object.getLabelsAsString();
			}
		};
		labelsColumn.setFieldUpdater(new FieldUpdater<Note, String>() {
			public void update(int index, Note object, String value) {
				showDialogBoxWithLabels(object);
			}

		});

		table.addColumn(labelsColumn, "labels");
	}

	private void showDialogBoxWithLabels(Note object) {
		final DialogBox dialogBox = new DialogBox(true);
		dialogBox.setText("Change labels");
		dialogBox.setAnimationEnabled(true);
		final MultiFieldsPanelWithSuggest panelWithSuggest = MultiFieldsPanelWithSuggest
				.create("", "Add Label", db, object.getLabels());
		VerticalPanel panel = new VerticalPanel();
		panel.add(panelWithSuggest.getPanel());
		HorizontalPanel closePanel = new HorizontalPanel();
		closePanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		closePanel.setSpacing(5);
		Button closeButton = new Button("Close");
		closePanel.add(closeButton);
		Button cancelButton = new Button("Cancel");
		closePanel.add(cancelButton);
		panel.add(closePanel);
		dialogBox.setWidget(panel);

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		final Note note = object;
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				dataProvider.refresh();
				Services.noteService.changeNote(note,
						new AsyncCallback<Void>() {
							public void onSuccess(Void result) {
//								db.update();
								db.noteWasChanged(note);
							}

							public void onFailure(Throwable caught) {
							}
						});
			}
		});
		dialogBox.center();
	}

	public void applyFilter(DataFilter dataFilter) {
		List<Note> notesFromDb = db.getNotes(db.getCurrentDataModelSeqNum());
		List<Note> notesFromProvider = dataProvider.getList();
		notesFromProvider.clear();
		for (Note note : notesFromDb) {
			if (dataFilter.accept(note))
				notesFromProvider.add(note);
		}

		// for (Iterator<Note> iter = notesFromDb.iterator(); iter.hasNext();) {
		// Note s = iter.next();
		// if (!dataFilter.accept(s)) {
		// iter.remove();
		// }
		// }
	}

	public Set<Note> getSelectedNoteIds() {
		return selectionModel.getSelectedSet();
	}

	public void newNoteWasAdded(Note note) {
		if (note.getDataModelSeqNum() == this.db.getCurrentDataModelSeqNum()) {
			databaseChanged();
//			if (db.getNotes(db.getCurrentDataModelSeqNum()).size() == 1) {
//				drawTable();
//			} else {
//				List<Note> notesFromProvider = dataProvider.getList();
//				notesFromProvider.add(note);
//			}
		}
	}

	public void databaseChanged() {
		List<Note> notesFromProvider = dataProvider.getList();
		notesFromProvider.clear();
		notesFromProvider.addAll(db.getNotes(db.getCurrentDataModelSeqNum()));
	}

	public void notesWereRemoved(Set<Note> set) {
		databaseChanged();
		selectionModel.clear();
	}
}
