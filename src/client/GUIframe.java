package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * @category GUI
 * @author wele(Alan Xue)
 */
@SuppressWarnings("serial")
public final class GUIframe extends JFrame {
	private static final int Height=600;
	private static final int Width=500;	
	private ArrayList<ChatPanel> panels=new ArrayList<ChatPanel>();
	private JComboBox<String> cmb=new JComboBox();
	public ArrayList<String> addedChat=new ArrayList<String>();
	private KeyPanel firstPanel=new KeyPanel("test");
	private CardLayout cl=new CardLayout(5,5);
	private JPanel cards=new JPanel(cl);
	
	public MessagePackup mp=new MessagePackup();
	public GUIframe(String title) {
		super(title);
		addedChat.add("first one");
		
		setSize(Width,Height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		cards.add(firstPanel,"0");
		add(cards,BorderLayout.CENTER);
		
		cmb.addItem("Log in");
		add(cmb,BorderLayout.NORTH);
		cmb.addActionListener((e)->{
			cl.show(cards,cmb.getSelectedIndex()+"");
		});
		
		setVisible(true);
	}
	public void msgDistribute(int i,String msg) {
		if(i<panels.size()) {
			panels.get(i).showmsg(msg);
		}
	}
	public void msgDistribute(String id,String msg) {
		if(getbyid(id)!=null) {
			getbyid(id).showmsg(msg);;
		}
	}
	public void setPubKey(String id,String key) {
		if(getbyid(id)!=null) {
			getbyid(id).pubkey=key;
		}
	}
	public ChatPanel getbyid(String id) {
		//ClientMain.wlog("looking for "+id);
		for (int i = 0; i < panels.size(); i++) {
            ChatPanel p=panels.get(i);
            //ClientMain.wlog(p.chatname);
            if(p.chatname.equals(id)) {
				return p;
			}
        }
		return null;
	}
	private class ChatPanel extends JPanel{
		private JLabel title=new JLabel("",JLabel.CENTER);
		private JTextArea inputText=new JTextArea(5,38);
		private JButton sendbutton=new JButton("Send");
		private JTextArea display=new JTextArea(20,40);
		public String pubkey;
		public String chatname;
		public ChatPanel(String name) {
			super();
			chatname=name;
			setLayout(new BorderLayout());
			
			title.setText("Chat with "+name);
			add(title,BorderLayout.NORTH);
			
			JPanel bpanel=new JPanel();
			bpanel.setLayout(new FlowLayout());
			inputText.setLineWrap(true);
			inputText.setEditable(true);
			JScrollPane jsp=new JScrollPane(inputText);    //将文本域放入滚动窗口
		    Dimension size=inputText.getPreferredSize();    //获得文本域的首选大小
		    jsp.setBounds(110,90,size.width,size.height);
			bpanel.add(jsp);
			sendbutton.setPreferredSize(new Dimension(70,size.height));
			bpanel.add(sendbutton);
			add(bpanel,BorderLayout.SOUTH);
			
			JScrollPane dsp=new JScrollPane(display);
		    Dimension size2=display.getPreferredSize();
		    dsp.setBounds(110,90,size2.width,size2.height);
		    display.setLineWrap(true);
		    display.setEditable(false);
			add(dsp,BorderLayout.CENTER);
			
			sendbutton.addActionListener((e)->{
				String msg=inputText.getText();
				if(pubkey==null) {
					showmsg("Waiting for pubkey to pack up message.");
					return;
				}
				inputText.setText("");
				ClientMain.sendmsg(mp.packup(chatname,msg,pubkey));
			});
		}
		public void showmsg(String msg) {//和wlog类似，但是写到显示区域
			SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			String log="["+formatter.format(date)+"]"+msg;
			display.append(log+"\n");
		}
	}
	private class KeyPanel extends ChatPanel{

		public KeyPanel(String name) {
			super(name);
			super.title.setText("Input key to log in.");
			super.inputText.setRows(10);
			super.sendbutton.setText("Done");
			super.display.append("Crypto Chat v1.0 by Alan Xue \n");
			super.display.append("Type chat [userid] to create new chat.\n\n");
			super.inputText.setText("Input your key to log in,"
					+ "\n Or type chat [userid](SPACE IN BETWEEN) to create new chat.");
			
			super.sendbutton.removeActionListener(super.sendbutton.getActionListeners()[0]);
			super.sendbutton.addActionListener((e)->{
				String text=super.inputText.getText();
				text = text.replace("\n", "").replace("\r", "");
				super.inputText.setText("");
				if(text.substring(0,5).equals("chat ")&&text.length()==13) {
					//new chat initralize
					if(addedChat.contains(text.substring(5))) {
						ClientMain.wlog("Chat already added.");
						return;
					}else if(!mp.loggedIn) {
						ClientMain.wlog("Please input key to log in first.");
						return;
					}
					
					ClientMain.wlog("Added chat. Using top bar to open chat window.");
					ChatPanel p=new ChatPanel(text.substring(5));
					panels.add(p);
					//ClientMain.wlog("added chat"+p.chatname);
					ClientMain.sendmsg("g"+text.substring(5));
					
					cards.add(p,(addedChat.size())+"");
					cmb.addItem(text.substring(5));
					addedChat.add(text.substring(5));
				}else if(text.length()>=1000) {
					//log in with key
					//ClientMain.wlog(text.substring(0,216)+"\n"+text.substring(216,1064));
					//ClientMain.wlog(text.length()+"");
					//↑for debug only
					if(mp.setKeys(text.substring(0,216),text.substring(216))) {
						ClientMain.sendmsg("p"+text.substring(0,216));
					}
				}
			});
		}
		public void applog(String log) {
			super.display.append(log+"\n");
		}
	}
	public void applog(String log) {
		firstPanel.applog(log);
	}
	
}
