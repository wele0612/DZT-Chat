package client;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import server.Algo;
import server.ServerMain;
public class ClientMain {
	//static Netio netio;
	static User user;
	static GUIframe gui=new GUIframe("Crypto Chat");
	static String ipv4="127.0.0.1";
	static int port=12712;
	static Base64.Decoder decoder = Base64.getDecoder();
	static Base64.Encoder encoder = Base64.getEncoder();
	public static void main(String[] args) {
		//algoPost();
		/*netio=new Netio();
		netio.estconnection("127.0.0.1",12712);
		//�������ĵ�ַ�����ƣ�����������ʱ��ǵø�����*/
		try {
			Socket socket =new Socket(ipv4,port);
			user=new User(socket);
		} catch (IOException e) {
			wlog("Failed to connect. You're offline. Exception:"+e);
		}
		user.send("test!");
	}
	public static void msgprocess(String str) {
		char pro=str.charAt(0);
		String data=str.substring(1);
		//wlog("Received length:"+str.length());
		//wlog("Received:"+str);
		switch(pro) {
		case 'g':
			String id=data.substring(0,8);
			gui.setPubKey(id,data.substring(8,8+216));
			gui.msgDistribute(id,"Received public key.");
			//wlog(id);
			break;
		case 'p':
			break;
		case 'm':
			String text=gui.mp.unpack(str);
			//wlog(text);
			gui.msgDistribute(str.substring(1,9),"Chat received:"+text);
			break;
		default:
			wlog("WARNING:Server unrecognized msg:"+str);
	}
	return;
	}
	public static void algoPost() {
		wlog("Start Algorithm posting");
		String password = "1234abcd5678";
		//����ʾ��
		try {
			//wlog(Algo.getRandomString(8));
			
	        Algo.KeyStore keys = Algo.createKeys();
	        byte[] publicKey = keys.getPublicKey();
	        byte[] privateKey = keys.getPrivateKey();
	        System.out.println("��Կ��"+encoder.encodeToString(publicKey));
	        System.out.println("��Կ���ȣ�"+encoder.encodeToString(publicKey).length());
	        System.out.println("˽Կ��"+ encoder.encodeToString(privateKey));
	        System.out.println("˽Կ���ȣ�"+encoder.encodeToString(privateKey).length());
	
	        byte[] encryptByPublicKey = Algo.RSAencryptByPublicKey(password.getBytes(), publicKey);
	        System.out.println("ʹ�ù�Կ���ܺ�����ݣ�"+encoder.encodeToString(encryptByPublicKey));
	        System.out.println("���ܺ󳤶ȣ�"+encryptByPublicKey.length);
	        
	        byte[] encryptByPrivateKey = Algo.RSAencryptByPrivateKey(password.getBytes(), privateKey);
	        System.out.println("ʹ��˽Կ���ܺ�����ݣ�"+encoder.encodeToString(encryptByPrivateKey));
	        System.out.println("���ܺ󳤶ȣ�"+encryptByPrivateKey.length);
	        
	        byte[] decryptByPrivateKey = Algo.RSAdecryptByPrivateKey(encryptByPublicKey, privateKey);
	        System.out.println("ʹ��˽Կ���ܺ�����ݣ�"+new String(decryptByPrivateKey));
	        
	        String dbPassword = "12712";
	        String encryptDbPwd = Algo.AESencode(Algo.DEFAULT_SECRET_KEY, dbPassword);
	        System.out.println("AES���ܺ������: " + encryptDbPwd);
	        String decrypt = Algo.AESdecode(Algo.DEFAULT_SECRET_KEY, encryptDbPwd);
	        System.out.println("AES���ܺ������:" + decrypt);
		}catch(Exception e) {System.out.println(e);}
	}
	public static void sendmsg(String msg) {
		//wlog("Sending message with length "+msg.length());
		user.send(msg);
	}
	public static void wlog(String msg) {//����system.out,д��־�õ� 
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String log="["+formatter.format(date)+"]"+msg;
		System.out.println(log);
		gui.applog(log);
	}
}
