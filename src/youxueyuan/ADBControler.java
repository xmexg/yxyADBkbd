package youxueyuan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ADBControler {
	public static boolean canADBconnect() {
		StringBuffer txt = commonmain("adb devices",true);
		int where = txt.indexOf("device");
		txt.delete(where , where + 8);
		if (txt.indexOf("device") != -1) {
			return true;
		}
		return false;
	}

	public static StringBuffer commonmain(String cmd, boolean echo) {
		StringBuffer echocmd = new StringBuffer();
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd);
			echocmd = new StringBuffer(InputStream2String(process.getInputStream()));
			if (echo) {
				System.out.println("=========================" + "\n" + echocmd  + "=========================");
			}
			return echocmd;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String InputStream2String(InputStream inputStream) {
		String result = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String temp = "";
			while ((temp = br.readLine()) != null) {
				result += temp + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
