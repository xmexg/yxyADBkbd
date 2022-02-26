package youxueyuan;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;

//v2.4的所有乱七八糟的提示窗口全放这里
public class INFO_GUI {
	private static JFrame windows = new JFrame("提示");
	private Container container_main = windows.getContentPane();// 获取窗口容器
	
	public INFO_GUI() {
		Point origin = new Point();//坐标
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int DEFAULE_WIDTH = (int) (toolkit.getScreenSize().getWidth() * 0.5 - 200);
		int DEFAULE_HEIGH = (int) (toolkit.getScreenSize().getHeight() * 0.5 - 100);
		windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置窗口关闭方式为退出并关闭程序
		windows.setBounds(DEFAULE_WIDTH, DEFAULE_HEIGH, 400, 200);// 设置默认窗口大小
		windows.setResizable(false);// 窗口不可改变大小
		windows.setBackground(Color.red);
		windows.setUndecorated(true);//关闭标题栏
		/**
		 * 各种窗口风格
		 * NONE 无装饰（即去掉标题栏）
		 * FRAME 普通窗口风格
		 * PLAIN_DIALOG 简单对话框风格
		 * INFORMATION_DIALOG 信息对话框风格
		 * ERROR_DIALOG 错误对话框风格
		 * COLOR_CHOOSER_DIALOG 拾色器对话框风格
		 * FILE_CHOOSER_DIALOG 文件选择对话框风格
		 * QUESTION_DIALOG 问题对话框风格WARNING_DIALOG 警告对话框风格
		 */
		windows.getRootPane().setWindowDecorationStyle(JRootPane.WARNING_DIALOG);
		container_main.setLayout(new GridLayout());//绝对布局
		//container_main.setBackground(new Color(251,139,5));
		windows.addMouseListener(new MouseAdapter() {
			//鼠标按下不抬起
			public void mousePressed(MouseEvent e) {
				//鼠标按下时获取坐标
				origin.x = e.getX();
				origin.y = e.getY();
			}
		});
		windows.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {//鼠标按下拖动
				// TODO 自动生成的方法存根
				Point p = windows.getLocation();//获取当前窗口坐标
				windows.setLocation(p.x+arg0.getX()-origin.x, p.y+arg0.getY()-origin.y);
			}
		});
		windows.setVisible(false);//窗口隐藏
	}
	
	public void ERR(String err_text) {
		JLabel jlabel = new JLabel();
//		windows.remove(jlabel);;//移除所有控件
		jlabel.setHorizontalAlignment(SwingConstants.CENTER);//文字居中
		
		jlabel.setText("<html><boady><center>"+err_text+"<center><body></html>");//要显示的文字
		jlabel.setFont(new Font("宋体", 1, 15));
		jlabel.setBackground(new Color(255,204,153));
		jlabel.setForeground(new Color(25,77,51));
		windows.add(jlabel);
		windows.setVisible(true);//窗口可见
//		loading();//测试 //意外的发现界面挺好看
	}
	
	public void loading() {//界面启动时的随机动图
		JLabel jlabel1 = new JLabel();
		File folder = new File("src"+File.separator+"youxueyuan"+File.separator+"GIF");
		File folder_add = new File("gif");
		if((!folder.exists())&&(!folder_add.exists())) {
			return;
		}
		File[] loading_files = null;
		int num_loading_files = 0;
		if(folder.exists()) {
			loading_files = folder.listFiles();
			num_loading_files = loading_files.length;
		}
		File[] loading_files_add = null;
		int numall = num_loading_files;
		if(folder_add.exists()) {
			loading_files_add = folder_add.listFiles();
			numall += loading_files_add.length;
		}
		if(numall<=0) {
			return;
		}
		Random random = new Random();
		int num = random.nextInt(numall);
//		System.out.println("随机数:"+num);
		Icon icon;
		if(num>=num_loading_files) {
			icon = new ImageIcon(folder_add+File.separator+loading_files_add[num-num_loading_files].getName());			
		}else {
			icon = new ImageIcon(folder+File.separator+loading_files[num].getName());
		}
//		System.out.println("图标为:"+icon);
		jlabel1.setOpaque(false);//组件透明
		int icon_width = icon.getIconWidth();
		int icon_heigh = icon.getIconHeight();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int DEFAULE_WIDTH = (int) (toolkit.getScreenSize().getWidth() * 0.5 - icon_width * 0.5);
		int DEFAULE_HEIGH = (int) (toolkit.getScreenSize().getHeight() * 0.5 - icon_heigh *0.5);
		windows.setBounds(DEFAULE_WIDTH, DEFAULE_HEIGH, icon_width, icon_heigh);//让窗口的尺寸和图片的尺寸相等
		windows.setOpacity((float) 0.85);//窗口不透明度[0,1]
		jlabel1.setIcon(icon);//设置图片
		jlabel1.setHorizontalAlignment(SwingConstants.CENTER);//图片居中
		windows.add(jlabel1);
		windows.getRootPane().setWindowDecorationStyle(JRootPane.NONE);//没有标题栏
		windows.setVisible(true);
//		System.out.println(num+":"+loading_files[num].getName());
//		for(File file : loading_files) {
//			if(file.isFile()) {
//				System.out.println("文件:"+file.getName());
//			}
//		}
		
	}
	
	public void set_windows_Visible(boolean visible) {
		windows.setVisible(visible);
	}
	
	public void install_ADBKeyboard() {
		
	}
}
