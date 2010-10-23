/**
 * Sample reference implementation of the TableModelService class.
 * For simplicity it does not use database but shows how to implement
 * data paging, sorting and filtering.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.WordComparator;
import wordOfTheDay.client.advancedTable.DataFilter;
import wordOfTheDay.client.advancedTable.TableColumn;
import wordOfTheDay.client.advancedTable.TableModelService;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord24;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LocalGetWordsServiceImpl implements TableModelService {

	private static final long serialVersionUID = 1L;

	private TableColumn[] columns = new TableColumn[] {
			new TableColumn("Date", "Date"), new TableColumn("Name", "Name"),
			new TableColumn("Explanation", "Explanation"),
			new TableColumn("Usage", "Usage"),
			new TableColumn("Labels", "Labels") };

	private Word9[] allWords = new Word9[0];

	private List<Word9> filteredWords;

	private DatabaseOnClient database;

	private Vector<Word9> getWords() {
		return this.database.getWords();
	}

	public LocalGetWordsServiceImpl(DatabaseOnClient database) {
		this.database = database;
	}

	void initiate() {
		Vector<Word9> words = getWords();
		System.out.println("initiate begining: " + words.size());
		allWords = new Word9[0];
		allWords = words.toArray(allWords);
		System.out.println("initiate: " + allWords.length);
	}

	public LocalGetWordsServiceImpl() {
		this.applyDataFilters(null);
	}

	public TableColumn[] getColumns() {
		return this.columns;
	}

	public int getRowsCount(DataFilter[] filters) {
		initiate();
		applyDataFilters(filters);
		int count = this.filteredWords.size();
		System.out.println("rows count: " + count);
		return count;
	}

	public String[][] getRows(int startRow, int rowsCount,
			DataFilter[] filters, String sortColumn, boolean sortingOrder) {
		initiate();
		System.out.println("rowsCount " + rowsCount);
		Word9[] rowsData = getRowsData(startRow, rowsCount, filters,
				sortColumn, sortingOrder);
		int columnsCount = this.columns.length;
		String[][] rows = new String[rowsCount][columnsCount];
		for (int row = 0; row < rowsCount; row++) {
			for (int col = 0; col < columnsCount; col++) {
				String columnName = this.columns[col].getName();
				rows[row][col] = ReflectionUtils.getPropertyStringValue(
						rowsData[row], columnName);
			}
		}
		return rows;
	}

	private Word9[] getRowsData(int startRow, int rowsCount,
			DataFilter[] filters, String sortColumn, boolean sortingOrder) {
		applyDataFilters(filters);
		applySorting(sortColumn, sortingOrder);
		Word9[] rows = new Word9[rowsCount];
		for (int row = startRow; row < startRow + rowsCount; row++) {
			rows[row - startRow] = this.filteredWords.get(row);
		}
		return rows;
	}

	private void applyDataFilters(DataFilter[] filters) {
		this.filteredWords = new ArrayList<Word9>();
		if (filters == null) {
			// No filter - append all words
			for (Word9 word : this.allWords) {
				this.filteredWords.add(word);
			}
		} else
			for (Word9 word : this.allWords) {
				if (filters[0].accept(word))
					this.filteredWords.add(word);
			}
	}

	private void applySorting(String sortColumn, boolean sortingOrder) {
		if (sortColumn != null) {
			WordComparator userComparator = new WordComparator(sortColumn,
					sortingOrder);
			Collections.sort(this.filteredWords, userComparator);
		}
	}

}
