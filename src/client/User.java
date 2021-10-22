package client;

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
	protected String clientid=ClientMain.ipv4;
	public boolean connected=false;
	public boolean checked=false;
	public User(Socket s) {//���췽�� ��ʼ���շ�
		try {
			socket=s;
			bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
			ReceiveThread rthread=new ReceiveThread();
			rthread.setUser(this,socket);
			rthread.start();
			connected=true;
		}
		catch(Exception e){
			ClientMain.wlog("Failed initializing user. Exception:\n"+e);
			connected=false;
		}
	}
	//������Ϣ
	public boolean send(String msg) {
		try {
			//ClientMain.wlog("Preparing to send to server "+clientid);
			bufferedWriter.write(msg);
			bufferedWriter.write("\n");
			bufferedWriter.flush();
		}catch (IOException e){
			ClientMain.wlog(clientid+" sending failed. Exception:"+e);
			}
		return true;
	}
	//����id
	public void setID(String id) {
		clientid=id;
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
				ClientMain.wlog("Connection established with server "+ClientMain.ipv4);
				bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				String str;//ͨ��whileѭ�����϶�ȡ��Ϣ��
				while ((str = bufferedReader.readLine())!=null){
				//���
					ClientMain.msgprocess(str);
					System.out.println(str);
					//user.send(str);
				}
			}catch (IOException e){
				ClientMain.wlog("Receiving thread terminated. Exception:"+e);
				try {
					socket.close();
				} catch (IOException e1) {
					ClientMain.wlog("WARNING:Failed to close socket at client"+712);
				}
			}
		}
	}
}