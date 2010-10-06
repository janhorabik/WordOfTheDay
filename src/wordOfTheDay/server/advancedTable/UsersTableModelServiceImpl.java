/**
 * Sample reference implementation of the TableModelService class.
 * For simplicity it does not use database but shows how to implement
 * data paging, sorting and filtering.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.server.advancedTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import wordOfTheDay.client.Word8;
import wordOfTheDay.client.advancedTable.DataFilter;
import wordOfTheDay.client.advancedTable.TableColumn;
import wordOfTheDay.client.advancedTable.TableModelService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord22;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UsersTableModelServiceImpl extends RemoteServiceServlet implements
		TableModelService {

	private static final long serialVersionUID = 1L;

	private TableColumn[] columns = new TableColumn[] {
			new TableColumn("Date", "Date"), new TableColumn("Name", "Name"),
			new TableColumn("Explanation", "Explanation"),
			new TableColumn("Usage", "Usage"), new TableColumn("Labels", "Labels") };

	private Word8[] allWords = new Word8[0];

	private List<Word8> filteredWords;

	private Vector<Word8> getWords() {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		if (email == null)
			email = "NULL";
		List<PersistentWord22> persistentWords = PMF.getAllWords(email);
		Vector<Word8> ret = new Vector<Word8>();
		for (PersistentWord22 persistentWord : persistentWords) {
			System.out.println("word: " + persistentWord);
			ret.add(PMF.persistentWordToWordWithPreviousPossible(
					persistentWord, true));
		}
		return ret;
	}

	void initiate() {
		Vector<Word8> words = getWords();
		System.out.println("initiate begining: " + words.size());
		allWords = new Word8[0];
		allWords = words.toArray(allWords);
		System.out.println("initiate: " + allWords.length);
	}

	public UsersTableModelServiceImpl() {
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
		Word8[] rowsData = getRowsData(startRow, rowsCount, filters,
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

	private Word8[] getRowsData(int startRow, int rowsCount,
			DataFilter[] filters, String sortColumn, boolean sortingOrder) {
		applyDataFilters(filters);
		applySorting(sortColumn, sortingOrder);
		Word8[] rows = new Word8[rowsCount];
		for (int row = startRow; row < startRow + rowsCount; row++) {
			rows[row - startRow] = this.filteredWords.get(row);
		}
		return rows;
	}

	private void applyDataFilters(DataFilter[] filters) {
		this.filteredWords = new ArrayList<Word8>();
		if (filters == null) {
			// No filter - append all users
			for (Word8 word : this.allWords) {
				this.filteredWords.add(word);
			}
		} else {
			// Simulate data filtering
			String keyword = filters[0].getValue().toUpperCase();
			System.out.println("simulate filtering");
			for (Word8 word : this.allWords) {
				System.out.println("check " + word);
				String name = word.getName();
				if (name == null) {
					name = "";
				}
				String explanation = word.getExplanation();
				if (explanation == null) {
					explanation = "";
				}
				List<String> tags = word.getLabels();
				if (tags == null) {
					tags = new LinkedList<String>();
				}
				System.out.println("tags" + tags);
				List<String> usage = word.getUsage();
				if (name.toUpperCase().contains(keyword)
						|| explanation.toUpperCase().contains(keyword)
						|| contains(tags, keyword) || contains(usage, keyword)) {
					System.out.println("adding " + word);
					this.filteredWords.add(word);
				}
			}
		}
	}

	private static boolean contains(List<String> data, String value) {
		for (String element : data) {
			if (element.toUpperCase().contains(value)) {
				return true;
			}
		}
		return false;
	}

	private void applySorting(String sortColumn, boolean sortingOrder) {
		if (sortColumn != null) {
			UserComparator userComparator = new UserComparator(sortColumn,
					sortingOrder);
			Collections.sort(this.filteredWords, userComparator);
		}
	}

}
