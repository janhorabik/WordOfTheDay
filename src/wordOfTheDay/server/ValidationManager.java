package wordOfTheDay.server;

public final class ValidationManager {
	public static String validate(String input) {
		if (input == null || input.equals(""))
			input = " ";
		char[] charsInput = input.toCharArray();
		String regex = "[#<>`~]";
		for (int i = 0; i < charsInput.length; i++) {
			char[] charTable = new char[1];
			charTable[0] = charsInput[i];
			String charString = new String(charTable);
			if (charString.matches(regex)) {
				charsInput[i] = ' ';
			}
		}
		return new String(charsInput);
	}

	public static void main(String[] args) {
		System.out.println(validate("/a\\f !:)t"));
		System.out.println(validate("a<>ft"));
		System.out.println(validate("ąćź'`<fŻt"));
		System.out.println("janhorabik@gmail.com: " + emailIsValid("janhorabik@gmail.com"));
		System.out.println("janhorabikgmail.com: " + emailIsValid("janhorabikgmail.com"));
		System.out.println("janhorabik@gmail: " + emailIsValid("janhorabik@gmail"));
	}
	
	public static boolean emailIsValid(String input){
		if (input == null || input.equals(""))
			return false;
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";
		return input.toUpperCase().matches(regex);
	}
	
}