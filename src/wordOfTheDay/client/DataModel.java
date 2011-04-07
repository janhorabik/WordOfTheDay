package wordOfTheDay.client;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.widgets.tree.TreeNode;

public class DataModel implements IsSerializable, Serializable,
		Comparable<DataModel> {
	private String email;

	private int seqNum;

	private List<String> fields;

	private String name;

	public DataModel() {
	}

	public void setName(String nameArg) {
		this.name = nameArg;
	}

	@Override
	public int compareTo(DataModel compared) {
		return this.getSeqNum() > compared.getSeqNum() ? 1 : -1;
	}

	public DataModel(String email, int seqNum, String name, List<String> fields) {
		super();
		this.email = email;
		this.seqNum = seqNum;
		this.name = name;
		this.fields = fields;
	}

	public String getEmail() {
		return email;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public List<String> getFields() {
		return fields;
	}

	public String getName() {
		return name;
	}

	public void setSeqNum(Integer result) {
		this.seqNum = result;
	};
}
