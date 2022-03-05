package youxueyuan;

import java.util.Scanner;

public class Main {
	public static String text;
	public static String version = "v2.5";
	public static String OS = System.getProperty("os.name");
	public static boolean inside_adb = false;
	public static String logo = "";//在v2.4更换为随机logo
//	public static String logo = (""
//			+ " ______________________________________________\r\n" 
//			+ "< 优学院考试粘贴解决方案 >\r\n" 
//			+ "< 开源项目: >\r\n"
//			+ "< https://github.com/xmexg/yxyADBkbd  > \r\n"
//			+ "< https://gitee.com/vinamex/yxyADBkbd > \r\n"
//			+ " ----------------------------------------------\r\n"
//			+ "    \\\r\n" 
//			+ "     \\\r\n" 
//			+ "                                   .::!!!!!!!:.\r\n"
//			+ "  .!!!!!:.                        .:!!!!!!!!!!!!\r\n"
//			+ "  ~~~~!!!!!!.                 .:!!!!!!!!!UWWW$$$\r\n"
//			+ "      :$$NWX!!:           .:!!!!!!XUWW$$$$$$$$$P\r\n"
//			+ "      $$$$$##WX!:      .<!!!!UW$$$$\"  $$$$$$$$#\r\n"
//			+ "      $$$$$  $$$UX   :!!UW$$$$$$$$$   4$$$$$*\r\n"
//			+ "      ^$$$B  $$$$\\     $$$$$$$$$$$$   d$$R\"\r\n" 
//			+ "        \"*$bd$$$$      '*$$$$$$$$$$$o+#\"\r\n"
//			+ "             \"\"\"\"          \"\"\"\"\"\"\"\r\n" 
//			+ "");
	
	public static void main(String[] args) throws InterruptedException {
		/**
		 * v2.2以前：
		 * 打开图形界面:java -Dgui=true -Dfile.encoding=UTF-8 -jar xxx.jar 
		 * 命令行运行jar:java -jar xxx.jar
		 * 
		 * v2.2及以后：
		 * 打开图形界面:java -jar xxx.jar 或 java -Dgui -Dfile.encoding=UTF-8 -jar xxx.jar
		 * 命令行运行jar:java -Dcmd -jar xxx.jar
		 */
		logo = new Logo().get_logo();
		System.out.println(logo);
		System.out.println("系统信息:"+System.getProperty("os.name")+" ("+System.getProperty("os.arch")+")  (java.version:"+System.getProperty("java.version")+")");

		if((((String) OS.subSequence(0, 1)).toLowerCase()).equals("l"))
			logo = Logo.logo_only_text;
		
		// 系统是否有ADB判断
		INFO_GUI info_gui = new INFO_GUI();
		if(System.getProperty("cmd") == null){
			info_gui.loading();
		}
		Thread.sleep(2000);
		if (!ADBControler.haveADB()) {
			if((((String) OS.subSequence(0, 1)).toLowerCase()).equals("w")) {
				System.out.println("当前系统没有ADB,已改用内置的ADB");
				info_gui.ERR("当前系统没有ADB<br>已改用内置的ADB");
				inside_adb = true;
				Thread.sleep(2000);
			}else {
				System.out.println("当前系统没有ADB,程序将于10秒后退出");
				info_gui.ERR("当前系统没有ADB<br>程序将于10秒后退出");
				Thread.sleep(10000);
				System.exit(2);
			}
		}
		// adb是否连接判断
		if (!ADBControler.canADBconnect(true)) {
			System.out.println("没有连接adb设备,程序将于10秒后退出");
			info_gui.ERR("没有连接adb设备<br>程序将于10秒后退出");
			Thread.sleep(10000);
			System.exit(2);
		}

		// 在v2.2中改掉了原来的启动方法
		Boolean opened = false;
		info_gui.set_windows_Visible(false);
		try {
			// 打开图形界面
			if ((System.getProperty("gui")) != null) {
				System.out.println("GUI界面");
				opened =true;
				info_gui.set_windows_Visible(false);
				new GUI().start();
			}
		} catch (Exception e) {
			System.out.println("Dgui错误");
			info_gui.ERR("无法打开图形界面");
			System.out.println(e.getMessage());
		}
		try {
			//打开命令行界面
			if((System.getProperty("cmd") != null)) {
				System.out.println("命令行界面");
				opened = true;
				new Main().scanner();
				ADBControler.sendtext(text, false, true, true);// 这里不是GUI,自动切换键盘
			}
		} catch (Exception e) {
			System.out.println("Dcmd错误");
			System.out.println(e.getMessage());
		}
		if(!opened) {
			new GUI().start();
		}

	}

	private void scanner() {
		Scanner input = new Scanner(System.in);
		System.out.print("在此处输入要传输的文字：");
		text = input.nextLine();
		input.close();
	}
}