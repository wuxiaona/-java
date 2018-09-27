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
	
	private InetAddress ip;//InetAddress����Java��װ������ʾIP��ַ�ĸ߼���ʾ
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
		Scan scan = new Scan();//�������
        scan.init(); //�Զ�����г�ʼ��
		
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
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 16));
		lblNewLabel.setBounds(20, 10, 71, 21);
		panel.add(lblNewLabel);
		
		IP = new JTextField();
		IP.setBounds(101, 10, 182, 23);
		panel.add(IP);
		IP.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u7AEF\u53E3\u53F7");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 16));
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
		Start.setFont(new Font("����", Font.PLAIN, 14));
		Start.setBounds(341, 10, 93, 38);
		panel.add(Start);
		Start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try{       
                    ip = InetAddress.getByName(IP.getText()); //��������IP
              }       //InetAddress.getByName() ͨ����������ȡ��Ӧ��ip��ַ��
             catch(UnknownHostException ex){       
                   lst.setText("�޷��ҵ�"+IP.getText()+"���߸�IP���Ϸ�");
                   return ;
              }
				
              startPort = Integer.parseInt(TStartPort.getText());//���ַ�ת��������
              endPort = Integer.parseInt(TEndPort.getText());
              
              if(startPort<1||startPort>65535||endPort<1||endPort>65535){         
             	 lst.setText("�˿ڷ�Χ������1~65535֮��");
             	 return ;
             	 
              }
              else if(startPort>endPort){          
             	 lst.setText("��ʼ�˿ںű���С����ֹ�˿ں�");
             	 return ;
             	 }
              else if(startPort<=endPort){
            	  start = true;
                  lst.setText("��ʼɨ��˿�\n");  
              }
              startTime = System.currentTimeMillis();//��ǰʱ��ĺ�����
                //����ʮ���߳̽��ж˿�ɨ��
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
		Stop.setFont(new Font("����", Font.PLAIN, 14));
		Stop.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				  start = false;
		          stop = true;
		          lst.append("ֹͣɨ�裡\n");
		          lst.append("����ɨ�蹲��ʱ��"+(System.currentTimeMillis()-startTime+"ms"));
		          lst.append("����ɨ����Ϊ��");
		          ListIterator li = result.listIterator();//ListIterator������
		         while(li.hasNext()){    
		  //hasNext()������������б�ʱ������б���������滹��Ԫ�أ��򷵻� true�����򷵻�false
		               lst.append("port :"+li.next().toString()+" is open\n");
		               //next()�������б���ListIteratorָ��λ�ú����Ԫ��
		         }
		        return ;
			}
		});
		Stop.setBounds(341, 69, 93, 38);
		panel.add(Stop);
		
		JButton btnNewButton = new JButton("\u4FDD\u5B58\u7ED3\u679C");
		btnNewButton.setFont(new Font("����", Font.PLAIN, 14));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFrame jfd = new JFrame();  //����һ��JFrame�Ķ���
				FileDialog fd = new FileDialog(jfd,"����",FileDialog.SAVE);  //�����Ի��򣬽���ѡ���ļ�����λ�õĿ���
				fd.setVisible(true);  //ʹ����ɼ�
				String filePath = fd.getDirectory();  //���ļ��ĵ�ַ��ֵ��filePath
				String fileName = fd.getFile();  //���ļ�����ֵ��fileName
				try{
					FileOutputStream fos =   //�����ļ�
							new FileOutputStream(filePath+fileName);  //����FileOutputStream�Ķ���
					strl = lst.getText();  //���ı�������ݸ�ֵ���ַ���str
					fos.write(strl.getBytes());  //д���ļ�
					fos.close();   //�ر���
		
				}catch (Exception e) {  //�׳��쳣
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
		while(start&&!stop){ //startΪtrue��stopΪfalse
			 int testPort = scanPort();
			 if(testPort == -1){
				try{
					 Thread.currentThread().sleep(2500);//�ȴ������̷߳������н��
					 }catch(Exception ex){
						 ex.printStackTrace();
						 }
				 if(!stop){
					 lst.append("ɨ�������\n");
					 lst.append("����ɨ�蹲��ʱ��"+(System.currentTimeMillis()-startTime+"ms")+"\n");
					 lst.append("����ɨ�迪�ŵĶ˿��У�"+"\n");
					 ListIterator li = result.listIterator();
					 while(li.hasNext()){  
						 lst.append("port :"+li.next().toString()+" is open\n");
						 }
					 return ;
					 }
				 }
			 try{
				 Socket s = new Socket(ip,testPort);//ʹ���׽��ֽ�������
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

