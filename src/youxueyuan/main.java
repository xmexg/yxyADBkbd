package youxueyuan;

import java.util.Scanner;

public class main {
	public static String text;
	public static String version = "v2.3";
	public static String OS = System.getProperty("os.name");
	
	public static void main(String[] args) {
		/**
		 * v2.2以前：
		 * 打开图形界面:java -Dgui=true -Dfile.encoding=UTF-8 -jar xxx.jar 
		 * 命令行运行jar:java -jar xxx.jar
		 * 
		 * v2.2及以后：
		 * 打开图形界面:java -jar xxx.jar 或 java -Dgui -Dfile.encoding=UTF-8 -jar xxx.jar
		 * 命令行运行jar:java -Dcmd -jar xxx.jar
		 */
//		System.out.println("Java 运行时环境版本:" + System.getProperty("java.version"));
//		System.out.println("Java 运行时环境供应商:" + System.getProperty("java.vendor"));
//		System.out.println("Java 供应商的 URL:" + System.getProperty("java.vendor.url"));
//		System.out.println("java_home:" + System.getProperty("java.home"));
//		System.out.println("java_class_version:" + System.getProperty("java.class.version"));
//		System.out.println("java_class_path:" + System.getProperty("java.class.path"));
//		System.out.println("os_name:" + System.getProperty("os.name"));
//		System.out.println("os_arch:" + System.getProperty("os.arch"));
//		System.out.println("os_version:" + System.getProperty("os.version"));
//		System.out.println("user_name:" + System.getProperty("user.name"));
//		System.out.println("user_home:" + System.getProperty("user.home"));
//		System.out.println("user_dir:" + System.getProperty("user.dir"));
//		System.out.println("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
//		System.out.println("java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
//		System.out.println("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
//		System.out.println("java_vm_version:" + System.getProperty("java.vm.version"));
//		System.out.println("java_vm_vendor:" + System.getProperty("java.vm.vendor"));
//		System.out.println("java_vm_name:" + System.getProperty("java.vm.name"));
//		System.out.println("java_ext_dirs:" + System.getProperty("java.ext.dirs"));
//		System.out.println("file_separator:" + System.getProperty("file.separator"));
//		System.out.println("path_separator:" + System.getProperty("path.separator"));
//		System.out.println("line_separator:" + System.getProperty("line.separator"));
		
		System.out.println("系统信息:"+System.getProperty("os.name")+" ("+System.getProperty("os.arch")+")  (java.version:"+System.getProperty("java.version")+")");
//		ADBControler.userKeyboard();
//		测试GUI部分
//		new GUI().start();
//		try {
//			Thread.sleep(100000);
//		} catch (InterruptedException e1) {
//			// TODO 自动生成的 catch 块
//			e1.printStackTrace();
//		}
		System.out.println(" ________________________\r\n" 
				+ "< 优学院考试粘贴解决方案 >\r\n" 
				+ " ------------------------\r\n"
				+ "    \\\r\n" 
				+ "     \\\r\n" 
				+ "                                   .::!!!!!!!:.\r\n"
				+ "  .!!!!!:.                        .:!!!!!!!!!!!!\r\n"
				+ "  ~~~~!!!!!!.                 .:!!!!!!!!!UWWW$$$\r\n"
				+ "      :$$NWX!!:           .:!!!!!!XUWW$$$$$$$$$P\r\n"
				+ "      $$$$$##WX!:      .<!!!!UW$$$$\"  $$$$$$$$#\r\n"
				+ "      $$$$$  $$$UX   :!!UW$$$$$$$$$   4$$$$$*\r\n"
				+ "      ^$$$B  $$$$\\     $$$$$$$$$$$$   d$$R\"\r\n" 
				+ "        \"*$bd$$$$      '*$$$$$$$$$$$o+#\"\r\n"
				+ "             \"\"\"\"          \"\"\"\"\"\"\"\r\n" 
				+ "");
		new ADBControler();
		// adb是否连接判断
		if (!ADBControler.canADBconnect()) {
			System.out.println("没有连接adb设备");
			System.exit(1);
		}
		// 系统是否有ADB判断
		if (!ADBControler.haveADB()) {
			System.out.println("当前系统没有ADB");
			System.exit(2);
		}

		// 在v2.2中改掉了原来的启动方法
		Boolean opened = false;
		try {
			// 打开图形界面
			if ((System.getProperty("gui")) != null) {
				System.out.println("GUI界面");
				opened =true;
				new GUI().start();
			}
		} catch (Exception e) {
			System.out.println("Dgui错误");
			System.out.println(e.getMessage());
		}
		try {
			//打开命令行界面
			if((System.getProperty("cmd") != null)) {
				System.out.println("命令行界面");
				opened = true;
				new main().scanner();
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