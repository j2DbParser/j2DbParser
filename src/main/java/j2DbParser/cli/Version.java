package j2DbParser.cli;

public class Version {
	public static String getVersionString() {
		return "0.0.0";
	}

	public static String getTimeString() {
		return "2000-00-00 00:00";
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getVersionString());
		System.out.println(getTimeString());
	}

}
