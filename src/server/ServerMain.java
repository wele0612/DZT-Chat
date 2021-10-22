package server;
import java.io.IOException;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class ServerMain {
	static int MAXCLIENTNUMBER=65536;
	static int useport = 12712;
	public static HashMap<String,User> cl = new HashMap<String,User>();
	String[] sendbuf=new String[MAXCLIENTNUMBER];
	static Base64.Decoder decoder = Base64.getDecoder();
	static Base64.Encoder encoder = Base64.getEncoder();
	static ChatList chatlist=new ChatList();
	//static Map<Integer, Game> clientInviteCode=new HashMap<Integer, Game>();
	public static void main(String[] args) {
		wlog("service initialize");		
		algoPost();
		try {
			wlog("server using port "+useport+" and pid "+getProcessID());
			ServerSocket serverSocket =new ServerSocket(useport);
			while(true) {
				Socket socket = serverSocket.accept();
				new User(socket);
				wlog("Start receiving from new client.");
			}
		}catch (IOException e) {
			wlog("WARNING:Main thread crashed. It's probably a connection issue\n"
					+ "Please provide the information below to the developer. Alan.Xue@crestwood.on.ca");
			e.printStackTrace();
			System.exit(1);
			}
	}
	/**
	 * �����㷨�Լ�
	 */
	public static void algoPost() {
		wlog("Start Algorithm posting");
		String password = "1234abcd5678";
		//����ʾ��
		try {
			wlog(Algo.getRandomString(8));
			
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
	
	static void msgprocess(String msg,User user) {
		String data=msg.substring(1);
		//Ҫ���ͻ��˷�������д���Ӧ�ͻ��˵�sharedbuf.str��ok�ˡ�-���Ǹ���ǲ�������Ҫѧϰ������Ϊ��
		//�������ǵõ���
		wlog("Received message length:"+msg.length());
		switch(msg.charAt(0)) {
			case 'p':
				user.clientid=Algo.SHA256(data).substring(0,8);
				cl.put(user.clientid,user);
				wlog("public key reported by "+user.clientid);
				user.setPublicKey(data);
				break;
			case 'g'://����Կ
				if(cl.containsKey(data)) {
					user.send(msg+cl.get(data).pubkey);
				}
				break;
			case 'm':
				String id=data.substring(0,8);
				if(cl.containsKey(id)) {
					cl.get(id).send(msg);
					//��Ϣת��
				}
				break;
			/*case 'k':
				try {
					data=encoder.encodeToString(Algo.RSAdecryptByPublicKey(data.getBytes(),user.publicKey));
				} catch (Exception e) {
					e.printStackTrace();
				}
				user.checked=user.equalsCheckKey(Integer.parseInt(data));
				if(user.checked) {
					cl.add(user);
					wlog(user+" joined the chatlist.");
				}*/
			default:
				wlog("WARNING:Client "+user+" unrecognized msg:"+msg);
		}
		return;
	}
	
	public static void wlog(String msg) {
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("["+formatter.format(date)+"]"+msg); 
		return;
	}
	
    public static final int getProcessID() {  //��ȡ����pid
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();  
    }
}
