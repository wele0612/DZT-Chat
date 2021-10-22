package keyGenerate;

import java.util.Base64;

public class Generate {
	static Base64.Decoder decoder = Base64.getDecoder();
	static Base64.Encoder encoder = Base64.getEncoder();
	public static void main(String[] args) {
		try {
			Algo.KeyStore keys=Algo.createKeys();
			byte[] publicKey = keys.getPublicKey();
		    byte[] privateKey = keys.getPrivateKey();
		    System.out.println("Public key length:"+encoder.encodeToString(publicKey).length());
		    System.out.println("Public key ori length:"+publicKey.length);
		    System.out.println("Private key length:"+encoder.encodeToString(privateKey).length());
		    String username=Algo.SHA256(encoder.encodeToString(publicKey)).substring(0,8);
		    System.out.println("Your user name:"+username);
		    System.out.println("Your generated key:(WARNING: there's no way to recover if you lose it.)");
		    System.out.println(encoder.encodeToString(publicKey)+encoder.encodeToString(privateKey));
		    System.out.println("¹«Ô¿£º"+encoder.encodeToString(publicKey));
		    
		    System.out.println("¹«Ô¿£º"+encoder.encodeToString(
		    		decoder.decode(encoder.encodeToString(publicKey))));
		    //System.out.println("Ë½Ô¿£º"+ encoder.encodeToString(privateKey));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
