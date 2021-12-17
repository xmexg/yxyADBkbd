package youxueyuan;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//v2.0新增GUI界面
public class GUI {
	public static JFrame windows = new JFrame("优学院考试粘贴 v2.0");//创建窗体
	public void start() {
		//窗口部分
		windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗口关闭方式为退出并关闭程序
		windows.setBounds(150, 150, 500, 650);//设置默认窗口大小
		windows.setResizable(false);//窗口不可改变大小
		Container container_main = windows.getContentPane();//获取窗口容器
		container_main.setLayout(new FlowLayout());//设置流布局
		
		//文本框部分
		JTextArea jtextarea_input = new JTextArea("开源项目:https://github.com/xmexg/yxyADBkbd"+"\n"+"请在此处填入要粘贴的答案",30,65);//默认显示的字,x行y列
		jtextarea_input.setLineWrap(true);//自动换行
		jtextarea_input.setFont(new Font("黑体",Font.PLAIN,14));
		JScrollPane jscrollpane = new JScrollPane(jtextarea_input);//创建滚动面板,并给文本框添加滚动条
		
		
		//按钮部分
		JButton jbutton_send = new JButton("发送");
		JButton jbutton_exit = new JButton("退出");
		//监控发送按钮
		jbutton_send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ADBControler.sendtext(jtextarea_input.getText(),true,true,true);//获取输入框中的文本并发送到手机
				jtextarea_input.requestFocus();//焦点回到输入框上
			}
		});
		//监控退出按钮
		jbutton_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);//退出程序
			}
		});
		
		//底部标签部分
		JLabel jlabel_useless = new JLabel("                                    ");//占位而已
		
		//v2.1新增的键盘下拉列表标签提示
		JLabel choice_Keyboard_tip = new JLabel("选择键盘:  ");
		
		//v2.1新增键盘下拉列表
		String list[] = ADBControler.userKeyboard();
		JComboBox<String> comboBox = new JComboBox<>(list);//创建一个下拉列表框
		
		//v2.1新增的键盘下拉列表确定按钮
		JButton choice_Keyboard = new JButton("确定");
		choice_Keyboard.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ADBControler cmd = new ADBControler();
				cmd.commonmain("adb shell ime set "+comboBox.getSelectedItem(), false);
				
			}
		});
		
		//把组件添加到容器
		container_main.add(choice_Keyboard_tip);//v2.1版本新增的键盘下拉列表标签提示
		container_main.add(comboBox);//v2.1版本的键盘下拉列表
		container_main.add(choice_Keyboard);//v2.1版本的确定按钮
		container_main.add(jscrollpane);//容器添加滚动面板
		container_main.add(jbutton_send);
		container_main.add(jlabel_useless);
		container_main.add(jbutton_exit);
		
		windows.setVisible(true);//使窗体可见
		}
		
}
