package client;

import java.util.Arrays;
import java.util.Base64;

public class MessagePackup {
	private byte[] publickey;
	private String mypubkeyStr;
	private byte[] privatekey;
	static Base64.Decoder decoder = Base64.getDecoder();
	static Base64.Encoder encoder = Base64.getEncoder();
	private static final int passwordLength=172;
	public boolean loggedIn=false;
	public MessagePackup() {
	}
	public boolean setKeys(String pub,String pri) {
		publickey=decoder.decode(pub);
		mypubkeyStr=pub;
		//ClientMain.wlog("pubkey length:"+publickey.length);
		privatekey=decoder.decode(pri);
		ClientMain.wlog("Checking keys...");
		byte[] testmsg="testonly".getBytes();
		try {
			byte[] testresult=Algo.RSAdecryptByPublicKey(Algo.RSAencryptByPrivateKey(testmsg,privatekey),publickey);
			if(Arrays.equals(testmsg,testresult)) {
				ClientMain.wlog("Valid key. Welcome!");
				ClientMain.wlog("Your userID is "+
					Algo.SHA256(pub).substring(0,8)+", you're at server "+ClientMain.ipv4);
				loggedIn=true;
				return true;
			}else {
				ClientMain.wlog("Invalid key, please try again.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;}
	}
	/**
	 * pack up message
	 * @param target 收信人8字ID
	 * @param msg 消息内容
	 * @param pkey 对方的216位公钥
	 * @return 打包好的信息,字符串形式
	 */
	public String packup(String target,String msg,String pkey) {
		String AESkey=Algo.getRandomString(16);
		byte[] usepubkey=decoder.decode(pkey);
		String result="";
		result+="m";
		result+=target;
		result+=mypubkeyStr;
		try {
			byte[] keypart=Algo.RSAencryptByPublicKey(stringToByte(AESkey),usepubkey);
			result+=byteToString(keypart);
			
			//ClientMain.wlog("length:"+keypart.length+" String:"+byteToString(keypart).length());
			
			result+=Algo.AESencode(AESkey,msg);
		}catch(Exception e) {
			ClientMain.wlog("Packing message failed.");
			return "";
		}
		return result;
	}
	public byte[] stringToByte(String str) {
		return decoder.decode(str);
	}
	public String byteToString(byte[] b) {
		return encoder.encodeToString(b);
	}
	public String unpack(String msg) {
		String password=msg.substring(225,225+passwordLength);
		
		//ClientMain.wlog("Password part length:"+stringToByte(password).length+" String:"+password.length());
		
		String result="Unpack failed or NULL message.";
		try {
			password=encoder.encodeToString(
					Algo.RSAdecryptByPrivateKey(stringToByte(password),privatekey));
			result=Algo.AESdecode(password, msg.substring(225+passwordLength));
		} catch (Exception e) {
			ClientMain.wlog("Exception occured when unpacking message. \n"+e);
			e.printStackTrace();
		}
		return result;
	}
}
