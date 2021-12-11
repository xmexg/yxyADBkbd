package youxueyuan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

public class ADBControler {
	public static void sendtext(String text,Boolean isGui,Boolean toBase64) {
		//这是发送base64编码:adb shell am broadcast -a ADB_INPUT_B64 --es msg "5rWL6K+V"
		//这是直接发送文字:adb shell am broadcast -a ADB_INPUT_TEXT --es msg "测试"
		ADBControler adbcontroler= new ADBControler();
		adbcontroler.commonmain("adb shell ime set com.android.adbkeyboard/.AdbIME", true);
		try {//暂停一下,免得出现开头掉字现象
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		int lengthtext = text.length();
		for (int i = 0; i < lengthtext; i++) {
			System.out.println("进度：" + (i + 1) + "/" + lengthtext + ":" + text.charAt(i));
			if(isGui) {//这里让窗口标签显示进度
				GUI.windows.setTitle("优学院考试粘贴 v2.0 (" + (i + 1) + "/" + lengthtext + ":" + text.charAt(i) + ")");
			}
			if(toBase64) {//这边编码有无法解决的大坑,留在以后解决
//				System.out.println("截字:"+text.substring(i,i+1));
//				System.out.println("getBytes():"+(text.substring(i,i+1)).getBytes());
//				System.out.println("编码为:"+Base64.getEncoder().encodeToString((text.substring(i,i+1)).getBytes()));
				adbcontroler.commonmain("adb shell am broadcast -a ADB_INPUT_B64 --es msg " + Base64.getEncoder().encodeToString((text.substring(i,i+1)).getBytes()), true);
			}else {
				adbcontroler.commonmain("adb shell am broadcast -a ADB_INPUT_TEXT --es msg " + text.charAt(i), true);//最后的true是显示回显，貌似也没什么用				
			}
		}
	}
	
	public static boolean canADBconnect() {
		StringBuffer txt = new ADBControler().commonmain("adb devices",true);
		int where = txt.indexOf("device");
		txt.delete(where , where + 8);
		if (txt.indexOf("device") != -1) {
			return true;
		}
		return false;
	}

	public StringBuffer commonmain(String cmd, boolean echo) {
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

	public String InputStream2String(InputStream inputStream) {
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
