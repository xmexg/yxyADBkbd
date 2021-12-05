package youxueyuan;

import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		/**
		 * 坚持真理、坚守理想，践行初心、担当使命，不怕牺牲、英勇斗争，对党忠诚、不负人民的伟大建党精神 实现中华民族的伟大复兴
		 * 《中共中央关于党的百年奋斗重大成就和历史经验的决议》和《关于召开党的第二十次全国代表大会的决议》 创新、协调、绿色、开放、共享
		 */
		if (!ADBControler.canADBconnect()) {
			System.out.println("没有连接adb设备");
			System.exit(1);
		}
		Scanner input = new Scanner(System.in);
		System.out.print("在此处输入要传输的文字：");
		String text = input.nextLine();
		input.close();
		ADBControler.commonmain("adb shell ime set com.android.adbkeyboard/.AdbIME", true);
		int lengthtext = text.length();
		for (int i = 0; i < lengthtext; i++) {
			System.out.println("进度：" + (i + 1) + "/" + lengthtext);
			ADBControler.commonmain("adb shell am broadcast -a ADB_INPUT_TEXT --es msg " + text.charAt(i), true);

		}
	}
}
