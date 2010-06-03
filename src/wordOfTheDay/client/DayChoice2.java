package wordOfTheDay.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum DayChoice2 implements IsSerializable, Serializable {
	YESTERDAY, TODAY, TOMORROW
}
