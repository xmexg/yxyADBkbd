package youxueyuan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

public class ADBControler {
	public static String now_keyboard;
	public static void sendtext(String text,Boolean isGui,Boolean toBase64,Boolean autoswitchkeyboard) {
		//这是发送base64编码:adb shell am broadcast -a ADB_INPUT_B64 --es msg "5rWL6K+V"
		//这是直接发送文字:adb shell am broadcast -a ADB_INPUT_TEXT --es msg "测试"
		if(autoswitchkeyboard) {//获取当前键盘
			now_keyboard=ADBControler.getnowkeyboard();
		}
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
				GUI.windows.setTitle("优学院考试粘贴 "+main.version+" (" + (i + 1) + "/" + lengthtext + ":" + text.charAt(i) + ")");
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
		if(autoswitchkeyboard) {//切换到初始键盘
			adbcontroler.commonmain("adb shell ime set "+now_keyboard, false);
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
	
	//在v2.1中新增，判断系统是否有ADB
	public static boolean haveADB() {
		ADBControler adbcontroler= new ADBControler();
		if((adbcontroler.commonmain("adb", false)).indexOf("options") != -1) {
			return true;
		}
		return false;
	}
	
	//在v2.1新增的判断是否有ADB键盘
	public static boolean haveADBKeyboard() {
		String[] list = ADBControler.userKeyboard();
		for(int i = 0 ; i < list.length ; i++) {
			if(list[i].equals("com.android.adbkeyboard/.AdbIME")) {
				return true;
			}
		}
		return false;
	}
	
	 //在v2.1新增的获取键盘列表
	public static String[] userKeyboard() {
		String list[] = new String[20];//不会有人同时安装20种输入法吧？
		ADBControler adbcontroler= new ADBControler();
		StringBuffer adb = adbcontroler.commonmain("adb shell ime list", false);
		int where_start = adb.indexOf("mId=");
		int list_num = 0;
		int where_this = adb.indexOf("mId=", where_start);
		int where_end = adb.indexOf(" mSettingsActivityName" , where_start);//要有个空格
		while(where_this != -1){
			list[list_num] = adb.substring(where_this+4, where_end);
			list_num++;
			where_this = adb.indexOf("mId=", where_end);
			where_end = adb.indexOf(" mSettingsActivityName" , where_this);
		}
		int useful = -1;
		for(int i=0;i<list.length;i++) {
			useful++;
			if(list[i]==null) {
				break;
			}
		}
		String endlist[] = new String[useful];
		System.arraycopy(list, 0, endlist, 0,useful);
		//测试使用
//		for(int i=0;i<endlist.length;i++) {
//			System.out.println(useful+endlist[i]);
//		}
		return endlist;
	}
	
	//在v2.1中新增，获取当前使用的键盘
	public static String getnowkeyboard() {
		ADBControler adbcontroler= new ADBControler();
		StringBuffer adb = adbcontroler.commonmain("adb shell settings get secure default_input_method", false);
		System.out.println("当前键盘："+adb.substring(adb.indexOf("com")));
		return adb.substring(adb.indexOf("com"));//有时返回的结果不规范
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
