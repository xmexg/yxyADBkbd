package youxueyuan;

import java.util.Scanner;

public class main {
	public static String text;

	public static void main(String[] args) {
		/**
		 * 打开图形界面:java -Dgui=true -Dfile.encoding=UTF-8 -jar xxx.jar 
		 * 命令行运行jar时直接输入文字:java -jar xxx.jar "此处填入要输入的内容"
		 */

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
		//系统是否有ADB判断
		if (!ADBControler.haveADB()) {
			System.out.println("当前系统没有ADB");
			System.exit(2);
		}
		// adb是否连接判断
		if (!ADBControler.canADBconnect()) {
			System.out.println("没有连接adb设备");
			System.exit(1);
		}

		try {
			// 打开图形界面
			if ((System.getProperty("gui").toLowerCase()).equals("true")) {
				new GUI().start();
			}
			System.out.println("GUI界面");
		} catch (NullPointerException e) {
			// 在命令行中输入文字
			new main().scanner();
			ADBControler.sendtext(text,false,true,true);//这里不是GUI,自动切换键盘
		}
	}

	private void scanner() {
		Scanner input = new Scanner(System.in);
		System.out.print("在此处输入要传输的文字：");
		text = input.nextLine();
		input.close();
	}
}