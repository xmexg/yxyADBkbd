package youxueyuan;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

public class ADBControler {
	private static StringBuffer ADBListtxt;//保存了adb devices获取到的列表
	
	public static String ADBlists[];
	public static String ADBlists_noADBKeyboard[];
	public static Object nowADBdevice;
	public static String now_keyboard;
	
	ADBControler() {
		canADBconnect(false);
		getADBList();
		if(ADBlists.length!=0) {
			nowADBdevice = ADBlists[0];
		}
		get_ADBlists_noADBKeyboard_lists(false);
		manage_ADBlists_noADBKeyboard();
	}
	
	private void manage_ADBlists_noADBKeyboard() {
		if(ADBlists_noADBKeyboard == null || ADBlists_noADBKeyboard.length == 0 || ADBlists_noADBKeyboard[0].equals("")){
			//所有设备都有ADB键盘了;
		 } else {
			 
		 }
	}

	//v2.4 设备是否有adb键盘相关
	public static String[] get_ADBlists_noADBKeyboard_lists(boolean refreshADBlists) {
		if(refreshADBlists) {
			getADBList();
		}
		String[] temp = new String[ADBlists.length];
		String[] list = null;
		for(int i=0,num=0;i<ADBlists.length;i++) {
			if(!haveADBKeyboard(ADBlists[i])) {
				temp[num]=ADBlists[i];
				num++;
			}
			if(i==ADBlists.length-1) {
				list = new String[num];
				System.arraycopy(temp, 0, list, 0, num);
			}
		}
		ADBlists_noADBKeyboard=list;
		return list;
	}
	
	public static void sendtext(String text,Boolean isGui,Boolean toBase64,Boolean autoswitchkeyboard) {
		//这是发送base64编码:adb shell am broadcast -a ADB_INPUT_B64 --es msg "5rWL6K+V"
		//这是直接发送文字:adb shell am broadcast -a ADB_INPUT_TEXT --es msg "测试"
		if(autoswitchkeyboard) {//获取当前键盘
			now_keyboard=ADBControler.getnowkeyboard();
		}
		ADBControler.commonmain_adb("adb -s "+nowADBdevice+" shell ime set com.android.adbkeyboard/.AdbIME", true);
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
				GUI.windows.setTitle("优学院考试粘贴 "+Main.version+" (" + (i + 1) + "/" + lengthtext + ":" + text.charAt(i) + ")");
			}
			if(toBase64) {//这边编码有无法解决的大坑,留在以后解决
//				System.out.println("截字:"+text.substring(i,i+1));
//				System.out.println("getBytes():"+(text.substring(i,i+1)).getBytes());
//				System.out.println("编码为:"+Base64.getEncoder().encodeToString((text.substring(i,i+1)).getBytes()));
				ADBControler.commonmain_adb("adb -s "+nowADBdevice+" shell am broadcast -a ADB_INPUT_B64 --es msg " + Base64.getEncoder().encodeToString((text.substring(i,i+1)).getBytes()), true);
			}else {
				ADBControler.commonmain_adb("adb -s "+nowADBdevice+" shell am broadcast -a ADB_INPUT_TEXT --es msg " + text.charAt(i), true);//最后的true是显示回显，貌似也没什么用				
			}
		}
		if(autoswitchkeyboard) {//切换到初始键盘
			ADBControler.commonmain_adb("adb -s "+nowADBdevice+" shell ime set "+now_keyboard, false);
		}
	}
	
	public static boolean canADBconnect(boolean echo) {
		ADBListtxt = commonmain_adb("adb devices",echo);
		int where = ADBListtxt.indexOf("device");
		ADBListtxt.delete(where , where + 8);
		if (ADBListtxt.indexOf("device") != -1) {
			return true;
		}
		return false;
	}
	
	//在v2.4中新增，列出所有已连接的ADB设备
	public static String[] getADBList()
	{
		String adb_devices = commonmain_adb("adb devices",false).toString();
		String[] templist = adb_devices.split("\n");
		String[] ADBList = new String[10];
		for(int i=0,num=0;i<templist.length;i++) {
//			System.out.println(templist[i]);
			if(templist[i].indexOf("device")!=-1&&templist[i].indexOf("List")==-1) {
				ADBList[num] = templist[i].substring(0, templist[i].indexOf("device"));
				num++;
			}
		}
//		int where = ADBListtxt.indexOf("device");
//		ADBListtxt.delete(where , where + 8);
//		for(int i=0;ADBListtxt.indexOf("device") != -1;i++) {
//			ADBList[i] = ADBListtxt.substring(ADBListtxt., ADBListtxt.indexOf("\tdevice"));
//			ADBListtxt.delete(ADBListtxt.indexOf("\n"), ADBListtxt.indexOf("device")+6);
//		}
//		System.out.println("当前设备:");
		ADBlists = ADBList.clone();
		for(int i=0;i<ADBList.length;i++) {
			if(ADBList[i] == null) {
				ADBlists = new String[i];
				System.arraycopy(ADBList, 0, ADBlists, 0, i);
				break;
			}
		}
		return ADBlists;
	}
	
	//在v2.1中新增，判断系统是否有ADB
	public static boolean haveADB() {
		StringBuffer ADB = ADBControler.commonmain_adb("adb", false);
//		System.out.println(ADB);
		if(ADB!=null && (ADB.indexOf("options") != -1)) {
			return true;
		}
		System.out.println("没有ADB");
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
	
	//v2.4 是否有ADB键盘
	public static boolean haveADBKeyboard(String device) {
		String[] list = ADBControler.userKeyboard(device);
		for(int i = 0 ; i < list.length ; i++) {
			if(list[i].equals("com.android.adbkeyboard/.AdbIME")) {
				return true;
			}
		}
		return false;
	}
	
	//v2.4 是否存在ADB键盘,包括未激活的
	public static boolean existADBKeyboard(String device) {
		String[] list = ADBControler.userKeyboard_all(device);
		for(int i = 0 ; i < list.length ; i++) {
			if(list[i].equals("com.android.adbkeyboard/.AdbIME")) {
				return true;
			}
		}
		return false;
	}
	
	//v2.4 所有键盘列表,包括未激活的
	public static String[] userKeyboard_all(String device) {
		String list[] = new String[20];//不会真的有人同时安装20种输入法吧？
		StringBuffer adb = ADBControler.commonmain_adb("adb -s "+device+" shell ime list -a", false);
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
		return endlist;
	}
	
	//在v2.1新增的获取键盘列表
	public static String[] userKeyboard() {
		String list[] = new String[20];//不会真的有人同时安装20种输入法吧？
		StringBuffer adb = ADBControler.commonmain_adb("adb -s "+nowADBdevice+" shell ime list", false);
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
	
	//v2.4 获取键盘列表
	public static String[] userKeyboard(String device) {
		String list[] = new String[20];//不会真的有人同时安装20种输入法吧？
		StringBuffer adb = ADBControler.commonmain_adb("adb -s "+device+" shell ime list", false);
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
		return endlist;
	}
	
	//在v2.1中新增，获取当前使用的键盘
	public static String getnowkeyboard() {
		StringBuffer adb = ADBControler.commonmain_adb("adb -s "+nowADBdevice+" shell settings get secure default_input_method", false);
		System.out.println("当前键盘："+adb.substring(adb.indexOf("com")));
		return adb.substring(adb.indexOf("com"));//有时返回的结果不规范
	}
	
	//在v2.4中自动安装和启用ADB键盘,
	public String install_and_enable_adbkeyboard(String device) {
		String returntext = "";
		for(int i=0;!existADBKeyboard(device);i++) {
			GUI.windows.setTitle("当前设备没有ADB键盘,正在尝试安装...");
			String apkurl = "src"+File.separator+"youxueyuan"+File.separator+"ADBKeyboard.apk";
			returntext += commonmain_adb("adb -s "+device+" install "+apkurl, true);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if(i>=1) {
				GUI.windows.setTitle("正在第"+i+"次重试...");
			}
			if(i>4) {
				System.out.println("设备:"+device+" 多次安装ADB键盘失败!");
				GUI.windows.setTitle("设备:"+device+" 多次安装ADB键盘失败!");
				return returntext;
			}
		}
		for(int i=0;!haveADBKeyboard(device);i++) {
			returntext = enable_adbkeyboard(device, "com.android.adbkeyboard/.AdbIME");
			if(i>3) {
				System.out.println("设备:"+device+" 无法激活ADB键盘");
				GUI.windows.setTitle("设备:"+device+" 无法激活ADB键盘");
				return returntext;
			}
		}
		return returntext;
	}
	
	//在v2.4中安装指定程序
	public String install_adbkeyboard(String device, String apkurl) {
		String returntext = "";
		returntext += commonmain_adb("adb -s "+device+" install "+apkurl, true);
		return returntext;
	}
	
	//在v2.4中启用指定键盘
	public String enable_adbkeyboard(String device, String keyboard) {
		String returntext = "";
		returntext += commonmain_adb("adb -s "+device+" shell ime enable "+keyboard, false);
		return returntext;
	}
	
	//v2.4 让adb命令使用内置的adb程序
	public static StringBuffer commonmain_adb(String cmd, boolean echo) {
		StringBuffer echocmd = new StringBuffer();
		if(Main.inside_adb&&cmd.startsWith("adb")) {
			String adbexeurl = "src"+File.separator+"youxueyuan"+File.separator;
			String endcmd = adbexeurl + cmd;
			echocmd = commonmain_base(endcmd, echo);
		}else {
			echocmd = commonmain_base(cmd, echo);
		}
		return echocmd;
	}
	
	public static StringBuffer commonmain_base(String cmd, boolean echo) {
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
