package youxueyuan;

import java.util.Scanner;

public class main {
	public static String text;

	public static void main(String[] args) {
		/**
		 * 打开图形界面:java -Dgui=true -Dfile.encoding=UTF-8 -jar xxx.jar 
		 * 命令行运行jar时直接输入文字:java -jar xxx.jar "此处填入要输入的内容"
		 */

		new ADBControler();
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
			ADBControler.sendtext(text,false,true);//这里不是GUI
		}
	}

	private void scanner() {
		Scanner input = new Scanner(System.in);
		System.out.print("在此处输入要传输的文字：");
		text = input.nextLine();
		input.close();
	}
}