package youxueyuan;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//v2.2的智障配色
//v2.0新增GUI界面
public class GUI {
	
	ADBControler cmd = new ADBControler();
	ExecutorService service = Executors.newFixedThreadPool(ADBControler.ADBlists.length);//v2.5创建线程池，里面容纳和连接设备一样多的线程
	
	// 窗口部分
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	int Screen_Width = (int) (toolkit.getScreenSize().getWidth() * 0.5);
	int Screen_Height = (int) (toolkit.getScreenSize().getHeight() * 0.5);
	public static JFrame windows = new JFrame("优学院考试粘贴 " + Main.version);// 创建窗体
	
	public void start() {
		int DEFAULE_WIDTH = (int) (Screen_Width - 340);
		int DEFAULE_HEIGH = (int) (Screen_Height - 375);
		windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置窗口关闭方式为退出并关闭程序
		windows.setBounds(DEFAULE_WIDTH, DEFAULE_HEIGH, 680, 750);// 设置默认窗口大小
		windows.setResizable(false);// 窗口不可改变大小
		Container container_main = windows.getContentPane();// 获取窗口容器
		container_main.setLayout(new FlowLayout());// 设置流布局
//		container_main.setLayout(null);//绝对布局
		container_main.setBackground(new Color(0, 91, 108));// v2.2的智障配色
		container_main.addMouseListener(new MouseListener() {//v2.3的双击删除一个文字

			public void mouseClicked(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				if (arg0.getButton()==1&&arg0.getClickCount() >= 2) {
					
//					ADBControler.sendtext("Delete", true, true, true);
					ADBControler.commonmain_adb("adb -s "+ADBControler.nowADBdevice+" shell am broadcast -a ADB_INPUT_CODE --ei code 67", false);//回删键
				}
				if (arg0.getButton()==3&&arg0.getClickCount() >= 2) {
					ADBControler.commonmain_adb("adb -s "+ADBControler.nowADBdevice+" shell am broadcast -a ADB_CLEAR_TEXT", false);
				}
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO 自动生成的方法存根
		
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO 自动生成的方法存根

			}

			public void mousePressed(MouseEvent arg0) {
				// TODO 自动生成的方法存根

			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO 自动生成的方法存根

			}
		});

		// 文本框部分
		JTextArea jtextarea_input = new JTextArea(Main.logo + "\n" + "请在此处填入要粘贴的答案", 38, 90);// 默认显示的字,x行y列30,50
		if ((((String) Main.OS.subSequence(0, 1)).toLowerCase()).equals("w")) {// windows系统
			jtextarea_input.setRows(38);
			jtextarea_input.setColumns(90);
		} else {// 其他系统
			jtextarea_input.setRows(30);
			jtextarea_input.setColumns(50);
		}
		jtextarea_input.setLineWrap(true);// 自动换行
		jtextarea_input.setFont(new Font("黑体", Font.PLAIN, 14));// 设置显示的文字
		JScrollPane jscrollpane = new JScrollPane(jtextarea_input);// 创建滚动面板,并给文本框添加滚动条
		jtextarea_input.setBackground(new Color(0, 146, 174));// v2.2的智障配色
		jtextarea_input.setForeground(new Color(204,251,234));
		// 按钮部分
		JButton jbutton_send = new JButton("发送文字");
		JButton jbutton_exit = new JButton("终止任务");//v2.5中改成了停止线程
		
		// 监控发送按钮
		jbutton_send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean ok_to_send_txt = cmd.install_and_enable_adbkeyboard((String)cmd.nowADBdevice);//确保当前设备有adbkeyboard
				//v2.5的发送时启动一个新线程
				if(ok_to_send_txt) {
					//第一种可行的方法
//					Thread sendtxt = new Thread(new Runnable() {
//						@Override
//						public void run() {
//							// TODO 自动生成的方法存根
//							ADBControler.sendtext(jtextarea_input.getText(), true, true, true);// 获取输入框中的文本并发送到手机
//						}
//					});
//					sendtxt.start();
					
					//第二种可行的方法
					//这里如果线程池满了的话，先执行1线程，然后再执行2线程
					service.submit(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							ADBControler.sendtext(jtextarea_input.getText(), true, true, true);
						}
					});
					
				}
				jtextarea_input.requestFocus();// 焦点回到输入框上
			}
		});
		// 监控退出按钮
		jbutton_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.exit(0);// 退出程序
				//v2.5结束所有线程
				service.shutdownNow();
				service = Executors.newFixedThreadPool(ADBControler.ADBlists.length);
			}
		});

		// 底部标签部分,在v2.2中用于监控鼠标和键盘动作，可根据滚轮滑动来移动手机光标，根据键盘功能键来操作手机文本
		JLabel jlabel_useless = new JLabel("字数统计");// 占位而已，在v2.2加入了些新功能
		jlabel_useless.setText("总计:" + jtextarea_input.getText().length() + "字");
//		jlabel_useless.setOpaque(false);//设置标签不透明
//		jlabel_useless.setBackground(Color.orange);//标签背景颜色
		jlabel_useless.setForeground(new Color(193,205,216));// 标签字体颜色
		jlabel_useless.setFont(new java.awt.Font("宋体", 1, 20));// 设置字体为宋体，加粗，20号字
		jtextarea_input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {// 当输入框文字改变时重新计算文字数量
				// TODO 自动生成的方法存根
				jlabel_useless.setText("总计:" + jtextarea_input.getText().length() + "字");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO 自动生成的方法存根
				jlabel_useless.setText("总计:" + jtextarea_input.getText().length() + "字");

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO 自动生成的方法存根
				jlabel_useless.setText("总计:" + jtextarea_input.getText().length() + "字");

			}
		});

		// v2.1新增的键盘下拉列表标签提示
		JLabel choice_Keyboard_tip = new JLabel("选择键盘:  ");
		choice_Keyboard_tip.setForeground(Color.WHITE);// 字体调成白色

		// v2.1新增键盘下拉列表
		JComboBox<String> comboBox = new JComboBox<>(ADBControler.userKeyboard());// 创建一个下拉列表框
		comboBox.setBackground(new Color(0, 72, 85));
		comboBox.setForeground(new Color(249, 244, 220));
		// v2.4删掉了之前版本的确定按钮,从此版本开始选中即确定
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				ADBControler.commonmain_adb("adb -s "+ADBControler.nowADBdevice+" shell ime set " + comboBox.getSelectedItem(), false);
			}
		});

		// v2.4新增的设备下拉列表标签提示
		JLabel choice_devices_tip = new JLabel("选择设备:  ");
		choice_devices_tip.setForeground(Color.WHITE);// 字体调成白色

		// v2.4新增设备下拉列表
		JComboBox<String> comboBox_devices = new JComboBox<>(ADBControler.ADBlists);// 创建一个下拉列表框
		comboBox_devices.setBackground(new Color(0, 72, 85));
		comboBox_devices.setForeground(new Color(249, 244, 220));
		comboBox_devices.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				windows.setTitle("正在切换设备...");
				Object chooseDevice = comboBox_devices.getSelectedItem();
				ADBControler.nowADBdevice = chooseDevice;
				ADBControler.now_keyboard = ADBControler.getnowkeyboard();
				//下面3行都是刷新键盘下拉列表,只有第三种有用,参考https://blog.csdn.net/wuyujin1997/article/details/90141392
//				comboBox.revalidate();
//				comboBox.repaint();
				cmd.install_and_enable_adbkeyboard((String)chooseDevice);
				String[] userkeyboardlist = ADBControler.userKeyboard();
				comboBox.setModel(new DefaultComboBoxModel<>(userkeyboardlist));//刷新键盘下拉列表
				windows.setTitle("优学院考试粘贴 "+Main.version);
			}

		});
		
		// 把组件添加到容器
		container_main.add(choice_Keyboard_tip);// v2.1版本新增的键盘下拉列表标签提示
		container_main.add(comboBox);// v2.1版本的键盘下拉列表
		container_main.add(jscrollpane);// 容器添加滚动面板
		container_main.add(choice_devices_tip); //v2.4的设备下拉提示
		container_main.add(comboBox_devices); //v2.4的设备下拉列表
		container_main.add(jbutton_send);
		container_main.add(jlabel_useless);
		container_main.add(jbutton_exit);

		windows.setVisible(true);// 使窗体可见
	}
}
