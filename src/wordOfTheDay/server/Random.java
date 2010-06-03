package wordOfTheDay.server;

public class Random {
	public static String getRandomString() {
		String ret = "";
		int max = 9;
		for (int i = 0; i < 20; i++) {
			int w = (int) Math.floor(Math.random() * max);
			ret += w;
		}
		return ret;
	}
}
