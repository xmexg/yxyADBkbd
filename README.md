### 通过使用ADBKeyboard变相实现优学院文字粘贴 ###
优学院考试输入框无法粘贴文字，输入框每次最多输入4个汉字，通过ADBKeyboard循环每次输入1个汉字，变相实现粘贴功能

ADBKeyboard由GitHub大佬提供：https://github.com/senzhk/ADBKeyBoard ，向大佬致敬！

# 正文：

### 运行环境：
- 电脑：
- java - jdk (16)
- Adb
- 安卓手机，oppo貌似不可运行？？



### 手机需要做的：

  1.下载并安装ADBKeyboard：https://github.com/senzhk/ADBKeyBoard/raw/master/ADBKeyboard.apk （注：安装后桌面无图标）

  2.激活ADBKeyboard:(小米手机在设置→更多设置→语言与输入法→输入法管理)
  ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/phone_keyboard.jpg)

  3.在开发者选项打开USB调试。
  ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/phone_adb.jpg)

### 电脑需要准备的：
- Windows篇：
  1. 安装java-jdk最新版：
  
      官网：https://www.oracle.com/java/technologies/downloads/#java17
      
      官网下载：https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe
  2. 安装ADB：https://blog.csdn.net/x2584179909/article/details/108319973
  3. 启动：
  
      直接使用已打包的jar：
      
        下载已打包的jar：https://github.com/xmexg/yxyADBkbd/releases/
        
        打开此电脑，进入这个文件保存目录，并在文件导航栏输入cmd，回车，弹出命令行
        ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/explorer_cmd.png)

   4. 把手机通过数据线连接电脑，手机提示usb调试就点允许
      ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/phone_usb.jpg)
      
   5. 手机打开优学院简答题输入框。
   
   6. 在v2.0版本开始中支持图形界面了，命令行通过　`java -Dgui=true -Dfile.encoding=UTF-8 -jar xxx.jar`　可打开图形界面
      其中　`-Dfile.encoding=UTF-8`　是指定使用UTF-8编码，当当前操作系统默认编码不为UTF-8编码时不可省略。
      ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/v2.0GUI.png)
      
   <strike>6. 电脑在刚才弹出的命令行输入 `java  -jar  ./优学院(具体下载的名称).jar` 回车
   
      如下情况可拔掉手机线重新连接，然后重新输入上面这条命令
      ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/cannotconnect.png)
      
      如下情况连接成功：
      ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/connect.png)

   7. 点击手机要输入文字的输入框，让手机处于正在输入的状态，在此处粘贴要粘贴的文字，注意这段文字不能有回车，然后按回车，他会发送到手机输入框。
      注：空格会被自动取消。</strike>
      
   



- Linux安装：
  
  更新源:`sudo apt-get update`
  
  安装jdk:`sudo apt-get install openjdk-17-jdk` 
  
  
  安装adb:`sudo apt-get install adb`
  
  克隆文件到本地:`sudo git clone https://github.com/xmexg/yxyADBkbd.git`
  
  cd到这个jar文件目录:`cd ./yxyADBkbd/Releases/`
  
  运行jar文件:`java -jar ./default.jar`
  ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/linux_jar.png)

### 结尾：
  当前输入法无法正常打字
  
  切换到其他输入法：

  方法一：在输入文字状态下下拉状态栏，选择＂选择输入法＂，选择其他任意输入法即可．
  
  方法二：在手机设置的语言和键盘中，将当前输入法换成别的任意输入法即可
  
  方法三：
  1. 电脑终端输入adb shell ime list -a 回车    
  2. 记下想使用的输入法的名称（每一段的开头为这个输入法的名称）
   ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/imelist.png)
  3.  我选择搜狗输入法，如图白色所示
   ![image](https://github.com/xmexg/yxyADBkbd/blob/main/files/imeset.png)
  4. 输入代码：`adb shell ime set com.sohu.inputmethod.sogou.xiaomi/.SogouIME`即可切换到该输入法




