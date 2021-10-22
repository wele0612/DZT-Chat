package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
/* Alan Xue Ѧ��ΰ 2021.5.23
 * ��������ͨ�� �����շ� �ȵ�
 */
public class User {
	private Socket socket;
	private BufferedWriter bufferedWriter=null;
	protected String clientid="unknow";
	public boolean connected=false;
	public boolean checked=false;
	public String pubkey;
	public byte[] publicKey;
	private int checkKey=0;
	public User(Socket s) {//���췽�� ��ʼ���շ�
		try {
			socket=s;
			bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
			ReceiveThread rthread=new ReceiveThread();
			rthread.setUser(this,socket);
			rthread.start();
			connected=true;
			checkKey=(int)Math.random()*1000000;
			send("p");
		}
		catch(Exception e){
			ServerMain.wlog("Failed initializing user. Exception:\n"+e);
			connected=false;
		}
	}
	public boolean equalsCheckKey(int key) {
		return key==checkKey;
	}
	public String toString() {
		return clientid;
	}
	public void setPublicKey(String pk) {
		clientid=Algo.SHA256(pk).substring(0,8);
		pubkey=pk;
	}
	//������Ϣ
	public boolean send(String msg) {
		try {
			ServerMain.wlog("Preparing to send to client "+clientid);
			bufferedWriter.write(msg);
			bufferedWriter.write("\n");
			bufferedWriter.flush();
		}catch (IOException e){
			ServerMain.wlog(clientid+" sending failed. Exception:"+e);
			}
		return true;
	}
	//�����߳�
	private class ReceiveThread extends Thread{
		private Socket socket;
		private User user;
		public void setUser(User u,Socket s) {
			socket=s;
			user=u;
		}
		public void run() {
			BufferedReader bufferedReader=null;
			try {
				ServerMain.wlog("Connection established with client "+712);
				bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				String str;//ͨ��whileѭ�����϶�ȡ��Ϣ��
				while ((str = bufferedReader.readLine())!=null){
				//���
					System.out.println(str);
					ServerMain.msgprocess(str,user);
				}
			}catch (IOException e){
				ServerMain.wlog("Receiving thread terminated. Exception:"+e);
				ServerMain.cl.remove(clientid);//�Ƴ���Ϣ�б�
				try {
					socket.close();
				} catch (IOException e1) {
					ServerMain.wlog("WARNING:Failed to close socket at client"+712);
				}
			}
		}
	}
}