package Scan;

import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Spring;

import Scan.Scan;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;

import javax.swing.JScrollBar;
import java.awt.TextArea;
import java.awt.Font;

public class Scan implements Runnable{
	
	private InetAddress ip;//InetAddress类是Java包装用来表示IP地址的高级表示
	private int startPort;
	private int endPort;
	private long startTime;
	ArrayList result = new ArrayList();
    private boolean start = false;
    private boolean stop = false;
	private static  JFrame f;
	public JTextField IP;
	public JTextField TStartPort;
	public JTextField TEndPort;
	public TextArea lst;
	
	String strl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Scan scan = new Scan();//构造对象
        scan.init(); //对对象进行初始化
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 f.setVisible(true);
				          
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});   
	}


	/**
	 * Initialize the contents of the frame.
	 */
	public void init(){   
		f = new JFrame();
		f.setTitle("\u7AEF\u53E3\u626B\u63CF\u5668");
		f.setBounds(100, 100, 472, 319);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Panel panel = new Panel();
		f.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u626B\u63CF\u7684IP");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel.setBounds(20, 10, 71, 21);
		panel.add(lblNewLabel);
		
		IP = new JTextField();
		IP.setBounds(101, 10, 182, 23);
		panel.add(IP);
		IP.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u7AEF\u53E3\u53F7");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(20, 46, 63, 26);
		panel.add(lblNewLabel_1);
		
	 
		JLabel lblNewLabel_2 = new JLabel("       -----");
		lblNewLabel_2.setBounds(166, 53, 54, 15);
		panel.add(lblNewLabel_2);
		
	    lst = new TextArea();
		lst.setBounds(10, 101, 325, 170);
		panel.add(lst);
		
		TEndPort = new JTextField();
		TEndPort.setBounds(232, 50, 66, 23);
		panel.add(TEndPort);
		TEndPort.setColumns(10);
		
		
		TStartPort = new JTextField();
		TStartPort.setBounds(93, 50, 66, 23);
		panel.add(TStartPort);
		TStartPort.setColumns(10);
		
		JButton Start = new JButton("\u5F00\u59CB\u626B\u63CF");
		Start.setFont(new Font("宋体", Font.PLAIN, 14));
		Start.setBounds(341, 10, 93, 38);
		panel.add(Start);
		Start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try{       
                    ip = InetAddress.getByName(IP.getText()); //获得输入的IP
              }       //InetAddress.getByName() 通过主机名获取对应的ip地址：
             catch(UnknownHostException ex){       
                   lst.setText("无法找到"+IP.getText()+"或者该IP不合法");
                   return ;
              }
				
              startPort = Integer.parseInt(TStartPort.getText());//把字符转化成整数
              endPort = Integer.parseInt(TEndPort.getText());
              
              if(startPort<1||startPort>65535||endPort<1||endPort>65535){         
             	 lst.setText("端口范围必须在1~65535之间");
             	 return ;
             	 
              }
              else if(startPort>endPort){          
             	 lst.setText("起始端口号必须小于终止端口号");
             	 return ;
             	 }
              else if(startPort<=endPort){
            	  start = true;
                  lst.setText("开始扫描端口\n");  
              }
              startTime = System.currentTimeMillis();//当前时间的毫秒数
                //创建十个线程进行端口扫描
              for(int i = 0;i<10;i++){          
             	 new Thread(Scan.this).start();
             	 }          
			}
		});
		
		
		JButton Stop = new JButton("\u7ED3\u675F\u626B\u63CF");
		Stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		Stop.setFont(new Font("宋体", Font.PLAIN, 14));
		Stop.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				  start = false;
		          stop = true;
		          lst.append("停止扫描！\n");
		          lst.append("本次扫描共用时："+(System.currentTimeMillis()-startTime+"ms"));
		          lst.append("本次扫描结果为：");
		          ListIterator li = result.listIterator();//ListIterator迭代器
		         while(li.hasNext()){    
		  //hasNext()：以正向遍历列表时，如果列表迭代器后面还有元素，则返回 true，否则返回false
		               lst.append("port :"+li.next().toString()+" is open\n");
		               //next()：返回列表中ListIterator指向位置后面的元素
		         }
		        return ;
			}
		});
		Stop.setBounds(341, 69, 93, 38);
		panel.add(Stop);
		
		JButton btnNewButton = new JButton("\u4FDD\u5B58\u7ED3\u679C");
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 14));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFrame jfd = new JFrame();  //创建一个JFrame的对象
				FileDialog fd = new FileDialog(jfd,"保存",FileDialog.SAVE);  //弹出对话框，进入选择文件储存位置的框体
				fd.setVisible(true);  //使窗体可见
				String filePath = fd.getDirectory();  //将文件的地址赋值给filePath
				String fileName = fd.getFile();  //将文件名赋值给fileName
				try{
					FileOutputStream fos =   //保存文件
							new FileOutputStream(filePath+fileName);  //创建FileOutputStream的对象
					strl = lst.getText();  //将文本框的内容赋值给字符串str
					fos.write(strl.getBytes());  //写入文件
					fos.close();   //关闭流
		
				}catch (Exception e) {  //抛出异常
					// TODO: handle exception
					e.printStackTrace();
				} 
				}
		});
		btnNewButton.setBounds(341, 127, 93, 38);
		panel.add(btnNewButton);
	}

	
	
	public  int scanPort(){  
		if(startPort<=endPort){
			  int testPort = startPort;
			  startPort++;
			  return testPort;
			  } 
		  else{
			  start = false;
			  return -1;
			  }
		  }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(start&&!stop){ //start为true，stop为false
			 int testPort = scanPort();
			 if(testPort == -1){
				try{
					 Thread.currentThread().sleep(2500);//等待其他线程返回运行结果
					 }catch(Exception ex){
						 ex.printStackTrace();
						 }
				 if(!stop){
					 lst.append("扫描结束！\n");
					 lst.append("本次扫描共用时："+(System.currentTimeMillis()-startTime+"ms")+"\n");
					 lst.append("本次扫描开放的端口有："+"\n");
					 ListIterator li = result.listIterator();
					 while(li.hasNext()){  
						 lst.append("port :"+li.next().toString()+" is open\n");
						 }
					 return ;
					 }
				 }
			 try{
				 Socket s = new Socket(ip,testPort);//使用套接字建立连接
				 result.add(testPort);
				 s.close();
				 lst.append(" port: "+testPort+"  is open\n");
				 }catch(Exception e){ 
					 if(!stop){
						 lst.append(" port: "+testPort+"  is closed\n");
						 }
					 }
			 }
		 }
}

